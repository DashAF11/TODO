package com.example.to_do;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Button btngtstd;
    TextView tvgro, tvmov, tvtravel, tvsport, tvwork;
    CheckBox checkBoxgro, checkBoxmovies, checkBoxtravel, checkBoxsport, checkBoxwork;
    ArrayList<String> cat;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        checkFirstOpen();

        mydb = new DatabaseHelper(this);

        tvgro = (TextView) findViewById(R.id.tvgro);
        tvmov = (TextView) findViewById(R.id.tvmov);
        tvtravel = (TextView) findViewById(R.id.tvtravel);
        tvsport = (TextView) findViewById(R.id.tvsport);
        tvwork = (TextView) findViewById(R.id.tvwork);

        btngtstd = (Button) findViewById(R.id.btnstart);
        checkBoxgro = (CheckBox) findViewById(R.id.chkbgro);
        checkBoxmovies = (CheckBox) findViewById(R.id.chkbmovies);
        checkBoxtravel = (CheckBox) findViewById(R.id.chkbtravel);
        checkBoxsport = (CheckBox) findViewById(R.id.chkbsports);
        checkBoxwork = (CheckBox) findViewById(R.id.chkbwork);

        cat=new ArrayList<>();

        btngtstd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (int i = 0; i < cat.size(); i++)
                {
                    boolean isInserted = mydb.insertData(cat.get(i));
                    if(isInserted == true)
                        Toast.makeText(MainActivity.this,"Default list selected",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this,"Default list not selected",Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(MainActivity.this, Tabbed.class);
                startActivity(intent);
            }
        });

        checkBoxgro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    tvgro.setTypeface(null, Typeface.BOLD);
                    cat.add("Groceries");
                } else
                    {
                        tvgro.setTypeface(null, Typeface.NORMAL);
                    cat.remove("Groceries");
                }
            }
        });

        checkBoxmovies.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    tvmov.setTypeface(null, Typeface.BOLD);
                    cat.add("Movies to watch");
                } else {
                    tvmov.setTypeface(null, Typeface.NORMAL);
                    cat.remove("Movies to watch");
                }
            }
        });

        checkBoxtravel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    tvtravel.setTypeface(null, Typeface.BOLD);

                    cat.add("Travel");
                } else {
                    tvtravel.setTypeface(null, Typeface.NORMAL);
                    cat.remove("Travel");
                }
            }
        });

        checkBoxsport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    tvsport.setTypeface(null, Typeface.BOLD);

                    cat.add("Sports");
                } else {
                    tvsport.setTypeface(null, Typeface.NORMAL);

                    cat.remove("Sports");
                }
            }
        });

        checkBoxwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    tvwork.setTypeface(null, Typeface.BOLD);

                    cat.add("Work");
                } else
                    {
                        tvwork.setTypeface(null, Typeface.NORMAL);
                    cat.remove("Work");
                }
            }
        });
    }

    private void checkFirstOpen(){
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (!isFirstRun) {
            Intent intent = new Intent(MainActivity.this, Tabbed.class);
            finish();
            startActivity(intent);
            finish();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun",
                false).apply();
    }
}
