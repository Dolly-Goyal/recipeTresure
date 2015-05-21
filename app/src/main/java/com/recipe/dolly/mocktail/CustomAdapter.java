package com.recipe.dolly.mocktail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DELL on 01-05-2015.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    protected Context mContext;
    protected List<String> mRecipeName;
    protected List<String> mPersonName;

    public CustomAdapter(Context context,List<String> recipeName,List<String> personName){
        super(context,R.layout.singlr_row,recipeName);
        mContext = context;
        mRecipeName = recipeName;
        mPersonName = personName;
        System.out.print("Custom Adapter Called");
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.singlr_row, null);
            holder = new ViewHolder();

            holder.recipeName = (TextView) convertView.findViewById(R.id.rId);
            holder.personName = (TextView) convertView.findViewById(R.id.uId);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.recipeName.setText(mRecipeName.get(position));
        holder.personName.setText(mPersonName.get(position));
        return convertView;
    }
    public static class ViewHolder {
        TextView recipeName;
        TextView personName;
        //ImageView image;
    }
}
