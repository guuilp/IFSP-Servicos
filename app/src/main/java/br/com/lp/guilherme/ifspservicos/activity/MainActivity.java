package br.com.lp.guilherme.ifspservicos.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.fragment.DisciplinasFragment;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;

    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        setUpNavDrawer();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        replaceFragment(new DisciplinasFragment());
                        Snackbar.make(mContentFrame, "Item One", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 0;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_disciplinas:
                        replaceFragment(new DisciplinasFragment());
                        //Snackbar.make(mContentFrame, "Item One", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 1;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_messages:
                        Snackbar.make(mContentFrame, "Item Two", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 2;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_friends:
                        Snackbar.make(mContentFrame, "Item Three", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 3;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_discussion:
                        Snackbar.make(mContentFrame, "Item Four", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 4;
                        mDrawerLayout.closeDrawers();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    private void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_contentframe, frag, "TAG").commit();
    }
}
