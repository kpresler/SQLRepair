# SQLRepair: Identifying and Repairing Mistakes in Student-Authored SQL Queries

This repository is the companion to the paper `Identifying and Repairing Mistakes in Student-Authored SQL Queries` by [Kai Presler-Marshall](https://kpresler.github.io/), [Sarah Heckman](https://people.engr.ncsu.edu/sesmith5/) and [Kathryn Stolee](http://kstolee.github.io/).  This project contains three main components:

* SQLSearch: An activity and tool we used for collecting SQL queries from students
* `data`: A dataset of SQL queries submitted by students using SQLSearch.  The data is broken up into three CSV files, based on the course and semester it was collected from.
* SQLRepair: Our tool for automated SQL query repair, evaluated on the queries in `data`.

## SQLSearch

SQLSearch is a Spring Boot project written in Java.  In order to run it, your system needs:
* Java 8+ (tested on Java 8 and 11, should work on any other recent version)
* Maven 3.5+ (tested with 3.5.3)
* A MySQL/MariaDB database (tested with MySQL5.7 and MariaDB 10.4)

To run SQLSearch on your computer, there are several steps:
* Install Z3.  SQLSearch depends on SQLRepair, which depends on Z3.  Details for configuring SQLRepair are in the section below.
* Clone this repository
* Acquire an HTTPS certificate.  We used Java's `keytool` to generate a self-signed certificate, but any method for acquiring one works.  Guidance on how to generate your own is available [here](https://drissamri.be/blog/java/enable-https-in-spring-boot/)
* Copy the `keystore.p12` file into the `sqlsearcher` directory
* Configure your database credentials in `SQLExecutor` and `hibernate.cfg.xml`.  By default, we use a database running on the application server (`localhost`) although this is not a requirement.
* Run the `DBBuilder` and `HibernateDataGenerator` classes to populate the database with the set of problems we wrote and the tables corresponding to each one.
* Run the application: `mvn clean spring-boot:run`

The application will now be available at `localhost:8080` or `localhost:8443`.  Participants can now begin solving the problems from the PDF linked in the application.


## SQLRepair

This part of the project contains SQLRepair, our tool for automated repair of SQL statements.

### Requirements

SQLRepair is a Spring Boot project written in Java.  In order to run it, your system needs:
* Java 8+ (tested on Java 8 and 11, should work on any other recent version)
* Maven 3.5+ (tested with 3.5.3)
* Microsoft Z3 installed.  Instructions are available in [Microsoft's README](https://github.com/Z3Prover/z3).  Z3 runs on Linux, Windows, and MacOS.  From our experience, the Linux installation is the most straightforward and least prone to errors.

### Setup

Clone this repository, and ensure that you have the requirements from above installed.

Microsoft does not make their Java API for Z3 available in any centralised Maven repository.  Thus, after ensuring that the above requirements are set, you must add the Microsoft Z3 jar file to your local Maven repository.  The installation process will give you a jar that is the Java API, called `com.microsoft.z3.jar`.  Then, run `mvn install:install-file -Dfile=/path/to/com.microsoft.z3.jar -DgroupId=com.microsoft -DartifactId=z3 -Dversion=1.0 -Dpackaging=jar`, replacing `/path/to` with wherever the jar is located on your local filesystem.  The group, artifact, and version must match exactly, regardless of the version that you have built, because these versions are expected in our `pom.xml` file.


### Use

We distribute SQLRepair as a `jar` file that you can use in your Java application.  SQLRepair depends on Java (obviously) and Z3, but has no other dependencies that must be installed (a database, etc).  When you add SQLRepair to your project, make a call to `APIZ3Controller.repairAQuery` and pass in the incorrect query and the `(source, destination)` tables to repair against.   To do this, construct an instance of the `SQLTableExamples` class, which contains both the incorrect query and the examples.  `repairAQuery` returns a `String` that is the query repaired; if anything goes wrong, it returns `null` instead.  For more detailed feedback (if anything goes wrong) but output that requires some additional parsing, you can call `submitExample` instead.