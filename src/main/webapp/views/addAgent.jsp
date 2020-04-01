<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>  
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
    <head>
    </head>
    <body>              
        <h1><spring:message code="addagentform.header" /> </h1>
        <form:form method="POST" action="/AgentsCRUD/agents/" modelAttribute="agent">
            <table>
                <tr> 
                    <td><form:label path="agentId"><spring:message code="label.id" /></form:label></td>
                    <td><form:input path="agentId"/></td> 
                    <td style="color:red"><form:errors path="agentId"/> </td>
               </tr> 
                <tr>
                    <td><form:label path="name"><spring:message code="label.name" /></form:label></td>
                    <td><form:input path="name" /></td>
                    <td style="color:red"><form:errors path="name"/> </td>
                </tr>
                <tr>
                    <td><form:label path="phone"><spring:message code="label.phone" /></form:label></td>
                    <td><form:input path="phone"/></td>
                    <td style="color:red"><form:errors path="phone"/> </td>
                    
                </tr>
                <tr>
                    <td><form:label path="fax"><spring:message code="label.fax" /></form:label></td>
                    <td><form:input path="fax"/></td>
                    <td style="color:red"> <form:errors path="fax"/> </td>
                </tr>
               <tr>
                    <td><form:label path="email"><spring:message code="label.email" /></form:label></td>
                    <td><form:input path="email"/></td>
                    <td style="color:red"> <form:errors path="email"/> </td>
                </tr>
                
               
               <tr> 
                    <td><form:label path="gender"><spring:message code="label.gender" /></form:label></td>
                    <td><form:radiobutton path="gender" value="Male"/><spring:message code="label.gender.male" />
                        <form:radiobutton path="gender" value="Female"/><spring:message code="label.gender.female" />
                    </td>
                     <td style="color:red"> <form:errors path="gender"/> </td>
                </tr>
                <tr>
                    
                    <td><form:label path="dateJoined"><spring:message code="label.datejoined" /></form:label></td>
                    <td><form:input type="date" path="dateJoined"/></td>
                    <td style="color:red"> <form:errors path="dateJoined"/> </td>
                   
                </tr>
                <tr>
                    <td><form:label path="averageSaleThisYear"><spring:message code="label.sales" /></form:label></td>
                    <td><form:input path="averageSaleThisYear"/></td>
                    <td style="color:red"> <form:errors path="averageSaleThisYear"/> </td>
                </tr>
                <tr>
                    <spring:message code="submit.button" var="labelSubmit"></spring:message>
                    <td><input type="submit" value="${labelSubmit}"/></td>
                </tr>
            </table>
        </form:form>
    </body>
</html>









<%-- 
    Document   : addAgent
    Created on : Feb 3, 2020, 7:19:17 PM
    Author     : brend


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
    <head>
    </head>
  
        <h3>Enter The Agent Details!</h3>
        
        <form:form method="POST" action="/AgentsCRUD/agent/addAgent" modelAttribute="agent">
     
            <table>
                <tr>
                    <td><form:label path="agentId">ID</form:label></td>
                    <td><form:input path="agentId" required='true'/></td> 
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
                    <td><input type="submit" value="Submit!"/></td>
                </tr>
            </table>
        </form:form>
    
	
</html>
---%>