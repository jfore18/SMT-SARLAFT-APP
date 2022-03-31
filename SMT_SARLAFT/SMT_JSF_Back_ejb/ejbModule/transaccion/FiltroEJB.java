package transaccion;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface FiltroEJB {
    Integer create(Hashtable pDatos) throws CreateException;

    Filtro findByPrimaryKey(Integer primaryKey) throws Exception;

    Collection findAll() throws FinderException;

    void actualizarFiltro(Filtro filtro);
    
    void actualizarFiltro2(Filtro filtro);

    void aprobarFiltro(Filtro filtro);

    void rechazarFiltro(Filtro filtro);

    Filtro getFiltro();

    Collection filtrosCliente(Hashtable cliente, Hashtable datosRegistro);
}
