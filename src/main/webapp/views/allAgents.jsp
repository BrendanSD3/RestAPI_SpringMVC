<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>  
<%@ page contentType="text/html; charset=UTF-8" %>

    
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
    </head>
       

    <spring:message code="welcome.message" /> 
    <body>
        <table class="display compact hover stripe" id="tableP">
            <thead>
                <tr>
                    <th align="left">ID</th>
                    <th align="left">Name</th>
                    <th align="left">City</th>
                    <th align="left">State</th>
                    <th align="left">Country</th>
                    <th align="left">Website</th>
                    <th align="left">Image</th>
                    <th align="left">Email</th>
                    <th align="left">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${List}" var="brewery"> 
                    <tr>
                        <td>${brewery.brewid}</td>
                        <td>${brewery.name}</td>
                        <td>${brewery.city}</td>
                        <td>${brewery.state}</td>
                        <td>${brewery.country}</td>
                        <td>${brewery.website}</td>
                        <td><img src="/TasteMVC/images/${brewery.image}" style="height:50px; width: 50px;" alt="${brewery.image}"></img></td>
                        <td>${brewery.email}</td>
                        <td>
                            <button> <a href="\TasteMVC\taste\editbrew?id=${brewery.brewid}">Edit</a></button>
                            <button><a href="\TasteMVC\taste\display?id=${brewery.brewid}">Display</a></button>
                            <button><a href="\TasteMVC\taste\delete?id=${brewery.brewid}" onclick="Areyousure()">Delete</a></button>
                        
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table></center>
    </body>
    </body>
</html>




<%-- 
    Document   : allAgents
    Created on : Feb 3, 2020, 7:19:34 PM
    Author     : brend


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
            <c:forEach items="${agentList}" var="agent"> 
                <tr>
                    <td>${agent.agentId}</td>
                    <td>${agent.name}</td>
                    <td>${agent.fax}</td>
                    <td>${agent.phone}</td>
                    <td>${agent.email}</td>
                    <td>
                        <a href="\AgentsCRUD\agent\delete?id=${agent.agentId}">Delete</a>
                        <a href="\AgentsCRUD\agent\displayAgent?id=${agent.agentId}">Display</a>
                        <a href="\AgentsCRUD\agent\add">Insert</a>
                        
                    </td>
                   
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
----%>