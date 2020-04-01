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

/**
 *
 * @author brend
 */
//import org.springframework.hateoas.Resource;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import DAO.BrewService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;





@RestController
@RequestMapping("/brew")
public class TasteRestController {
    
    @Autowired
    BrewService service;

     @RequestMapping("/home")
    public ModelAndView gotohome(){
            return new ModelAndView("/Home");
    }
     @RequestMapping("/Success")
    public ModelAndView successpage(){
            return new ModelAndView("/Success");
    }
//     @RequestMapping("/error")
//    public ModelAndView errorpage(){
//            return new ModelAndView("/error");
//    }
   @GetMapping
    @Produces(MediaType.APPLICATION_JSON_VALUE) //optional to specify that this method produces JSON as its returned by default
    public List<Breweries> getBreweries() {
        return service.getall(); //the list is returned in JSON format by default. Each browser will handle this differently (e.g. IE will let you download a file containing the objects in JSON format)
    }
   
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE) //see previous comment
    public Breweries getbrew(@PathVariable("id") int id) {
        return service.getBreweriesByID(id);
     }
    
      @GetMapping(value="/hateoas/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
      public Resource<Breweries> getbrewhateos(@PathVariable("id") int id) {
        Resource<Breweries> resource = new Resource<Breweries>(service.getBreweriesByID(id));
          
        ControllerLinkBuilder linkTo= linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getBreweries());
        resource.add(linkTo.withRel("all-Breweries"));
        return resource;
      }
//        for(Breweries b : brews)
//        {
//            int bid=b.getId();
//            Link selfLink=linkTo(this.getClass()).slash(bid).withSelfRel();
//                 
//            brews.add(selfLink);
//            linkTo(methodOn(this.getClass()).)
//            
//        
//        }
        
        
        
        
        
        
     
    @DeleteMapping(value="/{id}")
    public ModelAndView delete(@PathVariable("id") int id){
        try{
        service.deleteAnBreweries(id);
     }
        catch(Exception e)
        {
            return new ModelAndView("/error","e",e);
        
        }
        return new ModelAndView("redirect:/brew/Success");
    
    }
    @PutMapping(value="/{id}")
    //@Produces(MediaType.APPLICATION_JSON_VALUE)
    //@ResponseStatus(HttpStatus.OK)
    public ModelAndView update(@PathVariable("id") int id, @RequestBody Breweries b){
       try{
        service.updateBreweries(b);
        }
        catch(Exception e)
        {
            return new ModelAndView("/error","e",e);
        
        }
        return new ModelAndView("redirect:/brew/Success");
    }
    
    
    
    
    //Adds new Brewery
    @PostMapping
    public ModelAndView create(@RequestBody Breweries b){
        try{
        service.addBreweries(b);
        int id=b.getId();
        String website =b.getWebsite();
        if(website.isEmpty()||website.isBlank())
        {
            createQrcode(id,"www.google.com");
        }
        else{
             createQrcode(id,website);
        }
        }
        catch(Exception e)
        {
            return new ModelAndView("/error","e",e);
        
        }
        return new ModelAndView("redirect:/brew/Success");
    }
    
   
    @GetMapping(value="/code/{id}")
    public ModelAndView viewcode(@PathVariable("id") int id,  ModelMap model) throws IOException
    {
        Breweries brew=service.getBreweriesByID(id);
        String website=brew.getWebsite();
            File file = new File("C:\\Users\\brend\\Desktop\\Year4\\RestAPI\\TasteRest\\src\\main\\webapp\\images\\"+id+".png");
            boolean exists=file.exists();
       if(website.isEmpty()||website.isBlank())
        {
            try{
            createQrcode(id,"www.google.com");
        }
        catch(Exception e){
            System.out.println("website empty error");
        return new ModelAndView("/error","e",e);
        
        }
        }
       if(exists==false){
       try{
            createQrcode(id,website);
            
        }
        catch(Exception e){
            System.out.println("Exists is false error"+e.getMessage());
        return new ModelAndView("/error","e",e);
        }
       
       }
    return new ModelAndView("/barcode","id",id);
    
    
    }
    
    @GetMapping(value="/qrc/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] generateQcode(@PathVariable("id") int id ) throws  WriterException, IOException {
        String text="Tester";
        int width=250;
        int height=250;
       QRCodeWriter writer = new QRCodeWriter();
	BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MatrixToImageWriter.toBufferedImage(matrix);
	byte[] qrCode = stream.toByteArray();
        System.out.println("Stream size"+stream.size());
      return qrCode;
        
       //byte[] encoded = Base64.getEncoder().encode(qrCode.getBytes());
	//return new ModelAndView("/barcode","qrcode",qrCode);
        
    }
    
    
    
//    @RestController
//@RequestMapping("/barcodes")
//public class BarcodesController {
    @PostMapping(value="/createqr/{id}")
    public ModelAndView createQrcode(@PathVariable("id") int id , String text) throws IOException, InterruptedException
    {
       
        System.out.println("Got Called");
        
        ByteArrayOutputStream bout =
                QRCode.from(text)
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();
        //byte[] b= bout.toByteArray();
        
        
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_PNG);
       // ImageIO.write(image, "png", bout);
    
      
      
//      String Output="<html><body><img src=\"/AgentsCRUD/images/qr-code.png\"><br></body></html>";
//    return Output;
         //String output = "<html><body><img src='" +bout+  "'><br></body></html>";
        
            File file = new File("C:\\Users\\brend\\Desktop\\Year4\\RestAPI\\TasteRest\\src\\main\\webapp\\images\\"+id+".png");
            boolean exists=file.exists();
            System.out.println("does it exist?"+exists);
        if(exists==false)
        { 
         try {
           OutputStream out = new FileOutputStream(file);
            bout.writeTo(out);
            out.flush();
            out.close();
             //System.out.println("out>"+out.toString());
       
        
        } catch (FileNotFoundException e){
             System.out.println("file not found exception");
            e.printStackTrace();
        } catch (IOException e) {
             System.out.println("IOException");
            e.printStackTrace();
        }
            }
        
        //Thread.sleep(10);
       return new ModelAndView("/barcode","id",id);
              //  return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
    }
    
    
    
    @GetMapping(value = "/barbecue/ean13/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> barbecueEAN13Barcode(@PathVariable("barcode") String barcode)
    throws Exception {
        return okResponse(BarbecueBarcodeGenerator.generateEAN13BarcodeImage(barcode));
    }
    //...

        @PostMapping(value = "/qrgen/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> qrgenQRCode(@RequestBody String barcode) throws Exception {
            System.out.println("In the method");
        return okResponse(QRGenBarcodeGenerator.generateQRCodeImage(barcode));
    }

     @PostMapping(value = "/zxing/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody String barcode) throws Exception {
        return okResponse(ZxingBarcodeGenerator.generateQRCodeImage(barcode));
    }
    
    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
        return new ResponseEntity<BufferedImage>(image, HttpStatus.OK);
    }

   
    
   
    
    
    
   
}