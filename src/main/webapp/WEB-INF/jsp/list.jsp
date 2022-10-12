<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table>
        <a href="resume?action=new"><img src="<c:url value='/img/add.png'/>" alt="new">Добавить новое резюме</a>
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" class="com.urise.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=view"><c:out value="${resume.fullName}"/></a></td>
                <td><a href='${resume.getContact(ContactType.MAIL)}'><c:out value="${resume.getContact(ContactType.MAIL)}"/></a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="<c:url value='/img/delete.png'/>"
                                                                            alt="delete"></a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="<c:url value='/img/pencil.png'/>"
                                                                          alt="edit"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>