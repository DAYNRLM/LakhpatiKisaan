package com.nrlm.lakhpatikisaan.view.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.nrlm.lakhpatikisaan.databinding.FragmentMemberEntryAfterNrlmBinding;
import com.nrlm.lakhpatikisaan.databinding.FragmentMemberEntryBinding;
import com.nrlm.lakhpatikisaan.view.BaseFragment;

public class IncomeEntryAfterNrlmFragment extends BaseFragment<HomeViewModel, FragmentMemberEntryAfterNrlmBinding> {
    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentMemberEntryAfterNrlmBinding getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentMemberEntryAfterNrlmBinding.inflate(inflater, container, false);
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void onFragmentReady() {

    }
}
