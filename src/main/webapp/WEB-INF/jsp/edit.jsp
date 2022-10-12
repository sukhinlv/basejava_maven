<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form name="mainForm" method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="formResult" value="ok"/>
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" id="fullName" size=50 value="${resume.fullName}" autofocus required>
            </dd>
        </dl>

        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <br>

        <h3>Секции:</h3>
        <c:set var="sectionId" value="sect"/>
        <c:set var="organizationId" value="org"/>
        <c:set var="organizationEntryId" value="orgE"/>

        <c:forEach var="type" items="<%=SectionType.values()%>">
            <br>${type.title.toUpperCase()}:

            <c:set var="section" value="${resume.getSection(type)}"/>
            <c:choose>
                <c:when test="${type eq 'PERSONAL' || type eq 'OBJECTIVE'}">
                    <br>
                    <c:set var="textSection" value="${section}"/>
                    <jsp:useBean id="textSection" class="com.urise.webapp.model.TextSection"/>
                    <input type="text" name="${type.name()}" size=120 value="${textSection.data}"><br/>
                </c:when>

                <c:when test="${type eq 'ACHIEVEMENTS' || type eq 'QUALIFICATIONS'}">
                    <br>
                    <c:set var="listSection" value="${section}"/>
                    <jsp:useBean id="listSection" class="com.urise.webapp.model.ListSection"/>
                    <%--          https://stackoverflow.com/questions/8627902/how-to-add-a-new-line-in-textarea-element--%>
                    <textarea rows="15" cols="120" name="${type.name()}"
                              wrap="soft">${fn:join(listSection.data.toArray(), "&#13&#10;")}</textarea> <br/>
                </c:when>

                <c:when test="${type eq 'EXPERIENCE' || type eq 'EDUCATION'}">
                    <c:set var="organizationSection" value="${resume.getSection(type)}"/>
                    <c:set var="sectionId" value="${sectionId += 1}"/>
                    <button onclick="CreateOrganizationSection('${sectionId}', '${type.name()}')" type="button">Добавить
                        организацию
                    </button>
                    <br>
                    <div id="${sectionId}">
                        <jsp:useBean id="organizationSection" class="com.urise.webapp.model.OrganizationSection"/>
                        <c:forEach var="organizationSectionEntry" items="${organizationSection.data}">

                            <c:set var="organizationId" value="${organizationId += 1}"/>
                            <div id="${organizationId}">
                                <hr>
                                <input type="hidden" name="${type.name()}" size=80 value="${organizationId}">
                                <dl>
                                    <dt>Организация:</dt>
                                    <dd><input type="text" name="${type.name()}" size=80
                                               value="${organizationSectionEntry.title}" required></dd>
                                    <dd>
                                        <button onclick="document.getElementById('${organizationId}').remove()"
                                                type="button">X
                                        </button>
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Сайт:</dt>
                                    <dd><input type="text" name="${type.name()}" size=80
                                               value="${organizationSectionEntry.website}"></dd>
                                </dl>

                                <button onclick="CreateOrganizationEntry('${organizationId}', '${type.name()}')"
                                        type="button">Добавить место
                                </button>
                                <c:forEach var="organizationEntry" items="${organizationSectionEntry.periods}">
                                    <c:set var="organizationEntryId" value="${organizationEntryId += 1}"/>
                                    <div id="${organizationEntryId}" class="organizationEntry">
                                        <input type="hidden" name="${type.name()}_ENTRY" size=80
                                               value="${organizationId}">
                                        <dl>
                                            <dt>Профессия/должность:</dt>
                                            <dd><input type="text" name="${type.name()}_ENTRY" size=80
                                                       value="${organizationEntry.title}" required></dd>
                                            <dd>
                                                <button onclick="document.getElementById('${organizationEntryId}').remove()"
                                                        type="button">X
                                                </button>
                                            </dd>
                                        </dl>
                                        <dl>
                                            <dt>Описание:</dt>
                                            <dd><input type="text" name="${type.name()}_ENTRY" size=80
                                                       value="${organizationEntry.desc}"></dd>
                                        </dl>
                                        <dl>
                                            <dt>Временной период:</dt>
                                            <dd><input type="text" name="${type.name()}_ENTRY" size=10
                                                       value="${organizationEntry.startDate.year}.${organizationEntry.startDate.month.value}"
                                                       required pattern="[1-2][0-9]{3}.[0-9]{1,2}"></dd>
                                            <dd> -</dd>
                                            <c:if test="${DateUtil.NOW != organizationEntry.endDate}">
                                                <dd><input type="text" name="${type.name()}_ENTRY" size=10
                                                           value="${organizationEntry.endDate.year}.${organizationEntry.endDate.month.value}"
                                                           placeholder='ГГГГ.ММ' pattern="[1-2][0-9]{3}.[0-9]{1,2}">
                                                </dd>
                                            </c:if>
                                            <c:if test="${DateUtil.NOW == organizationEntry.endDate}">
                                                <dd><input type="text" name="${type.name()}_ENTRY" size=10
                                                           value=""
                                                           placeholder='ГГГГ.ММ' pattern="[1-2][0-9]{3}.[0-9]{1,2}">
                                                </dd>
                                            </c:if>
                                        </dl>
                                    </div>
                                    <br>
                                </c:forEach>
                            </div>
                        </c:forEach>
                        <br>
                    </div>
                </c:when>

                <c:otherwise>
                    Здесь могла бы быть ваша реклама...
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <hr>
        <button onclick="OnSubmitForm()" type="submit">Сохранить</button>
        <button onclick="window.history.back()" type="button">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>


