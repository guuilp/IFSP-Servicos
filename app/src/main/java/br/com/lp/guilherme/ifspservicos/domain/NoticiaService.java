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

import br.com.lp.guilherme.ifspservicos.helper.SQLiteHandler;

/**
 * Created by Guilherme on 22-Nov-15.
 */
public class NoticiaService {
    private static final boolean LOG_ON = false;
<<<<<<< HEAD:app/src/main/java/br/com/lp/guilherme/ifspservicos/domain/NoticiaService.java
    private static final String TAG = "NoticiaService";
    private static String URL = "http://192.168.1.17/IFSP-ServicosWS/noticia/listar";
=======
    private static final String TAG = "NoticiasService";
    private static String URL = "http://192.168.1.10/IFSP-ServicosWS/noticia/listar";
>>>>>>> origin/master:app/src/main/java/br/com/lp/guilherme/ifspservicos/domain/NoticiasService.java

    public static List<Noticia> getNoticia(Context context) throws IOException {
        List<Noticia> noticias = null;

        if(noticias != null && noticias.size() > 0){
            //Encontrou o arquivo
            return noticias;
        }
        if (isOnline(context)) {
            //Se estiver online, busca do webservice
            noticias = getNoticiasFromWebService(context);
        } else {
            //Se não estiver online, busca do JSON local
            noticias = getNoticiasFromArquivo(context);
            Toast.makeText(context, "Conexão com a internet não disponível", Toast.LENGTH_SHORT).show();
        }
        return noticias;
    }

    private static String doPost(String url, Context context) throws IOException{
        SQLiteHandler db = new SQLiteHandler(context);

        HashMap<String, String> user = db.getUserDetails();
        String token = user.get("token");

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = new FormEncodingBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private static List<Noticia> parserJSON(Context context, String json) throws IOException {
        List<Noticia> noticias = new ArrayList<Noticia>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray jsonNoticias = root.getJSONArray("data");
            // Insere cada Noticia na lista
            for (int i = 0; i < jsonNoticias.length(); i++) {
                JSONObject jsonNoticia = jsonNoticias.getJSONObject(i);
                Noticia d = new Noticia();
                // Lê as informações de cada Noticia
                d.id_noticia = jsonNoticia.optLong("id_noticia");
                d.titulo = jsonNoticia.optString("titulo");
                d.corpo = jsonNoticia.optString("corpo");
                if (LOG_ON) {
                    Log.d(TAG, "Noticia " + d.id_noticia);
                }
                noticias.add(d);
            }
            if (LOG_ON) {
                Log.d(TAG, noticias.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return noticias;
    }

    public static List<Noticia> getNoticiasFromWebService(Context context) throws IOException{
        String json;

        json = doPost(URL, context);
        salvaArquivoNaMemoriaInterna(context, json);
        List<Noticia> noticias = parserJSON(context, json);
        return noticias;
    }

    //Abre o arquivo salvo, se existir, e cria a lista de carros
    public static List<Noticia> getNoticiasFromArquivo(Context context) throws IOException{
        String fileName = String.format("noticias_ifsp.json");
        Log.d(TAG, "Abrindo o arquivo: " + fileName);
        //Lê o arquivo da memória interna
        String json = readFile(context, fileName, "UTF-8");

        if(json == null){
            Log.d(TAG, "Arquivo " + fileName + " não encontrado.");
            return null;
        }

        List<Noticia> noticias = parserJSON(context, json);
        Log.d(TAG, "Noticias lidas do arquivo " + fileName);
        return noticias;
    }

    private static void salvaArquivoNaMemoriaInterna(Context context, String json){
        String fileName = "noticias_ifsp.json";
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
