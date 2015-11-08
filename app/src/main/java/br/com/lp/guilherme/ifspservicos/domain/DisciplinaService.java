package br.com.lp.guilherme.ifspservicos.domain;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private static final String URL_3semestre = "http://192.168.1.15/IFSP-ServicosWS/medias";
    private static final String URL_4semestre = "http://192.168.1.15/IFSP-ServicosWS/medias";
    private static final String URL_5semestre = "http://192.168.1.15/IFSP-ServicosWS/medias";

    public static List<Disciplina> getDisciplinas(Context context, String semestre) throws IOException{
        String json;

        if ("3".equals(semestre)){
            json = doPost(URL_3semestre, context);
        } else if ("4".equals(semestre)){
            json = doPost(URL_4semestre, context);
        } else {
            json = doPost(URL_5semestre, context);
        }

        List<Disciplina> disciplinas = parserJSON(context, json);
        return disciplinas;
    }

    private static String doGet(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
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
                d.codigo = jsonDisciplina.optString("codigo_disciplina");
                d.descricao = "Descrição: " + jsonDisciplina.optString("nome_disciplina") + " (" + jsonDisciplina.optString("codigo_disciplina") + ")";
                d.nota = "Nota: " + jsonDisciplina.optString("media");
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

}
