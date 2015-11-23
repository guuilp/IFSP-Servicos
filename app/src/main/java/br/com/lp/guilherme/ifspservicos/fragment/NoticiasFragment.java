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
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.activity.MainActivity;
import br.com.lp.guilherme.ifspservicos.activity.NoticiaActivity;
import br.com.lp.guilherme.ifspservicos.adapter.NoticiaAdapter;
import br.com.lp.guilherme.ifspservicos.domain.Noticia;
import br.com.lp.guilherme.ifspservicos.domain.NoticiasService;

/**
 * Created by Guilherme on 22-Nov-15.
 */
public class NoticiasFragment extends Fragment {

    protected RecyclerView recyclerView;
    private List<Noticia> noticias;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        ((MainActivity) getActivity()).setTitle("IFSP Serviços - Notícias");

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        taskNoticias();
    }

    private void taskNoticias() {
        new GetNoticiasTask().execute();
    }

    private class GetNoticiasTask extends AsyncTask<Void, Void, List<Noticia>> {

        @Override
        protected List<Noticia> doInBackground(Void... params) {
            try{
                //Caso não estiver online, coloca na fila
                if(!isOnline()){
                    Looper.prepare();
                }
                //Busca as noticias em background (Thread)
                return NoticiasService.getNoticia(getContext());
            } catch (IOException e){
                Toast.makeText(getContext(), "Não foi possivel recuperar a lista de noticias", Toast.LENGTH_LONG).show();
                Log.e("ifspservicos", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Noticia> noticias) {
            if(noticias != null){
                NoticiasFragment.this.noticias = noticias;
                //Atualiza a view na UI Thread
                recyclerView.setAdapter(new NoticiaAdapter(noticias, getContext(), onClickNoticia()));
            }
        }
    }

    private NoticiaAdapter.NoticiaOnClickListener onClickNoticia() {
        return new NoticiaAdapter.NoticiaOnClickListener() {
            @Override
            public void onClickNoticia(View view, int idx) {
                Noticia c = noticias.get(idx);
                Intent intent = new Intent(getContext(), NoticiaActivity.class);
                intent.putExtra("noticia", c);
                startActivity(intent);
                Toast.makeText(getContext(), "Noticia: " + c.id_noticia, Toast.LENGTH_LONG).show();
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
