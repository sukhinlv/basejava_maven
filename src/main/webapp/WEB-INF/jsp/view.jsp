<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме <c:out value="${resume.fullName}"/></title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2><c:out value="${resume.fullName}"/>&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img
            src="<c:url value='/img/pencil.png'/>"
            alt="edit"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            ${contactEntry.key.title}: <a href='${contactEntry.value}'><c:out value="${contactEntry.value}"/></a>
            <br>
        </c:forEach>
        <br>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            ${sectionEntry.key.title.toUpperCase()}:<br>
            <c:choose>
                <c:when test="${sectionEntry.key eq 'PERSONAL' || sectionEntry.key eq 'OBJECTIVE'}">
                    <c:set var="textSectionEntry" value="${sectionEntry}"/>
                    <jsp:useBean id="textSectionEntry"
                                 type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.TextSection>"/>
                    <c:out value="${textSectionEntry.value.data}"/><br><br>
                </c:when>

                <c:when test="${sectionEntry.key eq 'ACHIEVEMENTS' || sectionEntry.key eq 'QUALIFICATIONS'}">
                    <c:set var="listSectionEntry" value="${sectionEntry}"/>
                    <jsp:useBean id="listSectionEntry"
                                 type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.ListSection>"/>
                    <c:forEach var="listSectionEntry" items="${listSectionEntry.value.data}">
                        <c:out value="${listSectionEntry}"/><br>
                    </c:forEach><br>
                </c:when>

                <c:when test="${sectionEntry.key eq 'EXPERIENCE' || sectionEntry.key eq 'EDUCATION'}">
                    <c:set var="organizationSectionEntry" value="${sectionEntry}"/>
                    <jsp:useBean id="organizationSectionEntry"
                                 type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.OrganizationSection>"/>
                    <c:forEach var="organizationSectionEntry" items="${organizationSectionEntry.value.data}">
                        <a href='${organizationSectionEntry.website}'><c:out
                                value="${organizationSectionEntry.title}"/></a><br>
                        <c:set var="organizationEntry" value="${sectionEntry}"/>
                        <jsp:useBean id="organizationEntry"
                                     type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Organization>"/>
                        <c:forEach var="organizationEntry" items="${organizationSectionEntry.periods}">
                            <c:out value="${organizationEntry.title}"/><br>
                            <c:if test="${organizationEntry.desc != ''}"><c:out value="${organizationEntry.desc}"/><br></c:if>
                            ${organizationEntry.startDate.year}.${organizationEntry.startDate.month.value} -
                            <c:if test="${DateUtil.NOW != organizationEntry.endDate && (organizationEntry.endDate.year != LocalDate.now().getYear() && organizationEntry.endDate.month != LocalDate.now().getMonth())}">${organizationEntry.endDate.year}.${organizationEntry.endDate.month.value}</c:if>
                            <c:if test="${DateUtil.NOW == organizationEntry.endDate || (organizationEntry.endDate.year == LocalDate.now().getYear() && organizationEntry.endDate.month == LocalDate.now().getMonth())}">сейчас</c:if>
                            <br>
                        </c:forEach>
                    </c:forEach><br>
                </c:when>

                <c:otherwise>
                    Здесь могла бы быть ваша реклама...
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </p>
    <br>
    <button onclick="window.history.back()">Назад</button>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
