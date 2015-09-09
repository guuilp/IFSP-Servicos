package br.com.lp.guilherme.ifspservicos.domain;

import java.io.Serializable;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class Disciplina implements Serializable{
    private static final long serialVersionUID = 6601006766832473959L;

    public long id;
    public String nome;
    public String nota;
    public String frequencia;

    @Override
    public String toString() {
        return "Disciplina{" + "nome='" + nome + '\'' + '}';
    }
}
