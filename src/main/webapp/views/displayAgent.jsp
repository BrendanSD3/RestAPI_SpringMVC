<%-- 
    Document   : allAgents
    Created on : Feb 3, 2020, 7:19:34 PM
    Author     : brend
--%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>All Agents</title>
    </head>
    <body>
        <table style="width:100%">
            <tr>
             <th align="left">ID</th>
             <th align="left">Name</th>
             <th align="left">Fax</th>
             <th align="left">Phone</th>
             <th align="left">Email</th>
             <th align="left">Actions</th>
            </tr>
            
                <tr>
                    <td>${foundagent.agentId}</td>
                    <td>${foundagent.name}</td>
                    <td>${foundagent.fax}</td>
                    <td>${foundagent.phone}</td>
                    <td>${foundagent.email}</td>
                    <td>  <a href="\AgentsCRUD\agent\edit?id=${foundagent.agentId}">edit</a></td>
                   
                </tr>
           
        </table>
    </body>
</html>