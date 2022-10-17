## Basejava graduation project

Hi there!

Here you can see my graduation project from the course [Basejava](https://javaops.ru/view/basejava) by Grigory Kislin. This course is for junior Java developers.
In this project you can find many examples of Java core technologies and technics. Take a look at the list below.

Please don't judge me harshly, this was my first more or less serious project.

## Working sample

For this application I decide not to use some PaaS like Heroku, but set up my own web server. Here it is. VirtualBox VM, Ubuntu server with Java, Tomcat, Nginx, Postgresql, SSL

Online app: [https://jsft.ru/basejava_Web](https://jsft.ru/basejava_Web)

## Before compiling project in your own enviroment

- Database credentials stored in "basejava_maven/src/resources/resumes.properties" file. You should set your parameters.
- ... (create base and tables)
- You should set test database credentials in com.urise.webapp.storage.SqlStorageTest constructor 
- When tests starts, the "test_storage" directory will be created in the project root automatically

## Used technologies

### Implementation of object storage using
* array
* sorted array
* list
* map
* file / path (File API & NIO File API)
* database (jdbc / Postgresql)

Look into com.urise.webapp.storage package

### Load configuration parameters from file

Look into the com.urise.webapp.Config.java

### Implementation of serializers (see serializer package)
* data
* object
* xml
* json

Look into com.urise.webapp.serializer package for serializers and into com.urise.webapp.util package for JsonParser & XmlParser

### Servlet / JSP / JSTL library

You can find servlet implementation in
- com.urise.webapp.web.ResumeServlet.java
- src/main/webapp/WEB-INF

### Patterns such as
* Template method (implementation of storages based on AbstractStorage.class)
* Strategy (PathStorage.class & FileStorage.class)
* Singleton (Config.class) 

### Unit and integration tests of the storage and servlet implementation

See src/test
