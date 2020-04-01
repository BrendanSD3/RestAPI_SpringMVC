
package model;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author brend
 */
public class DBUtil {
     private static final EntityManagerFactory EMF = 
            Persistence.createEntityManagerFactory("taste_PU");
    
    public static EntityManagerFactory getEMF() { return EMF; }
}

