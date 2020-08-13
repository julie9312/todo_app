package com.julie.todo2.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julie.todo2.MainActivity;
import com.julie.todo2.R;
import com.julie.todo2.todo.Todo;
import com.julie.todo2.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Todo> todoArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Todo> todoArrayList) {
        this.context = context;
        this.todoArrayList = todoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = todoArrayList.get(position);
        holder.txt_title.setText(todo.getTitle());

        String startdate=todo.getDate().substring(0, 10);
        String starttime=todo.getDate().substring(11, 16);


        holder.txt_date.setText( startdate + " "+starttime);

        if(todo.getCompleted().equals("1")){
            holder.btn_completed.setImageResource(R.drawable.btn_check_on);
        }else{
            holder.btn_completed.setImageResource(R.drawable.btn_check_off);
        }
    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_title;
        TextView txt_date;
        ImageView btn_completed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_date = itemView.findViewById(R.id.txt_date);
            btn_completed = itemView.findViewById(R.id.btn_completed);

            btn_completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ((MainActivity)context).addCompleted(position);
                }
            });
        }
    }

}




