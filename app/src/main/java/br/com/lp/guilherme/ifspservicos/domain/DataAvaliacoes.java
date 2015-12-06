package br.com.lp.guilherme.ifspservicos.domain;

import java.io.Serializable;

/**
 * Created by Guilherme on 05/12/2015.
 */
public class DataAvaliacoes implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;

    public String descricao_disciplina;
    public String descricao_avaliacao;
    public String data_avaliacao;
    public String dias_restantes;

    @Override
    public String toString() {
        return "Nota{" + "nome='" + descricao_avaliacao + '\'' + '}';
    }
}
