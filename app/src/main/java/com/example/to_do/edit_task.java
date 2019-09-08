
package com.example.to_do;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class edit_task extends AppCompatActivity
{
    DatabaseHelper mydb;
    String taskid, taskname, taskdate, tasktime,taskpref, mytime, catid, catname, pref;
    TextInputEditText txtn,txtd,txtt;
    TextInputLayout tn,td,tt;
    TextView txtid;
    RadioGroup radioGroup;
    RadioButton high,med,low;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView iconsave, btnDatePicker, btnTimePicker;
    ArrayList<String>list_id=new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        getSupportActionBar().hide();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        radioGroup=(RadioGroup)findViewById(R.id.rdgroup);

        tn=(TextInputLayout) findViewById(R.id.tn);
        td=(TextInputLayout)findViewById(R.id.td);
        tt=(TextInputLayout)findViewById(R.id.tt);
        txtn=(TextInputEditText) findViewById(R.id.txtname);

//        txtn.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                v.setFocusable(true);
//                v.setFocusableInTouchMode(true);
//                return false;
//            }
//        });
        txtd=(TextInputEditText)findViewById(R.id.txtdate);
        txtt=(TextInputEditText)findViewById(R.id.txttime);
        iconsave = (ImageView) findViewById(R.id.saveicon);
        btnDatePicker = (ImageView) findViewById(R.id.btn_dateicon);
        btnTimePicker = (ImageView) findViewById(R.id.btn_timeicon);

        btnDatePicker.setEnabled(false);
        btnTimePicker.setEnabled(false);

        mydb = new DatabaseHelper(edit_task.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskid = extras.getString("taskId");
            taskname = extras.getString("taskname");
            taskdate = extras.getString("taskdate");
            tasktime = extras.getString("tasktime");
            taskpref = extras.getString("taskpref");
        }

        txtn.setText(taskname);
        txtd.setText(taskdate);
        txtt.setText(tasktime);

        if(taskpref.equals("high"))
        {
            radioGroup.check(R.id.high);
        }
        else if(taskpref.equals("med"))
        {
            radioGroup.check(R.id.med);
        }
       else if(taskpref.equals("low"))
        {
            radioGroup.check(R.id.low);
        }

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
        txtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(edit_task.this,new DatePickerDialog.OnDateSetListener() {

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(edit_task.this,new DatePickerDialog.OnDateSetListener() {

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

                TimePickerDialog timePickerDialog = new TimePickerDialog(edit_task.this,new TimePickerDialog.OnTimeSetListener()
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(edit_task.this,new TimePickerDialog.OnTimeSetListener()
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

        iconsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateN() && validateD() && validateT())
                {
                    String id = taskid;
                    boolean isInserted = mydb.update_editTask(txtn.getText().toString(), txtd.getText().toString(), mytime, txtt.getText().toString(), id, pref);

                    if (isInserted == true) {
                        Toast.makeText(edit_task.this, "Task Updated", Toast.LENGTH_SHORT).show();
                        clearText();
                        finish();
                    } else {
                        Toast.makeText(edit_task.this, "Task is not updated", Toast.LENGTH_SHORT).show();
                    }
                    iconsave.setVisibility(View.GONE);
                }
            }
        });

        Cursor res = mydb.getRefcatID(taskid);
        if(res.getCount() == 0)
        {
            Log.i("Nothing found","empty");
        }
        while (res.moveToNext())
        {
            catid=(res.getString(6));
        }
        res.close();

        Cursor res1 = mydb.getcategoriename(catid);
        if(res1.getCount() == 0)
        {
            Log.i("Nothing found","empty");
        }
        while (res1.moveToNext())
        {
            catname=(res1.getString(1));
        }
        res1.close();
    }

    public void clearText()
    {
        txtn.setText("");
        txtd.setText("");
        txtt.setText("");
    }

    protected void data()
    {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            taskid = extras.getString("taskId");
            taskname = extras.getString("taskname");
            taskdate = extras.getString("taskdate");
            tasktime = extras.getString("tasktime");
            taskpref = extras.getString("taskpref");
        }
        txtn.setText(taskname);
        txtd.setText(taskdate);
        txtt.setText(tasktime);
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

}

