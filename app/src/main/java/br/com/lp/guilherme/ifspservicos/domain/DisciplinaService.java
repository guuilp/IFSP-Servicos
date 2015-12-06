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
 * Created by Guilherme on 08-Sep-15.
 */
public class DisciplinaService {

    private static final boolean LOG_ON = false;
    private static final String TAG = "DisciplinaService";
    private static final String URL = "http://192.168.1.10/IFSP-ServicosWS/notas/mediaFinalAlunoTurmaDisciplina";

    public static List<Disciplina> getDisciplinas(Context context, String ano, String semestre) throws IOException{
        List<Disciplina> disciplinas = null;

        if(disciplinas != null && disciplinas.size() > 0){
            //Encontrou o arquivo
            return disciplinas;
        }
        if (isOnline(context)) {
            //Se estiver online, busca do webservice
            disciplinas = getDisciplinasFromWebService(context, ano, semestre);
        } else {
            //Se não estiver online, busca do JSON local
            disciplinas = getDisciplinasFromArquivo(context, ano, semestre);
            Toast.makeText(context, "Conexão com a internet não disponível", Toast.LENGTH_SHORT).show();
        }
        return disciplinas;
    }

    private static String doPost(String url, Context context, String ano, String semestre) throws IOException{
        SQLiteHandler db = new SQLiteHandler(context);

        HashMap<String, String> user = db.getUserDetails();
        String token = user.get("token");
        String id_usuario = user.get("id_usuario");

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = new FormEncodingBuilder()
                .add("id_usuario", id_usuario)
                .add("ano", ano)
                .add("semestre", semestre)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private static List<Disciplina> parserJSON(Context context, String json) throws IOException {
        List<Disciplina> disciplinas = new ArrayList<Disciplina>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray jsonDisciplinas = root.getJSONArray("data");
            // Insere cada Disciplina na lista
            for (int i = 0; i < jsonDisciplinas.length(); i++) {
                JSONObject jsonDisciplina = jsonDisciplinas.getJSONObject(i);
                Disciplina d = new Disciplina();
                // Lê as informações de cada Disciplina
                d.id = jsonDisciplina.optString("id_disciplina");
                d.codigo = jsonDisciplina.optString("codigo_disciplina");
                d.descricao = "Descrição: " + jsonDisciplina.optString("descricao_disciplina") + " (" + jsonDisciplina.optString("codigo_disciplina") + ")";
                d.nota = "Media: " + jsonDisciplina.optString("media_final");
                d.frequencia = "Frequencia: " + jsonDisciplina.optString("frequencia");
                if (LOG_ON) {
                    Log.d(TAG, "Disciplina " + d.codigo);
                }
                disciplinas.add(d);
            }
            if (LOG_ON) {
                Log.d(TAG, disciplinas.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return disciplinas;
    }

    public static List<Disciplina> getDisciplinasFromWebService(Context context, String ano, String semestre) throws IOException{
        String json;

        json = doPost(URL, context, ano, semestre);
        salvaArquivoNaMemoriaInterna(context, ano, semestre, json);
        List<Disciplina> disciplinas = parserJSON(context, json);
        return disciplinas;
    }

    //Abre o arquivo salvo, se existir, e cria a lista de carros
    public static List<Disciplina> getDisciplinasFromArquivo(Context context, String ano, String semestre) throws IOException{
        String fileName = String.format("disciplinas_ano_" + ano + "_semestre_" + semestre + ".json");
        Log.d(TAG, "Abrindo o arquivo: " + fileName);
        //Lê o arquivo da memória interna
        String json = readFile(context, fileName, "UTF-8");

        if(json == null){
            Log.d(TAG, "Arquivo " + fileName + " não encontrado.");
            return null;
        }

        List<Disciplina> disciplinas = parserJSON(context, json);
        Log.d(TAG, "Disciplinas lidas do arquivo " + fileName);
        return disciplinas;
    }

    private static void salvaArquivoNaMemoriaInterna(Context context, String ano, String semestre, String json){
        String fileName = "disciplinas_ano_" + ano + "_semestre_" + semestre + ".json";
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
