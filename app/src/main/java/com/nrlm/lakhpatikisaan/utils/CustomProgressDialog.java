package com.nrlm.lakhpatikisaan.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

public class CustomProgressDialog {
    ProgressDialog mDialog;
    Context mContext;

    public CustomProgressDialog(Context context) {
        this.mContext = context;
    }

    public static CustomProgressDialog newInstance(Context context) {
        CustomProgressDialog  customProgress = new CustomProgressDialog(context);
        return customProgress;
    }


    public void showProgress( String message, boolean cancelable) {
        try {
            mDialog = new ProgressDialog(mContext);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setMessage(message);
            mDialog.setCancelable(cancelable);
            mDialog.setCanceledOnTouchOutside(cancelable);
            mDialog.show();
        }catch (Exception e){
        }
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
