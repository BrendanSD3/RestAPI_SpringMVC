/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.BeerService;
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
import controller.TasteRestController;
import model.Categories;
import model.Styles;
/**
 *
 * @author brend
 */


@RestController
@RequestMapping("/beers")
public class BeerController {
    @Autowired
    BeerService service;
    
       @GetMapping(value="/{page}/{size}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resources<Beers> getBeers(@PathVariable("page")int page,@PathVariable("size") int size) throws WriterException, IOException {
            //System.out.println("HEERRREEE in get BEERS");
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
                           .withRel("Style");
                   b.add(stylelink);
                }
                if(catid >=1){
                Link catlink =linkTo(this.getClass()).slash("categories").slash(catid).withRel("Category");
                b.add(catlink);
                }
                b.add(brewlink.slash(brewid).withRel("Breweries"));
                linkTo(methodOn(this.getClass()).getbeerbyid(id));
            }
        Link link =linkTo(this.getClass()).withSelfRel();
        Resources<Beers> result=new Resources<Beers>(allBeers,link);
        return result;
                          
      }
    
      @GetMapping(value="/{id}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resource getbeerbyid(@PathVariable("id") int id) throws WriterException, IOException {
        Resource resource = new Resource<Beers>(service.getBeerByID(id));
        // resource.add(linkTo(methodOn(this.getClass()).getBreweries(1,10)).withRel("AllBReweries"));
         //resource.add(getqrcode(id));
        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBeers(1,10));
        resource.add(linkTo.withRel("all-Beers"));
        //resource.add(getqrcode(id).withRel("QRcode"));
      
          System.out.println("resource"+resource.toString());
        return resource;    
                
      }
      @GetMapping(value="/categories/{catid}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resource getcategorybyid(@PathVariable("catid") int catid) throws WriterException, IOException  {
        Resource resource = new Resource<Categories>(service.getCategoryByID(catid));
       //Categories cat= service.getCategoryByID(catid);
        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBeers(1,10));
        
        resource.add(linkTo.withRel("all-Beers"));
        
      
          System.out.println("resource"+resource.toString());
        return resource;    
                
      }
      
            @GetMapping(value="/styles/{styleid}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resource getstylebyid(@PathVariable("styleid") int styleid) throws WriterException, IOException  {
        Resource resource = new Resource<Styles>(service.getStylesByID(styleid));
       //Categories cat= service.getCategoryByID(catid);
        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBeers(1,10));
        
        resource.add(linkTo.withRel("all-Beers"));
        
      
          System.out.println("resource"+resource.toString());
        return resource;    
                
      }
      
      
//     Categories cat= service.getCategoryByID(catid);
    
}
