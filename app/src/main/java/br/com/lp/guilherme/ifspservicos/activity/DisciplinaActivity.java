package br.com.lp.guilherme.ifspservicos.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.adapter.DisciplinaAdapter;
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;
import br.com.lp.guilherme.ifspservicos.domain.Nota;
import br.com.lp.guilherme.ifspservicos.domain.NotaService;
import br.com.lp.guilherme.ifspservicos.domain.Noticia;
import br.com.lp.guilherme.ifspservicos.fragment.DisciplinaFragment;
import br.com.lp.guilherme.ifspservicos.fragment.NoticiaFragment;

/**
 * Created by Guilherme on 29/11/2015.
 */
public class DisciplinaActivity extends AppCompatActivity {
    private List<Nota> notas;
    private String id_disciplina;
    private DisciplinaFragment df;
    private Nota n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);
        //Configura a Toolbar como action bar
        setUpToolbar();
        //Atualiza o carro no fragment
        df = (DisciplinaFragment) getSupportFragmentManager().findFragmentById(R.id.DisciplinaFragment);
        Disciplina d = (Disciplina) getIntent().getSerializableExtra("disciplina");
        id_disciplina = d.id;
        n = (Nota) getIntent().getSerializableExtra("nota");
        taskNotas();

        //Título da toolbar e botão up navigation
        getSupportActionBar().setTitle("Disciplina");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

    private void taskNotas() {
        new GetNotasTask().execute();
    }

    private class GetNotasTask extends AsyncTask<Void, Void, List<Nota>> {

        @Override
        protected List<Nota> doInBackground(Void... params) {
            try{
                //Caso não estiver online, coloca na fila
                if(!isOnline()){
                    Looper.prepare();
                }
                //Busca as disciplinas em background (Thread)
                return NotaService.getNotasDisciplinas(getApplicationContext(), id_disciplina);
            } catch (IOException e){
                Toast.makeText(getApplicationContext(), "Não foi possivel recuperar a lista de notas", Toast.LENGTH_LONG).show();
                Log.e("ifspservicos", e.getMessage(), e);
                return notas;
            }
        }

        @Override
        protected void onPostExecute(List<Nota> notas) {
            //super.onPostExecute(notas);
            if(notas != null) {
                DisciplinaActivity.this.notas = notas;
                for (int i=0; i< notas.size(); i++){
                    df.setNotas(notas.get(i));
                }
                //recyclerView.setAdapter(new DisciplinaAdapter(disciplinas, getContext(), onClickDisciplina()));
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
