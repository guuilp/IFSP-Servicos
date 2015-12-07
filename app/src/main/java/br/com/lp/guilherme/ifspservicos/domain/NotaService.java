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
 * Created by Guilherme on 29/11/2015.
 */
public class NotaService {

    private static final boolean LOG_ON = false;
    private static final String TAG = "DisciplinaService";

    public static List<Nota> getNotasDisciplinas(Context context, String id_disciplina) throws IOException {
        List<Nota> notas = null;

        if(notas != null && notas.size() > 0){
            //Encontrou o arquivo
            return notas;
        }
        if (isOnline(context)) {
            //Se estiver online, busca do webservice
            notas = getNotasFromWebService(context, id_disciplina);
        } else {
            //Se não estiver online, busca do JSON local
            notas = getNotasFromArquivo(context, id_disciplina);
            Toast.makeText(context, "Conexão com a internet não disponível", Toast.LENGTH_SHORT).show();
        }
        return notas;
    }

    private static String doPost(String url, Context context, String id_disciplina) throws IOException{
        SQLiteHandler db = new SQLiteHandler(context);

        HashMap<String, String> user = db.getUserDetails();
        String token = user.get("token");
        String id_usuario = user.get("id_usuario");

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = new FormEncodingBuilder()
                .add("id_usuario", id_usuario)
                .add("id_disciplina", id_disciplina)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private static List<Nota> parserJSON(Context context, String json) throws IOException {
        List<Nota> notas = new ArrayList<Nota>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray jsonDisciplinas = root.getJSONArray("data");
            // Insere cada Disciplina na lista
            for (int i = 0; i < jsonDisciplinas.length(); i++) {
                JSONObject jsonDisciplina = jsonDisciplinas.getJSONObject(i);
                Nota n = new Nota();
                // Lê as informações de cada Disciplina
                n.descricao_avaliacao = "Descrição da Avaliação: " + jsonDisciplina.optString("descricao_avaliacao");
                n.id_disciplina = jsonDisciplina.optString("id_disciplina");
                n.data_avaliacao = "Data da Avaliação: " + jsonDisciplina.optString("data_avaliacao");
                n.nota_avaliacao = "Nota: " + jsonDisciplina.optString("nota_avaliacao");
                n.peso_avaliacao = "Peso: " + jsonDisciplina.optString("peso_avaliacao");
                if (LOG_ON) {
                    Log.d(TAG, "Disciplina " + n.id_disciplina);
                }
                notas.add(n);
            }
            if (LOG_ON) {
                Log.d(TAG, notas.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return notas;
    }

    public static List<Nota> getNotasFromWebService(Context context, String id_disciplina) throws IOException{
        String json;

        json = doPost(AppConfig.URL_NOTA, context, id_disciplina);
        salvaArquivoNaMemoriaInterna(context, id_disciplina, json);
        List<Nota> notas = parserJSON(context, json);
        return notas;
    }

    //Abre o arquivo salvo, se existir, e cria a lista de carros
    public static List<Nota> getNotasFromArquivo(Context context, String id_disciplina) throws IOException{
        String fileName = String.format("notas_disciplina_" + id_disciplina + ".json");
        Log.d(TAG, "Abrindo o arquivo: " + fileName);
        //Lê o arquivo da memória interna
        String json = readFile(context, fileName, "UTF-8");

        if(json == null){
            Log.d(TAG, "Arquivo " + fileName + " não encontrado.");
            return null;
        }

        List<Nota> notas = parserJSON(context, json);
        Log.d(TAG, "Notas lidas do arquivo " + fileName);
        return notas;
    }

    private static void salvaArquivoNaMemoriaInterna(Context context, String id_disciplina, String json){
        String fileName = "notas_disciplina_" + id_disciplina + ".json";
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
