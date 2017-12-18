package com.example.miloslavszczypka.maty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button start;
    Button vyhry;
    AutomatView v;
    TextView vsadit;
    TextView kredit;
    TextView vyhra;

    boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vyhry = (Button) findViewById(R.id.button);

        vyhry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), VyhryTabulka.class);
                startActivity(i);
            }
        });

        v = (AutomatView) findViewById(R.id.automatView);

        kredit = (TextView)findViewById(R.id.textView4);
        vyhra = (TextView)findViewById(R.id.textView3);

        v.setControls(kredit, vyhra);

        vsadit = (TextView)findViewById(R.id.editText);
        start = (Button)findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sazka = Integer.parseInt(vsadit.getText().toString());
                if(v.getKredit() - sazka > 0) {
                    v.Reset(started, sazka);
                    v.playStart();
                    started = true;
                }

            }
        });
    }
}
