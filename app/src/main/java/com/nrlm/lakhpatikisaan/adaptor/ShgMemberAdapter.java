package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.home.ShgMemberFragmentDirections;

import java.util.List;

public class ShgMemberAdapter extends RecyclerView.Adapter<ShgMemberAdapter.MyViewHolder> {

    List<MemberListDataBean> dataItem;
    Context context;
    NavController navController;

    public ShgMemberAdapter(List<MemberListDataBean> dataItem, Context context, NavController navController) {
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
        holder.itemBinding.tvMemberNameCode.setTextColor(context.getResources().getColor(R.color.green_500));
        holder.itemBinding.tvMemberNameCode.setText(dataItem.get(position).getMemberName()+"("+dataItem.get(position).getMemberCode()+")");
        String lastFilledBeforeNrlmEntry=dataItem.get(position).getLastFilledBeforeNrlmEntry();
        String lastFilledAfterNrlmEntry=dataItem.get(position).getLastFilledAfterNrlmEntry();

        if (lastFilledBeforeNrlmEntry==null){
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.beforeNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        }else {
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.beforeNrlmEntry.setText(lastFilledBeforeNrlmEntry);
        }

        if (lastFilledAfterNrlmEntry==null){
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.afterNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        }else {
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.afterNrlmEntry.setText(lastFilledBeforeNrlmEntry);
        }


        holder.itemBinding.tvGoToEntry.setOnClickListener(view -> {
            PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefSelectedMemberCode(),dataItem.get(position).getMemberCode(),context);
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
