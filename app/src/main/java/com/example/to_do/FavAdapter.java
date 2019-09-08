package com.example.to_do;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder>
{
    DatabaseHelper mydb;
    Context context;
    ArrayList<String> list_id, list_name, list_fav,list_count,list_countp;

    public FavAdapter(Context context,ArrayList<String> list_id, ArrayList<String> list_name, ArrayList<String> list_fav, ArrayList<String> list_count, ArrayList<String> list_countp)
    {
        this.context = context;
        this.list_id = list_id;
        this.list_name = list_name;
        this.list_fav = list_fav;
        this.list_count = list_count;
        this.list_countp = list_countp;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView fav_id;
        RelativeLayout btnnext;
        TextView tvid,tvname,count,countp;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.tvid = (TextView) itemView.findViewById(R.id.tvid);
            this.tvname = (TextView) itemView.findViewById(R.id.tvname);
            this.count = (TextView) itemView.findViewById(R.id.count_catfav);
            this.countp = (TextView) itemView.findViewById(R.id.count_pendingtaskfav);
            this.fav_id = (ImageView) itemView.findViewById(R.id.fav);
            this.btnnext = (RelativeLayout) itemView.findViewById(R.id.btnrelative_categoryonclick2);
            this.fav_id=(ImageView)itemView.findViewById(R.id.fav);
        }
    }

    @Override
    public FavAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewgroup)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_task_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition)
    {
        holder.tvid.setText(list_id.get(listPosition));
        holder.count.setText(list_count.get(listPosition));
        holder.countp.setText(list_countp.get(listPosition));
        holder.tvname.setText(list_name.get(listPosition));

        holder.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Task_list.class);
                intent.putExtra("catId",holder.tvid.getText());
                intent.putExtra("catName",holder.tvname.getText());
                context.startActivity(intent);
            }
        });

        holder.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Task_list.class);
                intent.putExtra("catId",holder.tvid.getText().toString());
                intent.putExtra("catName",holder.tvname.getText().toString());
                context.startActivity(intent);
            }
        });

        if(list_fav.get(listPosition).equals("notfav"))
        {
            holder.fav_id.setImageResource(R.drawable.notlike);
        }
        else
        {
            holder.fav_id.setImageResource(R.drawable.like);
        }

        holder.fav_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_fav.get(listPosition).equals("notfav"))
                {
                    Log.i("inside if","clicked");

                    String status="fav";
                    String id =(holder.tvid.getText().toString());

                    mydb = new DatabaseHelper(context);

                    mydb.update_cat_fav(status,id);
                    holder.fav_id.setImageResource(R.drawable.like);
                    Log.i("status",status);

                    list_fav.set(listPosition,"fav");//onscreen list refresh
                    notifyItemChanged(listPosition);
                }
                else if(list_fav.get(listPosition).equals("fav"))
                {
                    Log.i("inside if","clicked");

                    String status="notfav";
                    String id =(holder.tvid.getText().toString());

                    mydb = new DatabaseHelper(context);

                    mydb.update_cat_fav(status,id);
                    holder.fav_id.setImageResource(R.drawable.notlike);
                    Log.i("status",status);

                    list_fav.set(listPosition,"notfav");
                    notifyItemChanged(listPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_name.size();
    }
}
