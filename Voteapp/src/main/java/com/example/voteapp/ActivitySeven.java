package com.example.voteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivitySeven extends Activity {
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityseven);

        Intent intent = getIntent();
        final String[] username = new String[1];
        final String[] userage = new String[1];
        username[0] = intent.getStringExtra("Username");
        userage[0] = intent.getStringExtra("Userage");

        Button returnsecond = (Button) findViewById(R.id.returnsecond);
        returnsecond.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("Username", username[0]);
                intent.putExtra("Userage", userage[0]);
                startActivity(intent);
                finish();
            }
        });
    }
}
