<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <div th:if="${message}">
            <h2 th:text="${message}"/>
        </div>

        <div>
            <form method="POST" enctype="multipart/form-data" action="/image/upload">
                <table>
                    <tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
                    <tr><td></td><td><input type="submit" value="Upload" /></td></tr>
                </table>
            </form>
        </div>
    </body>
</html>