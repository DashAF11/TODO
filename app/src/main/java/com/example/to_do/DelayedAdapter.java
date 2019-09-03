package com.example.to_do;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class DelayedAdapter extends RecyclerView.Adapter<DelayedAdapter.MyViewHolder>
{
    Context context;
    ArrayList<String>list_id;
    ArrayList<String> list_name;
    ArrayList<String>list_date;
    ArrayList<String>list_time;
    ArrayList<String> list_catname;

    public DelayedAdapter(Context context, ArrayList<String>list_id, ArrayList<String> list_name, ArrayList<String>list_date, ArrayList<String> list_time, ArrayList<String> list_catname){
        this.context = context;
        this.list_id = list_id;
        this.list_name = list_name;
        this.list_date = list_date;
        this.list_time = list_time;
        this.list_catname = list_catname;
   }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvid, tvname,tvdate,tvtime,tvcatname;
        RelativeLayout click;

        public MyViewHolder( View itemView) {
            super(itemView);
          this.click=(RelativeLayout)itemView.findViewById(R.id.relative_delay);
          this.tvcatname=(TextView) itemView.findViewById(R.id.catnamedelayed);
          this.tvid=(TextView) itemView.findViewById(R.id.tv_delayed_id);
          this.tvname=(TextView)itemView.findViewById(R.id.tv_delayed_name);
          this.tvdate=(TextView) itemView.findViewById(R.id.tv_delayed_date);
          this.tvtime=(TextView) itemView.findViewById(R.id.tv_delayed_time);
        }
    }

   @Override
    public DelayedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewtype)
   {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.delayed_list_layout, parent, false);
       MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int listPosition)
    {
        String tmpString = list_name.get(listPosition);
         StringBuffer finalString;
        int index =0;

        finalString = new StringBuffer();
        while (index < tmpString.length())
        {
            finalString.append(tmpString.substring(index, Math.min(index +30, Integer.parseInt(tmpString.length()+"\n"))));
            index += 30;

        }
        holder.tvname.setText(finalString);
        holder.tvid.setText(list_id.get(listPosition));
        holder.tvcatname.setText(list_catname.get(listPosition));
        holder.tvdate.setText(list_date.get(listPosition));
        holder.tvtime.setText(list_time.get(listPosition));

       holder.click.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, edit_task.class);
               intent.putExtra("data", "back");
               intent.putExtra("taskid", holder.tvid.getText().toString());
               intent.putExtra("taskname", holder.tvname.getText().toString());
               intent.putExtra("taskdate", holder.tvdate.getText().toString());
               intent.putExtra("tasktime", holder.tvtime.getText().toString());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return list_name.size();
    }
}
