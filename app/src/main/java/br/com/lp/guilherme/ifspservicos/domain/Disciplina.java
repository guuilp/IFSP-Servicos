package br.com.lp.guilherme.ifspservicos.domain;

import java.io.Serializable;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class Disciplina implements Serializable{
    private static final long serialVersionUID = 6601006766832473959L;

    public String id;
    public String codigo;
    public String descricao;
    public String nota;
    public String frequencia;
    public String descricao_situacao;

    @Override
    public String toString() {
        return "Disciplina{" + "nome='" + codigo + '\'' + '}';
    }
}
