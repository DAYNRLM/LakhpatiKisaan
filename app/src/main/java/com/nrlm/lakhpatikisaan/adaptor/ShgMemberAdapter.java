package com.nrlm.lakhpatikisaan.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;
import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.auth.AuthActivity;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;
import com.nrlm.lakhpatikisaan.view.home.ShgMemberFragmentDirections;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShgMemberAdapter extends RecyclerView.Adapter<ShgMemberAdapter.MyViewHolder> {

    List<MemberListDataBean> dataItem;
    Context context;
    NavController navController;
    HomeViewModel homeViewModel;
    AlertDialog alertDialog;
    String ss = "Active";

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
        String aadharStatus = dataItem.get(position).getAadhaar_verified_status();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Active");
        arrayList.add("InActive");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.simple_item_adapter_list,arrayList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        holder.itemBinding.activeSpinner.setAdapter(arrayAdapter);
        holder.itemBinding.activeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                ss =  holder.itemBinding.activeSpinner.getSelectedItem().toString();
                if (ss.equalsIgnoreCase("InActive")){
                    alertDialog =   new MaterialAlertDialogBuilder(context).setIcon(R.drawable.ic_baseline_logout_24)
                            .setTitle(context.getResources().getString(R.string.dialog_sign_out_title)).setMessage(context.getResources().getString(R.string.dialog_inActive))
                            .setCancelable(false)
                            .setPositiveButton(context.getResources().getString(R.string.dialog_btn_InActive), (dialogInterface, i) -> {
                                ViewUtilsKt.toast(context,context.getResources().getString(R.string.dialog_toast_InActive));
                                dialogInterface.dismiss();
                                ((TextView) arg0.getChildAt(0)).setTextColor(Color.RED);
                                PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getTimeForInActive(),""+new Timestamp(new Date().getTime()),context);

                            }).setNegativeButton(context.getResources().getString(R.string.dialog_cancel_btn), (dialogInterface, i) -> {
                                ((TextView) arg0.getChildAt(0)).setTextColor(Color.GREEN);
                                ((TextView) arg0.getChildAt(0)).setText(arrayList.get(0));


                                dialogInterface.dismiss();
                            }).show();



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (lastFilledBeforeNrlmEntry == null) {
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.beforeNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        } else {
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.beforeNrlmEntry.setText("Captured for "+lastFilledBeforeNrlmEntry);
        }

        if (lastFilledAfterNrlmEntry == null) {
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.afterNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        } else {
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.afterNrlmEntry.setText("Captured for "+lastFilledAfterNrlmEntry);
        }


        if (aadharStatus.equalsIgnoreCase("0")) {
            holder.itemBinding.aadharDetails.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.aadharDetails.setText(context.getResources().getString(R.string.not_filled));
        } else if (aadharStatus.equalsIgnoreCase("1")){
            holder.itemBinding.aadharDetails.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.aadharDetails.setText("Captured");
        }else if (aadharStatus.equalsIgnoreCase("2")){
            holder.itemBinding.aadharDetails.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.aadharDetails.setText("Requested for verification");
        }else if (aadharStatus.equalsIgnoreCase("3")){
            holder.itemBinding.aadharDetails.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.aadharDetails.setText("Rejected");
        }


        holder.itemBinding.holderView.setOnClickListener(view -> {
            PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefSelectedMemberCode(), dataItem.get(position).getMemberCode(), context);


            String beforeDate = homeViewModel.getBeforeDate(dataItem.get(position).getMemberCode());
            String afterDate = homeViewModel.getAfterDate(dataItem.get(position).getMemberCode());

           // ViewUtilsKt.toast(context, "DATE IS " + beforeDate);
            if(ss.equalsIgnoreCase("Active")) {

                if (beforeDate == null) {
                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
                    navController.navigate(navDirections);
                }else if(afterDate ==null){
                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToIncomeEntryAfterNrlmFragment();
                    navController.navigate(navDirections);
                }else if (aadharStatus.equalsIgnoreCase("0") ||aadharStatus.equalsIgnoreCase("3") ){
                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
                    navController.navigate(navDirections);

                }else  ViewUtilsKt.toast(context, context.getResources().getString(R.string.entry_complete_msg));

            }
            else
                ViewUtilsKt.toast(context, context.getResources().getString(R.string.member_is_InActive));


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
