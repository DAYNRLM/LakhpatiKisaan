package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.lakhpatikisaan.adaptor.LanguageAdapter;
import com.nrlm.lakhpatikisaan.database.entity.LanguageEntity;
import com.nrlm.lakhpatikisaan.databinding.FragmentChangeLanguageBinding;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.LanguageConstant;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChangeLanguageFragment extends BaseFragment<HomeViewModel, FragmentChangeLanguageBinding> {
    List<LanguageEntity> languagedata;
    private HomeViewModel homeViewModel;

    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentChangeLanguageBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentChangeLanguageBinding.inflate(inflater,container,false);
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void onFragmentReady() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //homeViewModel.getHomeViewModelRepos(getCurrentContext());
        viewModel.getHomeViewModelRepos(getCurrentContext());


        try {

            bindData();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void bindData() throws ExecutionException, InterruptedException {

        languagedata=new ArrayList<>();

        String index = "0";

       String languageId = viewModel.getLanguageCode();

        AppUtils.getInstance().showLog("languageId" + languageId, ChangeLanguageFragment.class);
        /* String languageId = loginInfo.getLanguageId();*/

        for (int i = 0; i < LanguageConstant.language_id.length; i++) {
            if (LanguageConstant.language_id[i].equalsIgnoreCase(languageId)) {
                index = String.valueOf(i);


            }
        }


        LanguageEntity englisgLang = new LanguageEntity();
        englisgLang.setLanguagecode(LanguageConstant.language_code[0]);
        englisgLang.setLocalLanguage(LanguageConstant.local_language[0]);
        englisgLang.setEnglishLanguage(LanguageConstant.language_english[0]);
        englisgLang.setLanguageid(LanguageConstant.language_id[0]);
        languagedata.add(englisgLang);

       if(!index.equalsIgnoreCase("0")) {
           String languageCode = LanguageConstant.language_code[Integer.parseInt(index)];
           String localLanguage = LanguageConstant.local_language[Integer.parseInt(index)];
           String language = LanguageConstant.language_english[Integer.parseInt(index)];
           String lanId = LanguageConstant.language_id[Integer.parseInt(index)];

           LanguageEntity localLangFromDb = new LanguageEntity();
           localLangFromDb.setLanguagecode(languageCode);
           localLangFromDb.setLocalLanguage(localLanguage);
           localLangFromDb.setEnglishLanguage(language);
           localLangFromDb.setLanguageid(lanId);
           languagedata.add(localLangFromDb);
       }


        LanguageAdapter selectLanguageAdaptor = new LanguageAdapter(getContext(), languagedata);
        binding.changLanguageRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.changLanguageRv.setAdapter(selectLanguageAdaptor);
        selectLanguageAdaptor.notifyDataSetChanged();
    }


}
