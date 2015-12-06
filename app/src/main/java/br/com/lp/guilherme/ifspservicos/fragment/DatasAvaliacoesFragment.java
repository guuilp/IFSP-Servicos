package br.com.lp.guilherme.ifspservicos.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.activity.MainActivity;
import br.com.lp.guilherme.ifspservicos.adapter.DataAvaliacoesAdapter;
import br.com.lp.guilherme.ifspservicos.adapter.DisciplinaAdapter;
import br.com.lp.guilherme.ifspservicos.domain.DataAvaliacoes;
import br.com.lp.guilherme.ifspservicos.domain.DataAvaliacoesService;

/**
 * Created by Guilherme on 05/12/2015.
 */
public class DatasAvaliacoesFragment extends Fragment {

    protected RecyclerView recyclerView;
    private List<DataAvaliacoes> datasAvaliacoes;

    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_datas_avaliacoes, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        ((MainActivity) getActivity()).setTitle("IFSP Serviços - Datas");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        taskDatasAvaliacoes();
    }

    private void taskDatasAvaliacoes() {
        new GetDatasAvaliacoesTask().execute();
    }

    private class GetDatasAvaliacoesTask extends AsyncTask<Void, Void, List<DataAvaliacoes>> {

        @Override
        protected List<DataAvaliacoes> doInBackground(Void... params) {
            try{
                //Caso não estiver online, coloca na fila
                if(!isOnline()){
                    Looper.prepare();
                }
                //Busca as disciplinas em background (Thread)
                return DataAvaliacoesService.getDataAvaliacoes(getContext());
            } catch (IOException e){
                Toast.makeText(getContext(), "Não foi possivel recuperar a lista de disciplinas", Toast.LENGTH_LONG).show();
                Log.e("ifspservicos", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DataAvaliacoes> dataAvaliacoes) {
            if(dataAvaliacoes != null){
                DatasAvaliacoesFragment.this.datasAvaliacoes = dataAvaliacoes;
                //Atualiza a view na UI Thread
                recyclerView.setAdapter(new DataAvaliacoesAdapter(dataAvaliacoes, getContext(), onClickDataAvaliacoes()));
            }
        }
    }

    private DataAvaliacoesAdapter.DataAvaliacoesOnClickListener onClickDataAvaliacoes() {
        return new DataAvaliacoesAdapter.DataAvaliacoesOnClickListener() {
            @Override
            public void onClickDataAvaliacoes(View view, int idx) {
                DataAvaliacoes da = datasAvaliacoes.get(idx);
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
