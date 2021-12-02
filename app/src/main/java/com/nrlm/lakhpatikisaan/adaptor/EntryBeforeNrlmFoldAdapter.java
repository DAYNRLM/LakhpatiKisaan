package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.lakhpatikisaan.databinding.CustomEntryBeforeNrlmFoldBinding;
import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;

import java.util.List;

public class EntryBeforeNrlmFoldAdapter extends RecyclerView.Adapter<EntryBeforeNrlmFoldAdapter.MyViewHolder> {

    List<String> dataItem;
    Context context;

    public EntryBeforeNrlmFoldAdapter(List<String> dataItem, Context context) {
        this.dataItem = dataItem;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomEntryBeforeNrlmFoldBinding rootView = CustomEntryBeforeNrlmFoldBinding.inflate(LayoutInflater.from(context),parent,false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomEntryBeforeNrlmFoldBinding itemBinding;
        public MyViewHolder(@NonNull CustomEntryBeforeNrlmFoldBinding rootView) {
            super(rootView.getRoot());
            itemBinding = rootView;
        }
    }
}
