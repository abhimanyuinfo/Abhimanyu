## SpringBoot 4, Hibernate 4, MySQL 5, and java 8

##Main Purposes

###Clustered Data Warehouse

System to import deals details from csv files into DB. The requested performance is to be able to import the file containing 100,000 records in less than 5 seconds.

Request logic as following :

File format is CSV contains the following fields (Deal Unique Id, From Currency ISO Code "Ordering Currency", To Currency ISO Code, Deal timestamp, Deal Amount in ordering currency).
Validate row structure.
Valid rows should be stored in table/document, with reference to source file name .
Invalid rows should be stored into another table/document, with reference to source file name.
The DB contains another table to maintain accumulative count of deals per Ordering Currency "Columns : Currency ISO Code, CountOfDeals ", so upon completion of importing process the system should increase count of deals per currency.
System should not import same file twice.
No rollback allowed, what every rows imported should be saved in DB.

Technical Specs :

Access to DB should be through JPA.
MySql DB 
Web interface for uploading files.
Inquire about results "using filename".


###Info
Upload csv files and store into the database.

It's cloud deploy-able (jetty-runner.jar + compiled war file)

For configuration of Spring MVC, it uses Java config instead of xml config.
For Hibernate 4 and MySQL, please modify src/main/resources/application.properties file
Build and run the app in command line environment

###1.Create table in MySQL

Copy the text in src/main/resources/sample/bloomberg_db.sql and run it to create a table in MySQL.

###2.Build package
$ mvn package

###Test it

Go to http://localhost:8099/ and test it.

#Steps to set up development environment in Eclipse

##After you clone this project from github, run
```
$ mvn package
$ mvn eclipse:eclipse
```
Open Eclipse to import the project
Click top File -> Import -> Maven -> Existing Maven Projects (Choose the folder that you just downloded)
Right click your project, click Properties -> Java Build Path, remove all M2_REPO/***/*** jars (names started with M2_REPO), click OK.
Check if Maven dependencies are setup correctly. Right click your project, click Properties -> Java Build Path-> Libraries, and the Maven Dependencies include all your dependent jar files
Right click pom.xml, click Run as-> Maven clean, then Run as-> Maven Install
Right click your project, click Run as -> Run as SpringBoot Applciation/ Java Application (SpringBootWebApplication.java is the main class)


Development environment

Windows 10 64bit, STS 3.6.2.RELEASE
