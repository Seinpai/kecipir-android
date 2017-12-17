package com.kecipir.petani;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kecipir.petani.fragment.DashboardFragment;
import com.kecipir.petani.fragment.OrderFragment;
import com.kecipir.petani.fragment.PanenanFragment;
import com.kecipir.petani.fragment.PembayaranFragment;
import com.kecipir.petani.fragment.NotificationFragment;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.util.CircleImageTransform;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private FragmentManager fragmentManager;
    private Fragment fragment = null;

    TextView mTitle;
    EditText mSearch;

    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    Menu menu_gl = null;

    private AccountPreference mAccountPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mSearch = (EditText) findViewById(R.id.input_search);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);

        Button mLogOutButton = (Button) header.findViewById(R.id.action_logout);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearLoginSession();
            }
        });

        mAccountPreference = getApp().getAccountPreference();
        TextView profileName = (TextView) header.findViewById(R.id.nav_header_profile_name);
        profileName.setText(mAccountPreference.getName().toUpperCase());

        ImageView profileAvatar = (ImageView) header.findViewById(R.id.nav_header_profile_avatar);
        if (mAccountPreference.getPhoto() != null) {
            Picasso.with(context)
                    .load(mAccountPreference.getPhoto())
                    .transform(new CircleImageTransform())
                    .fit() // Fix centerCrop issue: http://stackoverflow.com/a/20824141/1936697
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(profileAvatar);
        }

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new DashboardFragment();
            fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
            fragmentTransaction.commit();
        }
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
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);

        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);
        this.menu_gl = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_messages) {
            mTitle.setVisibility(View.VISIBLE);
            mSearch.setVisibility(View.GONE);
            fragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new NotificationFragment();
            fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the my account action
            mTitle.setVisibility(View.VISIBLE);
            mSearch.setVisibility(View.GONE);
            fragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new DashboardFragment();
            fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_harvest) {
            mTitle.setVisibility(View.VISIBLE);
            mSearch.setVisibility(View.GONE);
            fragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new PanenanFragment();
            fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_order) {
            mTitle.setVisibility(View.VISIBLE);
            mSearch.setVisibility(View.GONE);
            fragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new OrderFragment();
            fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
//        else if (id == R.id.nav_payment) {
//            mTitle.setVisibility(View.VISIBLE);
//            mSearch.setVisibility(View.GONE);
//            fragmentManager = getSupportFragmentManager();
//            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragment = new PembayaranFragment();
//            fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void enableViews(boolean enable) {

        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (enable) {
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    /**
     * Log out
     * Clear all account preference data
     */
    private void clearLoginSession() {
        getApp().getAccountPreference().deleteAll();

        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }
}
