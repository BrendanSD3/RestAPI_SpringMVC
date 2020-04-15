/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.awt.print.Pageable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import model.Beers;
import model.Categories;
import model.DBUtil;
import model.Styles;
import org.springframework.stereotype.Service;

/**
 *
 * @author brend
 */
@Service
public class BeerService {
    
     public List<Beers> getalllimit(int pagenum, int pagesize )
    {
        //System.out.println("Here");
      EntityManager em = DBUtil.getEMF().createEntityManager();
        String query="SELECT b FROM Beers b";
        TypedQuery<Beers> tq = em.createQuery(query, Beers.class);
//        
        tq.setFirstResult((pagenum-1)*pagesize);
        tq.setMaxResults(pagesize);
 
        List<Beers> list = null;
        
        try {
            list = tq.getResultList();
            System.out.println("list"+ list);
            if (list == null || list.size() == 0) {
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }
        //System.out.println("Got the list"+list);
        return list;       
    
    
    }
     public boolean updateBeerSellPrice(double sellPrice, int id)
     {
     EntityManager em = DBUtil.getEMF().createEntityManager();
     boolean updated=false;
        String query="update Beers set sellPrice = :sellPrice where id = :id ";
        TypedQuery<Beers> tq = em.createQuery(query, Beers.class);
        tq.setParameter("sellPrice" , sellPrice);
        tq.setParameter("id", id);
       
        EntityTransaction trans=em.getTransaction();
          try{
              trans.begin();
              tq.executeUpdate();
              trans.commit();
              //System.out.println("Updating");
              updated=true;
          }
      catch(Exception ex){
          System.out.println("ex"+ ex);
          updated=false;
      }
          finally{
              em.close();
          }
     return updated;
     }
     
     
    public List<Beers> getall( )
    {
        //System.out.println("Here");
      EntityManager em = DBUtil.getEMF().createEntityManager();
        String query="SELECT b FROM Beers b";
        TypedQuery<Beers> tq = em.createQuery(query, Beers.class);
//        
        
        List<Beers> list = null;
        
        try {
            list = tq.getResultList();
            System.out.println("list"+ list);
            if (list == null || list.size() == 0) {
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }
        //System.out.println("Got the list"+list);
        return list;       
    
    
    }
    
    public Beers getBeerByID(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();

        Beers beer = null;
        try {
            beer = em.find(Beers.class, id);
            //System.out.println("PRinting result"+ brew);
            if (beer == null)
                return null;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }

        return beer;
    }
        
    public Categories getCategoryByID(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
            
        Categories cat = null;
        try {
            cat = em.find(Categories.class, id);
            //System.out.println("PRinting result"+ brew);
            if (cat == null)
                return null;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }

        return cat;
    }
     public Styles getStylesByID(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();
            
        Styles style = null;
        try {
            style = em.find(Styles.class, id);
            //System.out.println("PRinting result"+ brew);
            if (style == null)
                return null;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }

        return style;
    }
}
