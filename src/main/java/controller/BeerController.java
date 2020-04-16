/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.BeerService;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.List;
import model.Beers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import model.Categories;
import model.Styles;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;



@RestController
@RequestMapping("/beers")
public class BeerController {
    @Autowired
    BeerService service;
    
       @JsonView(Views.Beeridnameabvprice.class)
    @GetMapping("/id/name/abv/price")//Get all 
     //see previous comment
    public List<Beers> getallbeernameid() {
        return service.getall();
     }
        @JsonView(Views.Beeridnameabvprice.class)
    @GetMapping("/{id}/name/abv/price")//Get by ID
     //see previous comment
    public Beers getbeernamebyid(@PathVariable("id") int id) {
        return service.getBeerByID(id);
     }
    
    @Path("")
       @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
      public Resources<Beers> getBeers(@QueryParam("page")int page,@QueryParam("size") int size) throws WriterException, IOException {
            //System.out.println("HEERRREEE in get BEERS");
          if(page<=0||size<=0)
          {
          page=1;
          size=5;
          }
          List<Beers> allBeers = service.getalllimit(page, size);
          
           
        for(Beers b : allBeers)
            {
                int id= b.getBeersId();
                int brewid=b.getBreweryId();
                int styleid=b.getStyleId();
                int catid=b.getCatId();
               
                
                Link selfLink = linkTo(this.getClass()).slash(id).withSelfRel();
                ControllerLinkBuilder brewlink=linkTo(TasteRestController.class);
                b.add(selfLink);
                if(styleid>=1)
                {
                   Link stylelink = linkTo(this.getClass())
                           .slash("styles")
                           .slash(styleid)
                           .withRel("Style").withType("GET");
                   b.add(stylelink);
                }
                if(catid >=1){
                Link catlink =linkTo(this.getClass()).slash("categories").slash(catid).withRel("Category").withType("GET");
                b.add(catlink);
                }
                b.add(brewlink.slash(brewid).withRel("Breweries"));
                linkTo(methodOn(this.getClass()).getbeerbyid(id));
            }
       
        Link link =linkTo(this.getClass()).slash("?page="+page+"&size="+size).withSelfRel();
        Resources<Beers> result=new Resources<Beers>(allBeers,link);
        return result;
                          
      }
    
      @GetMapping(value="/{id}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resource getbeerbyid(@PathVariable("id") int id) throws WriterException, IOException {
     
      Beers b = service.getBeerByID(id);
      
       if(b!=null)
       {
          
        // resource.add(linkTo(methodOn(this.getClass()).getBreweries(1,10)).withRel("AllBReweries"));
         //resource.add(getqrcode(id));
         int page=1;
          int size=10;
           Link allbeers = linkTo(this.getClass()).slash("?page="+page+"&size="+size).withRel("allBeers").withType("GET");
        //resource.add(linkTo.withRel("all-Beers"));
        
        Resource resource = new Resource<Beers>(b,allbeers);
        //resource.add(getqrcode(id).withRel("QRcode"));
      
          System.out.println("resource"+resource.toString());
        return resource;    
       }
       else{
           throw new NotFoundException("Not Found with id "+ id );
       
       }
      }
      @GetMapping(value="/categories/{catid}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resource getcategorybyid(@PathVariable("catid") int catid) throws WriterException, IOException  {
        
       //Categories cat= service.getCategoryByID(catid);
//        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBeers(1,10));
//        
//        resource.add(linkTo.withRel("all-Beers"));
         int page=1;
          int size=10;
           Link allbeers = linkTo(this.getClass()).slash("?page="+page+"&size="+size).withRel("allBeers").withType("GET");
      Resource resource = new Resource<Categories>(service.getCategoryByID(catid),allbeers);
          System.out.println("resource"+resource.toString());
        return resource;    
                
      }
      
            @GetMapping(value="/styles/{styleid}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resource getstylebyid(@PathVariable("styleid") int styleid) throws WriterException, IOException  {
        
       //Categories cat= service.getCategoryByID(catid);
        //ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBeers(1,10));
        
        //resource.add(linkTo.withRel("all-Beers"));
        int page=1;
          int size=10;
           Link allbeers = linkTo(this.getClass()).slash("?page="+page+"&size="+size).withRel("allBeers").withType("GET");
        Resource resource = new Resource<Styles>(service.getStylesByID(styleid),allbeers);
      
          System.out.println("resource"+resource.toString());
        return resource;    
                
      }
      @PatchMapping(value="{id}/sellprice")
      @ResponseStatus(HttpStatus.OK)
      public String updateSellprice(@RequestBody double sellPrice, @PathVariable("id") int id)
      {
          System.out.println("sellprice" + sellPrice + " Formatted"+ String.format("%.2f", sellPrice));
          double fsellprice=Double.parseDouble(String.format("%.2f", sellPrice));
          
          System.out.println("sellPrice"+ fsellprice);
          boolean success=false;
          
          Beers b = service.getBeerByID(id);
          if(b==null)
          {
              return "Error occured";
          }
          
          double oldprice=b.getSellPrice();
          
          success=service.updateBeerSellPrice(fsellprice, id);
                    
          if(success==true)
          {
          return "Updated the sell price from: " + oldprice + " to:  "+ fsellprice;
          }
          else{
             return "Some Error occured";
          }
          
          
      }
      
//     Categories cat= service.getCategoryByID(catid);
    
}
