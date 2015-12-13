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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;
import br.com.lp.guilherme.ifspservicos.domain.Nota;
import br.com.lp.guilherme.ifspservicos.domain.NotaService;
import br.com.lp.guilherme.ifspservicos.fragment.NotaFragment;

/**
 * Created by Guilherme on 29/11/2015.
 */
public class NotaActivity extends AppCompatActivity {
    public TextView mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        Disciplina d = (Disciplina) getIntent().getSerializableExtra("disciplina");

        //Configura a Toolbar como action bar
        setUpToolbar();

        //Título da toolbar e botão up navigation
        getSupportActionBar().setTitle("Notas " + d.codigo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mensagem = (TextView) findViewById(R.id.mensagem);
    }

    protected void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
    }
}
