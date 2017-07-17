package com.project.team.parking.smart.smartparkingadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int ACTIVITYREQUESTCODE_LOGIN = 0;
    private static final int ACTIVITYREQUESTCODE_ADDSENSOR = 1;
    private static final int ACTIVITYREQUESTCODE_ADDUSER = 2;

    private boolean loggedin = false;
    private FloatingActionButton fab;

    private MainActivity mainActivity;
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

        mainActivity = this;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new MyFloatingActionButtonOnClickListener());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle(getString(R.string.title_nav_statistic));
        setFragment(new StasticFragment());
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        switch (item.getItemId()) {
            case R.id.action_logout:
                loggedin = false;
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, ACTIVITYREQUESTCODE_LOGIN);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: temporary remove login step for development
        if (!this.loggedin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, ACTIVITYREQUESTCODE_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case ACTIVITYREQUESTCODE_LOGIN:
                //Update user names or anything
                loggedin = Activity.RESULT_OK == resultCode;
                if(loggedin) {
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View hView = navigationView.getHeaderView(0);
                    TextView nav_user = (TextView) hView.findViewById(R.id.accountName);
                    nav_user.setText(data.getStringExtra(getString(R.string.intent_result_name_admin_id)));
                }
                break;

            case ACTIVITYREQUESTCODE_ADDSENSOR:
                fab.show();
                setTitle(getString(R.string.title_nav_sensor_management));
                setFragment(new SensorManagementFragment());
                break;

            case ACTIVITYREQUESTCODE_ADDUSER:
                fab.show();
                setTitle(getString(R.string.title_nav_admin_management));
                setFragment(new AdminManagementFragment());
                break;
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_stastic:
                fab.hide();
                setTitle(getString(R.string.title_nav_statistic));
                setFragment(new StasticFragment());
                break;

            case R.id.nav_sensor_management:
                fab.show();
                setTitle(getString(R.string.title_nav_sensor_management));
                setFragment(new SensorManagementFragment());
                break;

            case R.id.nav_admin_management:
                fab.show();
                setTitle(getString(R.string.title_nav_admin_management));
                setFragment(new AdminManagementFragment());
                break;

            default:
                fab.hide();
                setTitle(getString(R.string.title_nav_statistic));
                setFragment(new StasticFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public class MyFloatingActionButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.flContent);

            if (fragment instanceof SensorManagementFragment) {
                Intent intent = new Intent(mainActivity, EditSensorActivity.class);
                intent.putExtra(getString(R.string.intent_launch_action), getString(R.string.intent_launch_action_add));
                startActivityForResult(intent, MainActivity.ACTIVITYREQUESTCODE_ADDSENSOR);

            } else if (fragment instanceof AdminManagementFragment) {
                Intent intent = new Intent(mainActivity,EditAdminActivity.class);
                intent.putExtra("launch action","add action");
                startActivityForResult(intent, MainActivity.ACTIVITYREQUESTCODE_ADDUSER);
            }
        }
    }
}
