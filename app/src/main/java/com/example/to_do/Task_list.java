package com.example.to_do;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Task_list extends AppCompatActivity
{
    Context context;
    DatabaseHelper mydb;
    RecyclerView listview;
    RelativeLayout r2;
    LinearLayout r1;
    TaskListAdapter listAdapter;
    SwipeRefreshLayout mySwipeRefreshLayout;
    String catId, catName, str;
    ImageView btn_newtask, btnback, btnedit, btnsave, btncancle;
    EditText txtcat_name;
    ArrayList<String> list_task_id, list_task_name, list_task_date, list_task_time, list_task_status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        getSupportActionBar().hide();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mydb = new DatabaseHelper(Task_list.this);

        btn_newtask=(ImageView)findViewById(R.id.add_newtask);
        btnback=(ImageView)findViewById(R.id.back_icon);
        btnsave=(ImageView)findViewById(R.id.save);
        btncancle=(ImageView)findViewById(R.id.cancle);
        btnedit=(ImageView)findViewById(R.id.edit_catname);

        txtcat_name=(EditText) findViewById(R.id.txtcat_name);
        txtcat_name.setEnabled(false);

        listview = (RecyclerView) findViewById(R.id.Recyclelistview_taskList);
        mySwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        r1=(LinearLayout)findViewById(R.id.r1);
        r2=(RelativeLayout) findViewById(R.id.r2);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());

        list_task_id = new ArrayList<>();
        list_task_name = new ArrayList<>();
        list_task_date = new ArrayList<>();
        list_task_time = new ArrayList<>();
        list_task_status = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            catId = extras.getString("catId");
            catName = extras.getString("catName");
        }
        txtcat_name.setText(catName);
        refresh();
        mySwipeRefreshLayout.setColorSchemeColors(Color.MAGENTA);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        btn_newtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Task_list.this,New_task.class);
                intent.putExtra("catId",catId);
                startActivity(intent);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Task_list.this,Tabbed.class);
                finish();
                startActivity(intent);
            }
        });

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtcat_name.setEnabled(true);
                txtcat_name.setBackgroundColor(Color.WHITE);

                btnsave.setVisibility(View.VISIBLE);
                btncancle.setVisibility(View.VISIBLE);
                btnedit.setVisibility(View.GONE);
                btnback.setVisibility(View.GONE);
                btn_newtask.setVisibility(View.GONE);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Task_list.this);
                alertDialog.setTitle("Update Group name");
                alertDialog.setMessage("Do you want to update group name?");
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.setPositiveButton(
                        "Update",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String catid=catId;
                                str=txtcat_name.getText().toString();
                                mydb = new DatabaseHelper(Task_list.this);

                                boolean isUpdated= mydb.update_catname(str,catid);
                                if(isUpdated == true)
                                    Toast.makeText(Task_list.this,"Category is Updated",Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(Task_list.this,"Category is not Updated",Toast.LENGTH_LONG).show();

                                txtcat_name.setEnabled(false);
                                btnsave.setVisibility(View.GONE);
                                btnedit.setVisibility(View.VISIBLE);
                                btncancle.setVisibility(View.GONE);
                                btnback.setVisibility(View.VISIBLE);
                                btn_newtask.setVisibility(View.VISIBLE);
                                dialog.cancel();
                            }
                        }
                );
                alertDialog.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                txtcat_name.setEnabled(false);
                                btnsave.setVisibility(View.GONE);
                                btnedit.setVisibility(View.VISIBLE);
                                btn_newtask.setVisibility(View.VISIBLE);
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Task_list.this);
                alertDialog.setTitle("Discard changes?");
                alertDialog.setMessage("Do you want to discard changes?");
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.setPositiveButton(
                        "Discard",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                btnback.setVisibility(View.VISIBLE);
                                btnedit.setVisibility(View.VISIBLE);
                                btncancle.setVisibility(View.GONE);
                                btnsave.setVisibility(View.GONE);
                                btn_newtask.setVisibility(View.VISIBLE);
                                txtcat_name.setEnabled(false);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        txtcat_name.setEnabled(false);
    }

    public void refresh()
    {
        listAdapter = new TaskListAdapter(this, list_task_id, list_task_name, list_task_date, list_task_time, list_task_status);//
        listview.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        list_task_id.clear();
        list_task_name.clear();
        list_task_date.clear();
        list_task_time.clear();
        list_task_status.clear();

        Cursor res = mydb.getAllDatatask(catId);
        if(res.getCount() == 0)
        {
           Log.i("Nothing found","empty");
           r1.setVisibility(View.GONE);
           r2.setVisibility(View.VISIBLE);
        }
        else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
        }
        while (res.moveToNext())
        {
            list_task_id.add(res.getString(0));
            list_task_name.add(res.getString(1));
            list_task_date.add(res.getString(2));
            list_task_time.add(res.getString(4));
            list_task_status.add(res.getString(5));
        }
        res.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Task_list.this,Tabbed.class);
        finish();
        startActivity(intent);
    }
}
