package transaccion;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface ComentarioEJB {
    Integer create(Hashtable pDatos) throws CreateException;

    ComentarioEJB findByPrimaryKey(Integer primaryKey) throws FinderException;

    Collection findAll() throws FinderException;

    Comentario getComentario();
}