<script type="text/javascript">
    function OnSubmitForm() {
        let inputs = document.mainForm.getElementsByTagName('input');
        for (let input of inputs) {
            input.value = input.value.trim();
        }
    }

    // TODO: get rid of repeating code
    function CreateOrganizationSection(parentId, typeName, title = "", website = "") {
        let parentElement = document.getElementById(parentId);
        let organizationId = uuidv4();
        let newItem = document.createElement("dl");
        newItem.innerHTML = "" +
            "<div id=\"" + organizationId + "\">" +
            "<hr>" +
            "<input type=\"hidden\" name=\"" + typeName + "\" size=80 value=\"" + organizationId + "\">" +
            "<dl>" +
            "  <dt>Организация:</dt>" +
            "  <dd><input type=\"text\" name=\"" + typeName + "\" size=80 value=\"" + title + "\"  required></dd>" +
            "  <dd><button onclick=\"document.getElementById('" + organizationId + "').remove()\" type=\"button\">X</button></dd>" +
            "</dl>" +
            "<dl>" +
            "  <dt>Сайт:</dt>" +
            "  <dd><input type=\"text\" name=\"" + typeName + "\" size=80 value=\"" + website + "\"></dd>" +
            "</dl>" +
            "<button onclick=\"CreateOrganizationEntry('" + organizationId + "', '" + typeName + "')\" type=\"button\">Добавить место</button>" +
            "</div>"
        ;
        parentElement.prepend(newItem);
    }

    function CreateOrganizationEntry(parentId, typeName, title = "", desc = "", startDate = "", endDate = "") {
        let parentElement = document.getElementById(parentId);
        let entryId = uuidv4();
        let newItem = document.createElement("dl");
        newItem.innerHTML = "" +
            "<div id=\"" + entryId + "\" class=\"organizationEntry\">" +
            "<input type=\"hidden\" name=\"" + typeName + "_ENTRY\" size=80 value=\"" + parentId + "\">" +
            "<dl>" +
            "  <dt>Профессия/должность:</dt>" +
            "  <dd><input type=\"text\" name=\"" + typeName + "_ENTRY\" size=80 value=\"" + title + "\" required></dd>" +
            "  <dd><button onclick=\"document.getElementById('" + entryId + "').remove()\" type=\"button\">X</button></dd>" +
            "</dl>" +
            "<dl>" +
            "  <dt>Описание:</dt>" +
            "  <dd><input type=\"text\" name=\"" + typeName + "_ENTRY\" size=80 value=\"" + desc + "\"></dd>" +
            "</dl>" +
            "<dl>" +
            "  <dt>Временной период:</dt>" +
            "  <dd><input type=\"text\" name=\"" + typeName + "_ENTRY\" size=10 value=\"" + startDate + "\" placeholder='ГГГГ.ММ' required pattern=\"[1-2][0-9]{3}.[0-9]{1,2}\"></dd>" +
            "  <dd> - </dd>" +
            "  <dd><input type=\"text\" name=\"" + typeName + "_ENTRY\" size=10 value=\"" + endDate + "\" placeholder='ГГГГ.ММ' pattern=\"[1-2][0-9]{3}.[0-9]{1,2}\"></dd>" +
            "</dl>" +
            "</div>"
        ;
        parentElement.appendChild(newItem);
    }

    function uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            let r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
</script>