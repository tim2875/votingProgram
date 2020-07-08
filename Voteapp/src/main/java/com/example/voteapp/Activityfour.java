package com.example.voteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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
import java.util.ArrayList;

public class Activityfour extends Activity {

    /* 전역변수 */
    final String[] output_list = new String[10]; // 화면에 띄울 string 배열
    final int[] output_index = new int[10]; // 체크를 위한 index 배열
    int output_count = 0; // 최대 10개까지 갯수 판단
    final String[] username = new String[1];
    final String[] userage = new String[1];
    final ArrayList<String> end_list = new ArrayList<String>(); // 진행중인 리스트
    final ArrayList<Integer> match_index = new ArrayList<Integer>(); // 각 항목의 모든 리스트 에서의 인덱스
    int end_index = 0; // ing_list의 인덱스
    final int[] start_index = {0}; // 현재 인덱스, 페이지 이동 시 사용

    Button leftbutton;
    Button rightbutton;
    Button list1;
    Button list2;
    Button list3;
    Button list4;
    Button list5;
    Button list6;
    Button list7;
    Button list8;
    Button list9;
    Button list10;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityfour);
        Intent intent = getIntent();

        username[0] = intent.getStringExtra("Username");
        userage[0] = intent.getStringExtra("Userage");

        leftbutton = (Button) findViewById(R.id.leftbutton);
        rightbutton = (Button) findViewById(R.id.rightbutton);
        list1 = (Button) findViewById(R.id.list1);
        list2 = (Button) findViewById(R.id.list2);
        list3 = (Button) findViewById(R.id.list3);
        list4 = (Button) findViewById(R.id.list4);
        list5 = (Button) findViewById(R.id.list5);
        list6 = (Button) findViewById(R.id.list6);
        list7 = (Button) findViewById(R.id.list7);
        list8 = (Button) findViewById(R.id.list8);
        list9 = (Button) findViewById(R.id.list9);
        list10 = (Button) findViewById(R.id.list10);

        connectThread th = new connectThread();
        th.start();
    }

    class connectThread extends Thread{
        public void run(){
            int port = 5000;
            Socket sock = new Socket();
            try {
                sock.setSoTimeout(5000);
                sock.connect(new InetSocketAddress("192.168.123.131", port),5000);
                if(!sock.isConnected()){
                }
                else{
                    try{
                        read(sock);
                        String text = send(sock, "4");
                        if(text.equals("4")){
                            int num = Integer.parseInt(read(sock));
                            if(num!=0)
                            {
                                for(int i=0;i<num;i++)
                                {
                                    final String message = read(sock);	//투표제목
                                    if(message.equals("end"))
                                        break;
                                    final String get_index = read(sock);
                                    final int sect_index = Integer.parseInt(get_index);	//투표인덱스
                                          //스레드에서 ui갱신하는 핸들러
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                end_list.add(message);      //end_list에 일단 받아온 리스트 넣어둠
                                                match_index.add(sect_index);
                                                end_index++;
                                            }
                                        });
                                }
                            }
                            else
                                read(sock);
                        }
                        sock.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "서버와 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < 10; i++){
                        output_list[i] = "-";
                        output_index[i] = -1;
                    }

                    for(int i = 0; ; i++){
                        if(i == 10 || i == end_index) {
                            break;
                        }
                        output_list[i] = end_list.get(i);           //여기서 옮겨줌
                        output_index[i] = match_index.get(i);       //이게 전체리스트에서의 투표 위치??!!  뭐지!!
                    }

                    list1.setText(output_list[0]);
                    list2.setText(output_list[1]);
                    list3.setText(output_list[2]);
                    list4.setText(output_list[3]);
                    list5.setText(output_list[4]);
                    list6.setText(output_list[5]);
                    list7.setText(output_list[6]);
                    list8.setText(output_list[7]);
                    list9.setText(output_list[8]);
                    list10.setText(output_list[9]);

                    leftbutton.setEnabled(false);
                    if(end_index <= 10)
                        rightbutton.setEnabled(false);
                    // 좌우 버튼 클릭 시 페이지 변경 및 버튼 활성화
                    final int finalIng_index = end_index;
                    leftbutton.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            start_index[0] -= 10;
                            for(int i = 0; i < 10; i++){
                                output_list[i] = "-";
                                output_index[i] = -1;
                                if(start_index[0] + i + 1 <= finalIng_index){
                                    output_list[i] = end_list.get(start_index[0] + i);
                                    output_index[i] = match_index.get(start_index[0] + i);
                                }
                            }
                            list1.setText(output_list[0]);
                            list2.setText(output_list[1]);
                            list3.setText(output_list[2]);
                            list4.setText(output_list[3]);
                            list5.setText(output_list[4]);
                            list6.setText(output_list[5]);
                            list7.setText(output_list[6]);
                            list8.setText(output_list[7]);
                            list9.setText(output_list[8]);
                            list10.setText(output_list[9]);
                            if(start_index[0] == 0)
                                leftbutton.setEnabled(false);
                            if(start_index[0] < (finalIng_index / 10) * 10)
                                rightbutton.setEnabled(true);
                        }
                    });

                    rightbutton.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            start_index[0] += 10;
                            for(int i = 0; i < 10; i++){
                                output_list[i] = "-";
                                output_index[i] = -1;
                                if(start_index[0] + i + 1 <= finalIng_index){
                                    output_list[i] = end_list.get(start_index[0] + i);
                                    output_index[i] = match_index.get(start_index[0] + i);
                                }
                            }
                            list1.setText(output_list[0]);
                            list2.setText(output_list[1]);
                            list3.setText(output_list[2]);
                            list4.setText(output_list[3]);
                            list5.setText(output_list[4]);
                            list6.setText(output_list[5]);
                            list7.setText(output_list[6]);
                            list8.setText(output_list[7]);
                            list9.setText(output_list[8]);
                            list10.setText(output_list[9]);
                            leftbutton.setEnabled(true);
                            if(start_index[0] == (finalIng_index / 10) * 10)
                                rightbutton.setEnabled(false);
                        }
                    });

                    list1.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[0] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[0]));
                                startActivity(intent);
                            }
                        }
                    });
                    list2.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[1] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[1]));
                                startActivity(intent);
                            }
                        }
                    });
                    list3.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[2] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[2]));
                                startActivity(intent);
                            }
                        }
                    });
                    list4.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[3] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[3]));
                                startActivity(intent);
                            }
                        }
                    });
                    list5.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[4] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[4]));
                                startActivity(intent);
                            }
                        }
                    });
                    list6.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[5] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[5]));
                                startActivity(intent);
                            }
                        }
                    });
                    list7.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[6] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[6]));
                                startActivity(intent);
                            }
                        }
                    });
                    list8.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[7] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[7]));
                                startActivity(intent);
                            }
                        }
                    });
                    list9.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[8] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[8]));
                                startActivity(intent);
                            }
                        }
                    });
                    list10.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            if(output_index[9] != -1){
                                // index값을 다음 페이지에 넘겨주고 다음 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(), Activitysix.class);
                                intent.putExtra("Username", username[0]);
                                intent.putExtra("Userage", userage[0]);
                                intent.putExtra("Select_Index", Integer.toString(output_index[9]));
                                startActivity(intent);
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