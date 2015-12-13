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
import br.com.lp.guilherme.ifspservicos.adapter.NoticiaAdapter;
import br.com.lp.guilherme.ifspservicos.adapter.DisciplinaTabsAdapter;
import br.com.lp.guilherme.ifspservicos.domain.Semestre;
import br.com.lp.guilherme.ifspservicos.domain.SemestreService;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class DisciplinasTabFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private List<Semestre> semestres;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplinas_tab, container, false);
        //View Pager
        ((MainActivity) getActivity()).setTitle("IFSP Serviços - Disciplinas");
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);
        taskSemestres();
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        int cor = getContext().getResources().getColor(R.color.white);
        tabLayout.setTabTextColors(cor, cor);
        tabLayout.setOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setCurrentItem(2);
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

    private void taskSemestres() {
        new GetSemestresTask().execute();
    }

    private class GetSemestresTask extends AsyncTask<Void, Void, List<Semestre>> {

        @Override
        protected List<Semestre> doInBackground(Void... params) {
            try{
                //Caso não estiver online, coloca na fila
                if (Looper.myLooper() == null){
                    Looper.prepare();
                }
                //Busca as noticias em background (Thread)
                return SemestreService.getSemestres(getContext());
            } catch (IOException e){
                Toast.makeText(getContext(), "Não foi possivel recuperar a lista de noticias", Toast.LENGTH_LONG).show();
                Log.e("ifspservicos", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Semestre> semestres) {
            if(semestres != null){
                DisciplinasTabFragment.this.semestres = semestres;
                mViewPager.setAdapter(new DisciplinaTabsAdapter(getContext(), getChildFragmentManager(), semestres));
                for (int i = 0; i < semestres.size(); i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(semestres.get(i).ano + " - " + semestres.get(i).semestre));
                }
            }
        }
    }

    private NoticiaAdapter.NoticiaOnClickListener onClickNoticia() {
        return new NoticiaAdapter.NoticiaOnClickListener() {
            @Override
            public void onClickNoticia(View view, int idx) {
                Semestre s = semestres.get(idx);
            }
        };
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
