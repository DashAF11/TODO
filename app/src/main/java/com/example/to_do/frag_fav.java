package com.example.to_do;


import android.content.Context;
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

import java.util.ArrayList;

public class frag_fav extends Fragment
{
    Context context;
    DatabaseHelper mydb;
    SwipeRefreshLayout mySwipeRefreshLayout;
    RecyclerView listview;
    FavAdapter favAdapter;
    RelativeLayout r2,r1;
    ArrayList<String> list_id, list_name, list_fav, list_count, list_countp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_fav, container, false);

        listview = (RecyclerView) view.findViewById(R.id.Recyclelistview_fav);
        mySwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefreshfav);

        r1=(RelativeLayout)view.findViewById(R.id.r1);
        r2=(RelativeLayout) view.findViewById(R.id.r2);

        mydb = new DatabaseHelper(getActivity());

        list_id = new ArrayList<>();
        list_name = new ArrayList<>();
        list_fav = new ArrayList<>();
        list_count= new ArrayList<>();
        list_countp= new ArrayList<>();

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
    public void onResume()
    {
        super.onResume();
        refresh();
    }

    public void refresh()
    {
        favAdapter = new FavAdapter(getActivity(),list_id, list_name, list_fav, list_count, list_countp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(favAdapter);
        favAdapter.notifyDataSetChanged();

        list_fav.clear();
        list_id.clear();
        list_name.clear();
        list_count.clear();
        list_countp.clear();

        Cursor res = mydb.fav_cat();
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
            list_id.add(res.getString(0));
            list_name.add(res.getString(1));
            list_fav.add(res.getString(2));
        }
        res.close();

        ArrayList<String>ref=list_id;
        for(int i=0; i<ref.size(); i++)
        {
            Cursor c = mydb.getcatCount(String.valueOf(ref.get(i)));
            while (c.moveToNext())
            {
                if(c.getString(0) == null)
                {
                    list_count.add("0");
                }
                else {
                    list_count.add(c.getString(0));
                }
            }

            Cursor c1 = mydb.getcatCountpending(String.valueOf(ref.get(i)));
            Log.i("listgetindex", String.valueOf(ref.get(i)));
            while (c1.moveToNext())
            {
                if(c1.getString(0) == null)
                {
                    list_countp.add("0");
                    Log.i("tasksEMPTY", c1.getString(0).toString());
                }
                else {
                    list_countp.add(c1.getString(0));
                    Log.i("tasksdone or null", c1.getString(0));
                }
            }
            c.close();
            c1.close();
        }
    }
}
