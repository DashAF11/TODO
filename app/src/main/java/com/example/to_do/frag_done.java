package com.example.to_do;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class frag_done extends Fragment implements DoneAdapter.SendDataToFragment
{
    Context context;
    DatabaseHelper mydb;
    SQLiteDatabase db;
    RecyclerView listview;
    RelativeLayout rl,r2;
    SwipeRefreshLayout mySwipeRefreshLayout;
    DoneAdapter doneAdapter;
    ArrayList<String>list_id, list_catid, list_catname, list_date, list_name, list_time, list_pref;
    TextView tvi, tvn, tvd, tvt, tvp;
    FloatingActionButton clearcompleted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_done, container, false);
        tvi=(TextView)view.findViewById(R.id.tv_taskid) ;
        tvn=(TextView)view.findViewById(R.id.tv_taskname) ;
        tvd=(TextView)view.findViewById(R.id.tv_taskdate) ;
        tvt=(TextView)view.findViewById(R.id.tv_tasktime) ;
        tvp=(TextView)view.findViewById(R.id.tvtask_pref) ;
        mySwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefreshdonetask);
        clearcompleted=(FloatingActionButton)view.findViewById(R.id.clearall);

        rl=(RelativeLayout)view.findViewById(R.id.rl);
        r2=(RelativeLayout) view.findViewById(R.id.r2);

        listview = (RecyclerView) view.findViewById(R.id.Recyclelistview_done);

        list_id=new ArrayList<>();
        list_catid=new ArrayList<>();
        list_catname=new ArrayList<>();
        list_name=new ArrayList<>();
        list_date=new ArrayList<>();
        list_time=new ArrayList<>();
        list_pref=new ArrayList<>();

        db = getActivity().openOrCreateDatabase("todo", Context.MODE_PRIVATE, null);

        mydb = new DatabaseHelper(getActivity());

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

        clearcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Delete Done Tasks?");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Do you really want to delete all Completed Tasks?");
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                mydb.deleteall_taskDONE();
                                doneAdapter.notifyDataSetChanged();
                                refresh();
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

        return view;
    }

    @SuppressLint("RestrictedApi")
    private void refresh()
    {
        doneAdapter = new DoneAdapter(frag_done.this,getActivity(), list_id, list_name, list_date, list_time, list_catid, list_catname, list_pref);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(doneAdapter);
        doneAdapter.notifyDataSetChanged();

        list_id.clear();
        list_catid.clear();
        list_name.clear();
        list_date.clear();
        list_time.clear();
        list_pref.clear();

        Cursor res = mydb.done_task();
        if(res.getCount() == 0)
        {
            Log.i("Nothing found","empty");
            rl.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            clearcompleted.setVisibility(View.GONE);
        }
        else {
            rl.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            clearcompleted.setVisibility(View.VISIBLE);
        }
        while (res.moveToNext())
        {
            list_id.add(res.getString(0));
            list_name.add(res.getString(1));
            list_date.add(res.getString(2));
            list_time.add(res.getString(4));
            list_catid.add(res.getString(6));
            list_pref.add(res.getString(7));

            //Log.i("xxxxx_done", list_id+"\n" +list_name+"\n"+list_date+"\n"+list_time+"\n"+list_catid);
        }
        res.close();

        ArrayList<String> id=list_catid;
        for(int i=0;i<=id.size()-1;i++)
        {
            Cursor res1 = mydb.catname(id.get(i));
            while (res1.moveToNext())
            {
                list_catname.add(res1.getString(1));
            }
            res1.close();
        }

        doneAdapter = new DoneAdapter(frag_done.this,getActivity(), list_id, list_name, list_date, list_time, list_catid, list_catname, list_pref);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(doneAdapter);
        doneAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendData(String type,String Data) {
        if (Data.equals("1"))
        refresh();
    }
}
