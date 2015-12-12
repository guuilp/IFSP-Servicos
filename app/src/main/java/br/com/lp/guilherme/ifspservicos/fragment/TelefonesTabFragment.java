package br.com.lp.guilherme.ifspservicos.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.activity.MainActivity;
import br.com.lp.guilherme.ifspservicos.adapter.DisciplinaTabsAdapter;
import br.com.lp.guilherme.ifspservicos.adapter.NoticiaAdapter;
import br.com.lp.guilherme.ifspservicos.adapter.TelefoneTabsAdapter;
import br.com.lp.guilherme.ifspservicos.domain.Semestre;
import br.com.lp.guilherme.ifspservicos.domain.SemestreService;
import br.com.lp.guilherme.ifspservicos.domain.Telefone;
import br.com.lp.guilherme.ifspservicos.domain.TelefoneService;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class TelefonesTabFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_telefones_tab, container, false);
        //View Pager
        ((MainActivity) getActivity()).setTitle("IFSP Serviços - Telefones");
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mViewPager.setAdapter(new TelefoneTabsAdapter(getContext(), getChildFragmentManager()));
        tabLayout.addTab(tabLayout.newTab().setText("Educacional"));
        tabLayout.addTab(tabLayout.newTab().setText("Administrativo"));
        int cor = getContext().getResources().getColor(R.color.white);
        tabLayout.setTabTextColors(cor, cor);
        tabLayout.setOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setCurrentItem(1);
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
