package br.com.lp.guilherme.ifspservicos.domain;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class DisciplinaService {

    public static List<Disciplina> getDisciplinas(Context context){
        List<Disciplina> materias = new ArrayList<Disciplina>();
        List<String> materia = new ArrayList<String>();
        materia.add("Português");
        materia.add("História");
        materia.add("Geografia");
        for (int i = 0; i < materia.size(); i++) {
            Disciplina d = new Disciplina();
            d.nome = "Disciplina: " + materia.get(i);
            d.nota = "Nota: " + i;
            d.frequencia = "Frequência: " + i*10 + "%";
            materias.add(d);
        }
        return materias;
    }

}
