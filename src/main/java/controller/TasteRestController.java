/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import DAO.BeerService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.ws.rs.Produces;
import model.Breweries;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import DAO.BrewService;
import java.io.InputStream;
import static java.lang.System.in;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import model.Beers;
import model.BreweriesGeocode;
import org.apache.commons.io.IOUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@EnableWebMvc
@RestController
@RequestMapping("/brew")
public class TasteRestController {
    
    @Autowired
    BrewService service;
    //BeerService bservice;
    //ServletContext servletContext;
    
 
//   @GetMapping
//    @Produces(MediaType.APPLICATION_JSON_VALUE) //optional to specify that this method produces JSON as its returned by default
//    public List<Breweries> getBreweries() {
//        return service.getall(); //the list is returned in JSON format by default. Each browser will handle this differently (e.g. IE will let you download a file containing the objects in JSON format)
//    }
//   
//    @GetMapping("/{id}")
//    @Produces(MediaType.APPLICATION_JSON_VALUE) //see previous comment
//    public Breweries getbrew(@PathVariable("id") int id) {
//        return service.getBreweriesByID(id);
//     }
    //****************Brewery Crud****************/
      @GetMapping(value="/{id}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resource getbrewbyid(@PathVariable("id") int id) throws WriterException, IOException {
        Resource resource = new Resource<Breweries>(service.getBreweriesByID(id));
        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBreweries(1,10));
        resource.add(linkTo.withRel("all-Breweries"));
  //     System.out.println("resource"+resource.toString());
        return resource;    
                
      }
   
     
        @GetMapping(value="/{page}/{size}",produces = MediaTypes.HAL_JSON_VALUE)
      public Resources<Breweries> getBreweries(@PathVariable("page")int page,@PathVariable("size") int size) throws WriterException, IOException {
        
          List<Breweries> allbrews = service.getall(page,size);
        
        for(Breweries b : allbrews)
            {
                int id= b.getBreweryId();
                Link selfLink = linkTo(this.getClass()).slash(id).withSelfRel();
                b.add(selfLink);
                Link mapl = linkTo(this.getClass()).slash("map").slash(id).withRel("Map");
                Link qrl =linkTo(this.getClass()).slash("code").slash(id).withRel("QRCode");
                b.add(mapl);
                b.add(qrl);
                linkTo(methodOn(this.getClass()).getbrewbyid(id));
            }
        Link link =linkTo(this.getClass()).withSelfRel();
        Resources<Breweries> result=new Resources<Breweries>(allbrews,link);
        return result;
                          
      }
          @GetMapping(value="/map/{id}", produces=MediaType.TEXT_HTML_VALUE)
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
        try{
        service.deleteAnBreweries(id);
     }
        catch(Exception e)
        {
            return "The following Exception occured: "+e ;
        
        }
        return "Deleted brewery with id: "+id;
    
    }
    @PutMapping(value="/{id}")
    //@Produces(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String update(@PathVariable("id") int id, @RequestBody Breweries b){
       try{
        service.updateBreweries(b);
        }
        catch(Exception e)
        {
            return "Exception has occured "+e;
        
        }
        return "Succesfully updated "+ b.getName();
    }
      
    //Adds new Brewery
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String create(@RequestBody Breweries b){
    try{ 
        service.addBreweries(b);
        
    }
      catch(Exception e)
        {
            return "Exception has occured "+e;
        
        }
    return "Created new brewery with name: " + b.getName();
    }
    
   
//   
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
    //*******************END of Brewery Crud**************************/
    //********************Beer CRUD*************************/
    
//        @GetMapping(value="/beers/{page}/{size}",produces = MediaTypes.HAL_JSON_VALUE)
//      public Resources<Beers> getBeers(@PathVariable("page")int page,@PathVariable("size") int size) throws WriterException, IOException {
//            System.out.println("HEERRREEE in get BEERS");
//          List<Beers> allBeers = bservice.getall(page, size);
//        
//        for(Beers b : allBeers)
//            {
//                int id= b.getBeersId();
//                Link selfLink = linkTo(this.getClass()).slash(id).withSelfRel();
//                b.add(selfLink);
//                linkTo(methodOn(this.getClass()).getbeerbyid(id));
//            }
//        Link link =linkTo(this.getClass()).withSelfRel();
//        Resources<Beers> result=new Resources<Beers>(allBeers,link);
//        return result;
//                          
//      }
//    
//      @GetMapping(value="/beer/{id}",produces = MediaTypes.HAL_JSON_VALUE)
//      public Resource getbeerbyid(@PathVariable("id") int id) throws WriterException, IOException {
//        Resource resource = new Resource<Beers>(bservice.getBeerByID(id));
//        // resource.add(linkTo(methodOn(this.getClass()).getBreweries(1,10)).withRel("AllBReweries"));
//         //resource.add(getqrcode(id));
//        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBeers(1,10));
//        resource.add(linkTo.withRel("all-Beers"));
//        //resource.add(getqrcode(id).withRel("QRcode"));
//      
//          System.out.println("resource"+resource.toString());
//        return resource;    
//                
//      }
//    
    
    
    
    
    
    
    
    
    
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

 //@GetMapping(value="/code/{id}")
