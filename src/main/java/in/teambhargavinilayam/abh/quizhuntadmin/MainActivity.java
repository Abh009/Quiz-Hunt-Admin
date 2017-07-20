package in.teambhargavinilayam.abh.quizhuntadmin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBhelper myDB;
    GoogleApiClient apiClient;
    TextView NavHeaderTxt, WelcomeTxt;
    ImageView NavHeaderImage;
    TextView questionNum;
    String UserName = "Admin", UserEmail, UserPicURL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SetHome(R.id.main_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();
            int backPressedCount = 0;

            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getFragmentManager().popBackStack();
                setTitle(R.string.app_name);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.home) {
            SetHome(R.id.main_layout);
        } else if (id == R.id.sync) {

        }else if (id == R.id.manage_acc) {
            setTitle("Sign In");
            ManageAcoountFragment homeFragment = new ManageAcoountFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_layout,homeFragment);
            ft.commit();
        }
        else if (id == R.id.about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean IsNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected())
            isAvailable = true;
        return isAvailable;
    }

    public void SetHome(int ResId)
    {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(ResId,homeFragment);
        ft.commit();
    }
    public void UpdateNavDrawerIcons(){
        myDB = new DBhelper(this);
        Cursor data = myDB.ShowUserData();
        if (data.getCount() == 0) {

            UserName = "ADMIN";
            UserPicURL = "";
            UserEmail = "";
        }
        else {
            data.moveToFirst();
            UserName = data.getString(1);
            UserEmail = data.getString(2);
            UserPicURL = data.getString(3);
        }

        String text = "";
        if (NavHeaderTxt != null && NavHeaderImage != null) {
            SetNavDrawer(UserName);
            if (!(UserPicURL.trim().equals("USER"))) {
                Glide.with(this).load(UserPicURL).into(NavHeaderImage);
            }
        }
        String[] splitUserName = UserName.split("");
        for (int i = 0 ; i < splitUserName.length; i++){
            if (splitUserName[i].equals(" ")) {
                text = "Welcome " + text;
                WelcomeTxt.setText(text);
                return;
            }
            else {
                text += splitUserName[i];
                text += " ";
            }
        }
        text = "Welcome " + text;
        WelcomeTxt.setText(text);
    }
    public void SetNavDrawer(String text){
        NavHeaderTxt.setText(text);
    }


    public void SetActionBarTitle(int ResId){
        setTitle(ResId);
    }
    public void SetActionBarTitle(String title){
        setTitle(title);
    }
    public void SetText(String text,int ResId){
        TextView tv = (TextView) findViewById(ResId);
        tv.setText(text);
    }

}

