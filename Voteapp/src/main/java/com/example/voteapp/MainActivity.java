package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button loginbtn = (Button) findViewById(R.id.loginbtn);
        final EditText name, age;

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        final String[] username = new String[1];
        final String[] userage = new String[1];

        loginbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                username[0] = name.getText().toString();
                userage[0] = age.getText().toString();
                Pattern patternkor = Pattern.compile("[가-힣]+$");
                Matcher matcherkor = patternkor.matcher(username[0]);
                Pattern patternnum = Pattern.compile("[0-9]+$");
                Matcher matchernum = patternnum.matcher(userage[0]);
                if(username[0].getBytes().length <= 0 || userage[0].getBytes().length <= 0){
                    Toast.makeText(getApplicationContext(), "정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!matcherkor.find()){
                    Toast.makeText(getApplicationContext(), "한글 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!matchernum.find()){
                    Toast.makeText(getApplicationContext(), "나이를 확인하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(userage[0]) <= 0){
                    Toast.makeText(getApplicationContext(), "나이를 확인하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                    intent.putExtra("Username", username[0]);
                    intent.putExtra("Userage", userage[0]);
                    startActivity(intent);
                    finish();
                }
            }
        });
        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    age.requestFocus();
                    return true;
                }
                return false;
            }
        });
        age.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER)
                {
                    loginbtn.performClick();
                    return true;
                }
                return false;
            }
        });
    }
}
