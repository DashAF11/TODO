package com.example.to_do;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Task_list extends AppCompatActivity
{
    Context context;
    DatabaseHelper mydb;
    RecyclerView listview;
    RelativeLayout r2,emptyadd_click;
    LinearLayout r1;
    TaskListAdapter listAdapter;
    SwipeRefreshLayout mySwipeRefreshLayout;
    String catId, catName, str;
    ImageView btn_newtask;
    EditText txtcat_name;
    ArrayList<String> list_task_id, list_task_name, list_task_date, list_task_time, list_task_status, list_task_pref;
    FloatingActionButton filter;
    Spinner spinner;
    String [] arraySpinner = {"By HIGH(Red)","By MEDIUM(Orange)","By LOW(Green)","BY Time Created","By Alphabetical Order","Clear All"};
    Switch toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        getSupportActionBar().hide();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mydb = new DatabaseHelper(Task_list.this);

        btn_newtask=(ImageView)findViewById(R.id.add_newtask);

        txtcat_name=(EditText) findViewById(R.id.txtcat_name);
        txtcat_name.setEnabled(false);

        listview = (RecyclerView) findViewById(R.id.Recyclelistview_taskList);
        mySwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        filter=(FloatingActionButton)findViewById(R.id.filter);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        r1=(LinearLayout)findViewById(R.id.r1);
        r2=(RelativeLayout) findViewById(R.id.r2);
        emptyadd_click=(RelativeLayout)findViewById(R.id.emptyadd_click);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());

        list_task_id = new ArrayList<>();
        list_task_name = new ArrayList<>();
        list_task_date = new ArrayList<>();
        list_task_time = new ArrayList<>();
        list_task_status = new ArrayList<>();
        list_task_pref = new ArrayList<>();

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

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select Color Preference");
        spinner.setAdapter(adapter);

        String myString = "Clear All"; //the value you want the position for
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(myString);
        spinner.setSelection(spinnerPosition);
