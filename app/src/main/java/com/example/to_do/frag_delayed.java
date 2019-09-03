package com.example.to_do;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class frag_delayed extends Fragment
{
    DatabaseHelper mydb;
    RecyclerView listview;
    RelativeLayout rl,r2;
    //DelayedAdapter delayedAdapter;
    ArrayList<String> list_id, list_catid, list_name, list_date, list_time, list_catname;
    SwipeRefreshLayout mySwipeRefreshLayout;
    TextView tvn,tvd,tvt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_delayed, container, false);
        listview = (RecyclerView) view.findViewById(R.id.Recyclelistview_delayed);
        mySwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefreshdelayed);

        mydb = new DatabaseHelper(getActivity());

        tvn=(TextView)view.findViewById(R.id.tv_delayed_name);
        tvd=(TextView)view.findViewById(R.id.tv_delayed_date);
        tvt=(TextView)view.findViewById(R.id.tv_delayed_time);
        rl=(RelativeLayout)view.findViewById(R.id.rl);
        r2=(RelativeLayout) view.findViewById(R.id.r2);

        list_id = new ArrayList<>();
        list_catid = new ArrayList<>();
        list_name = new ArrayList<>();
        list_date = new ArrayList<>();
        list_time = new ArrayList<>();
        list_catname = new ArrayList<>();

       // delayedAdapter = new DelayedAdapter(getActivity(), list_id, list_name, list_date, list_time, list_catname);

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        //listview.setAdapter(delayedAdapter);
        //delayedAdapter.notifyDataSetChanged();

        list_name.clear();
        list_catid.clear();
        list_catname.clear();
        list_date.clear();
        list_time.clear();
        list_id.clear();

        Cursor res = mydb.time_exceed();
        if(res.getCount() == 0)
        {
            Log.i("Nothing found","empty");
            rl.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
        }
        else {
            rl.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
        }
        while (res.moveToNext())
        {
            list_id.add(res.getString(0));
            list_name.add(res.getString(1));
            list_date.add(res.getString(2));
            list_time.add(res.getString(4));
            list_catid.add(res.getString(6));
        }
        res.close();

        ArrayList<String> id=list_catid;
        for(int i=0; i<=id.size()-1; i++)
        {
            Cursor res1 = mydb.catname(id.get(i));
            while (res1.moveToNext()) {
                list_catname.add(res1.getString(1));
            }
            res1.close();
        }
    }
}
