package com.example.voteapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Activitysix extends Activity {
    String[] title = new String[1]; // 제목
    int[] age = new int[2]; // 나이
    String[] time = new String[2]; // 시간
    String[] cand = new String[10]; // 후보
    int[] Select_Index = {-1};
    String[] username = new String[1];
    String[] userage = new String[1];

    Button vote;
    TextView title_view;
    TextView firstage_view;
    TextView secondage_view;
    TextView firsttime_view;
    TextView secondtime_view;
    TextView cand1;
    TextView cand2;
    TextView cand3;
    TextView cand4;
    TextView cand5;
    TextView cand6;
    TextView cand7;
    TextView cand8;
    TextView cand9;
    TextView cand10;
    TextView cand1_vote;
    TextView cand2_vote;
    TextView cand3_vote;
    TextView cand4_vote;
    TextView cand5_vote;
    TextView cand6_vote;
    TextView cand7_vote;
    TextView cand8_vote;
    TextView cand9_vote;
    TextView cand10_vote;
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

    final int[] cand_vote = new int[10]; // 후보 투표 수
    final int[] highest_vote = {0}; // 가장 높은 투표 순

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysix);

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
        cand1 = (TextView) findViewById(R.id.cand1);
        cand2 = (TextView) findViewById(R.id.cand2);
        cand3 = (TextView) findViewById(R.id.cand3);
        cand4 = (TextView) findViewById(R.id.cand4);
        cand5 = (TextView) findViewById(R.id.cand5);
        cand6 = (TextView) findViewById(R.id.cand6);
        cand7 = (TextView) findViewById(R.id.cand7);
        cand8 = (TextView) findViewById(R.id.cand8);
        cand9 = (TextView) findViewById(R.id.cand9);
        cand10 = (TextView) findViewById(R.id.cand10);
        cand1_vote = (TextView) findViewById(R.id.cand1_vote);
        cand2_vote = (TextView) findViewById(R.id.cand2_vote);
        cand3_vote = (TextView) findViewById(R.id.cand3_vote);
        cand4_vote = (TextView) findViewById(R.id.cand4_vote);
        cand5_vote = (TextView) findViewById(R.id.cand5_vote);
        cand6_vote = (TextView) findViewById(R.id.cand6_vote);
        cand7_vote = (TextView) findViewById(R.id.cand7_vote);
        cand8_vote = (TextView) findViewById(R.id.cand8_vote);
        cand9_vote = (TextView) findViewById(R.id.cand9_vote);
        cand10_vote = (TextView) findViewById(R.id.cand10_vote);
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

        final int[] cand_vote = new int[10]; // 후보 투표 수
        final int[] highest_vote = {0}; // 가장 높은 투표 순


        connectThread2 th2 = new connectThread2();      // 여기는 집계하기 위한 쓰레드
        th2.start();

        connectThread th = new connectThread();         //여기는 투표 정보, 시간, 후보들 들고 오기 위한 쓰레드
        th.start();

    }

    class connectThread extends Thread{
        public void run(){
            int port = 5000;
            Socket sock = new Socket();
            try {
                sock.connect(new InetSocketAddress("192.168.123.131", port));
                if(!sock.isConnected()){
                }else{
                    read(sock);
                    String text = send(sock, "3");
                    if(text.equals("3")){
                        read(sock);
                        int num = Select_Index[0]+1;
                        String sending = Integer.toString(num);
                        send(sock, sending);
                        read(sock);
                        String text2 = send(sock, "5");
                        if(text2.equals("5")){
                            final String check = read(sock);
                            int canNum = check.length()/2-1;        //총 투표후보갯수 몇개인지 알기 위해서
                            for(int i=0;i<canNum;i++){
                                cand_vote[i]=Integer.parseInt(String.valueOf(check.charAt(2*i+2)));     //각각의 투표후보들의 투표수를 알맞게 배정해줌
                            }
                            for(int i=canNum;i<10;i++){
                                cand_vote[i]=-1;
                            }
                        }
                    }
                }sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    class connectThread2 extends Thread{
        public void run(){
            int port = 5000;
            Socket sock = new Socket();
            try {
                sock.connect(new InetSocketAddress("192.168.123.131", port));
                read(sock);
                String text = send(sock,"3");
                if(text.equals("3")){
                    read(sock);
                    int num = Select_Index[0]+1;
                    String sending = Integer.toString(num);
                    send(sock, sending);
                    String exception = read(sock);
                    if (!exception.equals("투표없음.")) {
                        String choice = send(sock, "1");
                        if(choice.equals("1")){
                            String msg = read(sock);
                            if (msg.equals("시간오류")){
                            }
                            else {
                                title[0]=msg;
                                age[0]=Integer.parseInt(read(sock));
                                age[1]=Integer.parseInt(read(sock));
                                time[0]=read(sock);
                                time[1]=read(sock);

                                final String tmp = read(sock);
                                final int temp = Integer.parseInt(tmp);

                                for (int i = 0; i < temp; i++) {
                                    cand[i] = read(sock);              //여기서 투표 후보들 이름 가져옴
                                    final int finalI = i;
                                }
                                for (int i = Integer.parseInt(tmp); i < 10; i++) {
                                    cand[i] = "";
                                }
                            }
                        }
                    }

                } sock.close();
            }catch(IOException e){
                e.printStackTrace();
            }

            runOnUiThread(new Runnable(){
                public void run(){
                    title_view.setText(title[0]);
                    firstage_view.setText(Integer.toString(age[0]));
                    secondage_view.setText(Integer.toString(age[1]));
                    firsttime_view.setText(time[0]);
                    secondtime_view.setText(time[1]);
                    if(cand_vote[0] != -1) {
                        cand1.setText(cand[0]);
                        cand1_vote.setText(Integer.toString(cand_vote[0]));
                        if(highest_vote[0] < cand_vote[0])
                            highest_vote[0] = cand_vote[0];
                    } else{
                        text_1.setText("");
                    }
                    if(cand_vote[1] != -1) {
                        cand2.setText(cand[1]);
                        cand2_vote.setText(Integer.toString(cand_vote[1]));
                        if(highest_vote[0] < cand_vote[1])
                            highest_vote[0] = cand_vote[1];
                    } else{
                        text_2.setText("");
                    }
                    if(cand_vote[2] != -1) {
                        cand3.setText(cand[2]);
                        cand3_vote.setText(Integer.toString(cand_vote[2]));
                        if(highest_vote[0] < cand_vote[2])
                            highest_vote[0] = cand_vote[2];
                    } else{
                        text_3.setText("");
                    }
                    if(cand_vote[3] != -1) {
                        cand4.setText(cand[3]);
                        cand4_vote.setText(Integer.toString(cand_vote[3]));
                        if(highest_vote[0] < cand_vote[3])
                            highest_vote[0] = cand_vote[3];
                    } else{
                        text_4.setText("");
                    }
                    if(cand_vote[4] != -1) {
                        cand5.setText(cand[4]);
                        cand5_vote.setText(Integer.toString(cand_vote[4]));
                        if(highest_vote[0] < cand_vote[4])
                            highest_vote[0] = cand_vote[4];
                    } else{
                        text_5.setText("");
                    }
                    if(cand_vote[5] != -1) {
                        cand6.setText(cand[5]);
                        cand6_vote.setText(Integer.toString(cand_vote[5]));
                        if(highest_vote[0] < cand_vote[5])
                            highest_vote[0] = cand_vote[5];
                    } else{
                        text_6.setText("");
                    }
                    if(cand_vote[6] != -1) {
                        cand7.setText(cand[6]);
                        cand7_vote.setText(Integer.toString(cand_vote[6]));
                        if(highest_vote[0] < cand_vote[6])
                            highest_vote[0] = cand_vote[6];
                    } else{
                        text_7.setText("");
                    }
                    if(cand_vote[7] != -1) {
                        cand8.setText(cand[7]);
                        cand8_vote.setText(Integer.toString(cand_vote[7]));
                        if(highest_vote[0] < cand_vote[7])
                            highest_vote[0] = cand_vote[7];
                    } else{
                        text_8.setText("");
                    }
                    if(cand_vote[8] != -1) {
                        cand9.setText(cand[8]);
                        cand9_vote.setText(Integer.toString(cand_vote[8]));
                        if(highest_vote[0] < cand_vote[8])
                            highest_vote[0] = cand_vote[8];
                    } else{
                        text_9.setText("");
                    }
                    if(cand_vote[9] != -1) {
                        cand10.setText(cand[9]);
                        cand10_vote.setText(Integer.toString(cand_vote[9]));
                        if(highest_vote[0] < cand_vote[9])
                            highest_vote[0] = cand_vote[9];
                    } else{
                        text_10.setText("");
                    }

                    if(cand_vote[0] != -1) {
                        if(highest_vote[0] == cand_vote[0]){
                            cand1_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[1] != -1) {
                        if(highest_vote[0] == cand_vote[1]){
                            cand2_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[2] != -1) {
                        if(highest_vote[0] == cand_vote[2]){
                            cand3_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[3] != -1) {
                        if(highest_vote[0] == cand_vote[3]){
                            cand4_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[4] != -1) {
                        if(highest_vote[0] == cand_vote[4]){
                            cand5_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[5] != -1) {
                        if(highest_vote[0] == cand_vote[5]){
                            cand6_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[6] != -1) {
                        if(highest_vote[0] == cand_vote[6]){
                            cand7_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[7] != -1) {
                        if(highest_vote[0] == cand_vote[7]){
                            cand8_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[8] != -1) {
                        if(highest_vote[0] == cand_vote[8]){
                            cand9_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }
                    if(cand_vote[9] != -1) {
                        if(highest_vote[0] == cand_vote[9]){
                            cand10_vote.setTextColor(Color.parseColor("#00c1f2"));
                        }
                    }

                    vote.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                            intent.putExtra("Username", username[0]);
                            intent.putExtra("Userage", userage[0]);
                            startActivity(intent);
                            finish();
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
