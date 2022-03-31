package admin;

import admin.autenticacion.AutenticacionEJB;

import admin.seguridad.LogConsultaEJB;
import admin.seguridad.LogIngresoEJB;

import admin.usuario.UsuarioEJB;

import admin.zona.ZonasEJB;

import baseDatos.ConsultaEJB;
import baseDatos.ConsultaTablaEJB;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import presentacion.FacadeEJB;

import transaccion.ComentarioCargoEJB;
import transaccion.ComentarioEJB;
import transaccion.DetalleAnalisisRepEJB;
import transaccion.FiltroEJB;
import transaccion.HistoricoTransaccionEJB;
import transaccion.ReporteEJB;
import transaccion.TransaccionEJB;

public class Contextos {
    static String ctxProy;

    public Contextos() {
        ctxProy = "SMT_JSF";
    }

    public Object getContext(String nombreEJB) throws NamingException {
        Object ctx = null;
        if (nombreEJB.equals("ConsultaTablaEJB")) {
            final InitialContext context = new InitialContext();
            return (ConsultaTablaEJB) context.lookup(ctxProy + "#baseDatos.ConsultaTablaEJB");
        } else if (nombreEJB.equals("AutenticacionEJB")) {
            final InitialContext context = new InitialContext();
            return (AutenticacionEJB) context.lookup(ctxProy + "#admin.autenticacion.AutenticacionEJB");
        } else if (nombreEJB.equals("ConsultaEJB")) {
            final InitialContext context = new InitialContext();
            return (ConsultaEJB) context.lookup(ctxProy + "#baseDatos.ConsultaEJB");
        } else if (nombreEJB.equals("LogIngresoEJB")) {
            final InitialContext context = new InitialContext();
            return (LogIngresoEJB) context.lookup(ctxProy + "#admin.seguridad.LogIngresoEJB");
        } else if (nombreEJB.equals("LogConsultaEJB")) {
            final InitialContext context = new InitialContext();
            return (LogConsultaEJB) context.lookup(ctxProy + "#admin.seguridad.LogConsultaEJB");
        } else if (nombreEJB.equals("UsuarioEJB")) {
            final InitialContext context = new InitialContext();
            return (UsuarioEJB) context.lookup(ctxProy + "#admin.usuario.UsuarioEJB");
        } else if (nombreEJB.equals("TransaccionEJB")) {
            final InitialContext context = new InitialContext();
            return (TransaccionEJB) context.lookup(ctxProy + "#transaccion.TransaccionEJB");
        } else if (nombreEJB.equals("HistoricoTransaccionEJB")) {
            final InitialContext context = new InitialContext();
            return (HistoricoTransaccionEJB) context.lookup(ctxProy + "#transaccion.HistoricoTransaccionEJB");
        } else if (nombreEJB.equals("HistoricoPersonasReportadasEJB")) {
            final InitialContext context = new InitialContext();
            return (HistoricoPersonasReportadasEJB) context.lookup(ctxProy + "#admin.HistoricoPersonasReportadasEJB");
        } else if (nombreEJB.equals("HistoricoUsuarioCargoEJB")) {
            final InitialContext context = new InitialContext();
            return (HistoricoUsuarioCargoEJB) context.lookup(ctxProy + "#admin.HistoricoUsuarioCargoEJB");
        } else if (nombreEJB.equals("FacadeEJB")) {
            final InitialContext context = new InitialContext();
            return (FacadeEJB) context.lookup(ctxProy + "#presentacion.FacadeEJB");
        } else if (nombreEJB.equals("ComentarioCargoEJB")) {
            final InitialContext context = new InitialContext();
            return (ComentarioCargoEJB) context.lookup(ctxProy + "#transaccion.ComentarioCargoEJB");
        } else if (nombreEJB.equals("ComentarioEJB")) {
            final InitialContext context = new InitialContext();
            return (ComentarioEJB) context.lookup(ctxProy + "#transaccion.ComentarioEJB");
        } else if (nombreEJB.equals("CargoEJB")) {
            final InitialContext context = new InitialContext();
            return (CargoEJB) context.lookup(ctxProy + "#admin.CargoEJB");
        } else if (nombreEJB.equals("ZonasEJB")) {
            final InitialContext context = new InitialContext();
            return (ZonasEJB) context.lookup(ctxProy + "#admin.zona.ZonasEJB");
        } else if (nombreEJB.equals("UnidadesNegocioEJB")) {
            final InitialContext context = new InitialContext();
            return (UnidadesNegocioEJB) context.lookup(ctxProy + "#admin.UnidadesNegocioEJB");
        } else if (nombreEJB.equals("PersonasReportadasEJB")) {
            final InitialContext context = new InitialContext();
            return (PersonasReportadasEJB) context.lookup(ctxProy + "#admin.PersonasReportadasEJB");
        } else if (nombreEJB.equals("ListaValoresEJB")) {
            final InitialContext context = new InitialContext();
            return (ListaValoresEJB) context.lookup(ctxProy + "#admin.ListaValoresEJB");
        } else if (nombreEJB.equals("PreguntasEJB")) {
            final InitialContext context = new InitialContext();
            return (PreguntasEJB) context.lookup(ctxProy + "#admin.PreguntasEJB");
        } else if (nombreEJB.equals("HistoricoEntidadesExcluidasEJB")) {
            final InitialContext context = new InitialContext();
            return (HistoricoEntidadesExcluidasEJB) context.lookup(ctxProy + "#admin.HistoricoEntidadesExcluidasEJB");
        } else if (nombreEJB.equals("TipoTransaccionEJB")) {
            final InitialContext context = new InitialContext();
            return (TipoTransaccionEJB) context.lookup(ctxProy + "#admin.TipoTransaccionEJB");
        } else if (nombreEJB.equals("EntidadExcluidaEJB")) {
            final InitialContext context = new InitialContext();
            return (EntidadExcluidaEJB) context.lookup(ctxProy + "#admin.EntidadExcluidaEJB");
        } else if (nombreEJB.equals("CriteriosInusualidadEJB")) {
            final InitialContext context = new InitialContext();
            return (CriteriosInusualidadEJB) context.lookup(ctxProy + "#admin.CriteriosInusualidadEJB");
        } else if (nombreEJB.equals("ReporteEJB")) {
            final InitialContext context = new InitialContext();
            return (ReporteEJB) context.lookup(ctxProy + "#transaccion.ReporteEJB");
        } else if (nombreEJB.equals("FiltroEJB")) {
            final InitialContext context = new InitialContext();
            return (FiltroEJB) context.lookup(ctxProy + "#transaccion.FiltroEJB");
        } else if (nombreEJB.equals("DetalleAnalisisRepEJB")) {
            final InitialContext context = new InitialContext();
            return (DetalleAnalisisRepEJB) context.lookup(ctxProy + "#transaccion.DetalleAnalisisRepEJB");
        }
        return ctx;
    }
}
