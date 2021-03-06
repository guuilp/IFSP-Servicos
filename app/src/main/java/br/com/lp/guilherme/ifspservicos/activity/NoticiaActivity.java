package br.com.lp.guilherme.ifspservicos.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.domain.Noticia;
import br.com.lp.guilherme.ifspservicos.fragment.NoticiaFragment;

/**
 * Created by Guilherme on 22-Nov-15.
 */
public class NoticiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        //Configura a Toolbar como action bar
        setUpToolbar();
        //Atualiza o carro no fragment
        NoticiaFragment cf = (NoticiaFragment) getSupportFragmentManager().findFragmentById(R.id.NoticiaFragment);
        Noticia c = (Noticia) getIntent().getSerializableExtra("noticia");
        cf.setNoticia(c);
        //Título da toolbar e botão up navigation
        getSupportActionBar().setTitle("Notícia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

}
