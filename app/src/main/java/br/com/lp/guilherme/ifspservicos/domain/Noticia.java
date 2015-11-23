package br.com.lp.guilherme.ifspservicos.domain;

import java.io.Serializable;

/**
 * Created by Guilherme on 22-Nov-15.
 */
public class Noticia implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;

    public long id_noticia;
    public String titulo;
    public String corpo;
}
