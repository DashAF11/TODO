package com.example.to_do;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class search_Adapter extends RecyclerView.Adapter<search_Adapter.MyViewHolder>
{
    DatabaseHelper mydb;
    Context context;
    ArrayList<String> list_id, list_name, list_date, list_time, list_type, list_status, list_count, list_pcount;

    public search_Adapter(Context context, ArrayList<String> list_id, ArrayList<String> list_name, ArrayList<String> list_date, ArrayList<String> list_time, ArrayList<String> list_type, ArrayList<String> list_status, ArrayList<String> list_count, ArrayList<String> list_pcount)
    {
        this.context = context;
        this.list_id = list_id;
        this.list_name = list_name;
        this.list_date = list_date;
        this.list_time = list_time;
        this.list_type = list_type;
        this.list_status = list_status;
        this.list_count = list_count;
        this.list_pcount = list_pcount;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView id, date, time, type, name, status, count, pcount, tvstatus, tvcount, tvpcount;
        RelativeLayout click;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.id=(TextView)itemView.findViewById(R.id.id);
            this.name=(TextView)itemView.findViewById(R.id.name);
            this.date=(TextView)itemView.findViewById(R.id.td);
            this.time=(TextView)itemView.findViewById(R.id.tt);
            this.type=(TextView)itemView.findViewById(R.id.type);
            this.click=(RelativeLayout)itemView.findViewById(R.id.click_relative_search);

            this.status=(TextView)itemView.findViewById(R.id.status_search);
            this.count=(TextView)itemView.findViewById(R.id.count_search);
            this.pcount=(TextView)itemView.findViewById(R.id.pcount_search);

            this.tvstatus=(TextView)itemView.findViewById(R.id.s);
            this.tvcount=(TextView)itemView.findViewById(R.id.task);
            this.tvpcount=(TextView)itemView.findViewById(R.id.pt);
        }
    }
    @Override
    public search_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_layout, parent, false);
         MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        holder.id.setText(list_id.get(listPosition));
        holder.name.setText(list_name.get(listPosition));
        holder.type.setText(list_type.get(listPosition));
        holder.date.setText(list_date.get(listPosition));
        holder.time.setText(list_time.get(listPosition));

        holder.status.setText(list_status.get(listPosition));
        holder.count.setText(list_count.get(listPosition));
        holder.pcount.setText(list_pcount.get(listPosition));

        if(holder.type.getText().equals("Group"))
        {
            holder.count.setVisibility(View.VISIBLE);
            holder.tvcount.setVisibility(View.VISIBLE);
            holder.pcount.setVisibility(View.VISIBLE);
            holder.tvpcount.setVisibility(View.VISIBLE);
            holder.status.setVisibility(View.INVISIBLE);
            holder.tvstatus.setVisibility(View.INVISIBLE);
        }
        else if(holder.type.getText().equals("Task"))
            {
                holder.status.setVisibility(View.VISIBLE);
                holder.tvstatus.setVisibility(View.VISIBLE);
                holder.count.setVisibility(View.INVISIBLE);
                holder.tvcount.setVisibility(View.INVISIBLE);
                holder.pcount.setVisibility(View.INVISIBLE);
                holder.tvpcount.setVisibility(View.INVISIBLE);
            }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i("click","on name");
                if(list_type.get(listPosition).equals("Group")) {
                    Intent intent = new Intent(context, Task_list.class);
                    intent.putExtra("catId", holder.id.getText());
                    intent.putExtra("catName", holder.name.getText());

                    Log.i("catid", String.valueOf(holder.id.getText()));
                    Log.i("catname", String.valueOf(holder.name.getText()));

                    context.startActivity(intent);
                }
                else {

                    Intent intent2 = new Intent(context, edit_task.class);
                    intent2.putExtra("taskId",holder.id.getText());
                    intent2.putExtra("taskname",holder.name.getText());
                    intent2.putExtra("taskdate",holder.date.getText());
                    intent2.putExtra("tasktime",holder.time.getText());

                    Log.i("taskid", String.valueOf(holder.id.getText()));
                    Log.i("taskname", String.valueOf(holder.name.getText()));
                    Log.i("taskdate", String.valueOf(holder.date.getText()));
                    Log.i("tasktime", String.valueOf(holder.time.getText()));

                    context.startActivity(intent2);
                }
                }
        });
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i("clicked","layout clicked");
                if(list_type.equals("Group"))
                {
                    Intent intent3 = new Intent(context, Task_list.class);
                    intent3.putExtra("catId",holder.id.getText());
                    intent3.putExtra("catName",holder.name.getText());

                    context.startActivity(intent3);
                }
                else if (list_type.equals("Task")){
                    Intent intent4 = new Intent(context, edit_task.class);
                    intent4.putExtra("taskId",holder.id.getText());
                    intent4.putExtra("taskname",holder.name.getText());
                    intent4.putExtra("taskdate",holder.date.getText());
                    intent4.putExtra("tasktime",holder.time.getText());

                    context.startActivity(intent4);
                }
            }
        });
    }
    @Override
    public int getItemCount() {

            return list_name.size();
    }
}
