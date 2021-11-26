package com.nrlm.lakhpatikisaan.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<VM extends ViewModel,
        B extends ViewBinding> extends Fragment {

    //R extends BaseRepository,
    // VMF extends ViewModelProvider.Factory
    public B binding;
    public VM viewModel;
    public NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getFragmentBinding(inflater, container);
        viewModel = new ViewModelProvider(this).get(getViewModel());
        navController = NavHostFragment.findNavController(this);

        this.onFragmentReady();
        return binding.getRoot();
    }

    public abstract Class<VM> getViewModel();
    public abstract B getFragmentBinding(LayoutInflater inflater, @Nullable ViewGroup container);
   // public abstract R getFragmentRepository();
    public abstract Context getCurrentContext();
   // public abstract  VMF getFactory();
    public abstract  void onFragmentReady();
}
