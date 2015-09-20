package br.com.lp.guilherme.ifspservicos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class DisciplinaFragment extends Fragment {
    private Disciplina disciplina;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplina, container, false);
        return view;
    }

    //Método público chamado pela activity para atualizar a lista das disciplinas
    public void setDisciplina(Disciplina disciplina){
        if(disciplina != null){
            this.disciplina = disciplina;
            View view = this.getView();
            if (view != null){
                TextView nome = (TextView) view.findViewById(R.id.nome);
                TextView nota = (TextView) view.findViewById(R.id.nota);
                TextView frequencia = (TextView) view.findViewById(R.id.frequencia);
                nome.setText(disciplina.descricao);
                nota.setText(disciplina.nota);
                frequencia.setText(disciplina.frequencia);
            }
        }
    }
}
