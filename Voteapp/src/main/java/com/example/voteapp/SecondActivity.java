package com.example.voteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        backPressCloseHandler = new BackPressCloseHandler(this);
        Button makeVote = (Button) findViewById(R.id.makeVote);
        Button doVote = (Button) findViewById(R.id.doVote);
        Button resultVote = (Button) findViewById(R.id.resultVote);

        Intent intent = getIntent();
        final String[] username = new String[1];
        final String[] userage = new String[1];
        username[0] = intent.getStringExtra("Username");
        userage[0] = intent.getStringExtra("Userage");

        makeVote.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Activityone.class);
                intent.putExtra("Username", username[0]);
                intent.putExtra("Userage", userage[0]);
                startActivity(intent);
            }
        });
        doVote.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Activitythree.class);
                intent.putExtra("Username", username[0]);
                intent.putExtra("Userage", userage[0]);
                startActivity(intent);
            }
        });
        resultVote.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Activityfour.class);
                intent.putExtra("Username", username[0]);
                intent.putExtra("Userage", userage[0]);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
class BackPressCloseHandler {
     private long backKeyPressedTime = 0;
     private Toast toast;
     private Activity activity;
     public BackPressCloseHandler(Activity context){
         this.activity = context;
     }
     public void onBackPressed(){
         if (System.currentTimeMillis() > backKeyPressedTime + 2000){
             backKeyPressedTime = System.currentTimeMillis();
             toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
             toast.show();
             return;
         }
         if (System.currentTimeMillis() <= backKeyPressedTime + 2000){
             activity.finish();
             toast.cancel();
         }
     }
}
