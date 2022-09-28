package com.urise.webapp.storage;

import com.urise.webapp.model.*;
import com.urise.webapp.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResumeTestData {
    public static void main(String[] args) {
        var resume = createTestResume("Кислин Григорий");

        System.out.println("ИМЯ: " + resume.getFullName()+ "\n");

        printContacts(resume);

        for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
            switch (entry.getKey()) {
                case PERSONAL, OBJECTIVE -> printTextSection(entry.getKey().getTitle(), (TextSection) entry.getValue());
                case ACHIEVEMENTS, QUALIFICATIONS ->
                        printListSection(entry.getKey().getTitle(), (ListSection) entry.getValue());
                case EXPERIENCE, EDUCATION ->
                        printOrgSection(entry.getKey().getTitle(), (OrganizationSection) entry.getValue());
            }
        }
    }

    private static void printOrgSection(String title, OrganizationSection value) {
        System.out.println("\n" + title.toUpperCase());
        for (var data : value.getData()) {
            printOrganization(data);
        }
    }

    private static void printOrganization(Organization data) {
        System.out.println("\n" + data.getTitle());
        printNotBlank(data.getWebsite());
        for (var period : data.getPeriods()) {
            printNotBlank(period.getTitle());
            printNotBlank(period.getDesc());
            System.out.println(period.getStartDate() + " - " + period.getEndDate());
        }
    }

    private static void printListSection(String title, ListSection value) {
        System.out.println("\n" + title.toUpperCase());
        for (var data : value.getData()) {
            System.out.println(data);
        }
    }

    private static void printTextSection(String title, TextSection value) {
        System.out.println("\n" + title.toUpperCase());
        System.out.println(value.getData());
    }

    private static void printContacts(Resume resume) {
        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
            System.out.println(entry.getKey().getTitle() + " : " + entry.getValue());
        }
    }

    private static void printNotBlank(String s) {
        if (!s.isBlank()) {
            System.out.println(s);
        }
    }

    public static Resume createTestResume(String fullName) {
        return createTestResume(UUID.randomUUID().toString(), fullName);
    }

    public static Resume createTestResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        Map<ContactType, String> contacts = new HashMap<>();
        contacts.put(ContactType.CELLPHONE, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "skype:grigory.kislin");
        contacts.put(ContactType.MAIL, "gkislin@yandex.ru");
        contacts.put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        contacts.put(ContactType.GITHUB, "https://github.com/gkislin");
        contacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        contacts.put(ContactType.HOMEPAGE, "http://gkislin.ru/");
        resume.setContacts(contacts);

        var sections = resume.getSections();

        sections.put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));

        sections.put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        // ACHIEVEMENT
        var achievements = new ArrayList<String>();
        achievements.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения " +
                "автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на " +
                "Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект " +
                "для комплексных DIY смет");
        achievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное " +
                "взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов." +
                " Более 3500 выпускников.");
        achievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievements.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция " +
                "с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: " +
                "Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, " +
                "интеграция CIFS/SMB java сервера.");
        achievements.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, " +
                "Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievements.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов " +
                "(SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии " +
                "через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга " +
                "системы по JMX (Jython/ Django).");
        achievements.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, " +
                "Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        sections.put(SectionType.ACHIEVEMENTS, new ListSection(achievements));

        // QUALIFICATIONS
        var qualifications = new ArrayList<String>();
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, " +
                "MS SQL, HSQLDB");
        qualifications.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        qualifications.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        qualifications.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, " +
                "Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, " +
                "Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        qualifications.add("Python: Django.");
        qualifications.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualifications.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualifications.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, " +
                "DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1," +
                " OAuth2, JWT.");
        qualifications.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        qualifications.add("администрирование  Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios," +
                " iReport, OpenCmis, Bonita, pgBouncer");
        qualifications.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, " +
                "архитектурных шаблонов, UML, функционального программирования");
        qualifications.add("Родной русский, английский \"upper intermediate\"");
        sections.put(SectionType.QUALIFICATIONS, new ListSection(qualifications));

        // EXPERIENCE
        var organizationData = new ArrayList<Organization>();
        var periods = new ArrayList<Organization.Period>();
        periods.add(new Organization.Period("Автор проекта.",
                "Создание, организация и проведение Java онлайн проектов и стажировок.",
                LocalDate.of(2013, 10, 1),
                DateUtil.NOW));
        var organization = new Organization("Java Online Projects", "http://javaops.ru/", periods);
        organizationData.add(organization);

        periods = new ArrayList<>();
        periods.add(new Organization.Period("Старший разработчик (backend)",
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, " +
                        "Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, " +
                        "авторизация по OAuth1, OAuth2, JWT SSO.",
                LocalDate.of(2014, 10, 1),
                LocalDate.of(2016, 1, 1)));
        organization = new Organization("Wrike", "https://www.wrike.com/", periods);
        organizationData.add(organization);
        sections.put(SectionType.EXPERIENCE, new OrganizationSection(organizationData));

        // EDUCATION
        organizationData = new ArrayList<>();
        periods = new ArrayList<>();
        periods.add(new Organization.Period("'Functional Programming Principles in Scala' by Martin Odersky",
                "",
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1)));
        organization = new Organization("Coursera", "https://www.coursera.org/course/progfun", periods);
        organizationData.add(organization);
        periods = new ArrayList<>();
        periods.add(new Organization.Period("Аспирантура (программист С, С++)",
                "",
                LocalDate.of(1993, 9, 1),
                LocalDate.of(1996, 7, 1)));
        periods.add(new Organization.Period("Инженер (программист Fortran, C)",
                "",
                LocalDate.of(1987, 9, 1),
                LocalDate.of(1993, 7, 1)));
        organization = new Organization("Санкт-Петербургский национальный исследовательский университет " +
                "информационных технологий, механики и оптики", "http://www.ifmo.ru/", periods);
        organizationData.add(organization);
        sections.put(SectionType.EDUCATION, new OrganizationSection(organizationData));

        return resume;
    }

    public static Resume createTestResume2(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        Map<ContactType, String> contacts = new HashMap<>();
        contacts.put(ContactType.CELLPHONE, "+7(910) 903-1027");
        contacts.put(ContactType.SKYPE, "skype: n/a");
        contacts.put(ContactType.MAIL, "leva1981@yandex.ru");
        contacts.put(ContactType.LINKEDIN, "n/a");
        contacts.put(ContactType.GITHUB, "https://github.com/sukhinlv");
        contacts.put(ContactType.STACKOVERFLOW, "n/a");
        contacts.put(ContactType.HOMEPAGE, "n/a");
        resume.setContacts(contacts);

        return resume;
    }

    public static Resume createTestResume3(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        Map<ContactType, String> contacts = new HashMap<>();
        contacts.put(ContactType.CELLPHONE, "+7(910) 903-1028");
        contacts.put(ContactType.SKYPE, "-");
        contacts.put(ContactType.MAIL, "musinov80@yandex.ru");
        contacts.put(ContactType.LINKEDIN, "-");
        contacts.put(ContactType.GITHUB, "-");
        contacts.put(ContactType.STACKOVERFLOW, "-");
        contacts.put(ContactType.HOMEPAGE, "http://vk.com/musinov80");
        resume.setContacts(contacts);

        var sections = resume.getSections();

        sections.put(SectionType.OBJECTIVE, new TextSection("Коммерческий директор крупной рекламной фирмы"));

        sections.put(SectionType.PERSONAL, new TextSection("Креативщик!"));

        // ACHIEVEMENT
        var achievements = new ArrayList<String>();
        achievements.add("Привлечение крупнейших торговых сетей, таких как Пятерочка, Магнит и прочих...");

        sections.put(SectionType.ACHIEVEMENTS, new ListSection(achievements));

        // EXPERIENCE
        var organizationData = new ArrayList<Organization>();
        var periods = new ArrayList<Organization.Period>();
        periods.add(new Organization.Period("Коммерческий директор",
                "Поиск и привлечение крупных заказчиков",
                LocalDate.of(2003, 6, 1),
                DateUtil.NOW));
        var organization = new Organization("Java Online Projects", "http://javaops.ru/", periods);
        organizationData.add(organization);
        sections.put(SectionType.EDUCATION, new OrganizationSection(organizationData));

        return resume;
    }
}