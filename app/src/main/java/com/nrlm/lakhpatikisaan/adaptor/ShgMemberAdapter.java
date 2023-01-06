package com.nrlm.lakhpatikisaan.adaptor;

import static com.nrlm.lakhpatikisaan.network.vollyCall.VolleyService.volleyService;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.nrlm.lakhpatikisaan.BuildConfig;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.dbbean.MemberListDataBean;

import com.nrlm.lakhpatikisaan.databinding.CustomShgMemberLayoutBinding;
import com.nrlm.lakhpatikisaan.network.model.request.MemberInActiveRequestBean;
import com.nrlm.lakhpatikisaan.network.vollyCall.VolleyResult;
import com.nrlm.lakhpatikisaan.network.vollyCall.VolleyService;

import com.nrlm.lakhpatikisaan.utils.AppConstant;
import com.nrlm.lakhpatikisaan.utils.AppDateFactory;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.Cryptography;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.NetworkFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;
import com.nrlm.lakhpatikisaan.view.home.HomeViewModel;
import com.nrlm.lakhpatikisaan.view.home.ShgMemberFragmentDirections;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ShgMemberAdapter extends RecyclerView.Adapter<ShgMemberAdapter.MyViewHolder> {

    List<MemberListDataBean> dataItem;
    public VolleyResult mResultCallBack = null;
    Context context;
    NavController navController;
    ProgressDialog progressDialog;
    HomeViewModel homeViewModel;
    AlertDialog alertDialog;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemBinding.tvMemberNameCode.setTextColor(context.getResources().getColor(R.color.green_500));
        holder.itemBinding.tvMemberNameCode.setText(dataItem.get(position).getMemberName() + "(" + dataItem.get(position).getMemberCode() + ")");
        String status = dataItem.get(position).getStatus();
        String lastFilledBeforeNrlmEntry = dataItem.get(position).getLastFilledBeforeNrlmEntry();
        String lastFilledAfterNrlmEntry = dataItem.get(position).getLastFilledAfterNrlmEntry();
        String aadharStatus = dataItem.get(position).getAadhaar_verified_status();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("-Select-");
        arrayList.add("Active");
        arrayList.add("InActive");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.simple_item_adapter_list, arrayList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        holder.itemBinding.memberStatus.setText(dataItem.get(position).getStatus());
        holder.itemBinding.activeSpinner.setAdapter(arrayAdapter);
        holder.itemBinding.activeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                if (holder.itemBinding.activeSpinner.getSelectedItem().toString().equalsIgnoreCase("InActive")){

                    alertDialog =   new MaterialAlertDialogBuilder(context).setIcon(R.drawable.ic_baseline_logout_24)
                            .setTitle("Alert").setMessage(context.getResources().getString(R.string.dialog_inActive))
                            .setCancelable(false)
                            .setPositiveButton(context.getResources().getString(R.string.dialog_btn_InActive), (dialogInterface, i) -> {
                       String    memberIsNotInClfAndVo  =   homeViewModel.getMemberIsNotInClfAndVo(dataItem.get(holder.getAdapterPosition()).getMemberCode());
                        if (memberIsNotInClfAndVo.equalsIgnoreCase("0")){
                            DialogFactory.getInstance().showAlertDialog(context, 1, "Alert", "Cannot Inactivate already Linked To CLF/VO"
                                    , "ok", (dialog, which) -> dialog.dismiss(), null, null, true

                            );
                            notifyItemChanged(holder.getAdapterPosition());
                           /* ((TextView) arg0.getChildAt(0)).setTextColor(Color.GREEN);
                            ((TextView) arg0.getChildAt(0)).setText(arrayList.get(0));
*/
                        }


                        else {
                            homeViewModel.setStatus("InActive",dataItem.get(holder.getAdapterPosition()).getMemberCode());


                            ViewUtilsKt.toast(context, context.getResources().getString(R.string.dialog_toast_InActive));

                            holder.itemBinding.memberStatus.setTextColor(context.getResources().getColor(R.color.red_500));

                            if(NetworkFactory.isInternetOn(context))
                            {


                                progressDialog = new ProgressDialog(context);
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();


                                /*******make json object is encrypted and *********/
                                JSONObject encryptedObject =new JSONObject();
                                try {
                                    Cryptography cryptography = new Cryptography();


                                 String imeiNo=  PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefImeiNo(),context);


                                    MemberInActiveRequestBean memberInActiveRequestBean=new MemberInActiveRequestBean();

                                    String stateShortName=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyStateShortName(),context);
                                    String loginId=PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyLoginid(),context);

                                    AppUtils.getInstance().showLog("inactiveMemSyncValue"+stateShortName,ShgMemberAdapter.class);
                                    AppUtils.getInstance().showLog("inactiveMemSyncValue"+loginId,ShgMemberAdapter.class);
                                    memberInActiveRequestBean.setLogin_id(loginId);
                                    memberInActiveRequestBean.setImei_no(imeiNo);
                                    memberInActiveRequestBean.setDevice_name(AppUtils.getInstance().getDeviceInfo());
                                    memberInActiveRequestBean.setLocation_coordinate("10.3111313,154.16546");
                                    memberInActiveRequestBean.setState_short_name(stateShortName);
                                    memberInActiveRequestBean.setApp_version(BuildConfig.VERSION_NAME);

                                    ArrayList<MemberInActiveRequestBean.InactiveMemSync> memberInavtivearr=new ArrayList<>();

                                    for(int j=0;j<homeViewModel.getInactiveMemberData().size();j++){

                                        MemberInActiveRequestBean.InactiveMemSync inactiveMemSync=new MemberInActiveRequestBean.InactiveMemSync();
                                        inactiveMemSync.setShg_code(homeViewModel.getInactiveMemberData().get(j).getShgCode());
                                        inactiveMemSync.setShg_member_code(homeViewModel.getInactiveMemberData().get(j).getMemberCode());
                                        inactiveMemSync.setVillage_code(homeViewModel.getInactiveMemberData().get(j).getVillageCode());
                                        inactiveMemSync.setUpdated_on(AppDateFactory.getInstance().getTimeStamp());
                                        memberInavtivearr.add(inactiveMemSync);
                                        AppUtils.getInstance().showLog("inactiveMemSyncValue"+inactiveMemSync,ShgMemberAdapter.class);
                                    }
                                    memberInActiveRequestBean.setNrlm_member_inactivate(memberInavtivearr);

                                    encryptedObject.accumulate("data",cryptography.encrypt(new Gson().toJson(memberInActiveRequestBean)));
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (NoSuchPaddingException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (InvalidAlgorithmParameterException e) {
                                    e.printStackTrace();
                                } catch (IllegalBlockSizeException e) {
                                    e.printStackTrace();
                                } catch (BadPaddingException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                /***********************************************/

                                AppUtils.getInstance().showLog(" Encryptrd response*****error" +encryptedObject, ShgMemberAdapter.class);
                                mResultCallBack = new VolleyResult() {
                                    @Override
                                    public void notifySuccess(String requestType, JSONObject response) {
                                        progressDialog.dismiss();
                                        JSONObject jsonObject = null;

                                        String objectResponse="";
                                        if(response.has("data")){
                                            try {
                                                objectResponse=response.getString("data");

                                            }catch (JSONException e)
                                            {
                                                AppUtils.getInstance().showLog("objjjjjjj"+objectResponse,ShgMemberAdapter.class);
                                            }
                                        }else {
                                            return;
                                        }

                                        try {
                                            JSONObject jsonObject1=new JSONObject(objectResponse);
                                            objectResponse=jsonObject1.getString("data");
                                            AppUtils.getInstance().showLog("inactiveStatus"+jsonObject1,ShgMemberAdapter.class);
                                        }catch (JSONException e)
                                        {
                                            AppUtils.getInstance().showLog("exceptionDataOfBlock"+e,ShgMemberAdapter.class);

                                        }


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            try {
                                                Cryptography cryptography = new Cryptography();
                                                jsonObject = new JSONObject(cryptography.decrypt(objectResponse)); //Main data of state
                                                //if (jsonObject.getString("E200").equalsIgnoreCase("Success"))

                                                JSONObject error=jsonObject.getJSONObject("error");
                                                if(error.getString("code").equalsIgnoreCase("E200"))
                                                AppUtils.getInstance().showLog("responseJSON" + error.toString(), ShgMemberAdapter.class);

                                                {
                                                    AppUtils.getInstance().showLog("responseJSONmain" + error.toString(), ShgMemberAdapter.class);
                                                    homeViewModel.deleteInactiveMember();
                                                    holder.itemBinding.memberStatus.setTextColor(context.getResources().getColor(R.color.red_500));
                                                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentSelf();
                                                    navController.navigate(navDirections);


                                                }
                                                AppUtils.getInstance().showLog("responseJSON" + jsonObject.toString(), ShgMemberAdapter.class);
                                            } catch (Exception e) {
                                                progressDialog.dismiss();

                                                homeViewModel.setStatus("Active",dataItem.get(holder.getAdapterPosition()).getMemberCode());
                                                notifyItemChanged(holder.getAdapterPosition());
                                                dialogInterface.dismiss();
                                                //  ((TextView) arg0.getChildAt(0)).setTextColor(Color.RED);
                                                holder.itemBinding.memberStatus.setTextColor(context.getResources().getColor(R.color.green_500));
                                                NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentSelf();
                                                navController.navigate(navDirections);

                                                AppUtils.getInstance().showLog("DecryptEx" + e, ShgMemberAdapter.class);
                                            }
                                        }


                                    }

                                    @Override
                                    public void notifyError(String requestType, VolleyError error) {
                                        progressDialog.dismiss();

                                    }
                                };
                              volleyService = VolleyService.getInstance(context);
                                volleyService.postDataVolley("InactiveRequest", AppConstant.baseUrl+"inactivememberdata", encryptedObject, mResultCallBack);



                            }
                            else
                            {

                                homeViewModel.setStatus("InActive",dataItem.get(holder.getAdapterPosition()).getMemberCode());

                                notifyItemChanged(holder.getAdapterPosition());
                                dialogInterface.dismiss();
                                ViewUtilsKt.toast(context, context.getResources().getString(R.string.dialog_toast_InActive));
                                //  ((TextView) arg0.getChildAt(0)).setTextColor(Color.RED);
                                holder.itemBinding.memberStatus.setTextColor(context.getResources().getColor(R.color.red_500));
                                NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentSelf();
                                navController.navigate(navDirections);

                            }


                        }  }).setCancelable(false)
                            .setNegativeButton(context.getResources().getString(R.string.dialog_cancel_btn), (dialogInterface, i) -> {
                               /* ((TextView) arg0.getChildAt(0)).setTextColor(Color.GREEN);
                                ((TextView) arg0.getChildAt(0)).setText(arrayList.get(0));*/

                                notifyItemChanged(holder.getAdapterPosition());


                                dialogInterface.dismiss();
                            }).show();



                }


                if (holder.itemBinding.activeSpinner.getSelectedItem().toString().equalsIgnoreCase("Active")){
                    homeViewModel.setStatus("Active",dataItem.get(holder.getAdapterPosition()).getMemberCode());
                    ViewUtilsKt.toast(context, "Active");
                    //  ((TextView) arg0.getChildAt(0)).setTextColor(Color.RED);
                    holder.itemBinding.memberStatus.setTextColor(context.getResources().getColor(R.color.green_500));
                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentSelf();
                    navController.navigate(navDirections);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        if (status.equalsIgnoreCase("Active")){
            holder.itemBinding.memberStatus.setTextColor(context.getResources().getColor(R.color.green_500));
        }
        else holder.itemBinding.memberStatus.setTextColor(context.getResources().getColor(R.color.red_500));

        if (lastFilledBeforeNrlmEntry == null) {
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.beforeNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        } else {
            holder.itemBinding.beforeNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.beforeNrlmEntry.setText(context.getString(R.string.capture)+lastFilledBeforeNrlmEntry);
        }

        if (lastFilledAfterNrlmEntry == null) {
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.afterNrlmEntry.setText(context.getResources().getString(R.string.not_filled));
        } else {
            holder.itemBinding.afterNrlmEntry.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.afterNrlmEntry.setText(context.getString(R.string.capturedfor)+lastFilledAfterNrlmEntry);
        }


        if (aadharStatus.equalsIgnoreCase("0")) {


            holder.itemBinding.aadharDetails.setTextColor(context.getResources().getColor(R.color.red_500));
            holder.itemBinding.aadharDetails.setText(context.getResources().getString(R.string.not_filled));
        } else if (aadharStatus.equalsIgnoreCase("1")){
            holder.itemBinding.aadharDetails.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.itemBinding.aadharDetails.setText(R.string.captured);
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
            if(dataItem.get(position).getStatus() .equalsIgnoreCase("Active")) {

                if (beforeDate == null) {
                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
                    navController.navigate(navDirections);
                }else if(afterDate ==null){
                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToIncomeEntryAfterNrlmFragment();
                    navController.navigate(navDirections);
                }else if (aadharStatus.equalsIgnoreCase("0") ||aadharStatus.equalsIgnoreCase("3") ){
                    NavDirections navDirections = ShgMemberFragmentDirections.actionShgMemberFragmentToMemberEntryFragment();
                    navController.navigate(navDirections);

                }
                else  ViewUtilsKt.toast(context, context.getResources().getString(R.string.entry_complete_msg));

            }
            else
                ViewUtilsKt.toast(context, context.getResources().getString(R.string.member_is_InActive));




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
