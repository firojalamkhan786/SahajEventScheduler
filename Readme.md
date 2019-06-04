# Getting Started Sahaj Event Scheduler 
Please follow the instruction to run this project.
## Step 1: MySQL Installation
* Donwload MySQL Community Edition
* Install the MySQL in your Windows/Mac/Linux system.
* Create the MySQL database user as "sahaj" and password "sahaj"
* Login to MySQL Workbench and execute the the scprit: mysql_db_script.sql
## Setp 2: Gmail Account details update
* You can open the "application.properties" file and change the spring.mail.username to your email address
spring.mail.username=firojalam.khan@gmail.com
* You need to pass the Environment variable spring.mail.password= along with your password when you run this application.

## Step 3: Run the Sahaj Event Scheduler 
* Goto the folder <SahajEventScheduler>: 
* $mvn spring-boot:run -Dspring.mail.password=<YOUR_SMTP_PASSWORD>

## Step 4: API Documentation Details:
API UI Documentation : http://localhost:8080/v2/api-docs
## Step 5: Swagger API Docs
* URL: http://localhost:8080/swagger-ui.html#/
 WE can test the scheduler functionality on the Swagger API itself.
 
## Step 6: API End Point:
API:  http://localhost:8080/api/event/schedule/
##Spring Basic security
* spring.security.user.name=sahaj
* spring.security.user.password=welcome123

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