//    public ModelAndView viewcode(@PathVariable("id") int id,  ModelMap model) throws IOException
//    {
//        Breweries brew=service.getBreweriesByID(id);
//        String website=brew.getWebsite();
//            File file = new File("C:\\Users\\brend\\Desktop\\Year4\\RestAPI\\TasteRest\\src\\main\\webapp\\images\\"+id+".png"); //ChangeME
//            boolean exists=file.exists();
//       if(website.isEmpty()||website.isBlank())
//        {
//            try{
//            createQrcode(id,"www.google.com");
//        }
//        catch(Exception e){
//            System.out.println("website empty error");
//        return new ModelAndView("/error","e",e);
//        
//        }
//        }
//       if(exists==false){
//       try{
//            createQrcode(id,website);
//            
//        }
//        catch(Exception e){
//            System.out.println("Exists is false error"+e.getMessage());
//        return new ModelAndView("/error","e",e);
//        }
//       
//       }
//    return new ModelAndView("/barcode","id",id);
//    
//    
//    }
        
//    @GetMapping(value = "/barbecue/ean13/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> barbecueEAN13Barcode(@PathVariable("barcode") String barcode)
//    throws Exception {
//        return okResponse(BarbecueBarcodeGenerator.generateEAN13BarcodeImage(barcode));
//    }
//    //...
//
//        @PostMapping(value = "/qrgen/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> qrgenQRCode(@RequestBody String barcode) throws Exception {
//            System.out.println("In the method");
//        return okResponse(QRGenBarcodeGenerator.generateQRCodeImage(barcode));
//    }
//
//     @PostMapping(value = "/zxing/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody String barcode) throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateQRCodeImage(barcode));
//    }
//        
//    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
//        return new ResponseEntity<BufferedImage>(image, HttpStatus.OK);
//    }
//@PostMapping(value="/createqr/{id}")
//    public ModelAndView createQrcode(@PathVariable("id") int id , String text) throws IOException, InterruptedException
//    {
//       
//        System.out.println("Got Called");
//        
//        ByteArrayOutputStream bout =
//                QRCode.from(text)
//                        .withSize(250, 250)
//                        .to(ImageType.PNG)
//                        .stream();
//        //byte[] b= bout.toByteArray();
//        
//        
//    final HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.IMAGE_PNG);
//       // ImageIO.write(image, "png", bout);
//    
//      
//      
////      String Output="<html><body><img src=\"/AgentsCRUD/images/qr-code.png\"><br></body></html>";
////    return Output;
//         //String output = "<html><body><img src='" +bout+  "'><br></body></html>";
//        
//            File file = new File("C:\\Users\\brend\\Desktop\\Year4\\RestAPI\\TasteRest\\TasteRest\\src\\main\\webapp\\images\\"+id+".png");
//            boolean exists=file.exists();
//            System.out.println("does it exist?"+exists);
//        if(exists==false)
//        { 
//         try {
//           OutputStream out = new FileOutputStream(file);
//            bout.writeTo(out);
//            out.flush();
//            out.close();
//             //System.out.println("out>"+out.toString());
//       
//        
//        } catch (FileNotFoundException e){
//             System.out.println("file not found exception");
//            e.printStackTrace();
//        } catch (IOException e) {
//             System.out.println("IOException");
//            e.printStackTrace();
//        }
//            }
//        
//        //Thread.sleep(10);
//       return new ModelAndView("/barcode","id",id);
//              //  return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
//    }
//    //byte[] qrCode = stream.toByteArray();
        //InputStream in = servletContext.getResourceAsStream("http://localhost:8081/TasteRest/images/no_image.jpg");
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
        //return new ResponseEntity<byte[]> (IOUtils.toByteArray(in), HttpStatus.CREATED);

//return qrCode;
        
       //byte[] encoded = Base64.getEncoder().encode(qrCode.getBytes());
	//return new ModelAndView("/barcode","qrcode",qrCode);
        
  //  }
    
    
    
//    @RestController
//@RequestMapping("/barcodes")
//public class BarcodesController {
//    
    