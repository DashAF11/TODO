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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class frag_group_category extends Fragment implements CategoryAdapter.SendDataToFragment
{
    Context context;
    DatabaseHelper mydb;
    SQLiteDatabase db;
    RecyclerView listview;
    CategoryAdapter categoryAdapter;
    ImageView imageView;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ArrayList<String> list_id, list_cat_name, list_cat_fav, list_count, list_countp;
    RelativeLayout rl,rl2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_group, container, false);
        imageView=(ImageView)view.findViewById(R.id.fav);
        listview = (RecyclerView) view.findViewById(R.id.Recyclelistview_task);
        mySwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefreshcategories);
        rl=(RelativeLayout)view.findViewById(R.id.rl);
        rl2=(RelativeLayout)view.findViewById(R.id.rl2);


        db = getActivity().openOrCreateDatabase("todo", Context.MODE_PRIVATE, null);
        mydb = new DatabaseHelper(getActivity());

        list_id = new ArrayList<>();
        list_cat_name = new ArrayList<>();
        list_cat_fav = new ArrayList<>();
        list_count = new ArrayList<>();
        list_countp = new ArrayList<>();

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
        categoryAdapter = new CategoryAdapter(getActivity(), frag_group_category.this, list_id, list_cat_name, list_cat_fav, list_count, list_countp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        list_id.clear();
        list_cat_name.clear();
        list_cat_fav.clear();
        list_count.clear();
        list_countp.clear();

        Cursor res = mydb.getAllcategories();
        if(res.getCount() == 0)
        {
            Log.i("Nothing found","empty");
            rl2.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
        }
        else{
            rl2.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
        }
        while (res.moveToNext())
        {
            list_id.add(res.getString(0));
            list_cat_name.add(res.getString(1));
            list_cat_fav.add(res.getString(2));
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
            c.close();

            Cursor c1 = mydb.getcatCountpending(String.valueOf(ref.get(i)));
            while (c1.moveToNext())
            {
                if(c1.getString(0) == null)
                {
                    list_countp.add("0");
                }
                else {
                    list_countp.add(c1.getString(0));
                }
            }
            c1.close();
        }
    }
    @Override
    public void sendData(String type,String Data) {
        if (Data.equals("2"))
            refresh();
    }
}
