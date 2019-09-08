package com.example.to_do;


import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class frag_delayed extends Fragment
{
    DatabaseHelper mydb;
    RecyclerView listview;
    RelativeLayout rl,r2;
    DelayedAdapter delayedAdapter;
    ArrayList<String> list_id, list_catid, list_name, list_date, list_time, list_catname, list_pref;
    SwipeRefreshLayout mySwipeRefreshLayout;
    TextView tvn,tvd,tvt,tvp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_delayed, container, false);

        listview = (RecyclerView) view.findViewById(R.id.Recyclelistview_delayed);
        mySwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefreshdelayed);
        delayedAdapter = new DelayedAdapter(getActivity(), list_id, list_name, list_date, list_time, list_catname, list_pref);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(delayedAdapter);
        delayedAdapter.notifyDataSetChanged();


        mydb = new DatabaseHelper(getActivity());

        tvn=(TextView)view.findViewById(R.id.tv_delayed_name);
        tvd=(TextView)view.findViewById(R.id.tv_delayed_date);
        tvt=(TextView)view.findViewById(R.id.tv_delayed_time);
        tvp=(TextView)view.findViewById(R.id.tv_delayed_pref);
        rl=(RelativeLayout)view.findViewById(R.id.rl);
        r2=(RelativeLayout) view.findViewById(R.id.r2);

        list_id = new ArrayList<>();
        list_catid = new ArrayList<>();
        list_name = new ArrayList<>();
        list_date = new ArrayList<>();
        list_time = new ArrayList<>();
        list_catname = new ArrayList<>();
        list_pref=new ArrayList<>();

//Log.i("insaDAd","insideddelayfrag");

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


    ItemTouchHelper.SimpleCallback item = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
            new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red))
                    .addActionIcon(R.drawable.deletewhite)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Delete this Group?");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Do you really want to delete this, it may contain tasks!!!");
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.setPositiveButton(
                    "Delete",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String id= list_id.get(viewHolder.getAdapterPosition());

                            mydb = new DatabaseHelper(getActivity());
                            mydb.delete_category(id);
                            mydb.deleteall_task(id);
                            delayedAdapter.notifyDataSetChanged();
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
            new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green))
                    .addActionIcon(R.drawable.editwhite)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            // ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.edit_groupname_layout_popup, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);
            final EditText input = (EditText) dialogView.findViewById(R.id.txtgroup_name);
            final TextView add = (TextView) dialogView.findViewById(R.id.btneditpop_up);
            final TextView can = (TextView) dialogView.findViewById(R.id.btncancelpop_up);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydb = new DatabaseHelper(getActivity());

                    String catid = list_id.get(viewHolder.getAdapterPosition());
                    String str = input.getText().toString();

                    boolean isUpdated = mydb.update_catname(str, catid);
                    if (isUpdated == true)
                        Toast.makeText(getActivity(), "Group Category is Updated", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), "Group Category is not Updated", Toast.LENGTH_LONG).show();
                    refresh();
                    alertDialog.dismiss();
                }
            });

            can.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refresh();
                    alertDialog.dismiss();
                }
            });

            dialogBuilder.create();
            alertDialog.show();
        }
    };

    public void refresh()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        delayedAdapter = new DelayedAdapter(getActivity(), list_id, list_name, list_date, list_time, list_catname,list_pref);
        new ItemTouchHelper(item).attachToRecyclerView(listview);
        new ItemTouchHelper(item_edit).attachToRecyclerView(listview);
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(delayedAdapter);
        delayedAdapter.notifyDataSetChanged();

        list_name.clear();
        list_catid.clear();
        list_catname.clear();
        list_date.clear();
        list_time.clear();
        list_id.clear();
        list_pref.clear();

        Cursor res = mydb.time_exceed();
//        Log.i("asdasdasd", String.valueOf(res.getString(0)));
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
            list_pref.add(res.getString(7));
            //Log.i("delayedadataaaaaa" ,list_id+"\n"+list_name+"\n"+list_date+"\n"+list_time+"\n"+list_catid);
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
