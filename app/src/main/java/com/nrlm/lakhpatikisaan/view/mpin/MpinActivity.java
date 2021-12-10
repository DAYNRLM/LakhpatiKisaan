package com.nrlm.lakhpatikisaan.view.mpin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;

public class MpinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin);
        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController=navHostFragment.getNavController();
        NavInflater navInflater=navController.getNavInflater();
        NavGraph navGraph=navInflater.inflate(R.navigation.mpin_nav_graph);
        if(!PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyMpin(),MpinActivity.this).equalsIgnoreCase(""))   // which fragment has to be reflect will decide on this variable(set Mpin fragmnet, Verify Mpin fragmet)
        { navGraph.setStartDestination(R.id.verifyMpinFragment);
        }else{
            navGraph.setStartDestination(R.id.setMpinFragment);
        }
        navController.setGraph(navGraph);
    }
    }
