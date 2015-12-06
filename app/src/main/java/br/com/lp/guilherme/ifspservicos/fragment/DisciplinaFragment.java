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
import br.com.lp.guilherme.ifspservicos.domain.Nota;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class DisciplinaFragment extends Fragment {
    private Disciplina disciplina;
    private Nota nota;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplina, container, false);
        return view;
    }

    //Método público chamado pela activity para atualizar a lista das disciplinas
    public void setNotas(Nota nota){
        if(nota != null){
            this.nota = nota;
            View view = this.getView();
            if (view != null){
                TextView descricao_avaliacao = (TextView) view.findViewById(R.id.descricao_avaliacao);
                TextView data_avaliacao = (TextView) view.findViewById(R.id.data_avaliacao);
                TextView nota_avaliacao = (TextView) view.findViewById(R.id.nota_avaliacao);
                TextView peso_avaliacao = (TextView) view.findViewById(R.id.peso_avaliacao);

                descricao_avaliacao.setText(nota.descricao_avaliacao);
                data_avaliacao.setText(nota.data_avaliacao);
                nota_avaliacao.setText(nota.nota_avaliacao);
                peso_avaliacao.setText(nota.peso_avaliacao);
            }
        }
    }
}
