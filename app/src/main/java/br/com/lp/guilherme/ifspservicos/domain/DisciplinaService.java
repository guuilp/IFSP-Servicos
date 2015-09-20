package br.com.lp.guilherme.ifspservicos.domain;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class DisciplinaService {

    private static final boolean LOG_ON = false;
    private static final String TAG = "DisciplinaService";
    private static final String URL_3semestre = "http://wsmock.com/v2/55ff325805f09c2f013aacb8";
    private static final String URL_4semestre = "http://wsmock.com/v2/55ff321505f09c2f013aacb6";
    private static final String URL_5semestre = "http://wsmock.com/v2/55fed3fa05f09cf5003aacb4";
    private static final String URL = "URL_{numero}semestre";

    public static List<Disciplina> getDisciplinas(Context context, String semestre) throws IOException{
        String json;

        if ("3".equals(semestre)){
            json = doGet(URL_3semestre);
        } else if ("4".equals(semestre)){
            json = doGet(URL_4semestre);
        } else {
            json = doGet(URL_5semestre);
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

    private static List<Disciplina> parserJSON(Context context, String json) throws IOException {
        List<Disciplina> disciplinas = new ArrayList<Disciplina>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("disciplinas");
            JSONArray jsonDisciplinas = obj.getJSONArray("disciplina");
            // Insere cada Disciplina na lista
            for (int i = 0; i < jsonDisciplinas.length(); i++) {
                JSONObject jsonDisciplina = jsonDisciplinas.getJSONObject(i);
                Disciplina d = new Disciplina();
                // Lê as informações de cada Disciplina
                d.codigo = jsonDisciplina.optString("codigo");
                d.descricao = "Descrição: " + jsonDisciplina.optString("descricao") + " (" + jsonDisciplina.optString("codigo") + ")";
                d.nota = "Nota: " + jsonDisciplina.optString("nota");
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
