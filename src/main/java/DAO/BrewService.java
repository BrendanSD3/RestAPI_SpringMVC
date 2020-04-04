/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author brend
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import model.Breweries;
import model.BreweriesGeocode;
import model.DBUtil;
import org.springframework.stereotype.Service;


@Service
public class BrewService {


    
    public List<Breweries> getall(int pagenum, int pagesize )
    {
      EntityManager em = DBUtil.getEMF().createEntityManager();
        String query="SELECT b FROM Breweries b";
        TypedQuery<Breweries> tq = em.createQuery(query, Breweries.class);
//        
        tq.setFirstResult((pagenum-1)*pagesize);
        tq.setMaxResults(pagesize);
 
        List<Breweries> list = null;
        
        try {
            list = tq.getResultList();
            //System.out.println("list"+ list);
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
      public void addBreweries(Breweries b)
      {
          EntityManager em =DBUtil.getEMF().createEntityManager();
          EntityTransaction trans=em.getTransaction();
          try{
              trans.begin();
              em.persist(b);
              trans.commit();
          }
      catch(Exception ex){
          System.out.println("ex"+ ex);
          
      }
          finally{
              em.close();
          }
      }
      public void updateBreweries(Breweries b)
      {
          EntityManager em = DBUtil.getEMF().createEntityManager();
           EntityTransaction trans=em.getTransaction();
          try{
              trans.begin();
              em.merge(b);
              trans.commit();
          }
      catch(Exception ex){
          System.out.println("ex"+ ex);
          
      }
          finally{
              em.close();
          }
      }
      public Breweries getBreweriesByID(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();

        Breweries brew = null;
        try {
            brew = em.find(Breweries.class, id);
            //System.out.println("PRinting result"+ brew);
            if (brew == null)
                return null;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }

        return brew;
    }
        public void deleteAnBreweries(int id)
      {
          
          Breweries b = getBreweriesByID(id);
          EntityManager em = DBUtil.getEMF().createEntityManager();
          EntityTransaction trans= em.getTransaction();
          try{
              trans.begin();
              em.remove(em.merge(b));
              trans.commit();
              
          }
          catch(Exception e){
              System.out.println("ex"+ e);
          }
          finally {
              em.close();
          }
      }
        
         public BreweriesGeocode getGeoByID(int id) {
        EntityManager em = DBUtil.getEMF().createEntityManager();

        BreweriesGeocode Geobrew = null;
        try {
            Geobrew = em.find(BreweriesGeocode.class, id);
            //System.out.println("PRinting result"+ Geobrew);
            if (Geobrew == null)
                return null;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }

        return Geobrew;
    }
        
        
        
        
}//end BreweriesService
