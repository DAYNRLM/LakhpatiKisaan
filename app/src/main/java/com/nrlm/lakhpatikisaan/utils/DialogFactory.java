package com.nrlm.lakhpatikisaan.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.nrlm.lakhpatikisaan.R;

public class DialogFactory {

    public static DialogFactory dialogFactory;
    private androidx.appcompat.app.AlertDialog alertDialog;
    private androidx.appcompat.app.AlertDialog serverAlertDialog;

    public static DialogFactory getInstance() {
        if (dialogFactory == null)
            dialogFactory = new DialogFactory();
        return dialogFactory;
    }


    public AlertDialog showAlertDialog(@NonNull Context context, @Nullable int titleIcon, String title, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener dialogPositiveClickListener, @Nullable String negativeButtonText, @Nullable DialogInterface.OnClickListener dialogNegativeClickListener, boolean isCancellable) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setCancelable(isCancellable);
        builder.setInverseBackgroundForced(true);
        builder.setMessage(dialogMessage);
        if (titleIcon != 0)
            builder.setIcon(titleIcon);

        builder.setPositiveButton(positiveButtonText, dialogPositiveClickListener);

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, dialogNegativeClickListener);
        }
        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations=R.style.DialogAnimationTheme;
        alert.show();
        return alert;
    }

    public AlertDialog showAlertDialog(@NonNull Context context, int titleIcon, String title, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener dialogPositiveClickListener, String negativeButtonText, boolean isCancellable) {

        return showAlertDialog(context, titleIcon, title, dialogMessage, positiveButtonText, dialogPositiveClickListener, negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }, isCancellable);

    }

    public AlertDialog showAlertDialog(@NonNull Context ctx, int titleIcon, String title, String dialogMessage, String positiveButtonText, boolean isCancellable) {
        return showAlertDialog(ctx, titleIcon, title, dialogMessage, positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }, null, null, isCancellable);
    }


    public AlertDialog showAlertDialog(@NonNull Context ctx, int titleIcon, String title, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener onPositiveClickListener, boolean isCancellable) {
        return showAlertDialog(ctx, 0, title, dialogMessage, positiveButtonText, onPositiveClickListener, null, null, isCancellable);
    }


    public AlertDialog showMultipleDialog(@NonNull Context context, @Nullable int titleIcon, @NonNull String title, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener dialogPositiveClickListener, @Nullable String negativeButtonText, @Nullable DialogInterface.OnClickListener dialogNegativeClickListener, boolean isCancellable) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setCancelable(isCancellable);
        builder.setInverseBackgroundForced(true);
        if (title.equals(context.getString(R.string.SERVER_ERROR_TITLE))) {
            builder.setCustomTitle(View.inflate(context, R.layout.server_error_dialog, null));
        } else if (title.equals(context.getString(R.string.NO_INTERNET_TITLE))) {
            builder.setCustomTitle(View.inflate(context, R.layout.no_internet_dialog, null));
        } else if (title.equalsIgnoreCase(context.getString(R.string.app_name))) {
            builder.setTitle(title);
        }/*else if (title.equalsIgnoreCase(context.getString(R.string.)))*/
        builder.setMessage(dialogMessage);
        if (titleIcon != 0)
            builder.setIcon(titleIcon);

        builder.setPositiveButton(positiveButtonText, dialogPositiveClickListener);

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, dialogNegativeClickListener);
        }

        alert = builder.create();
        alert.show();
        return alert;
    }

    @NonNull
    public ProgressDialog showProgressDialog(Context context, boolean setCancelable) {
        ProgressDialog progressDialogss = new ProgressDialog(context);
        progressDialogss.setMessage("Loading...");
        progressDialogss.setCancelable(setCancelable);
        return progressDialogss;
    }

    @NonNull
    public Dialog showCustomDialog(@NonNull Context context, int layoutId) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(layoutId);
        return dialog;
    }

    public void showErrorAlertDialog(@NonNull Context context, String title, String message, String positiveButtonText) {

        serverAlertDialog = DialogFactory.getInstance().showAlertDialog(context, 0, title, message, positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }, true);
        serverAlertDialog.show();

    }

    public void showServerCridentialDialog(@NonNull Context context, String title, String massege, String positiveButtonText, String negativeButtonText, @Nullable DialogInterface.OnClickListener dialogNegativeClickListener, boolean isCancleable, boolean finishActivity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTheme);
        builder.setTitle(title);
        builder.setMessage(massege);
        builder.setCancelable(isCancleable);
        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialog, int which) {
                if (finishActivity) {
                    System.exit(0);
                } else dialog.dismiss();
            }
        });
        if (dialogNegativeClickListener != null) {
            builder.setNegativeButton(negativeButtonText, dialogNegativeClickListener);
        }
        builder.show();
    }
}
