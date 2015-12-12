package br.com.lp.guilherme.ifspservicos.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import br.com.lp.guilherme.ifspservicos.domain.Semestre;
import br.com.lp.guilherme.ifspservicos.domain.Telefone;
import br.com.lp.guilherme.ifspservicos.fragment.DisciplinasFragment;
import br.com.lp.guilherme.ifspservicos.fragment.TelefonesFragment;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class TelefoneTabsAdapter extends FragmentPagerAdapter {
    private Context context;

    public TelefoneTabsAdapter(Context context, FragmentManager fm){
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        if(position == 0){
            args.putString("area", "Educacional");
        } else if(position == 1) {
            args.putString("area", "Administrativo");
        }
        Fragment f = new TelefonesFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Educacional";
        }
        return "Administrativo";
    }
}
