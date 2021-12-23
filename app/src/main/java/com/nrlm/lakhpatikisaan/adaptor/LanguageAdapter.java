package com.nrlm.lakhpatikisaan.adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.database.entity.LanguageEntity;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.DialogFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {

    List<LanguageEntity> languagedataList;
    Context context;


    public LanguageAdapter(Context context, List<LanguageEntity> languagedataList)
    {
        this.languagedataList=languagedataList;
        this.context=context;

    }
    @NonNull
    @Override
    public LanguageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myLanguageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_selection_custom_layout, parent, false);
        return new LanguageAdapter.MyViewHolder(myLanguageView);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.MyViewHolder holder, int position) {
        holder.localLanguage.setText(languagedataList.get(position).getLocalLanguage());
        holder.englishLanguage.setText(languagedataList.get(position).getEnglishLanguage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFactory.getInstance().showAlertDialog(context, R.drawable.ic_launcher_background, "Message", context.getResources().getString(R.string.do_you_want_to_change_the_language), context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String languageCode = languagedataList.get(position).getLanguagecode();
                        String languageID = languagedataList.get(position).getLanguageid();
                        PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLanguageCode(), languageCode, context);
                        PreferenceFactory.getInstance().saveSharedPrefrecesData(PreferenceKeyManager.getPrefLanguageId(), languageID, context);
                        AppUtils.getInstance().setLocale(languageCode, context.getResources(), context);
                        Intent refresh = new Intent(context, HomeActivity.class);
                        context.startActivity(refresh);
                      //  AppUtils.getInstance().makeIntent(context, HomeActivity.class, true);
                        // AppUtils.getInstance().makeIntent(context, HomeActivity.class, true);
                    }
                }, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, false);

            }
        });
    }

    @Override
    public int getItemCount() {
        return languagedataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView englishLanguage, localLanguage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            localLanguage = (TextView) itemView.findViewById(R.id.Local_LanguageTv);
            englishLanguage = (TextView) itemView.findViewById(R.id.english_languageTv);
        }
    }
}
