<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <!-- Points -->
        <form method="POST" enctype="plain/text" action="/rest/image/points" id="usrform">
            Points:<input type="text" name="points">
            <input type="submit">
        </form>
        <textarea name="comment" form="usrform">Enter text here...</textarea>
        
        <!-- File upload -->
        <div>
            <form method="POST" enctype="multipart/form-data" action="/rest/image/upload" id="imageform">
                <table>
                    <tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
                    <tr><td></td><td><input type="submit" value="Upload" /></td></tr>
                </table>
            </form>
        </div>        
    </body>
</html>