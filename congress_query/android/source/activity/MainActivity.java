package csci571.zhiqinliao.hw9.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import csci571.zhiqinliao.hw9.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private LegFragment legFragment;
    private BillFragment billFragment;
    private ComFragment comFragment;
    private FavFragment favFragment;

    private String curTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initDefaultView();
    }


    private void initDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_leg);
    }

    private void initDefaultView() {
        FragmentManager fragmentManager = getFragmentManager();
        legFragment = new LegFragment();
        fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, legFragment)
                        .commit();
        setTitle("Legislators");
        curTitle = "Legislators";
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

    /**
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
    **/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_leg) {
            if(!curTitle.equals("Legislators")) {
                if (legFragment == null)
                    legFragment = new LegFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, legFragment).commit();
                setTitle("Legislators");
                curTitle = "Legislators";
            }
        } else if (id == R.id.nav_bill) {
            if(!curTitle.equals("Bills")) {
                if (billFragment == null)
                    billFragment = new BillFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, billFragment).commit();
                setTitle("Bills");
                curTitle = "Bills";
            }
        } else if (id == R.id.nav_com) {
            if(!curTitle.equals("Committees")) {
                if (comFragment == null)
                    comFragment = new ComFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, comFragment).commit();
                setTitle("Committees");
                curTitle = "Committees";
            }
        } else if (id == R.id.nav_fav) {
            if(!curTitle.equals("Favorites")) {
                if (favFragment == null)
                    favFragment = new FavFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, favFragment).commit();
                setTitle("Favorites");
                curTitle = "Favorites";
            }
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent();
            intent.setClass(this, AboutMeActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
