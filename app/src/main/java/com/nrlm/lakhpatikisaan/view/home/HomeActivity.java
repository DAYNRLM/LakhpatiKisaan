package com.nrlm.lakhpatikisaan.view.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nrlm.lakhpatikisaan.R;
import com.nrlm.lakhpatikisaan.network.model.AadharPojo;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.ViewUtilsKt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

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

    XmlPullParser parser;

    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        tollBar = findViewById(R.id.tollBar);
        navigation_view = findViewById(R.id.navigation_view);
        home_drawer_layout = findViewById(R.id.home_drawer_layout);
        appUtils = AppUtils.getInstance();

        setSupportActionBar(tollBar);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

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

            case R.id.changeLanguage:
                navController.navigate(R.id.changeLanguageFragment2);
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){

           // ViewUtilsKt.toast(this,"INSIDE ONACTIVITY RESULT");
            XmlPullParserFactory pullParserFactory;
            try {
                pullParserFactory = XmlPullParserFactory.newInstance();
                parser = pullParserFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new StringReader(result.getContents()));
                processParsing(parser);

            }catch (Exception e){
                ViewUtilsKt.toast(this,"This is not right QR code");
            }

        }*/
    }

    private void processParsing(@NonNull XmlPullParser parser) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();
        appUtils.showLog("eventType" + eventType, HomeActivity.class);

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                appUtils.showLog("startDocuments", HomeActivity.class);
            } else if (eventType == XmlPullParser.START_TAG) {
                appUtils.showLog("Start tag " + parser.getName(), HomeActivity.class);
                if (parser.getName().equalsIgnoreCase("PrintLetterBarcodeData")) {
                    String uid = parser.getAttributeValue(null, "uid");
                    String name = parser.getAttributeValue(null, "name");
                    String  gender = parser.getAttributeValue(null, "gender");

                   AadharPojo  aadharPojo = new AadharPojo();
                   aadharPojo.aadhaarNumber=uid;
                   aadharPojo.aadharName=name;
                   aadharPojo.aadharGender=gender;
                   aadharPojo.error="";

                  // homeViewModel.sendAadharPojoData(aadharPojo);

                   // ViewUtilsKt.toast(this,""+uid+"--"+name+"--"+gender);
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                appUtils.showLog("End tag " + parser.getName(), HomeActivity.class);
            } else if (eventType == XmlPullParser.TEXT) {
                appUtils.showLog("Text" + parser.getText(), HomeActivity.class);
            }
            eventType = parser.next();
        }
        appUtils.showLog("EndDocuments", HomeActivity.class);
    }

}