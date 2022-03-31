package admin.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.crypto.Data;

public class Funciones {

    public Funciones() {
    }

    public String condicionFiltro(String condicion) {
        String operador = condicion.substring(0, 1);
        condicion = condicion.substring(1, condicion.length());
        if (operador.equals("<")) {
            condicion = "MENOR A " + this.formatoValor(condicion);
        }
        if (operador.equals("#")) {
            String[] valores = condicion.split(",");
            condicion = "ENTRE " + this.formatoValor(valores[0]) + " Y " + this.formatoValor(valores[1]);
        }
        return condicion;
    }

    public Hashtable quitarNull(Hashtable objeto) {
        Enumeration enuTemp = objeto.keys();
        while (enuTemp.hasMoreElements()) {
            String elemTemp = enuTemp.nextElement().toString();
            if (objeto.get(elemTemp) == null || objeto.get(elemTemp).equals("null")) {
                objeto.replace(elemTemp, "");
            }
        }
        return objeto;
    }

    public String mayorR(Object ob) {
        String valor = ob.toString();
        if (!valor.isEmpty()) {
            if (valor.equals("1")) {
                valor = "SI";
            } else {
                valor = "NO";
            }
        }
        return valor;
    }

    public String formatoValor(Object ob) {
        String valor = ob.toString();
        if (!valor.isEmpty()) {
            try {
                DecimalFormat formatoValor = new DecimalFormat("$###,###,###,###.##");
                valor = formatoValor.format(new java.math.BigDecimal(valor));
            } catch (Exception e) {
                System.out.println("Funciones|formatoValor: " + e.getMessage());
            }
        }
        return valor;
    }

    public String formatoPorcentaje(Object ob) {
        String valor = ob.toString();
        if (!valor.isEmpty()) {
            try {
                DecimalFormat formatoPorcentaje = new DecimalFormat("###.### %");
                valor = formatoPorcentaje.format(new java.math.BigDecimal(valor));
            } catch (Exception e) {
                System.out.println("Funciones|formatoPorcentaje: " + e.getMessage());
            }
        }
        return valor;
    }

    public String formatoMiles(Object ob) {
        String valor = ob.toString();
        if (!valor.isEmpty()) {
            try {
                DecimalFormat formatoPorcentaje = new DecimalFormat("###,###");
                valor = formatoPorcentaje.format(new java.math.BigDecimal(valor));
            } catch (Exception e) {
                System.out.println("Funciones|formatoMiles: " + e.getMessage());
            }
        }
        return valor;
    }

    public String formatoFecha(Object ob) {
        String fecha = ob.toString();
        if (!fecha.isEmpty()) {
            try {
                fecha = fecha.substring(0, 10).replace("-", "/");
            } catch (Exception e) {
                System.out.println("Funciones|formatoFecha: " + e.getMessage());
            }
        }
        return fecha;
    }

    public String formatoFechaHora(Object ob) {
        String fecha = ob.toString();
        if (!fecha.isEmpty()) {
            try {
                fecha = fecha.substring(0, 16).replace("-", "/");
            } catch (Exception e) {
                System.out.println("Funciones|formatoFecha: " + e.getMessage());
            }
        }
        return fecha;
    }

    public String str_AAAAMMDD_str_DDMMAAA(Object ob) {
        String fecha = "";
        try {
            fecha = ob.toString();
            String[] val = fecha.split("/");
            String dia = val[2]; //fecha.substring(8, 2);
            String mes = val[1]; //fecha.substring(5, 2);
            String anio = val[0]; //fecha.substring(0, 4);
            fecha = dia + "/" + mes + "/" + anio;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public String dateToString(Date fechaIn) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        if (fechaIn != null) {
            return format.format(fechaIn);
        }
        return null;
    }
    public String dateToStringddMMaaaa(Date fechaIn) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaIn != null) {
            return format.format(fechaIn);
        }
        return null;
    }
    public Object dateToString2(Object ob) {
        try {
            Date fecha = (Date) ob;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            return format.format(fecha);
        } catch (Exception e) {
            return ob;
        }
    }

    public Object stringToDate(Object ob) {
        try {
            String fecha = (String) ob;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date fechaRet = format.parse(fecha);
            return fechaRet;
        } catch (Exception e) {
            return ob;
        }
    }

    public static String codifica(String input) {
        return Funciones64.encodeString(input) + "|" + FuncionesMD5.getMD5(input).substring(0, 10) + "p" +
               input.length() + "f" + FuncionesMD5.getMD5(input).substring(10);
    }

    public static String decodifica(String input) {
        String cadena = "";

        try {
            cadena = Funciones64.decodeString(input);
        } catch (Exception ex) {
            cadena = "error";
        }
        return cadena;

    }

    public static Boolean valida(String input) {
        String _base64 = decodifica(input);
        if (input.equals(codifica(_base64)))
            return true;
        else
            return false;
    }
}
