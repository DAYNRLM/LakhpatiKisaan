package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;
import com.nrlm.lakhpatikisaan.view.home.ShgMemberFragmentDirections;

import java.util.List;

public class ShgMemberAdapter extends RecyclerView.Adapter<ShgMemberAdapter.MyViewHolder> {

    List<String> dataItem;
    Context context;
    NavController navController;

    public ShgMemberAdapter(List<String> dataItem, Context context,NavController navController) {
        this.dataItem = dataItem;
        this.context = context;
        this.navController = navController;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CustomShgMemberLayoutBinding rootView = CustomShgMemberLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemBinding.tvGoToEntry.setOnClickListener(view -> {
            NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
            navController.navigate(navDirections);
        });

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomShgMemberLayoutBinding itemBinding;

        public MyViewHolder(@NonNull CustomShgMemberLayoutBinding itemView) {
            super(itemView.getRoot());

            itemBinding = itemView;
        }
    }
}
