package com.example.hsport.blackjackswitch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
    private EditText Starting;
    private Button PlayBtn;
    private Button RulesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        InitBtns();

    }

    private void InitBtns() {
        Starting = (EditText) findViewById(R.id.TxtStartingStack);
        PlayBtn = (Button) findViewById(R.id.BtnPlay);
        RulesBtn = (Button) findViewById(R.id.BtnRules);
        PlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nIntent = new Intent(Splash.this, GameActivity.class);
                nIntent.putExtra("StartingStack", Double.parseDouble(Starting.getText().toString()));
                if(Double.parseDouble(Starting.getText().toString()) < 0)
                    Toast.makeText(getBaseContext(), "Must have a value greater than 0", Toast.LENGTH_LONG).show();
                else
                    startActivity(nIntent);
            }
        });
    }
}
