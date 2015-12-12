package br.com.lp.guilherme.ifspservicos.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import br.com.lp.guilherme.ifspservicos.domain.Semestre;
import br.com.lp.guilherme.ifspservicos.fragment.DisciplinasFragment;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class DisciplinaTabsAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Semestre> semestres;

    public DisciplinaTabsAdapter(Context context, FragmentManager fm, List<Semestre> semestres){
        super(fm);
        this.context = context;
        this.semestres = semestres;
    }

    @Override
    public int getCount() {
        return semestres.size();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        if(position == 0){
            args.putString("ano", semestres.get(0).ano);
            args.putString("semestre", semestres.get(0).semestre);
        } else if(position == 1) {
            args.putString("ano", semestres.get(1).ano);
            args.putString("semestre", semestres.get(1).semestre);
        } else if(position == 2) {
            args.putString("ano", semestres.get(2).ano);
            args.putString("semestre", semestres.get(2).semestre);
        } else if(position == 3) {
            args.putString("ano", semestres.get(3).ano);
            args.putString("semestre", semestres.get(3).semestre);
        } else if(position == 4) {
            args.putString("ano", semestres.get(4).ano);
            args.putString("semestre", semestres.get(4).semestre);
        } else if(position == 5) {
            args.putString("ano", semestres.get(5).ano);
            args.putString("semestre", semestres.get(5).semestre);
        }
        Fragment f = new DisciplinasFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "1º Semestre";
        } else if(position == 1){
            return "2º semestre";
        } else if(position == 2){
            return "3º semestre";
        } else if(position == 3){
            return "4º semestre";
        } else if(position == 4) {
            return "5º semestre";
        }
        return "6º semestre";
    }
}
