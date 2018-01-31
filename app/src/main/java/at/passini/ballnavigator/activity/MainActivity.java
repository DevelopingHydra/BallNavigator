package at.passini.ballnavigator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import at.passini.ballnavigator.R;

public class MainActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnAchievements;
    private Button btnShop;
    private ImageButton btnHelp;
    private ImageButton btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        initButtonListener();

    }

    private void initButtons(){
        btnStart = (Button) findViewById(R.id.btnStart);
        btnAchievements = (Button) findViewById(R.id.btnAchievements);
        btnShop = (Button) findViewById(R.id.btnOpenShop);
        btnHelp = (ImageButton) findViewById(R.id.btnOpenHelp);
        btnSetting = (ImageButton) findViewById(R.id.btnOpenSetting);
    }

    private void initButtonListener(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        btnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AchievementsActivity.class);
                startActivity(intent);
            }
        });

        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}