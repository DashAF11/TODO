package com.example.to_do;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class DelayedAdapter extends RecyclerView.Adapter<DelayedAdapter.MyViewHolder>
{
    Context context;
    ArrayList<String>list_id;
    ArrayList<String> list_name;
    ArrayList<String>list_date;
    ArrayList<String>list_time;
    ArrayList<String> list_catname;
    ArrayList<String> list_pref;

    public DelayedAdapter(Context context, ArrayList<String>list_id, ArrayList<String> list_name, ArrayList<String>list_date, ArrayList<String> list_time, ArrayList<String> list_catname, ArrayList<String> list_pref){
        this.context = context;
        this.list_id = list_id;
        this.list_name = list_name;
        this.list_date = list_date;
        this.list_time = list_time;
        this.list_catname = list_catname;
        this.list_pref = list_pref;
   }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvid, tvname,tvdate,tvtime,tvcatname,tvpref;
        RelativeLayout click, prefrence_color;


        public MyViewHolder( View itemView) {
            super(itemView);
          this.click=(RelativeLayout)itemView.findViewById(R.id.relative_delay);
            this.prefrence_color=(RelativeLayout)itemView.findViewById(R.id.prefrence_color);
          this.tvcatname=(TextView) itemView.findViewById(R.id.catnamedelayed);
          this.tvid=(TextView) itemView.findViewById(R.id.tv_delayed_id);
          this.tvname=(TextView)itemView.findViewById(R.id.tv_delayed_name);
          this.tvdate=(TextView) itemView.findViewById(R.id.tv_delayed_date);
          this.tvtime=(TextView) itemView.findViewById(R.id.tv_delayed_time);
            this.tvpref=(TextView) itemView.findViewById(R.id.tv_delayed_pref);
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
        int index = 0;

        finalString = new StringBuffer();
        while (index < tmpString.length()) {
            //Log.i(TAG, "test = " + tmpString.substring(index, Math.min(index + 25,tmpString.length())));
            finalString.append(tmpString.substring(index, Math.min(index + 30,tmpString.length()))+"\n");
            index += 30;
        }
        holder.tvname.setText(finalString);
        holder.tvid.setText(list_id.get(listPosition));
        holder.tvcatname.setText(list_catname.get(listPosition));
        holder.tvdate.setText(list_date.get(listPosition));
        holder.tvtime.setText(list_time.get(listPosition));
        holder.tvpref.setText(list_pref.get(listPosition));
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
//       holder.click.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               Intent intent = new Intent(context, edit_task.class);
//               intent.putExtra("data", "back");
//               intent.putExtra("taskid", holder.tvid.getText().toString());
//               intent.putExtra("taskname", holder.tvname.getText().toString());
//               intent.putExtra("taskdate", holder.tvdate.getText().toString());
//               intent.putExtra("tasktime", holder.tvtime.getText().toString());
//               intent.putExtra("taskpref", holder.tvpref.getText().toString());
//               context.startActivity(intent);
//           }
//       });
    }

    @Override
    public int getItemCount() {
        return list_name.size();
    }
}
