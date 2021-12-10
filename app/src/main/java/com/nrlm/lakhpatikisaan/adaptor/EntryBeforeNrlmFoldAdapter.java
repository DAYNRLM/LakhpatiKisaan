package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.databinding.CustomEntryBeforeNrlmFoldBinding;
import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;

import java.util.List;

public class EntryBeforeNrlmFoldAdapter extends RecyclerView.Adapter<EntryBeforeNrlmFoldAdapter.MyViewHolder> {

    List<MemberEntryEntity> memberEntryDataItem;
    Context context;

    public EntryBeforeNrlmFoldAdapter(List<MemberEntryEntity> memberEntryDataItem, Context context) {
        this.memberEntryDataItem = memberEntryDataItem;
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
        holder.itemBinding.tvSector.setText(memberEntryDataItem.get(position).getSectorName());
        holder.itemBinding.tvActivity.setText(memberEntryDataItem.get(position).getActivityName());
        holder.itemBinding.tvFrequency.setText(memberEntryDataItem.get(position).getIncomeFrequencyName());
        holder.itemBinding.tvIncomeRange.setText(memberEntryDataItem.get(position).getIncomeRangName());
        holder.itemBinding.tvMonth.setText(memberEntryDataItem.get(position).getMonthName());
        holder.itemBinding.tvYear.setText(memberEntryDataItem.get(position).getEntryYearCode());
    }

    @Override
    public int getItemCount() {
        return memberEntryDataItem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomEntryBeforeNrlmFoldBinding itemBinding;
        public MyViewHolder(@NonNull CustomEntryBeforeNrlmFoldBinding rootView) {
            super(rootView.getRoot());
            itemBinding = rootView;
        }
    }
}
