package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.entity.MemberEntryEntity;
import com.nrlm.lakhpatikisaan.databinding.CustomEntryBeforeNrlmFoldBinding;
import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;

import java.util.List;

public class EntryBeforeNrlmFoldAdapter extends RecyclerView.Adapter<EntryBeforeNrlmFoldAdapter.MyViewHolder> {

    List<MemberEntryEntity> memberEntryDataItem;
    Context context;
    HomeViewModel homeViewModel;

    public EntryBeforeNrlmFoldAdapter(List<MemberEntryEntity> memberEntryDataItem, Context context, HomeViewModel homeViewModel) {
        this.memberEntryDataItem = memberEntryDataItem;
        this.context = context;
        this.homeViewModel = homeViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomEntryBeforeNrlmFoldBinding rootView = CustomEntryBeforeNrlmFoldBinding.inflate(LayoutInflater.from(context),parent,false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int number = 0;
        holder.itemBinding.tvSector.setText(memberEntryDataItem.get(position).getSectorName());
        holder.itemBinding.tvActivity.setText(memberEntryDataItem.get(position).getActivityName());
        holder.itemBinding.tvFrequency.setText(memberEntryDataItem.get(position).getIncomeFrequencyName());
        holder.itemBinding.tvIncomeRange.setText(memberEntryDataItem.get(position).getIncomeRangName());
        holder.itemBinding.tvMonth.setText(memberEntryDataItem.get(position).getMonthName());
        holder.itemBinding.tvYear.setText(memberEntryDataItem.get(position).getEntryYearCode());

        holder.itemBinding.btnDeleteEnter.setOnClickListener(view -> {
            String shgMemebrCode = memberEntryDataItem.get(position).shgMemberCode;
            String activityCode = memberEntryDataItem.get(position).activityCode;

            homeViewModel.deleteActivity(shgMemebrCode,activityCode);
            memberEntryDataItem.remove(position);
            notifyDataSetChanged();

        });

        String rang =  memberEntryDataItem.get(position).getIncomeRangName();
        String frequencyCode = memberEntryDataItem.get(position).getIncomeFrequencyCode();
        String noSpaceStr = rang.replaceAll("\\s", ""); // using built in method

        if(isStringOnlyAlphabet(noSpaceStr)){
             number = Integer.parseInt(getNumber(rang));
        }else {
             number = Integer.parseInt(splitString(rang));
        }
        holder.itemBinding.tvYearlyIncome.setText(context.getResources().getString(R.string.yearly_income)+getYearlyData(frequencyCode,number));
       // String yearly = getYearlyData(frequencyCode,number);




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


    public static boolean isStringOnlyAlphabet(String str)
    {
        //^\d*[a-zA-Z][a-zA-Z\d]*$
        //^[a-zA-Z]*$
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^\\d*[a-zA-Z][a-zA-Z\\d]*$")));
    }

    public String getNumber(String str){
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i=0; i<str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if(Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

        System.out.println(alpha);
        System.out.println(num);
        System.out.println(special);

        return num.toString();
    }

    public String splitString(String str){
       // 5000-8000
        String[] arrOfStr = str.split("-");
        return arrOfStr[1];
    }

    public String getYearlyData(String freqCode,int amount){

        int totlIncome=0;
        switch (freqCode){
            case "1":
                totlIncome =amount*12;
                break;
            case "2":
                totlIncome =amount*4;
                break;
            case "3":
                totlIncome =amount*2;
                break;
            case "4":
                totlIncome =amount*1;
                break;
        }
        return ""+totlIncome;
    }
}
