package com.example.voteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Activityone extends Activity {

    /* 전역변수 */
    private int[] age = new int[2];
    private String[] time = new String[2];
    private String[] title = new String[1];
    private String[] cand = new String[10];
    String[] username = new String[1];
    String[] userage = new String[1];
    int candCount = 0;
    String txt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityone);
        Intent intent = getIntent();

        username[0] = intent.getStringExtra("Username");
        userage[0] = intent.getStringExtra("Userage");

        final Button makefinal = (Button) findViewById(R.id.makefinal);
        final EditText title_edit = (EditText) findViewById(R.id.title);
        final EditText firstage_edit = (EditText) findViewById(R.id.firstage);
        final EditText secondage_edit = (EditText) findViewById(R.id.secondage);
        final EditText firsttime_edit = (EditText) findViewById(R.id.firsttime);
        final EditText secondtime_edit = (EditText) findViewById(R.id.secondtime);
        final EditText cand1_edit = (EditText) findViewById(R.id.cand1);
        final EditText cand2_edit = (EditText) findViewById(R.id.cand2);
        final EditText cand3_edit = (EditText) findViewById(R.id.cand3);
        final EditText cand4_edit = (EditText) findViewById(R.id.cand4);
        final EditText cand5_edit = (EditText) findViewById(R.id.cand5);
        final EditText cand6_edit = (EditText) findViewById(R.id.cand6);
        final EditText cand7_edit = (EditText) findViewById(R.id.cand7);
        final EditText cand8_edit = (EditText) findViewById(R.id.cand8);
        final EditText cand9_edit = (EditText) findViewById(R.id.cand9);
        final EditText cand10_edit = (EditText) findViewById(R.id.cand10);

        makefinal.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try{
                    Exception e = new Exception();
                    title[0] = title_edit.getText().toString();
                    if(title[0].length() == 0 || title[0].length() > 10){
                        Toast.makeText(getApplicationContext(), "제목은 9글자 제한입니다.", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    try {
                        age[0] = Integer.parseInt(firstage_edit.getText().toString());
                        age[1] = Integer.parseInt(secondage_edit.getText().toString());
                    } catch(Exception e1){
                        Toast.makeText(getApplicationContext(), "나이를 확인하세요.", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    time[0] = firsttime_edit.getText().toString();
                    time[1] = secondtime_edit.getText().toString();

                    Pattern pnum = Pattern.compile("[0-9]+$");
                    Matcher mnum1 = pnum.matcher(time[0]);
                    Matcher mnum2 = pnum.matcher(time[1]);
                    if(age[0] <= 0){
                        Toast.makeText(getApplicationContext(), "나이를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    if(age[1] <= 0){
                        Toast.makeText(getApplicationContext(), "나이를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    if(age[1] - age[0] < 0){
                        Toast.makeText(getApplicationContext(), "나이를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    if(!mnum1.find()){
                        Toast.makeText(getApplicationContext(), "기간을 확인해주세요.\nex) 202001012000 ->\n2020년 1월 1일 20시 00분", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    if(!mnum2.find()){
                        Toast.makeText(getApplicationContext(), "기간을 확인해주세요.\nex) 202001012000 ->\n2020년 1월 1일 20시 00분", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    if(time[0].length() != 12){
                        Toast.makeText(getApplicationContext(), "기간을 확인해주세요.\nex) 202001012000 ->\n2020년 1월 1일 20시 00분", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    if(time[1].length() != 12){
                        Toast.makeText(getApplicationContext(), "기간을 확인해주세요.\nex) 202001012000 ->\n2020년 1월 1일 20시 00분", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    int[] time_check0 = new int[5];
                    int[] time_check1 = new int[5];
                    time_check0[0] = Integer.parseInt(time[0].substring(0,4));
                    time_check0[1] = Integer.parseInt(time[0].substring(4,6));
                    time_check0[2] = Integer.parseInt(time[0].substring(6,8));
                    time_check0[3] = Integer.parseInt(time[0].substring(8,10));
                    time_check0[4] = Integer.parseInt(time[0].substring(10,12));
                    time_check1[0] = Integer.parseInt(time[1].substring(0,4));
                    time_check1[1] = Integer.parseInt(time[1].substring(4,6));
                    time_check1[2] = Integer.parseInt(time[1].substring(6,8));
                    time_check1[3] = Integer.parseInt(time[1].substring(8,10));
                    time_check1[4] = Integer.parseInt(time[1].substring(10,12));
                    if(time_check0[0] > time_check1[0]){
                        Toast.makeText(getApplicationContext(), "종료 일자가 시작 일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    else if(time_check0[0] == time_check1[0]){
                        if(time_check0[1] > time_check1[1]){
                            Toast.makeText(getApplicationContext(), "종료 일자가 시작 일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else if(time_check0[1] == time_check1[1]){
                            if(time_check0[2] > time_check1[2]){
                                Toast.makeText(getApplicationContext(), "종료 일자가 시작 일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                                throw e;
                            }
                            else if(time_check0[2] == time_check1[2]){
                                if(time_check0[3] > time_check1[3]){
                                    Toast.makeText(getApplicationContext(), "종료 일자가 시작 일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                                    throw e;
                                }
                                else if(time_check0[3] == time_check1[3]){
                                    if(time_check0[4] > time_check1[4]){
                                        Toast.makeText(getApplicationContext(), "종료 일자가 시작 일자보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                                        throw e;
                                    }
                                    else if(time_check0[4] == time_check1[4]){
                                        Toast.makeText(getApplicationContext(), "시작 일자와 종료 일자가 같습니다.", Toast.LENGTH_SHORT).show();
                                        throw e;
                                    }
                                }
                            }
                        }
                    }

                    int seq_cand = 0;
                    candCount = 0;

                    cand[0] = cand1_edit.getText().toString();
                    if(cand[0].length() == 0){
                        Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        throw e;
                    }
                    else {
                        seq_cand = 1;
                        candCount++;
                    }
                    cand[1] = cand2_edit.getText().toString();
                    if(cand[1].length() != 0){
                        if(seq_cand != 1) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 2;
                            candCount++;
                        }
                    }
                    cand[2] = cand3_edit.getText().toString();
                    if(cand[2].length() != 0){
                        if(seq_cand != 2) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 3;
                            candCount++;
                        }
                    }
                    cand[3] = cand4_edit.getText().toString();
                    if(cand[3].length() != 0){
                        if(seq_cand != 3) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 4;
                            candCount++;
                        }
                    }
                    cand[4] = cand5_edit.getText().toString();
                    if(cand[4].length() != 0){
                        if(seq_cand != 4) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 5;
                            candCount++;
                        }
                    }
                    cand[5] = cand6_edit.getText().toString();
                    if(cand[5].length() != 0){
                        if(seq_cand != 5) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 6;
                            candCount++;
                        }
                    }
                    cand[6] = cand7_edit.getText().toString();
                    if(cand[6].length() != 0){
                        if(seq_cand != 6) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 7;
                            candCount++;
                        }
                    }
                    cand[7] = cand8_edit.getText().toString();
                    if(cand[7].length() != 0){
                        if(seq_cand != 7) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 8;
                            candCount++;
                        }
                    }
                    cand[8] = cand9_edit.getText().toString();
                    if(cand[8].length() != 0){
                        if(seq_cand != 8) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else {
                            seq_cand = 9;
                            candCount++;
                        }
                    }
                    cand[9] = cand10_edit.getText().toString();
                    if(cand[9].length() != 0){
                        if(seq_cand != 9) {
                            Toast.makeText(getApplicationContext(), "후보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                        else{
                            candCount++;
                        }
                    }
//스레드
                    client client1 = new client();
                    txt = client1.hostName.toString();
                    try{
                        client1.start();
                        //Toast.makeText(getApplicationContext(), time[1], Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception a){
                    }
                    finally {
                        Toast.makeText(getApplicationContext(), "조인시작", Toast.LENGTH_SHORT).show();
                        try {
                            client1.join();
                        }
                        catch(Exception a)
                        {
                            a.printStackTrace();
                        }
                        int err = client1.return_err();
                        Toast.makeText(getApplicationContext(), "err : "+err, Toast.LENGTH_SHORT).show();
                        if(err!=1) {
                            Intent intent = new Intent(getApplicationContext(), Activitytwo.class);
                            intent.putExtra("Username", username[0]);
                            intent.putExtra("Userage", userage[0]);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {

                            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                            intent.putExtra("Username", username[0]);
                            intent.putExtra("Userage", userage[0]);
                            startActivity(intent);
                            finish();

                        }
                    }
                } catch(Exception e){

                }
            }
        });

        title_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    firstage_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        firstage_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    secondage_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        secondage_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    firsttime_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        firsttime_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    secondtime_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        secondtime_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand1_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand1_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand2_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand2_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand3_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand3_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand4_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand4_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand5_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand5_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand6_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand6_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand7_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand7_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand8_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand8_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand9_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand9_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    cand10_edit.requestFocus();
                    return true;
                }
                return false;
            }
        });
        cand10_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    makefinal.performClick();
                    return true;
                }
                return false;
            }
        });



    }


    class client extends Thread {
        private Socket sock;
        String hostName = "192.168.123.131";          //여기에 본인 IP 입력
        int err=0;
        public int return_err()
        {
            return err;
        }
        public void run() {
            int port = 5000;
            int count = 0;
            Socket sock = new Socket();
            try {
               sock.setSoTimeout(5000);
               sock.connect(new InetSocketAddress(hostName, port),5000);
            } catch (Exception e) {
                err=1;
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "서버와 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                Looper.loop();
                try {
                    if (!sock.isClosed())
                        sock.close();
                }
                catch(Exception e1)
                {

                }
            }
            if(!sock.isConnected()){
            }
            else {
                try {
                    read(sock);
                    String text = send(sock, "1");
                    if (text.equals("1"))        //블록체인 입력화면
                    {
                        read(sock);
                        //테스트용 메세지, ui대체
                        //투표제목, 나이입력
                        send(sock, Integer.toString(age[0]));        //나이
                        sleep(100);

                        send(sock, Integer.toString(age[1]));
                        sleep(100);

                        //투표제목
                        send(sock, title[0]);        //투표 제목
                        sleep(100);

                        //시작시간(YYYY/MM/DD/HH/MM)
                        send(sock, time[0]);
                        sleep(200);

                        //제한시간(YYYY/MM/DD/HH/MM)
                        send(sock, time[1]);        //시간제한
                        sleep(200);
                        //----투표항목 입력--
                        count = 0;
                        while (true) {
                            if(count < candCount) {
                                read(sock);
                                String contents = send(sock, cand[count]);
                                count++;
                                sleep(100);
                            }
                            else {
                                read(sock);
                                String contents = send(sock, "gg");
                                sleep(100);
                                break;
                            }
                        }
                        read(sock);
                    }
                    sock.close();
                } catch (Exception e) {
                }
                finally {
                    try {
                        if (!sock.isClosed()) {
                            sock.close();
                        }
                    }
                    catch(Exception a)
                    {}
                }
            }
        }       //run 끝

        public String send(Socket sock, String text) {
            String data=null;
            try {
                Scanner input = new Scanner(System.in);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                data = text;
                bw.write(data+"\r\n");
                bw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        public String read(Socket sock) {
            String message = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                message = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return message;
        }
    }
}
