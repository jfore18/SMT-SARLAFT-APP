package baseDatos;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.SimpleTimeZone;

import java.sql.Timestamp;

public final class Fecha {

    private static String[] ids = TimeZone.getAvailableIDs(-5 * 60 * 60 * 1000);
    private static SimpleTimeZone zonaHorariaBogota = new SimpleTimeZone(-5 * 60 * 60 * 1000, ids[0]);
    private GregorianCalendar calendario = new GregorianCalendar(zonaHorariaBogota);

    public Fecha() {
        this.calendario.setTime(new Date());
    }

    public Fecha(Timestamp fecha) {
        Date fechaD = new Date(fecha.getTime());
        this.calendario.setTime(fechaD);
    }

    public Timestamp getFechaTimestamp() {
        String cadenaFecha =
            this.getAnio() + "-" + this.getMes() + "-" + this.getDia() + " " + this.getHoras() + ":" +
            this.getMinutos() + ":" + this.getSegundos();
        return Timestamp.valueOf(cadenaFecha);
    }

    public int getSegundos() {
        return this.calendario.get(this.calendario.SECOND);
    }

    public int getMinutos() {
        return this.calendario.get(this.calendario.MINUTE);
    }

    public int getHoras() {
        return this.calendario.get(this.calendario.HOUR);
    }

    public int getDia() {
        return this.calendario.get(this.calendario.DATE);
    }

    public int getMes() {
        return this.calendario.get(this.calendario.MONTH);
    }

    public int getAnio() {
        return this.calendario.get(this.calendario.YEAR);
    }

    public void ponerEn(Timestamp fecha) {
        Date fechaD = new Date(fecha.getTime());
        this.calendario.setTime(fechaD);
    }

    public Timestamp agregarDias(int dias) {
        this.calendario.add(this.calendario.DATE, dias);
        Timestamp nuevaFecha = this.getFechaTimestamp();
        this.calendario.add(this.calendario.DATE, -dias);
        return nuevaFecha;
    }

    public Timestamp sustraerDias(int dias) {
        this.calendario.add(this.calendario.DATE, -dias);
        Timestamp nuevaFecha = this.getFechaTimestamp();
        this.calendario.add(this.calendario.DATE, dias);
        return nuevaFecha;
    }

    public Timestamp agregarMeses(int meses) {
        this.calendario.add(this.calendario.MONTH, meses);
        Timestamp nuevaFecha = this.getFechaTimestamp();
        this.calendario.add(this.calendario.MONTH, -meses);
        return nuevaFecha;
    }

    public Timestamp sustraerMeses(int meses) {
        this.calendario.add(this.calendario.MONTH, -meses);
        Timestamp nuevaFecha = this.getFechaTimestamp();
        this.calendario.add(this.calendario.MONTH, meses);
        return nuevaFecha;
    }

    public Timestamp agregarAnios(int anios) {
        this.calendario.add(this.calendario.YEAR, anios);
        Timestamp nuevaFecha = this.getFechaTimestamp();
        this.calendario.add(this.calendario.YEAR, -anios);
        return nuevaFecha;
    }

    public Timestamp sustraerAnios(int anios) {
        this.calendario.add(this.calendario.YEAR, -anios);
        Timestamp nuevaFecha = this.getFechaTimestamp();
        this.calendario.add(this.calendario.YEAR, anios);
        return nuevaFecha;
    }

    public int[] diferenciaCon(Fecha fecha) {
        int[] diferencia = new int[3];
        diferencia[0] = this.getAnio() - fecha.getAnio();
        diferencia[1] = this.getMes() - fecha.getMes();
        diferencia[2] = this.getDia() - fecha.getDia();
        return diferencia;
    }

    public int aniosEntre(Fecha fecha) {
        int[] diferencia = fecha.diferenciaCon(this);

        if (diferencia[0] < 0) {
            if (diferencia[1] < 0) {
                return diferencia[0];
            } else {
                return (diferencia[0] + 1);
            }
        } else if (diferencia[0] > 0) {
            if (diferencia[1] < 0) {
                return (diferencia[0] - 1);
            } else {
                return (diferencia[0]);
            }
        } else {
            return diferencia[0];
        }
    }

    public String toString() {
        return this.calendario.getTime().toString();
    }

}
