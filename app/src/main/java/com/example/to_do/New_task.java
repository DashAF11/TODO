package com.example.to_do;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class New_task extends Activity
{
    DatabaseHelper mydb;
    ImageView btnsave,btnDatePicker, btnTimePicker;
    String catid,mytime,status="notdone", pref="low", catname;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TextInputEditText txtn,txtd,txtt;
    TextInputLayout tn,td,tt;
    TextView txtid;
    RadioGroup radioGroup;
    RadioButton high,med,low;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);

        aSwitch =(Switch)findViewById(R.id.switchalarmset);
        high=(RadioButton)findViewById(R.id.high);
        med=(RadioButton)findViewById(R.id.med);
        low=(RadioButton)findViewById(R.id.low);
        low.setChecked(true);
        radioGroup=(RadioGroup)findViewById(R.id.rdgroup);
        btnsave=(ImageView) findViewById(R.id.ssaveicon);
        btnDatePicker=(ImageView) findViewById(R.id.btn_dateicon);
        btnTimePicker=(ImageView) findViewById(R.id.btn_timeicon);
        txtid=(TextView)findViewById(R.id.txtid);
        tn=(TextInputLayout) findViewById(R.id.tn);
        td=(TextInputLayout)findViewById(R.id.td);
        tt=(TextInputLayout)findViewById(R.id.tt);
        txtn=(TextInputEditText) findViewById(R.id.txtname);
        txtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        txtd=(TextInputEditText)findViewById(R.id.txtdate);
        txtt=(TextInputEditText)findViewById(R.id.txttime);

        mydb = new DatabaseHelper(New_task.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            catid= extras.getString("catId");
            catname= extras.getString("catName");
        }

        txtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        txtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        (calendar.get(Calendar.DAY_OF_MONTH))

                );

                setAlarm(calendar.getTimeInMillis());
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
                                if (validateN() && validateD() && validateT())
                                {
                                    boolean isInserted = mydb.insertDatatask(txtn.getText().toString(), txtd.getText().toString(), mytime, txtt.getText().toString(), status, catid, pref);
                                    if (isInserted == true) {
                                        Toast.makeText(New_task.this, "Task saved", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(New_task.this,Task_list.class);
                                        intent.putExtra("catId",catid);
                                        intent.putExtra("catName",catname);
                                        //intent.putExtra("catName",tvcat_name.getText());
                                        finish();
                                        startActivity(intent);
                                        cleartext();
                                    } else {
                                        Toast.makeText(New_task.this, "Task not saved", Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }
                            }
                        });
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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.high) {
                    pref="high";
                } else if (checkedId == R.id.med) {
                    pref="med";
                }else if (checkedId == R.id.low) {
                    pref="low";
                }
            }
        });
    }

    public void cleartext()
    {
        txtn.setText("");
        txtd.setText("");
        txtt.setText("");
    }

    private boolean validateN()
    {
        if(txtn.getText().toString().isEmpty())
        {
            tn.setError("Task name is Empty!");
            return false;
        }
        else if(txtn.getText().toString().length()>50)
        {
            tn.setError("Too long task name!");
            return false;
        }
        else {
            tn.setError(null);
            return true;
        }

    }

    private boolean validateD()
    {
        if(txtd.getText().toString().isEmpty())
        {
            td.setError("Date is Empty!");
            return false;
        }
        else {
            td.setError(null);
            return true;
        }
    }

    private boolean validateT()
    {
        if(txtt.getText().toString().isEmpty())
        {
            tt.setError("Time is Empty!");
            return false;
        }
        else {
            tt.setError(null);
            return true;
        }
    }

    private void setAlarm(long timeInMillies)
    {
        AlarmManager alarmManager= ( AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this, MyAlarm.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC,timeInMillies,AlarmManager.INTERVAL_DAY,pendingIntent);

        Toast.makeText(this,"Alarm is been set",Toast.LENGTH_SHORT).show();
    }

}