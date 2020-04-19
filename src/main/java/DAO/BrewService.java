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

 public List<Breweries> getallnolimit()
    {
      EntityManager em = DBUtil.getEMF().createEntityManager();
        String query="SELECT b FROM Breweries b";
        TypedQuery<Breweries> tq = em.createQuery(query, Breweries.class);
//        
        
 
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
    public void addGeoBrew(float lat, float lon)
      {
         
          System.out.println("lat"+lat + "lon"+lon);
          int brew_id=getmaxBreweryID();
          int geoID=getmaxBreweryGeoID();
          BreweriesGeocode b= new BreweriesGeocode(geoID,brew_id,lat,lon);
          
          EntityManager em =DBUtil.getEMF().createEntityManager();
          EntityTransaction trans=em.getTransaction();
          try{
              trans.begin();
              em.persist(b);
              trans.commit();
              //b.getId();
              System.out.println("adding geo"+b.toString());
              
          }
      catch(Exception ex){
          System.out.println("ex"+ ex);
          
      }
          finally{
              em.close();
          }
      }
      public int getmaxBreweryID()
       {
             EntityManager em = DBUtil.getEMF().createEntityManager();
        Integer maxId = (Integer) em.createNamedQuery("Breweries.findMaxId").getSingleResult();
       
       
           System.out.println("maxid"+maxId);
       return maxId;
       
       }
          public int getmaxBreweryGeoID()
       {
             EntityManager em = DBUtil.getEMF().createEntityManager();
        Integer maxId = (Integer) em.createNamedQuery("BreweriesGeocode.findMaxId").getSingleResult();
       maxId+=1;
       
           System.out.println("maxgeoid"+maxId);
       return maxId;
       
       }
      public void addBreweries(Breweries b)
      {
          
//          System.out.println("Lat"+ b.getLatitude());
          float lat=b.getLatitude();
          float lon= b.getLongitude();
          System.out.println("Adding new geo brew aswell with lat and lon = "+ lat + lon);
         
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
          
          //addGeoBrew(lat,lon);
      }
      public void updateBreweries(Breweries b)//update multiple fields
      {
          EntityManager em = DBUtil.getEMF().createEntityManager();
           EntityTransaction trans=em.getTransaction();
          try{
              trans.begin();
              em.merge(b);
              trans.commit();
              System.out.println("Updating");
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
        public boolean deleteBrewery(int id)
      {
          boolean isdeleted = false;
          Breweries b = getBreweriesByID(id);
          if(b==null)
          {
              return isdeleted;
          }
          else{
          EntityManager em = DBUtil.getEMF().createEntityManager();
          EntityTransaction trans= em.getTransaction();
          try{
              trans.begin();
              em.remove(em.merge(b));
              trans.commit();
              isdeleted=true;
          }
          catch(Exception e){
              isdeleted=false;
              System.out.println("ex"+ e);
          }
          finally {
              em.close();
          }
          return isdeleted;
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
    public BreweriesGeocode getGeoByBrewID(int brewid) {
        EntityManager em = DBUtil.getEMF().createEntityManager();

        BreweriesGeocode Geobrew = null;
     
        String query="SELECT b FROM BreweriesGeocode b WHERE b.breweryId = :breweryId";
        TypedQuery<BreweriesGeocode> tq = em.createQuery(query, BreweriesGeocode.class);
//        
        tq.setParameter("breweryId", brewid);
        
        
        try {
            Geobrew = tq.getSingleResult();
            System.out.println("result"+ Geobrew);
            if (Geobrew == null ) {
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }
        //System.out.println("Got the list"+list);
       
        return Geobrew;
    }
        public Breweries getBrewbyname(String name) {
        EntityManager em = DBUtil.getEMF().createEntityManager();

        Breweries brew = null;
     
        String query="SELECT b FROM Breweries b WHERE b.name = :name";
        TypedQuery<Breweries> tq = em.createQuery(query, Breweries.class);
//        
        tq.setParameter("name", name);
        
        
        try {
            brew = tq.getSingleResult();
            System.out.println("result"+ brew);
            if (brew == null ) {
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            em.close();
        }
        //System.out.println("Got the list"+list);
       
        return brew;
    }
        
        
}//end BreweriesService
