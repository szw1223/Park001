package com.example.uidemo;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

import static android.text.format.DateFormat.format;
import static com.example.uidemo.MainActivity.NAME;

public class booking extends AppCompatActivity implements View.OnClickListener {

    TextView timer1, timer2;
    int t1H, t1M, t2H, t2M;

    private TextView begTV, endTV;
    private Button mButton;
    private static final String TAG = "NetworkActivity";
    private String mResult;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            name = getIntent().getStringExtra((NAME));
        }
        setContentView(R.layout.activity_booking);
        timer1 = findViewById(R.id.timer_1);
        timer2 = findViewById(R.id.timer_2);
        findViews();
        setListeners();
    }

    private void findViews() {
        begTV = findViewById(R.id.TVbeginning);
        endTV = findViewById(R.id.TVending);
        mButton = findViewById(R.id.getButton);
    }

    private void setListeners() {
        mButton.setOnClickListener(this);
        timer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        booking.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1H = hourOfDay;
                                t1M = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t1H, t1M);
                                timer1.setText(format("hh:mm aa", calendar));
                                endTV.setText(Integer.toString(t1H) + " : " + Integer.toString(t1M));
                            }
                        }, 24, 0, false
                );
                timePickerDialog.updateTime(t1H, t1M);
                timePickerDialog.show();
            }
        });

        timer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        booking.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t2H = hourOfDay;
                                t2M = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t1H, t1M);
                                timer2.setText(format("hh:mm aa", calendar));
                                begTV.setText(Integer.toString(t2H) + " : " + Integer.toString(t2M));
                            }
                        }, 24, 0, false
                );
                timePickerDialog.updateTime(t2H, t2M);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getButton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ScanPorts(5002).start();
                    }
                }).start();
                break;
        }
    }

    class ScanPorts extends Thread {
        private int port;
        public ScanPorts(int port) {
            this.port = port;
        }
        private String name;
        public void run() {
            if (getIntent() != null) {
                name = getIntent().getStringExtra((NAME));
            }
            try {
                //assign server address and port number
                Socket socket = new Socket("10.136.31.26", port);
                //send data
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                pw.write("book " + name + " " + Integer.toString(t2H) + " " + Integer.toString(t1H + 1));
                pw.flush();
                //shut outputstream
//
//                InputStream is = socket.getInputStream();
//                InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader br = new BufferedReader(isr);
//                String msg;
//                while ((msg = br.readLine()) != null) {
//                    System.out.println(msg);
//                }
//                br.close();
//                isr.close();
//                is.close();
                System.out.println("----->rec start");
                Scanner inScanner = new Scanner(socket.getInputStream());
                String msg = inScanner.nextLine();
                System.out.println("----->rec ="+msg);

                inScanner.close();
                pw.close();
                os.close();
                socket.close();
                Intent intent = new Intent(booking.this, resultActivity.class);
                intent.putExtra(NAME, name + " " + msg);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.getButton:
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String urlString = "https://www.youtube.com/watch?v=1aBrrAxbYyA";
//                        mResult = requestDataByGet(urlString);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResult = decode(mResult);
//                                mTextView.setText(mResult);
//                            }
//                        });
//                    }
//                }).start();
//                break;
//        }
//    }

//    private String requestDataByGet(String urlString) {
//        String result = null;
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(30000);
//            connection.setRequestMethod("GET");  // GET POST
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Charset", "UTF-8");
//            connection.setRequestProperty("Accept-Charset", "UTF-8");
//            connection.connect();
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                InputStream inputStream = connection.getInputStream();
//                result = streamToString(inputStream);
//            } else {
//                String responseMessage = connection.getResponseMessage();
//                Log.e(TAG, "requestDataByPost: " + responseMessage);
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public String streamToString(InputStream is) {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = is.read(buffer)) != -1) {
//                baos.write(buffer, 0, len);
//            }
//            baos.close();
//            is.close();
//            byte[] byteArray = baos.toByteArray();
//            return new String(byteArray);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//            return null;
//        }
//    }
//
//    public static String decode(String unicodeStr) {
//        if (unicodeStr == null) {
//            return null;
//        }
//        StringBuilder retBuf = new StringBuilder();
//        int maxLoop = unicodeStr.length();
//        for (int i = 0; i < maxLoop; i++) {
//            if (unicodeStr.charAt(i) == '\\') {
//                if ((i < maxLoop - 5)
//                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
//                        .charAt(i + 1) == 'U')))
//                    try {
//                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
//                        i += 5;
//                    } catch (NumberFormatException localNumberFormatException) {
//                        retBuf.append(unicodeStr.charAt(i));
//                    }
//                else {
//                    retBuf.append(unicodeStr.charAt(i));
//                }
//            } else {
//                retBuf.append(unicodeStr.charAt(i));
//            }
//        }
//        return retBuf.toString();
//    }


//    private void handleJSONData(String json) {
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//
//            LessonResult lessonResult = new LessonResult();
//            List<LessonResult.Lesson> lessonList = new ArrayList<>();
//            int status = jsonObject.getInt("status");
//            JSONArray lessons = jsonObject.getJSONArray("data");
//            if (lessons != null && lessons.length() > 0) {
//                for (int index = 0; index < lessons.length(); index++) {
//                    JSONObject item = (JSONObject) lessons.get(0);
//                    int id = item.getInt("id");
//                    String name = item.getString("name");
//                    String smallPic = item.getString("picSmall");
//                    String bigPic = item.getString("picBig");
//                    String description = item.getString("description");
//                    int learner = item.getInt("learner");
//
//                    LessonResult.Lesson lesson = new LessonResult.Lesson();
//                    lesson.setID(id);
//                    lesson.setName(name);
//                    lesson.setSmallPictureUrl(smallPic);
//                    lesson.setBigPictureUrl(bigPic);
//                    lesson.setDescription(description);
//                    lesson.setLearnerNumber(learner);
//                    lessonList.add(lesson);
//                }
//                lessonResult.setStatus(status);
//                lessonResult.setLessons(lessonList);
//                mTextView.setText("data is : " + lessonResult.toString());
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }