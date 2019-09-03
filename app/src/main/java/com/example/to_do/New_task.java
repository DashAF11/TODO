package com.example.to_do;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class New_task extends Activity
{
    DatabaseHelper mydb;
    ImageView btnsave,btnDatePicker, btnTimePicker,btnback;
    String ref,mytime,status="notdone";
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText txtn,txtd,txtt;
    TextView txtid;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnsave=(ImageView) findViewById(R.id.ssaveicon);
        btnDatePicker=(ImageView) findViewById(R.id.btn_dateicon);
        btnTimePicker=(ImageView) findViewById(R.id.btn_timeicon);
        btnback=(ImageView) findViewById(R.id.back_icon);
        txtid=(TextView)findViewById(R.id.txtid);
        txtn=(EditText)findViewById(R.id.txtname);
        txtd=(EditText)findViewById(R.id.txtdate);
        txtt=(EditText)findViewById(R.id.txttime);

        mydb = new DatabaseHelper(New_task.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            ref = extras.getString("catId");
        }

        txtd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(New_task.this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mon,day;
                        if(monthOfYear < 10) {
                            mon = "0"+(monthOfYear +1);
                        } else {
                            mon = ""+(monthOfYear+1);
                        }
                        if(dayOfMonth < 10) {
                            day = "0"+dayOfMonth;
                        }else {
                            day = ""+dayOfMonth;
                        }
                        txtd.setText(day + "-" + mon + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                return false;
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(New_task.this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mon,day;
                        if(monthOfYear < 10) {
                            mon = "0"+(monthOfYear +1);
                        } else {
                            mon = ""+(monthOfYear+1);
                        }
                        if(dayOfMonth < 10) {
                            day = "0"+dayOfMonth;
                        }else {
                            day = ""+dayOfMonth;
                        }
                        txtd.setText(day + "-" + mon + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        txtt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(New_task.this,new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        String hrs;
                        String min;
                        if(hourOfDay < 10) {
                            hrs = "0"+hourOfDay;
                        } else {
                            hrs = ""+hourOfDay;
                        }
                        if(minute < 10) {
                            min = "0"+minute;
                        }else {
                            min = ""+minute;
                        }
                        mytime=(hrs+":"+min);

                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12)
                        {
                            AM_PM = " PM";
                            if (hourOfDay >=13 && hourOfDay < 24)
                            {
                                hourOfDay -= 12;
                            }
                            else
                            {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10)
                        {
                            mm_precede = "0";
                        }
                        txtt.setText( hourOfDay + ":" + mm_precede + minute + AM_PM);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
                return false;
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(New_task.this,new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {

                        String hrs;
                        String min;
                        if(hourOfDay < 10) {
                            hrs = "0"+hourOfDay;
                        } else {
                            hrs = ""+hourOfDay;
                        }
                        if(minute < 10) {
                            min = "0"+minute;
                        }else {
                            min = ""+minute;
                        }
                        mytime=(hrs+":"+min);

                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12)
                        {
                            AM_PM = " PM";
                            if (hourOfDay >=13 && hourOfDay < 24)
                            {
                                hourOfDay -= 12;
                            }
                            else
                                {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10)
                        {
                            mm_precede = "0";
                        }
                        txtt.setText( hourOfDay + ":" + mm_precede + minute + AM_PM);

                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder((New_task.this));
                alertDialog.setTitle("New Task");
                alertDialog.setMessage("Do you want to save this task?");
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.setPositiveButton(
                        "Save",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(txtn.getText().toString().length() == 0)
                                {
                                    txtn.setFocusable(true);
                                    Toast.makeText(New_task.this,"Please Enter Task Name",Toast.LENGTH_SHORT).show();
                                }
                                else if(txtd.getText().toString().length() == 0)
                                {
                                    txtd.setFocusable(true);
                                    Toast.makeText(New_task.this,"Please Enter Date",Toast.LENGTH_SHORT).show();
                                }
                                if(txtt.getText().toString().length() == 0)
                                {
                                    txtt.setFocusable(true);
                                    Toast.makeText(New_task.this,"Please Enter Time",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    boolean isInserted = mydb.insertDatatask(txtn.getText().toString(), txtd.getText().toString(), mytime, txtt.getText().toString(), status, ref);
                                    if (isInserted == true) {
                                        Toast.makeText(New_task.this, "Task saved", Toast.LENGTH_SHORT).show();
                                        cleartext();
                                    } else
                                        Toast.makeText(New_task.this, "Task not saved", Toast.LENGTH_SHORT).show();
                                    cleartext();
                                }
                            }
                        }
                );
                alertDialog.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(New_task.this,Task_list.class);
                startActivity(intent);
            }
        });
    }
    public void cleartext()
    {
        txtn.setText("");
        txtd.setText("");
        txtt.setText("");
    }
}