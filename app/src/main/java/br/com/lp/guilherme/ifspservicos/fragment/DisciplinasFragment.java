package br.com.lp.guilherme.ifspservicos.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
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
import br.com.lp.guilherme.ifspservicos.adapter.DisciplinaAdapter;
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;
import br.com.lp.guilherme.ifspservicos.domain.DisciplinaService;

/**
 * Created by Guilherme on 20-Sep-15.
 */
public class DisciplinasFragment extends Fragment{

    protected RecyclerView recyclerView;
    private List<Disciplina> disciplinas;
    private LinearLayoutManager mLayoutManager;
    private String semestre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_disciplina, container, false);
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
            this.semestre = getArguments().getString("semestre");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        taskDisciplinas();
    }

    private void taskDisciplinas() {
        //Busca as disciplinas: dispara a task
        new GetDisciplinasTask().execute();
    }

    private class GetDisciplinasTask extends AsyncTask<Void, Void, List<Disciplina>>{

        @Override
        protected List<Disciplina> doInBackground(Void... params) {
            try{
                //Busca as disciplinas em background (Thread)
                return DisciplinaService.getDisciplinas(getContext(), semestre);
            } catch (IOException e){
                Log.e("ifspservicos", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Disciplina> disciplinas) {
            if(disciplinas != null){
                DisciplinasFragment.this.disciplinas = disciplinas;
                //Atualiza a view na UI Thread
                recyclerView.setAdapter(new DisciplinaAdapter(disciplinas, getContext(), onClickDisciplina()));
            }
        }
    }

    private DisciplinaAdapter.DisciplinaOnClickListener onClickDisciplina() {
        return new DisciplinaAdapter.DisciplinaOnClickListener() {
            @Override
            public void onClickDisciplina(View view, int idx) {
                Disciplina c = disciplinas.get(idx);
                Toast.makeText(getContext(), "Disciplina: " + c.codigo, Toast.LENGTH_LONG).show();
            }
        };
    }
}
