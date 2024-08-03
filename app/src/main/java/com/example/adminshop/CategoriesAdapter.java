package com.example.adminshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoriesAdapter extends ArrayAdapter<Category> {
    private Context mContext;
    private List<Category> categoryList;

    public CategoriesAdapter(Context context, List<Category> list) {
        super(context, 0, list);
        mContext = context;
        categoryList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate a new view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_new_category, parent, false);
        }

         Category category = getItem(position);

         TextView tvCategoryName = convertView.findViewById(R.id.etName);

         tvCategoryName.setText(category.getName());

         return convertView;
    }


}
