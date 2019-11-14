package com.cybertech.healthview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookingAdapter extends BaseAdapter {

    ArrayList<BookingModel> list;
    LayoutInflater inflater;
    Context context;
    TinyDB tinyDB;

    public BookingAdapter(Context context, ArrayList<BookingModel> objects) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = objects;
        this.context = context;
        tinyDB = new TinyDB(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BookingModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row, parent, false);

            holder.nameTV = (TextView) convertView.findViewById(R.id.row_name);
            holder.timeTV = (TextView) convertView.findViewById(R.id.row_time);
            holder.message = (TextView) convertView.findViewById(R.id.row_message);
            holder.cardView = (CardView) convertView.findViewById(R.id.rowCard);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTV.setText(list.get(position).getName());
        holder.timeTV.setText(list.get(position).getTime());
        holder.message.setText(list.get(position).getMessage());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.putObject("patient",getItem(position));

                Intent intent = new Intent(context, DetailPatientActivity.class).putExtra("message", list.get(position).getMessage());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView nameTV;
        TextView timeTV;
        TextView message;
        CardView cardView;
    }
}
