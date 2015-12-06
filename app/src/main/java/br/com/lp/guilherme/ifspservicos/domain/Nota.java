package br.com.lp.guilherme.ifspservicos.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Guilherme on 29/11/2015.
 */
public class Nota implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;

    public String descricao_avaliacao;
    public String id_disciplina;
    public String data_avaliacao;
    public String nota_avaliacao;
    public String peso_avaliacao;

    @Override
    public String toString() {
        return "Nota{" + "nome='" + descricao_avaliacao + '\'' + '}';
    }
}
