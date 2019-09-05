package com.example.to_do;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class frag_group_category extends Fragment implements CategoryAdapter.SendDataToFragment {
    Context context;
    DatabaseHelper mydb;
    SQLiteDatabase db;
    RecyclerView listview;
    CategoryAdapter categoryAdapter;
    ImageView imageView;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ArrayList<String> list_id, list_cat_name, list_cat_fav, list_count, list_countp;
    RelativeLayout rl, rl2, emptyadd_click;
    boolean check=true;
    String catname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_group, container, false);
        imageView = (ImageView) view.findViewById(R.id.fav);
        listview = (RecyclerView) view.findViewById(R.id.Recyclelistview_task);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshcategories);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        rl2 = (RelativeLayout) view.findViewById(R.id.rl2);
        emptyadd_click = (RelativeLayout) view.findViewById(R.id.emptyadd_click);

        db = getActivity().openOrCreateDatabase("todo", Context.MODE_PRIVATE, null);
        mydb = new DatabaseHelper(getActivity());

        list_id = new ArrayList<>();
        list_cat_name = new ArrayList<>();
        list_cat_fav = new ArrayList<>();
        list_count = new ArrayList<>();
        list_countp = new ArrayList<>();

        refresh();
/*        Bundle bundle = this.getArguments();
        if(bundle!=null){
            strtext=bundle.getString("refresh");
            Log.i("aasdasd",strtext);
            if (strtext.equals("refresh"))
            {
                refresh();
            }
        }
*/
    emptyadd_click.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_group_layout);		//create xml file

        // set the custom dialog components - text, image and button
        final EditText input = (EditText) dialog.findViewById(R.id.txtgroup_name);

        TextView add = (TextView) dialog.findViewById(R.id.btnaddpop_up);
        final TextView can = (TextView) dialog.findViewById(R.id.btncancelpop_up);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(input.getText().toString().length()> 30)
                {
                    check =false;
                    //Toast.makeText(Tabbed.this, "Please enter only 30 characters!!!", Toast.LENGTH_SHORT).show();
                    input.setError("30 characters ONLY !");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(check==true) {
                    catname = input.getText().toString();
                    if (catname == null || catname.isEmpty()) {
                        catname = "New Group";
                    }

                    boolean isInserted = mydb.insertData(catname);
                    if (isInserted == true) {
                        Toast.makeText(getActivity(), "Task Group Successfully added.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), Tabbed.class);
//                        startActivity(intent);
                        sendData("add","2");

                        dialog.cancel();
                    } else {
                        input.setError("Group Name Empty!");
                        input.requestFocus();
                        Toast.makeText(getActivity(), "Category is not added.", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });

        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Log.i("asd","ok");
        dialog.show();

    }
});


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

    ItemTouchHelper.SimpleCallback item_delete = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
            alertDialog.setMessage("Do you really want to delete this, it may contain tasks!!!");
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.setCancelable(false);
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
                            categoryAdapter.notifyDataSetChanged();
                            sendData("add","2");
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
        categoryAdapter = new CategoryAdapter(getActivity(), frag_group_category.this, list_id, list_cat_name, list_cat_fav, list_count, list_countp);
        new ItemTouchHelper(item_delete).attachToRecyclerView(listview);
        new ItemTouchHelper(item_edit).attachToRecyclerView(listview);
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
