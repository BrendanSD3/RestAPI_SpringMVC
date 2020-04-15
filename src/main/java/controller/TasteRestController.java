/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import model.Breweries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import DAO.BrewService;
import com.fasterxml.jackson.annotation.JsonView;
import javax.ws.rs.QueryParam;
import model.BreweriesGeocode;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@RestController
@RequestMapping("/breweries")
public class TasteRestController {
    
    @Autowired
    BrewService service;
   
 
    @RequestMapping(value="/", method=RequestMethod.OPTIONS)  
    ResponseEntity<?> getcollectionOptions() {
       return ResponseEntity
               .ok()
               .allow(HttpMethod.GET, HttpMethod.OPTIONS)
               .build();
       
    }
    @RequestMapping(value="/{id}", method=RequestMethod.OPTIONS)  
    ResponseEntity<?> getSingularOptions() {
       return ResponseEntity
               .ok()
               .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE , HttpMethod.OPTIONS)
               .build();
       
    }
   
    @JsonView(Views.BrewIDName.class)
    @GetMapping("/name/{id}")
    
    public Breweries getbrewnameid(@PathVariable("id") int id) {
        return service.getBreweriesByID(id);
     }
     @JsonView(Views.BrewIDName.class)
    @GetMapping("/name-and-id")
    
    public List<Breweries> getallbrewnameid() {
        return service.getallnolimit();
     }
    //****************Brewery Crud****************/
    
      
      @GetMapping(value="/{id}",produces = MediaTypes.HAL_JSON_VALUE)
      @ResponseStatus(HttpStatus.OK)
      public Resource getbrewbyid(@PathVariable("id") int id) throws WriterException, IOException {
       Breweries b = service.getBreweriesByID(id);
       if(b!=null)
       {
       
          Resource resource = new Resource<Breweries>(b);
          ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBreweries(1,10));
          resource.add(linkTo.withRel("all-Breweries"));
         
          return resource;
       }
       else{
           throw new NotFoundException("Not Found with id "+ id );
       
       }
          
         
       
      }
      @GetMapping(value="/{id}/geocode",produces = MediaTypes.HAL_JSON_VALUE)
      @ResponseStatus(HttpStatus.OK)
      public Resource getgeocode(@PathVariable("id") int id) throws WriterException, IOException {
        Resource resource = new Resource<BreweriesGeocode>(service.getGeoByBrewID(id));
        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBreweries(1,10));
        resource.add(linkTo.withRel("all-Breweries"));
 
        return resource;    
                
      }
  
     
        @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
        @ResponseStatus(HttpStatus.OK)
        
      public Resources<Breweries> getBreweries(@QueryParam("page")int page,@QueryParam("size") int size)  throws WriterException, IOException {
         
          List<Breweries> allbrews = service.getall(page,size);
        
        for(Breweries b : allbrews)
            {
                int id= b.getBreweryId();
                Link selfLink = linkTo(this.getClass()).slash(id).withSelfRel();
                b.add(selfLink);
                Link mapl = linkTo(this.getClass()).slash("map").slash(id).withRel("Map").withType("GET");
                Link qrl =linkTo(this.getClass()).slash("code").slash(id).withRel("QRCode").withType("GET");
                b.add(mapl);
                b.add(qrl);
                linkTo(methodOn(this.getClass()).getbrewbyid(id));
            }
        Link link =linkTo(this.getClass()).withSelfRel();
        Resources<Breweries> result=new Resources<Breweries>(allbrews,link);
        return result;
                          
      }
          @GetMapping(value="/map/{id}", produces=MediaType.TEXT_HTML_VALUE)
          @ResponseStatus(HttpStatus.OK)
      public String getGeobrewbyid(@PathVariable("id") int id) {
          BreweriesGeocode geo = service.getGeoByID(id);
          float lat= geo.getLatitude();
          float lon = geo.getLongitude();
          Breweries b =service.getBreweriesByID(id);
          String brewname = b.getName();
          
          String output="<html><body><h3>"+  brewname +"</h3>"+
                        "<br>\n" +"<iframe width='600px' height='500px' id='mapcanvas' src='https://maps.google.com/maps?coord="+lat+","+lon+"&amp;q="+brewname+"&amp;z=9&amp;ie=UTF8&amp;iwloc=B&amp;output=embed' frameborder='0' scrolling='yes' marginheight='10' marginwidth='10'></iframe>"+"<br></body></html>";
          return output;
      }
           
      
    @DeleteMapping(value="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable("id") int id){
      boolean deleted = false;  
      deleted = service.deleteBrewery(id);
     if(deleted == false)
     {
          throw new NotFoundException("could not delete brewery with id "+ id );
     }
     else{
       return "Deleted brewery with id: "+id;
     }
    }
    
    
    @PutMapping(value="/{id}")
    //@Produces(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable("id") int id, @RequestBody Breweries b){
        
       if(b.getBreweryId()!=null)
       {
           service.updateBreweries(b);
           return "Succesfully updated "+ b.getName();
       }
       else{
           throw new NotFoundException("could not Update brewery with id "+ id );
       } 
        
        
        
    }
      
    //Adds new Brewery
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Breweries b){
      
    try{ 
       if(b == null)
       {
       throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something Wrong with the request");
       
       }
       else{
        service.addBreweries(b);
        return "Created new brewery with name: " + b.getName();
    }
    }
      catch(Exception e)
        {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something Wrong with the request", e);
        
        }
    
    }

    @GetMapping(value="/code/{id}", produces=MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public byte[] createQrcode(@PathVariable("id") int id ) throws  WriterException, IOException {
        Breweries b = service.getBreweriesByID(id);
        String website = b.getWebsite();
        if (website.isBlank()|| website.isEmpty())
        {
            website="www.google.com";
            System.out.println("website changed to google.com because its blank");
        }
        System.out.println("Check if it changed"+website);
        int width=250;
        int height=250;
        QRCodeWriter writer = new QRCodeWriter();
	BitMatrix matrix = writer.encode(website, BarcodeFormat.QR_CODE, width, height);
	byte[] png;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix,"PNG", baos);
        png=baos.toByteArray();      
        return png;
	
    }
   
}    