package br.com.lp.guilherme.ifspservicos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.domain.Noticia;

/**
 * Created by Guilherme on 04-Oct-15.
 */
public class NoticiaFragment extends Fragment {

    private Noticia noticia;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticia, container, false);
        return view;
    }

    //Método público chamado pela activity para atualizar a lista das disciplinas
    public void setNoticia(Noticia noticia){
        if(noticia != null){
            this.noticia = noticia;
            View view = this.getView();
            if (view != null){
////                TextView titulo = (TextView) view.findViewById(R.id.titulo);
//                if(titulo != null) {
//                    titulo.setText(noticia.titulo);
//                }
                TextView corpo = (TextView)view.findViewById(R.id.corpo);
                if(corpo != null) {
                    corpo.setText(noticia.corpo);
                }
            }
        }
    }
}
