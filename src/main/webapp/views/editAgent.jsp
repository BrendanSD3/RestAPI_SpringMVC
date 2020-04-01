<%-- 
    Document   : addAgent
    Created on : Feb 3, 2020, 7:19:17 PM
    Author     : brend
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>  
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
    </head>
  
        <h3>Enter The Agent Details!</h3>
        <form:form method="POST" action="/AgentsCRUD/agent/updateAgent" modelAttribute="agent">
     
            <table>
                <tr>
                    <td><form:label path="agentId">ID</form:label></td>
                    <td><form:input path="agentId" required='true' /></td> 
               </tr>
                <tr>
                    <td><form:label path="name">Name</form:label></td>
                    <td><form:input path="name"/></td>
                    <form:errors path="name"/>
                </tr>
                <tr>
                    <td><form:label path="phone">Phone</form:label></td>
                    <td><form:input path="phone"/></td>
                </tr>
                <tr>
                    <td><form:label path="fax">Fax</form:label></td>
                    <td><form:input path="fax"/></td>
                </tr>
               <tr>
                    <td><form:label path="email">Email</form:label></td>
                    <td><form:input path="email"/></td>
                </tr>
                <tr>
                    <td><form:label path="username">Username</form:label></td>
                    <td><form:input path="username"/></td>
                </tr>
                <tr>
                    <td><form:label path="password">Password</form:label></td>
                    <td><form:input path="password"/></td>
                </tr>
                  <tr> 
                    <td><form:label path="gender"><spring:message code="label.gender" /></form:label></td>
                    <td><form:radiobutton path="gender" value="Male"/><spring:message code="label.gender.male" />
                        <form:radiobutton path="gender" value="Female"/><spring:message code="label.gender.female" />
                    </td>
                     <td style="color:red"> <form:errors path="gender"/> </td>
                </tr>
                 <tr>
                    <td><form:label path="averageSaleThisYear"><spring:message code="label.sales" /></form:label></td>
                    <td><form:input path="averageSaleThisYear"/></td>
                    <td style="color:red"> <form:errors path="averageSaleThisYear"/> </td>
                </tr>
                <tr>
                    
                    <td><form:label path="dateJoined"><spring:message code="label.datejoined" /></form:label></td>
                    <td><form:input type="date" path="dateJoined"/></td>
                    <td style="color:red"> <form:errors path="dateJoined"/> </td>
                   
                </tr>
                <tr>
                    <td><input type="submit" value="Submit!"/></td>
                </tr>
            </table>
        </form:form>
</html>