<%-- 
    Document   : ShowMap
    Created on : Apr 3, 2020, 1:03:08 PM
    Author     : brend
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Brewery Map</title>
    </head>
    <body>
        <h1> ${brewname}</h1>
        <iframe width='600px' height='500px' id='mapcanvas' src='https://maps.google.com/maps?coord=${lat},${lon}&AMP;q=${brewname}&z=16&ie=UTF8&iwloc=&output=embed' frameborder='0' scrolling='yes' marginheight='10' marginwidth='10'></iframe>
        
    </body>
</html>
