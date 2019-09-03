
package com.example.to_do;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class edit_task extends AppCompatActivity
{
    DatabaseHelper mydb;
    String taskid, taskname, taskdate, tasktime, mytime, catid, catname;
    EditText txtname, txtDate, txtTime;
    TextView tvtaskname,tvid;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView iconsave, btnDatePicker, btnTimePicker, btncancle, iconedit, iconback;
    ArrayList<String>list_id=new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        getSupportActionBar().hide();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvid = (TextView) findViewById(R.id.tvtaskidedit);
        tvtaskname = (TextView) findViewById(R.id.tvtaskname);

        txtname = (EditText) findViewById(R.id.txtname);
        txtDate = (EditText) findViewById(R.id.txtdate);
        txtTime = (EditText) findViewById(R.id.txttime);

        iconsave = (ImageView) findViewById(R.id.saveicon);
        iconedit = (ImageView) findViewById(R.id.editicon);
        iconback = (ImageView) findViewById(R.id.backicon);
        btncancle = (ImageView) findViewById(R.id.cancle_icon);
        btnDatePicker = (ImageView) findViewById(R.id.btn_dateicon);
        btnTimePicker = (ImageView) findViewById(R.id.btn_timeicon);

        txtname.setEnabled(false);
        txtDate.setEnabled(false);
        txtTime.setEnabled(false);
        btnDatePicker.setEnabled(false);
        btnTimePicker.setEnabled(false);

        mydb = new DatabaseHelper(edit_task.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskid = extras.getString("taskId");
            taskname = extras.getString("taskname");
            taskdate = extras.getString("taskdate");
            tasktime = extras.getString("tasktime");
        }

        txtname.setText(taskname);
        txtDate.setText(taskdate);
        txtTime.setText(tasktime);

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(edit_task.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                return false;
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(edit_task.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txtTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(edit_task.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hrs;
                        String min;
                        if (hourOfDay < 10) {
                            hrs = "0" + hourOfDay;
                        } else {
                            hrs = "" + hourOfDay;
                        }
                        if (minute < 10) {
                            min = "0" + minute;
                        } else {
                            min = "" + minute;
                        }
                        mytime = (hrs + ":" + min);

                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12) {
                            AM_PM = " PM";
                            if (hourOfDay >= 13 && hourOfDay < 24) {
                                hourOfDay -= 12;
                            } else {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10) {
                            mm_precede = "0";
                        }
                        txtTime.setText(hourOfDay + ":" + mm_precede + minute + AM_PM);
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(edit_task.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hrs;
                        String min;
                        if (hourOfDay < 10) {
                            hrs = "0" + hourOfDay;
                        } else {
                            hrs = "" + hourOfDay;
                        }
                        if (minute < 10) {
                            min = "0" + minute;
                        } else {
                            min = "" + minute;
                        }
                        mytime = (hrs + ":" + min);

                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12) {
                            AM_PM = " PM";
                            if (hourOfDay >= 13 && hourOfDay < 24) {
                                hourOfDay -= 12;
                            } else {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10) {
                            mm_precede = "0";
                        }
                        txtTime.setText(hourOfDay + ":" + mm_precede + minute + AM_PM);
                    }

                }, mHour, mMinute, false);
                timePickerDialog.show();
            }

        });

        iconsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = taskid;

                if(txtname.getText().toString().length() == 0)
                {
                    Toast.makeText(edit_task.this, "Please Enter Task Name!", Toast.LENGTH_SHORT).show();
                    txtname.setFocusable(true);
                }
                else if(txtDate.getText().toString().length() == 0)
                {
                    Toast.makeText(edit_task.this, "Please Enter Time!", Toast.LENGTH_SHORT).show();
                    txtDate.setFocusable(true);
                }
                else if(txtTime.getText().toString().length() == 0)
                {
                    Toast.makeText(edit_task.this, "Please Enter Date!", Toast.LENGTH_SHORT).show();
                    txtTime.setFocusable(true);
                }
                else {
                    boolean isInserted = mydb.update_editTask(txtname.getText().toString(), txtDate.getText().toString(), mytime, txtTime.getText().toString(), id);

                    if (isInserted == true) {
                        Toast.makeText(edit_task.this, "Task Updated", Toast.LENGTH_SHORT).show();
                        clearText();
                        finish();
                    } else {
                        Toast.makeText(edit_task.this, "Task is not updated", Toast.LENGTH_SHORT).show();
                    }

                    iconedit.setVisibility(View.VISIBLE);
                    iconsave.setVisibility(View.GONE);
                    iconback.setVisibility(View.VISIBLE);
                    btncancle.setVisibility(View.GONE);
                }

            }
        });

        iconedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtname.setEnabled(true);
                txtDate.setEnabled(true);
                txtTime.setEnabled(true);
                btnDatePicker.setEnabled(true);
                btnTimePicker.setEnabled(true);

                iconsave.setVisibility(View.VISIBLE);
                iconedit.setVisibility(View.GONE);
                btncancle.setVisibility(View.VISIBLE);
                iconback.setVisibility(View.GONE);
            }
        });

        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(edit_task.this);
                alertDialog.setTitle("Discard changes?");
                alertDialog.setMessage("Do you want to discard changes?");
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.setPositiveButton(
                        "Discard",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                clearText();
                                iconback.setVisibility(View.VISIBLE);
                                btncancle.setVisibility(View.GONE);
                                iconedit.setVisibility(View.VISIBLE);
                                iconsave.setVisibility(View.GONE);
                                txtname.setEnabled(false);
                                txtDate.setEnabled(false);
                                txtTime.setEnabled(false);
                                data();
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

        iconback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void clearText()
    {
        txtname.setText("");
        txtDate.setText("");
        txtTime.setText("");
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
        }
        txtname.setText(taskname);
        txtDate.setText(taskdate);
        txtTime.setText(tasktime);
    }
}

