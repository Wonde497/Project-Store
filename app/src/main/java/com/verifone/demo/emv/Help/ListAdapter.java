package com.verifone.demo.emv.Help;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class ListAdapter extends ArrayAdapter<ListData> {
    private Context mContext;
    private int mResource;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<ListData> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListData helpItem = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        ImageView imageViewIcon = convertView.findViewById(R.id.imageView_icon);
        TextView textViewTitle = convertView.findViewById(R.id.textView_title);

        if (helpItem != null) {
            imageViewIcon.setImageResource(helpItem.getImageResource());
            textViewTitle.setText(helpItem.getTitle());
        }

        return convertView;
    }
}
