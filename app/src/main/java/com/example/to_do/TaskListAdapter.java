package com.example.to_do;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder>
{
    DatabaseHelper mydb;
    Context context;
    String taskid;
    ArrayList<String> list, list_id, list_date, list_time, list_status;

    public TaskListAdapter(Context context, ArrayList<String> list_id, ArrayList<String> list , ArrayList<String> list_date, ArrayList<String> list_time, ArrayList<String> list_status)//
    {
        this.context = context;
        this.list_id = list_id;
        this.list = list;
        this.list_date = list_date;
        this.list_time = list_time;
        this.list_status = list_status;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvtask_id, tvtask_name, name, textView_date, textView_time, textView_status;
        CheckBox checkBox;
        ImageView btndeleteicon;
        RelativeLayout btnlayout_taskClick;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.tvtask_id=(TextView) itemView.findViewById(R.id.tvtask_id);
            this.name=(TextView) itemView.findViewById(R.id.name);
            this.tvtask_name=(TextView) itemView.findViewById(R.id.tvtask_name);
            this.textView_date=(TextView) itemView.findViewById(R.id.tvTASK_DATE);
            this.textView_time=(TextView) itemView.findViewById(R.id.tvTASK_TIME);
            this.textView_status=(TextView) itemView.findViewById(R.id.tvtask_status);
            this.btndeleteicon=(ImageView)itemView.findViewById(R.id.btn_delete_taskicon);
            this.checkBox=(CheckBox)itemView.findViewById(R.id.chkstatus);
            btnlayout_taskClick=(RelativeLayout) itemView.findViewById(R.id.btnlayout_taskClick);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        holder.tvtask_id.setText(list_id.get(listPosition));

        String tmpString = list.get(listPosition);
        StringBuffer finalString;
        holder.name.setText(list.get(listPosition));
        int index = 0;

        finalString = new StringBuffer();
        while (index < tmpString.length()) {
            finalString.append(tmpString.substring(index, Math.min(index + 32,tmpString.length()))+"\n");
            index += 32;
        }
       holder.tvtask_name.setText(finalString);

        holder.textView_date.setText(list_date.get(listPosition));
        holder.textView_time.setText(list_time.get(listPosition));
        holder.textView_status.setText(list_status.get(listPosition));

        if (list_status.get(listPosition).equals("notdone"))
        {
            holder.checkBox.setChecked(false);

            holder.tvtask_name.setPaintFlags(holder.tvtask_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textView_date.setPaintFlags(holder.textView_date.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textView_time.setPaintFlags(holder.textView_time.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        else {
            holder.checkBox.setChecked(true);

            holder.tvtask_name.setPaintFlags(holder.tvtask_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView_date.setPaintFlags(holder.textView_date.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView_time.setPaintFlags(holder.textView_time.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.tvtask_name.setPaintFlags(holder.tvtask_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.textView_date.setPaintFlags(holder.textView_date.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.textView_time.setPaintFlags(holder.textView_time.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    String status = "done";
                    String id = (holder.tvtask_id.getText().toString());

                    mydb = new DatabaseHelper(context);
                    mydb.updateDatastatus(status, id);

                    list_status.set(listPosition, status);
                    notifyItemChanged(listPosition);
                }
                else
                {
                    holder.tvtask_name.setPaintFlags(holder.tvtask_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.textView_date.setPaintFlags(holder.textView_date.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.textView_time.setPaintFlags(holder.textView_time.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                    String status = "notdone";
                    String id = (holder.tvtask_id.getText().toString());

                    mydb = new DatabaseHelper(context);
                    mydb.updateDatastatus(status, id);
                }
            }
        });

        holder.btnlayout_taskClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, edit_task.class);
                intent.putExtra("taskId", holder.tvtask_id.getText().toString());
                intent.putExtra("taskname", holder.name.getText().toString());
                intent.putExtra("taskdate", holder.textView_date.getText().toString());
                intent.putExtra("tasktime", holder.textView_time.getText().toString());
                context.startActivity(intent);
            }
        });

        holder.btndeleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder((Activity) v.getContext());
                alertDialog.setTitle("Delete this Task?");
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                mydb = new DatabaseHelper(context);
                                taskid = holder.tvtask_id.getText().toString();
                                mydb.delete_task(taskid);
                                dialog.cancel();
                            }
                        });

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
        return list.size();
    }
}