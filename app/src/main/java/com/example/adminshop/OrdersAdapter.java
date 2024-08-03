package com.example.adminshop;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends ArrayAdapter<Order> {
    private Context mContext;
    private List<Order> ordersList;

    public OrdersAdapter(Context context, List<Order> list) {
        super(context, 0, list);
        mContext = context;
        ordersList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);

        Order currentOrder = ordersList.get(position);

        TextView phoneNumberTv = listItem.findViewById(R.id.PhoneNumberTv);
        phoneNumberTv.setText(currentOrder.getPhoneNumber());

        TextView costTextView = listItem.findViewById(R.id.costTextView);
        costTextView.setText(String.valueOf(currentOrder.getCost()));

        TextView dateTextView = listItem.findViewById(R.id.dateTextView);
        dateTextView.setText(currentOrder.getDate());

        return listItem;
    }
}