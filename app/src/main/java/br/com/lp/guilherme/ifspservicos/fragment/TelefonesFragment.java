package br.com.lp.guilherme.ifspservicos.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import br.com.lp.guilherme.ifspservicos.activity.DisciplinaActivity;
import br.com.lp.guilherme.ifspservicos.adapter.DisciplinaAdapter;
import br.com.lp.guilherme.ifspservicos.adapter.TelefoneAdapter;
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;
import br.com.lp.guilherme.ifspservicos.domain.DisciplinaService;
import br.com.lp.guilherme.ifspservicos.domain.Telefone;
import br.com.lp.guilherme.ifspservicos.domain.TelefoneService;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class TelefonesFragment extends Fragment{

    protected RecyclerView recyclerView;
    private List<Telefone> telefones;

    private LinearLayoutManager mLayoutManager;
    private String area;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_telefones, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.area = getArguments().getString("area");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        taskDisciplinas();
    }

    private void taskDisciplinas() {
        new GetDisciplinasTask().execute();
    }

    private class GetDisciplinasTask extends AsyncTask<Void, Void, List<Telefone>>{

        @Override
        protected List<Telefone> doInBackground(Void... params) {
            try{
                //Caso não estiver online, coloca na fila
                if(!isOnline()){
                    Looper.prepare();
                }
                //Busca as disciplinas em background (Thread)
                return TelefoneService.getTelefones(getContext(), area);
            } catch (IOException e){
                Toast.makeText(getContext(), "Não foi possivel recuperar a lista de disciplinas", Toast.LENGTH_LONG).show();
                Log.e("ifspservicos", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Telefone> telefones) {
            if(telefones != null){
                TelefonesFragment.this.telefones = telefones;
                //Atualiza a view na UI Thread
                recyclerView.setAdapter(new TelefoneAdapter(telefones, getContext(), onClickTelefone()));
            }
        }
    }

    private TelefoneAdapter.TelefoneOnClickListener onClickTelefone() {
        return new TelefoneAdapter.TelefoneOnClickListener() {
            @Override
            public void onClickTelefone(View view, int idx) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + telefones.get(idx).numero_formatado));
                startActivity(intent);
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
