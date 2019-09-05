package com.example.to_do;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.MyViewHolder>
{
    DatabaseHelper mydb;
    Context context;
    ArrayList<String> list_id, list_catid, list_name, list_date, list_time, list_catname, list_pref;
    private SendDataToFragment sendDataToFragment;

    public DoneAdapter(frag_done frag_done,Context context, ArrayList<String> list_id, ArrayList<String> list_name, ArrayList<String> list_date, ArrayList<String> list_time, ArrayList<String> list_catid, ArrayList<String> list_catname, ArrayList<String> list_pref)
    {
        this.context = context;
        this.list_id = list_id;
        this.list_name = list_name;
        this.list_date = list_date;
        this.list_time = list_time;
        this.list_catid = list_catid;
        this.list_catname = list_catname;
        this.list_pref = list_pref;
        sendDataToFragment = (SendDataToFragment) frag_done;
    }

    //Interface to send data from adapter to fragment
    public interface SendDataToFragment {
        void sendData(String type,String Data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvi,tvd,tvn,tvt,tvcatname,tvp;
        ImageView reset;
        RelativeLayout prefrence_color;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.tvcatname=(TextView) itemView.findViewById(R.id.catname);
            this.tvi=(TextView) itemView.findViewById(R.id.tv_taskid);
            this.tvn=(TextView) itemView.findViewById(R.id.tv_taskname);
            this.tvd=(TextView) itemView.findViewById(R.id.tv_taskdate);
            this.tvt=(TextView) itemView.findViewById(R.id.tv_tasktime);
            this.tvp=(TextView) itemView.findViewById(R.id.tvtask_pref);
            this.reset=(ImageView)itemView.findViewById(R.id.btn_refresh);
            prefrence_color=(RelativeLayout)itemView.findViewById(R.id.prefrence_color);
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
            //Log.i(TAG, "test = " + tmpString.substring(index, Math.min(index + 25,tmpString.length())));
            finalString.append(tmpString.substring(index, Math.min(index + 25,tmpString.length()))+"\n");
            index += 25;
        }
        holder.tvn.setText(finalString);
        holder.tvd.setText(list_date.get(listPosition));
        holder.tvt.setText(list_time.get(listPosition));
        holder.tvcatname.setText(list_catname.get(listPosition));
        holder.tvp.setText(list_pref.get(listPosition));

        if (list_pref.get(listPosition).equals("high"))
        {
            holder.prefrence_color.setBackgroundColor(rgb(255,0,0));
        }
        else if (list_pref.get(listPosition).equals("med"))
        {
            holder.prefrence_color.setBackgroundColor(rgb(255,140,0));
        }
        else if (list_pref.get(listPosition).equals("low"))
        {
            holder.prefrence_color.setBackgroundColor(rgb(0,128,0));
        }

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
