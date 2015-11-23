package br.com.lp.guilherme.ifspservicos.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.lp.guilherme.ifspservicos.fragment.DisciplinasFragment;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class TabsAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabsAdapter(Context context, FragmentManager fm){
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
            args.putString("semestre", "1");
        } else if(position == 1) {
            args.putString("semestre", "2");
        } //else if(position == 2){
//            args.putString("semestre", "2");
//        }
        Fragment f = new DisciplinasFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "4º Semestre";
        } //else if (position == 1){
//            return "5º Semestre";
//        }
        return "5º Semestre";
    }
}
