package br.com.lp.guilherme.ifspservicos.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import br.com.lp.guilherme.ifspservicos.R;

/**
 * Created by Guilherme on 04-Oct-15.
 */
public class NoticiaFragment extends Fragment {
    private static String URL = "http://hto.ifsp.edu.br/index.php?start={numero_pagina}";
    private String teste;
    private WebView webview;
    private ProgressBar progress;
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticia, container, false);
        webview = (WebView) view.findViewById(R.id.webview);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        setWebViewClient(webview);

        FetchURL teste = new FetchURL();
        teste.execute();



        //Swipe to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener());
        swipeRefreshLayout.setColorSchemeColors(R.color.refresh_progress_1,
                                                R.color.refresh_progress_2,
                                                R.color.refresh_progress_3);
        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webview.reload(); //Atualiza a página
            }
        };
    }

    private void setWebViewClient(WebView webview) {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ;
                //Liga o progress
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Desliga o progress
                progress.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private class FetchURL extends AsyncTask<Void, String, Void>{
        Elements elements;
        Document document;
        String numeroPagina;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                URL = URL.replace("{numero_pagina}", "0");
                document = Jsoup.connect(URL).get();
                elements = document.select("div.items-leading");
                teste = elements.toString();
                elements = document.select("div.items-row.cols-1.row-0");
                teste = teste + elements.toString();
                elements = document.select("div.items-row.cols-1.row-1");
                teste = teste + elements.toString();
                elements = document.select("div.items-row.cols-1.row-2");
                URL = URL.replace("0", "{numero_pagina}" );
                for (int i = 1; i <= 12; i++) {
                    i = i + 3;
                    numeroPagina = String.valueOf(i);
                    URL = URL.replace("{numero_pagina}", numeroPagina);
                    document = Jsoup.connect(URL).get();
                    elements = document.select("div.items-leading");
                    teste = teste + elements.toString();
                    elements = document.select("div.items-row.cols-1.row-0");
                    teste = teste + elements.toString();
                    elements = document.select("div.items-row.cols-1.row-1");
                    teste = teste + elements.toString();
                    elements = document.select("div.items-row.cols-1.row-2");
                    teste = teste + elements.toString();
                    URL = URL.replace(numeroPagina, "{numero_pagina}" );
                }

//                elements = document.select("div.items-row.cols-1.row-0");
//                teste = teste + elements.toString();
//                elements = document.select("div.items-row.cols-1.row-1");
//                teste = teste + elements.toString();
//                elements = document.select("div.items-row.cols-1.row-2");
//                teste = teste + elements.toString();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Log.d("ifspservicos", teste);
            webview.loadData(teste, "text/html", "utf-8");
        }
    }
}
