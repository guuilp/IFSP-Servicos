package br.com.lp.guilherme.ifspservicos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.adapter.TabsAdapter;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class DisciplinasTabFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplinas_tab, container, false);
        //View Pager
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new TabsAdapter(getContext(), getChildFragmentManager()));
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        int cor = getContext().getResources().getColor(R.color.white);
        tabLayout.setTabTextColors(cor, cor);
        tabLayout.addTab(tabLayout.newTab().setText("3º Semestre"));
        tabLayout.addTab(tabLayout.newTab().setText("4º Semestre"));
        tabLayout.addTab(tabLayout.newTab().setText("5º Semestre"));
        tabLayout.setOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setCurrentItem(3);
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
