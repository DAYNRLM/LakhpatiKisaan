package com.nrlm.lakhpatikisaan.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

public class HomeActivity extends AppCompatActivity {
    NavigationView navigation_view;
    DrawerLayout home_drawer_layout;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    Toolbar tollBar;
    MenuItem menuItem;
    TextView badgeTv;
    int notification = 0;
    AppUtils appUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        tollBar = findViewById(R.id.tollBar);
        navigation_view = findViewById(R.id.navigation_view);
        home_drawer_layout = findViewById(R.id.home_drawer_layout);
        appUtils = AppUtils.getInstance();

        setSupportActionBar(tollBar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_nav_host);
        navController = navHostFragment.getNavController();

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(home_drawer_layout).build();

        NavigationUI.setupActionBarWithNavController(HomeActivity.this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(navigation_view, navController);


    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean status = false;

        switch (item.getItemId()) {
            case R.id.logOut:
                navController.navigate(R.id.logOutDialogFragment);
                break;
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}