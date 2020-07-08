package com.example.voteapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Activityfive extends Activity {
    int[] Select_Index = {-1}; // 선택한 인덱스
    String[] username = new String[1];
    String[] userage = new String[1];

    String android_id;

    Button vote;
    TextView title_view;
    TextView firstage_view;
    TextView secondage_view;
    TextView firsttime_view;
    TextView secondtime_view;
    Button cand1;
    Button cand2;
    Button cand3;
    Button cand4;
    Button cand5;
    Button cand6;
    Button cand7;
    Button cand8;
    Button cand9;
    Button cand10;
    TextView text_1;
    TextView text_2;
    TextView text_3;
    TextView text_4;
    TextView text_5;
    TextView text_6;
    TextView text_7;
    TextView text_8;
    TextView text_9;
    TextView text_10;

    String[] title = new String[1]; // 제목
    String[] age = new String[2]; // 나이
    String[] time = new String[2]; // 시간
    String[] cand = new String[10]; // 후보
    int[] choose = {-1}; // 선택 항목

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityfive);

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Intent intent = getIntent();
        username[0] = intent.getStringExtra("Username");
        userage[0] = intent.getStringExtra("Userage");
        Select_Index[0] = Integer.parseInt(intent.getStringExtra("Select_Index"));

        vote = (Button) findViewById(R.id.vote);
        title_view = (TextView) findViewById(R.id.title);
        firstage_view = (TextView) findViewById(R.id.firstage);
        secondage_view = (TextView) findViewById(R.id.secondage);
        firsttime_view = (TextView) findViewById(R.id.firsttime);
        secondtime_view = (TextView) findViewById(R.id.secondtime);
        cand1 = (Button) findViewById(R.id.cand1);
        cand2 = (Button) findViewById(R.id.cand2);
        cand3 = (Button) findViewById(R.id.cand3);
        cand4 = (Button) findViewById(R.id.cand4);
        cand5 = (Button) findViewById(R.id.cand5);
        cand6 = (Button) findViewById(R.id.cand6);
        cand7 = (Button) findViewById(R.id.cand7);
        cand8 = (Button) findViewById(R.id.cand8);
        cand9 = (Button) findViewById(R.id.cand9);
        cand10 = (Button) findViewById(R.id.cand10);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_3 = (TextView) findViewById(R.id.text_3);
        text_4 = (TextView) findViewById(R.id.text_4);
        text_5 = (TextView) findViewById(R.id.text_5);
        text_6 = (TextView) findViewById(R.id.text_6);
        text_7 = (TextView) findViewById(R.id.text_7);
        text_8 = (TextView) findViewById(R.id.text_8);
        text_9 = (TextView) findViewById(R.id.text_9);
        text_10 = (TextView) findViewById(R.id.text_10);

        connectThread th = new connectThread();
        th.start();
    }

    class connectThread extends Thread {
        public void run() {
            int port = 5000;
            Socket sock = new Socket();
            try {
                sock.connect(new InetSocketAddress("192.168.123.131", port));         //여기서 본인 IP 입력
                if (!sock.isConnected()) {
                    Toast.makeText(getApplicationContext(), "소켓없다~~.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        read(sock);
                        sleep(100);
                        String text = send(sock, "3");
                        if (text.equals("3")) {
                            try {
                                read(sock);
                                //sleep(100);
                                int num = Select_Index[0] + 1;
                                String sending = Integer.toString(num);
                                send(sock, sending);
                                //sleep(100);
                                try {
                                    String exception = read(sock);
                                    if (!exception.equals("투표없음.")) {
                                        try {
                                            String choice = send(sock, "1");
                                            if (choice.equals("1")) {
                                                //sleep(100);
                                                String msg = read(sock);
                                                //sleep(100);
                                                if (msg.equals("시간오류"))
                                                    System.out.println("시간오류");
                                                else {
                                                    title[0] = msg;
                                                    age[0] = read(sock);
                                                    age[1] = read(sock);
                                                    time[0] = read(sock);
                                                    time[1] = read(sock);

                                                    final String tmp = read(sock);
                                                    final int temp = Integer.parseInt(tmp);

                                                    for (int i = 0; i < temp; i++) {
                                                        cand[i] = read(sock);
                                                        final int finalI = i;
                                                    }
                                                    for (int i = Integer.parseInt(tmp); i < 10; i++) {
                                                        cand[i] = "";
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "확인1", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        sock.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    int age_cut = Integer.parseInt(userage[0]);
                    if (age_cut < Integer.parseInt(age[0]) || age_cut > Integer.parseInt(age[1])) {
                        Intent intent_cut = new Intent(getApplicationContext(), Activitythree.class);
                        intent_cut.putExtra("Username", username[0]);
                        intent_cut.putExtra("Userage", userage[0]);
                        startActivity(intent_cut);
                        Toast.makeText(getApplicationContext(), "해당 항목의 투표 가능한 나이가 아닙니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    title_view.setText(title[0]);
                    firstage_view.setText(age[0]);
                    secondage_view.setText(age[1]);
                    firsttime_view.setText(time[0]);
                    secondtime_view.setText(time[1]);
                    if (cand[0].length() > 0) {
                        cand1.setEnabled(true);
                        cand1.setText(cand[0]);
                    } else {
                        text_1.setText("");
                    }
                    if (cand[1].length() > 0) {
                        cand2.setEnabled(true);
                        cand2.setText(cand[1]);
                    } else {
                        text_2.setText("");
                    }
                    if (cand[2].length() > 0) {
                        cand3.setEnabled(true);
                        cand3.setText(cand[2]);
                    } else {
                        text_3.setText("");
                    }
                    if (cand[3].length() > 0) {
                        cand4.setEnabled(true);
                        cand4.setText(cand[3]);
                    } else {
                        text_4.setText("");
                    }
                    if (cand[4].length() > 0) {
                        cand5.setEnabled(true);
                        cand5.setText(cand[4]);
                    } else {
                        text_5.setText("");
                    }
                    if (cand[5].length() > 0) {
                        cand6.setEnabled(true);
                        cand6.setText(cand[5]);
                    } else {
                        text_6.setText("");
                    }
                    if (cand[6].length() > 0) {
                        cand7.setEnabled(true);
                        cand7.setText(cand[6]);
                    } else {
                        text_7.setText("");
                    }
                    if (cand[7].length() > 0) {
                        cand8.setEnabled(true);
                        cand8.setText(cand[7]);
                    } else {
                        text_8.setText("");
                    }
                    if (cand[8].length() > 0) {
                        cand9.setEnabled(true);
                        cand9.setText(cand[8]);
                    } else {
                        text_9.setText("");
                    }
                    if (cand[9].length() > 0) {
                        cand10.setEnabled(true);
                        cand10.setText(cand[9]);
                    } else {
                        text_10.setText("");
                    }

                    vote.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (choose[0] != -1) {
                                connectThread2 th = new connectThread2();
                                th.start();
                            } else {
                                Toast.makeText(getApplicationContext(), "후보를 선택해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        class connectThread2 extends Thread {
                            public void run() {
                                int port = 5000;
                                Socket sock = new Socket();
                                try {
                                    sock.connect(new InetSocketAddress("192.168.123.131", port));     //여기서 본인 IP 입력
                                    if (!sock.isConnected()) {
                                    } else {
                                        read(sock);
                                        String text = send(sock, "3");
                                        if (text.equals("3")) {
                                            read(sock);
                                            int num = Select_Index[0] + 1;
                                            String sending = Integer.toString(num);
                                            send(sock, sending);
                                            String exception = read(sock);
                                            if (!exception.equals("투표없음.")) {
                                                String choice = send(sock, "2");
                                                if (choice.equals("2")) {
                                                    //read(sock);
                                                    send(sock, android_id);  // 여기!!!! 여기서 계산된 imei 값을 저기 23값 자리에다가 넣어주면 됩니다.
                                                    String msg = read(sock);
                                                    if(msg.equals("중복")) {
                                                        runOnUiThread(new Runnable(){
                                                            public void run(){
                                                                Toast.makeText(getApplicationContext(), "이미 투표하셨습니다.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                    else{
                                                        send(sock, Integer.toString(choose[0]));
                                                        runOnUiThread(new Runnable(){
                                                            public void run(){
                                                                Intent intent = new Intent(getApplicationContext(), ActivitySeven.class);
                                                                intent.putExtra("Username", username[0]);
                                                                intent.putExtra("Userage", userage[0]);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    sock.close();
                                }catch(IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    cand1.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 1) {
                                choose[0] = 1;
                                cand1.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand2.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 2) {
                                choose[0] = 2;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand3.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 3) {
                                choose[0] = 3;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand4.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 4) {
                                choose[0] = 4;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand5.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 5) {
                                choose[0] = 5;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand6.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 6) {
                                choose[0] = 6;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand7.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 7) {
                                choose[0] = 7;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand8.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 8) {
                                choose[0] = 8;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand9.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 9) {
                                choose[0] = 9;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#00c1f2"));
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else{
                                choose[0] = -1;
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    cand10.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(choose[0] != 10) {
                                choose[0] = 10;
                                cand1.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand2.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand3.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand4.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand5.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand6.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand7.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand8.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand9.setBackgroundColor(Color.parseColor("#ffffff"));
                                cand10.setBackgroundColor(Color.parseColor("#00c1f2"));
                            }
                            else{
                                choose[0] = -1;
                                cand10.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                }
            });
        }

        public String send(Socket sock, String text) {
            String data=null;
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                data = text;
                bw.write(data+"\r\n");
                bw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        public String read(Socket sock){
            String message = null;
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                message = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return message;
        }
    }
}
