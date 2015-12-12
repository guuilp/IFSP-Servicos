package br.com.lp.guilherme.ifspservicos.domain;

import java.io.Serializable;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class Telefone implements Serializable{
    private static final long serialVersionUID = 6601006766832473959L;

    public String id_telefone;
    public String local;
    public String ddd;
    public String numero;
    public String numero_formatado;
    public String area;

    @Override
    public String toString() {
        return "Telefone{" + "nome='" + id_telefone + '\'' + '}';
    }
}
