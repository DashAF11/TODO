package com.example.to_do;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.MyViewHolder>
{
    DatabaseHelper mydb;
    Context context;
    ArrayList<String> list_id, list_catid, list_name, list_date, list_time, list_catname;
    private SendDataToFragment sendDataToFragment;

    public DoneAdapter(frag_done frag_done,Context context, ArrayList<String> list_id, ArrayList<String> list_name, ArrayList<String> list_date, ArrayList<String> list_time, ArrayList<String> list_catid, ArrayList<String> list_catname)
    {
        this.context = context;
        this.list_id = list_id;
        this.list_name = list_name;
        this.list_date = list_date;
        this.list_time = list_time;
        this.list_catid = list_catid;
        this.list_catname = list_catname;
        sendDataToFragment = (SendDataToFragment) frag_done;
    }

    //Interface to send data from adapter to fragment
    public interface SendDataToFragment {
        void sendData(String type,String Data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvi,tvd,tvn,tvt,tvcatname;
        ImageView reset;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.tvcatname=(TextView) itemView.findViewById(R.id.catname);
            this.tvi=(TextView) itemView.findViewById(R.id.tv_taskid);
            this.tvn=(TextView) itemView.findViewById(R.id.tv_taskname);
            this.tvd=(TextView) itemView.findViewById(R.id.tv_taskdate);
            this.tvt=(TextView) itemView.findViewById(R.id.tv_tasktime);
            this.reset=(ImageView)itemView.findViewById(R.id.btn_refresh);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewtype)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.done_list_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        holder.tvi.setText(list_id.get(listPosition));

        String tmpString = list_name.get(listPosition);
        StringBuffer finalString;
        int index = 0;

        finalString = new StringBuffer();
        while (index < tmpString.length()) {
            Log.i(TAG, "test = " + tmpString.substring(index, Math.min(index + 25,tmpString.length())));
            finalString.append(tmpString.substring(index, Math.min(index + 25,tmpString.length()))+"\n");
            index += 25;
        }
        holder.tvn.setText(finalString);

        holder.tvd.setText(list_date.get(listPosition));
        holder.tvt.setText(list_time.get(listPosition));
        holder.tvcatname.setText(list_catname.get(listPosition));

        holder.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendDataToFragment.sendData("add","1");
                Log.i("Refreshed Button","Clicked");

                 String status="notdone";
                 String id =(holder.tvi.getText().toString());

                 mydb = new DatabaseHelper(context);
                 mydb.updateDatastatus(status,id);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list_name.size();
    }
}
