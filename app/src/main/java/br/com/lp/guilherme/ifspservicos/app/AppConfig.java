package br.com.lp.guilherme.ifspservicos.app;

/**
 * Created by Guilherme on 01-Nov-15.
 */
public class AppConfig {
    // Server user login url
    public static String URL_BASE = "http://192.168.1.16/IFSP-ServicosWS/";
    public static String URL_LOGIN =  URL_BASE + "usuario/login";
    public static String URL_DATA_AVALIACOES = URL_BASE + "data/listarProximasAvaliacoes";
    public static String URL_DISCIPLINA = URL_BASE + "notas/mediaFinalAlunoTurmaDisciplina";
    public static String URL_NOTA = URL_BASE + "notas/listarNotasTurmaDisciplina";
    public static String URL_NOTICIA = URL_BASE + "noticia/listar";
    public static String URL_SEMESTRE = URL_BASE + "notas/listarSemestresDoAluno";
}
