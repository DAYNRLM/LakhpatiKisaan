package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;
import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;
import com.nrlm.lakhpatikisaan.view.home.ShgMemberFragmentDirections;

import java.util.List;

public class ShgMemberAdapter extends RecyclerView.Adapter<ShgMemberAdapter.MyViewHolder> {

    List<MemberListDataBean> dataItem;
    Context context;
    NavController navController;
    HomeViewModel homeViewModel;

    public ShgMemberAdapter(List<MemberListDataBean> dataItem, Context context, NavController navController, HomeViewModel homeViewModel) {
        this.dataItem = dataItem;
        this.context = context;
        this.navController = navController;
        this.homeViewModel = homeViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CustomShgMemberLayoutBinding rootView = CustomShgMemberLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemBinding.tvMemberNameCode.setTextColor(context.getResources().getColor(R.color.green_500));
        holder.itemBinding.tvMemberNameCode.setText(dataItem.get(position).getMemberName() + "(" + dataItem.get(position).getMemberCode() + ")");
        String lastFilledBeforeNrlmEntry = dataItem.get(position).getLastFilledBeforeNrlmEntry();
        String lastFilledAfterNrlmEntry = dataItem.get(position).getLastFilledAfterNrlmEntry();

        if (lastFilledBeforeNrlmEntry == null) {
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.beforeNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        } else {
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.beforeNrlmEntry.setText(lastFilledBeforeNrlmEntry);
        }

        if (lastFilledAfterNrlmEntry == null) {
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.afterNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        } else {
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.afterNrlmEntry.setText(lastFilledAfterNrlmEntry);
        }


        holder.itemBinding.tvGoToEntry.setOnClickListener(view -> {
            PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefSelectedMemberCode(), dataItem.get(position).getMemberCode(), context);


            String beforeDate = homeViewModel.getBeforeDate(dataItem.get(position).getMemberCode());
            String afterDate = homeViewModel.getAfterDate(dataItem.get(position).getMemberCode());

           // ViewUtilsKt.toast(context, "DATE IS " + beforeDate);

            if (beforeDate == null) {
                NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
                navController.navigate(navDirections);
            }else if(afterDate ==null){
                NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToIncomeEntryAfterNrlmFragment();
                navController.navigate(navDirections);
            }else {
                ViewUtilsKt.toast(context, "Entry Completed");
            }



            /*NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
            navController.navigate(navDirections);*/


           /* new MaterialAlertDialogBuilder(context).setTitle(context.getResources().getString(R.string.income_selection)).setIcon(R.drawable.ic_baseline_add_circle_outline_24)
                    .setItems(AppConstant.ConstantObject.getItems(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String arr[] =  AppConstant.ConstantObject.getItems();
                            String str = arr[i];
                            if(str.equalsIgnoreCase("Income (Before coming into NRLM Fold)")){
                                NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
                                navController.navigate(navDirections);

                            }else if(str.equalsIgnoreCase("Income at present")){
                                NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToIncomeEntryAfterNrlmFragment();
                                navController.navigate(navDirections);

                            }
                        }
                    }).setCancelable(true).show();*/
        });

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomShgMemberLayoutBinding itemBinding;

        public MyViewHolder(@NonNull CustomShgMemberLayoutBinding itemView) {
            super(itemView.getRoot());

            itemBinding = itemView;
        }
    }
}
