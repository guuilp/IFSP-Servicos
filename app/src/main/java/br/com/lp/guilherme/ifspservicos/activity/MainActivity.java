package br.com.lp.guilherme.ifspservicos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.fragment.DatasAvaliacoesFragment;
import br.com.lp.guilherme.ifspservicos.fragment.DisciplinasTabFragment;
import br.com.lp.guilherme.ifspservicos.fragment.NoticiasFragment;
import br.com.lp.guilherme.ifspservicos.fragment.TelefonesTabFragment;
import br.com.lp.guilherme.ifspservicos.helper.SQLiteHandler;
import br.com.lp.guilherme.ifspservicos.helper.SessionManager;


public class MainActivity extends AppCompatActivity {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mContentFrame;
    private SQLiteHandler db;
    private SessionManager session;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        View header = mNavigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.nomeAluno);

        HashMap<String, String> user = db.getUserDetails();

        text.setText(user.get("name"));

        setUpNavDrawer();

        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setCheckable(true);
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_data_avaliacoes:
                        replaceFragment(new DatasAvaliacoesFragment());
                        mCurrentSelectedPosition = 0;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_disciplinas:
                        replaceFragment(new DisciplinasTabFragment());
                        mCurrentSelectedPosition = 1;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_noticias:
                        replaceFragment(new NoticiasFragment());
                        mCurrentSelectedPosition = 2;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_discussion:
                        replaceFragment(new TelefonesTabFragment());
                        mCurrentSelectedPosition = 3;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        session.setLogin(false);
                        db.deleteUsers();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        mCurrentSelectedPosition = 4;
                        mDrawerLayout.closeDrawers();
                        return true;
                    default:
                        return true;
                }
            }
        });

        mDrawerLayout.openDrawer(Gravity.LEFT);
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
