package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.dbbean.GpDataBean;

import java.util.List;

public class GpDataAdaptor extends ArrayAdapter<GpDataBean> {

    public GpDataAdaptor(@NonNull Context context, @NonNull List<GpDataBean> docTypeDataList) {
        super(context, 0, docTypeDataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Nullable
    private View initView(int position, @Nullable View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_textview, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.spi);
        GpDataBean currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            String gpName=currentItem.getGpName();
            textViewName.setText(gpName);
        }
        return convertView;
    }

}
