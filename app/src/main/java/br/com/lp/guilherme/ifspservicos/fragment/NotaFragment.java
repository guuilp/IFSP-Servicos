package br.com.lp.guilherme.ifspservicos.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.activity.NotaActivity;
import br.com.lp.guilherme.ifspservicos.adapter.DisciplinaAdapter;
import br.com.lp.guilherme.ifspservicos.adapter.NotaAdapter;
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;
import br.com.lp.guilherme.ifspservicos.domain.Nota;
import br.com.lp.guilherme.ifspservicos.domain.NotaService;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class NotaFragment extends Fragment {
    private List<Nota> notas;
    private String id_disciplina;

    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nota, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        Disciplina d = (Disciplina) ((NotaActivity) getActivity()).getIntent().getSerializableExtra("disciplina");
        id_disciplina = d.id;
        taskNotas();
        return view;
    }

    private void taskNotas() {
        new GetNotasTask().execute();
    }

    private class GetNotasTask extends AsyncTask<Void, Void, List<Nota>> {

        @Override
        protected List<Nota> doInBackground(Void... params) {
            try{
                //Caso não estiver online, coloca na fila
                if (Looper.myLooper() == null){
                    Looper.prepare();
                }
                //Busca as disciplinas em background (Thread)
                return NotaService.getNotasDisciplinas(getContext(), id_disciplina);
            } catch (IOException e){
                Toast.makeText(getContext(), "Não foi possivel recuperar a lista de notas", Toast.LENGTH_LONG).show();
                Log.e("ifspservicos", e.getMessage(), e);
                return notas;
            }
        }

        @Override
        protected void onPostExecute(List<Nota> notas) {
            if(notas != null) {
                NotaFragment.this.notas = notas;
                recyclerView.setAdapter(new NotaAdapter(notas, getContext(), onClickNota()));
            }
            if(notas.size() == 0){
                ((NotaActivity) getActivity()).mensagem.setText("Você ainda não realizou nenhuma atividade (provas, trabalhos, etc) dessa matéria.");
            }
        }
    }

    private NotaAdapter.NotaOnClickListener onClickNota() {
        return new NotaAdapter.NotaOnClickListener() {
            @Override
            public void onClickNota(View view, int idx) {

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
