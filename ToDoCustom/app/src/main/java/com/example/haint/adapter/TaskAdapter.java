package com.example.haint.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haint.holder.ItemTask;
import com.example.haint.model.TaskModel;
import com.example.haint.todocustom.R;

import java.util.ArrayList;

/**
 * Created by haint on 03/06/2017.
 */

public class TaskAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private Activity activity;
    private ArrayList<TaskModel> objects;
    private ArrayList<TaskModel> originalObjects;

    public TaskAdapter() {
    }

    public TaskAdapter(Activity activity, Context mContext, ArrayList<TaskModel> objects) {
        this.activity = activity;
        this.mContext = mContext;
        this.objects = objects;
        this.originalObjects = objects;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<TaskModel> FilteredArrList = new ArrayList<>();
                if(objects == null){
                    objects = new ArrayList<>(originalObjects);
                }
                if(constraint == null || constraint.length() == 0){
                    results.count = objects.size();
                    results.values = objects;
                }else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i< objects.size(); i++){
                        String data = objects.get(i).getNameTask();
                        if (data.toString().toLowerCase().startsWith(constraint.toString())){
                            FilteredArrList.add(objects.get(i));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                originalObjects = (ArrayList<TaskModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        if(objects!=null)
            return originalObjects.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return originalObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ItemTask holder;
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_task, parent, false);
            holder = new ItemTask();
            holder.txtColor = (TextView) convertView.findViewById(R.id.txtColor);
            holder.txtNameTask = (TextView) convertView.findViewById(R.id.txtTaskName);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.imgDetails = (ImageView) convertView.findViewById(R.id.imgDetails);
            convertView.setTag(holder);
        }else holder = (ItemTask) convertView.getTag();
        if(this.originalObjects.get(position).getPriorityLevel() == 0){
            holder.txtColor.setBackgroundResource(R.color.colorRed);
        }else {
            if(this.originalObjects.get(position).getPriorityLevel() == 1)
                holder.txtColor.setBackgroundResource(R.color.colorYellow);
            else {
                if (this.originalObjects.get(position).getPriorityLevel() == 2)
                    holder.txtColor.setBackgroundResource(R.color.colorGreen);
                else holder.txtColor.setBackgroundResource(android.R.color.transparent);
            }
        }
        if(this.originalObjects.get(position).getNameTask().isEmpty()){
            holder.txtNameTask.setText("");
        }else holder.txtNameTask.setText(this.originalObjects.get(position).getNameTask());

        if(this.originalObjects.get(position).getStatusTask() == 0){
            holder.txtStatus.setText("TO-DO");
        }else {
            if (this.originalObjects.get(position).getStatusTask() == 1) {
                holder.txtStatus.setText("DONE");
            } else holder.txtStatus.setText("");
        }

        holder.imgDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails(originalObjects.get(position));
            }
        });
        return convertView;
    }

    private void showDetails(TaskModel temp) {
        TextView detailNameTask, detailContentTask, detailDueDate, detailPLevel, detailStatus;

        View inflater = activity.getLayoutInflater().inflate(R.layout.dialog_details, null);

        detailNameTask = (TextView) inflater.findViewById(R.id.detailNamTask);
        detailDueDate = (TextView) inflater.findViewById(R.id.detailDuaDate);
        detailContentTask = (TextView) inflater.findViewById(R.id.detailContentTask);
        detailPLevel = (TextView) inflater.findViewById(R.id.detailPLevel);
        detailStatus = (TextView) inflater.findViewById(R.id.detailStatus);

        detailNameTask.setText(temp.getNameTask());
        detailDueDate.setText(temp.getmDay()+"/"+(temp.getmMonth()+1)+"/"+temp.getmYear());
        detailContentTask.setText(temp.getContentTask());

        if(temp.getPriorityLevel() == 0){
            detailPLevel.setText("HIGH");
            detailPLevel.setTextColor(Color.RED);
        }else {
            if(temp.getPriorityLevel() == 1) {
                detailPLevel.setText("MEDIUM");
                detailPLevel.setTextColor(Color.YELLOW);
            }
            else {
                if (temp.getPriorityLevel() == 2) {
                    detailPLevel.setText("LOW");
                    detailPLevel.setTextColor(Color.GREEN);
                }
                else detailPLevel.setText("");;
            }
        }

        if(temp.getStatusTask() == 0){
            detailStatus.setText("TO-DO");
        }else {
            if (temp.getStatusTask() == 1) {
                detailStatus.setText("DONE");
            } else detailStatus.setText("");
        }

        final AlertDialog.Builder dialogDetail = new AlertDialog.Builder(mContext);
        dialogDetail.setTitle("Task detail");
        dialogDetail.setView(inflater);
        dialogDetail.setCancelable(false);
        dialogDetail.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dialogDetail.create();
        alertDialog.show();
    }
}