//
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
                spinner.setVisibility(View.VISIBLE);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                spinner.getItemAtPosition(position);

                    String str = spinner.getItemAtPosition(position).toString();
                    spinner.setVisibility(View.GONE);
                    switch(str){

                        case "Clear All":
                            Toast.makeText(Task_list.this,"Cleared sorting filter",Toast.LENGTH_SHORT).show();
                            refresh();
                        break;

                        case  "By HIGH(Red)":

                            clearlist();
                            Cursor res = mydb.color_prefdata(catId,"high");
                            if(res.getCount() == 0)
                            {
                                Log.i("xxxxx_countRed","empty");
                            }
                            else {
                            }
                            while (res.moveToNext())
                            {
                                list_task_id.add(res.getString(0));
                                list_task_name.add(res.getString(1));
                                list_task_date.add(res.getString(2));
                                list_task_time.add(res.getString(4));
                                list_task_status.add((res.getString(5)));
                                list_task_pref.add(res.getString(7));
                                Toast.makeText(Task_list.this,"By High Preference",Toast.LENGTH_SHORT).show();
                            }
                            res.close();
                            adapterjoin();

                            break;

                        case  "By MEDIUM(Orange)":

                            clearlist();
                            Cursor res1 = mydb.color_prefdata(catId,"med");
                            if(res1.getCount() == 0)
                            {
                                Log.i("xxxxx_countgreen","empty");
                            }
                            else {
                            }
                            while (res1.moveToNext())
                            {
                                list_task_id.add(res1.getString(0));
                                list_task_name.add(res1.getString(1));
                                list_task_date.add(res1.getString(2));
                                list_task_time.add(res1.getString(4));
                                list_task_status.add((res1.getString(5)));
                                list_task_pref.add(res1.getString(7));
                                Toast.makeText(Task_list.this,"By Medium Preference",Toast.LENGTH_SHORT).show();
                            }
                            res1.close();
                            adapterjoin();

                            break;

                        case  "By LOW(Green)":

                            clearlist();
                            Cursor res2 = mydb.color_prefdata(catId,"low");
                            if(res2.getCount() == 0)
                            {
                                Log.i("xxxxx_countgreen","empty");
                            }
                            else {
                            }
                            while (res2.moveToNext())
                            {
                                list_task_id.add(res2.getString(0));
                                list_task_name.add(res2.getString(1));
                                list_task_date.add(res2.getString(2));
                                list_task_time.add(res2.getString(4));
                                list_task_status.add((res2.getString(5)));
                                list_task_pref.add(res2.getString(7));
                                Toast.makeText(Task_list.this,"By Low Preference",Toast.LENGTH_SHORT).show();
                            }
                            res2.close();
                            adapterjoin();

                            break;

                        case  "By Alphabetical Order":

                            clearlist();
                            Cursor ress = mydb.task_asc(catId);
                            if(ress.getCount() == 0)
                            {
                                Log.i("xxxxx_counalphabetical","empty");
                            }
                            else {
                            }
                            while (ress.moveToNext())
                            {
                                list_task_id.add(ress.getString(0));
                                list_task_name.add(ress.getString(1));
                                list_task_date.add(ress.getString(2));
                                list_task_time.add(ress.getString(4));
                                list_task_status.add((ress.getString(5)));
                                list_task_pref.add(ress.getString(7));

                                Toast.makeText(Task_list.this,"By Alphabetical Order ",Toast.LENGTH_SHORT).show();
                            }
                            ress.close();
                            adapterjoin();
                            break;

                        case  "BY Time Created":

                            clearlist();

                            Cursor resss = mydb.timeorder(catId);

                            if(resss.getCount() == 0)
                            {
                                Log.i("xxxxx_counttimeorder","empty");
                            }
                            else {
                            }
                            while (resss.moveToNext())
                            {
                                list_task_id.add(resss.getString(0));
                                list_task_name.add(resss.getString(1));
                                list_task_date.add(resss.getString(2));
                                list_task_time.add(resss.getString(4));
                                list_task_status.add((resss.getString(5)));
                                list_task_pref.add(resss.getString(7));

                                Toast.makeText(Task_list.this,"By Time Created",Toast.LENGTH_LONG).show();
                            }
                            resss.close();
                            adapterjoin();
                            break;
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btn_newtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Task_list.this,New_task.class);
                intent.putExtra("catId",catId);
                intent.putExtra("catName",catName);
                startActivity(intent);
            }
        });

        emptyadd_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Task_list.this,New_task.class);
                intent.putExtra("catId",catId);
                intent.putExtra("catName",catName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        txtcat_name.setEnabled(false);
    }

    ItemTouchHelper.SimpleCallback item_delete = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
            new RecyclerViewSwipeDecorator.Builder(Task_list.this, c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(Task_list.this, R.color.red))
                    .addActionIcon(R.drawable.deletewhite)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Task_list.this);
            alertDialog.setTitle("Delete this Task?");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("You may want to complete the Task before you Delete!!!");
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.setPositiveButton(
                    "Delete",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String id= list_task_id.get(viewHolder.getAdapterPosition());
                            //Log.i("qqqqqqqqq",id);
                            mydb = new DatabaseHelper(Task_list.this);
                            mydb.delete_task(id);
                            listAdapter.notifyDataSetChanged();
                            refresh();
                        }
                    }
            );
            alertDialog.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            refresh();
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        }
    };

    ItemTouchHelper.SimpleCallback item_edit = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
            new RecyclerViewSwipeDecorator.Builder(Task_list.this, c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(Task_list.this, R.color.green))
                    .addActionIcon(R.drawable.editwhite)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

                            Intent intent=new Intent(Task_list.this,edit_task.class);
                            String taskid = list_task_id.get(viewHolder.getAdapterPosition());
                            String taskname = list_task_name.get(viewHolder.getAdapterPosition());
                            String taskdate = list_task_date.get(viewHolder.getAdapterPosition());
                            String tasktime = list_task_time.get(viewHolder.getAdapterPosition());
                            String taskpref = list_task_pref.get(viewHolder.getAdapterPosition());
                            Log.i("jhgzxcv",taskpref);

                            intent.putExtra("taskId",taskid);
                            intent.putExtra("taskname",taskname);
                            intent.putExtra("taskdate",taskdate);
                            intent.putExtra("tasktime",tasktime);
                            intent.putExtra("taskpref",taskpref);

                            startActivity(intent);
        }
    };

  @SuppressLint("RestrictedApi")
    public void refresh()
    {
        listAdapter = new TaskListAdapter(this, list_task_id, list_task_name, list_task_date, list_task_time, list_task_status, list_task_pref);
        new ItemTouchHelper(item_delete).attachToRecyclerView(listview);
        new ItemTouchHelper(item_edit).attachToRecyclerView(listview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Task_list.this);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setLayoutManager(mLayoutManager);
        listview.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        clearlist();
        Cursor res = mydb.getAllDatatask(catId);
        if(res.getCount() == 0)
        {
           Log.i("Nothing found","empty");
           r1.setVisibility(View.GONE);
           r2.setVisibility(View.VISIBLE);
           filter.setVisibility(View.GONE);
        }
        else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            filter.setVisibility(View.VISIBLE);
        }
        while (res.moveToNext())
        {
            list_task_id.add(res.getString(0));
            list_task_name.add(res.getString(1));
            list_task_date.add(res.getString(2));
            list_task_time.add(res.getString(4));
            list_task_status.add(res.getString(5));
            list_task_pref.add(res.getString(7));
        }
        res.close();
    }

    public void adapterjoin()
    {
        listAdapter = new TaskListAdapter(this, list_task_id, list_task_name, list_task_date, list_task_time, list_task_status, list_task_pref);
        new ItemTouchHelper(item_delete).attachToRecyclerView(listview);
        new ItemTouchHelper(item_edit).attachToRecyclerView(listview);
        listview.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    public void clearlist()
    {
        list_task_id.clear();
        list_task_name.clear();
        list_task_date.clear();
        list_task_time.clear();
        list_task_status.clear();
        list_task_pref.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Task_list.this,Tabbed.class);
        finish();
        startActivity(intent);
    }
}
