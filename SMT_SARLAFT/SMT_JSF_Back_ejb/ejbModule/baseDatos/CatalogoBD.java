package baseDatos;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

import java.util.Locale;

public abstract class CatalogoBD {

    public static String urlDataSource = "jdbc/SMT_SARLAFT";

    public static int codigoFormatoFecha = DateFormat.MEDIUM;
    public static int codigoFormatoHora = DateFormat.MEDIUM;

    public static Locale localizacion = new Locale("es", "CO");
    public static String patronFormatoFecha = "yyyy/MM/dd";
    public static String patronFormatoFechaHora = "yyyy/MM/dd HH:mm";
    public static SimpleDateFormat formatoFecha = new SimpleDateFormat(patronFormatoFecha, localizacion);
    public static SimpleDateFormat formatoFechaHora = new SimpleDateFormat(patronFormatoFechaHora, localizacion);
    public static int numeroColumnasConsulta = 30;


    public CatalogoBD() {
    }


}
