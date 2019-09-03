package com.example.to_do;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>
{
    private SendDataToFragment sendDataToFragment;
    DatabaseHelper mydb;
    Context context;
    ArrayList<String> category_id, category, cat_fav, count, countp;

    public CategoryAdapter(Context context, frag_group_category frag_group_category, ArrayList<String> id, ArrayList<String> category, ArrayList<String> cat_fav, ArrayList<String>count, ArrayList<String>countp)
    {
        super();
        this.context = context;
        this.sendDataToFragment = (SendDataToFragment) frag_group_category;
        this.category_id = id;
        this.category = category;
        this.cat_fav = cat_fav;
        this.count=count;
        this.countp=countp;
    }

    public interface SendDataToFragment {
        void sendData(String type,String Data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        //define things here
        TextView tvcat_name,txtcat_id,count_cat,countp;
        ImageView fav_id,btnicondelete;
        RelativeLayout btnnext;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.count_cat=(TextView)itemView.findViewById(R.id.count_task);
            this.countp=(TextView)itemView.findViewById(R.id.count_pendingtask);
            this.txtcat_id=(TextView) itemView.findViewById(R.id.cat_id);
            this.tvcat_name = (TextView) itemView.findViewById(R.id.cat_name);
            this.fav_id = (ImageView) itemView.findViewById(R.id.fav);
            this.btnnext = (RelativeLayout) itemView.findViewById(R.id.btnrelative_categoryonclick);
            this.btnicondelete=(ImageView)itemView.findViewById(R.id.cat_del);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.countp.setText(countp.get(listPosition));
        holder.count_cat.setText(count.get(listPosition));
       holder.tvcat_name.setText(category.get(listPosition));
       holder.txtcat_id.setText(category_id.get(listPosition));

       if(cat_fav.get(listPosition).equals("notfav"))
       {
           holder.fav_id.setImageResource(R.drawable.notlike);
       }
       else
           {
           holder.fav_id.setImageResource(R.drawable.like);
       }

       holder.fav_id.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               if(cat_fav.get(listPosition).equals("notfav"))
               {
                   String status="fav";
                   String id =(holder.txtcat_id.getText().toString());

                   mydb = new DatabaseHelper(context);

                   mydb.update_cat_fav(status,id);
                   holder.fav_id.setImageResource(R.drawable.like);

                    cat_fav.set(listPosition,"fav");//onscreen list refresh
                    notifyItemChanged(listPosition);
               }
               else if(cat_fav.get(listPosition).equals("fav"))
               {
                   String status="notfav";
                   String id =(holder.txtcat_id.getText().toString());

                   mydb = new DatabaseHelper(context);
                   mydb.update_cat_fav(status,id);

                   holder.fav_id.setImageResource(R.drawable.notlike);

                   cat_fav.set(listPosition,"notfav");
                   notifyItemChanged(listPosition);
               }
           }
       });

       holder.btnnext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(context, Task_list.class);
               intent.putExtra("catId",holder.txtcat_id.getText());
               intent.putExtra("catName",holder.tvcat_name.getText());
               ((Activity)context).finish();
               context.startActivity(intent);
           }
       });

     holder.btnicondelete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) v.getContext());
             alertDialog.setTitle("Delete this Group?");
             alertDialog.setMessage("Do you really want to delete this, it may contain tasks!!!");
             alertDialog.setIcon(R.drawable.icon);
             alertDialog.setPositiveButton(
                     "Delete",
                     new DialogInterface.OnClickListener()
                     {
                         public void onClick(DialogInterface dialog, int which)
                         {
                             String id=holder.txtcat_id.getText().toString();

                             mydb = new DatabaseHelper(context);
                             mydb.delete_category(id);
                             mydb.deleteall_task(id);
                             sendDataToFragment.sendData("add","2");
                         }
                     }
             );
             alertDialog.setNegativeButton(
                     "Cancel",
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                             dialog.cancel();
                         }
                     });

             alertDialog.show();
         }
     });
    }


    @Override
    public int getItemCount()
    {
        return category.size();
    }
}