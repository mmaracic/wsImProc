<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <c:if test="${sessionScope.points == null}">
            <!-- Points -->
            <table>
                <tr>
                    <td>Points:</td>
                    <td><textarea name="points" form="usrform"></textarea></td>
                </tr>
                <tr>
                    <td>
                        <form method="POST" enctype="application/x-www-form-urlencoded" action="/rest/image/points" id="usrform">
                            <input type="submit">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>

                    </td>
                </tr>
            </table>
        </c:if>

        <c:if test="${sessionScope.points != null}">
            <!-- File upload -->
            <div>
                <form method="POST" enctype="multipart/form-data" action="/rest/image/upload" id="imageform">
                    <table>
                        <tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
                        <tr><td></td><td><input type="submit" value="Upload" /></td></tr>
                    </table>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>   
        </c:if>
    </body>
</html>