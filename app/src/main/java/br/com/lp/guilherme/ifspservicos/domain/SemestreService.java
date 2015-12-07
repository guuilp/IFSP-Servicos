package br.com.lp.guilherme.ifspservicos.domain;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.lp.guilherme.ifspservicos.app.AppConfig;
import br.com.lp.guilherme.ifspservicos.helper.SQLiteHandler;

/**
 * Created by Guilherme on 05/12/2015.
 */
public class SemestreService {
    private static final boolean LOG_ON = false;
    private static final String TAG = "DataAvaliacoesService";

    public static List<Semestre> getSemestres(Context context) throws IOException {
        List<Semestre> dataAvaliacoes = null;

        if(dataAvaliacoes != null && dataAvaliacoes.size() > 0){
            //Encontrou o arquivo
            return dataAvaliacoes;
        }
        if (isOnline(context)) {
            //Se estiver online, busca do webservice
            dataAvaliacoes = getSemestresFromWebService(context);
        } else {
            //Se não estiver online, busca do JSON local
            dataAvaliacoes = getSemestresFromArquivo(context);
            Toast.makeText(context, "Conexão com a internet não disponível", Toast.LENGTH_SHORT).show();
        }
        return dataAvaliacoes;
    }

    private static String doPost(String url, Context context) throws IOException{
        SQLiteHandler db = new SQLiteHandler(context);

        HashMap<String, String> user = db.getUserDetails();
        String token = user.get("token");
        String id_usuario = user.get("id_usuario");

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = new FormEncodingBuilder()
                .add("id_usuario", id_usuario)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private static List<Semestre> parserJSON(Context context, String json) throws IOException {
        List<Semestre> semestres = new ArrayList<Semestre>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray jsonNoticias = root.getJSONArray("data");
            // Insere cada Noticia na lista
            for (int i = 0; i < jsonNoticias.length(); i++) {
                JSONObject jsonNoticia = jsonNoticias.getJSONObject(i);
                Semestre s = new Semestre();
                // Lê as informações de cada Noticia
                s.ano = jsonNoticia.optString("ano");
                s.semestre = jsonNoticia.optString("semestre");
                if (LOG_ON) {
                    Log.d(TAG, "Avaliacao " + s.ano);
                }
                semestres.add(s);
            }
            if (LOG_ON) {
                Log.d(TAG, semestres.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return semestres;
    }

    public static List<Semestre> getSemestresFromWebService(Context context) throws IOException{
        String json;

        json = doPost(AppConfig.URL_SEMESTRE, context);
        salvaArquivoNaMemoriaInterna(context, json);
        List<Semestre> semestres = parserJSON(context, json);
        return semestres;
    }

    //Abre o arquivo salvo, se existir, e cria a lista de carros
    public static List<Semestre> getSemestresFromArquivo(Context context) throws IOException{
        String fileName = String.format("semestres.json");
        Log.d(TAG, "Abrindo o arquivo: " + fileName);
        //Lê o arquivo da memória interna
        String json = readFile(context, fileName, "UTF-8");

        if(json == null){
            Log.d(TAG, "Arquivo " + fileName + " não encontrado.");
            return null;
        }

        List<Semestre> semestres = parserJSON(context, json);
        Log.d(TAG, "Semestres lidos do arquivo " + fileName);
        return semestres;
    }

    private static void salvaArquivoNaMemoriaInterna(Context context, String json){
        String fileName = "semestres.json";
        File file = context.getFileStreamPath(fileName);
        try{
            FileOutputStream out = new FileOutputStream(file);
            out.write(json.getBytes());
            out.flush();
            out.close();
        } catch(IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        Log.d(TAG, "Arquivo salvo: " + file);
    }

    public static String toString(InputStream in, String charset) throws IOException {
        byte[] bytes = toBytes(in);
        String texto = new String(bytes, charset);
        return texto;
    }

    /**
     * Converte a InputStream para bytes[]
     */
    public static byte[] toBytes(InputStream in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } finally {
            try {
                bos.close();
                in.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    /**
     * Lê o arquivo da memória interna.
     */
    public static String readFile(Context context, String fileName, String charset) throws IOException {
        try {
            // Cria o arquivo e retorna o OutputStream
            FileInputStream in = context.openFileInput(fileName);
            String s = toString(in, charset);
            return s;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
