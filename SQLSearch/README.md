# SQLSearch

This part of the project contains SQLSearch, our application built for collecting SQL queries from students.  Similar to work by [Stolee and Elbaum](https://kstolee.github.io/papers/esem2013.pdf), where students were asked to write SQL table examples for a given query, we have students wrie queries corresponding to a given set of table examples.

Students were given ten problems to solve; the problems are shown in a PDF [here](sqlactivity.pdf).  The problems we wrote are based off of the NIH's [UMLS dataset](https://www.nlm.nih.gov/research/umls/implementation_resources/query_diagrams/index.html).

SQLSearch is a Spring Boot project written in Java.  In order to run it, your system needs:
* Java 8+ (tested on Java 8 and 11, should work on any other recent version)
* Maven 3.5+ (tested with 3.5.3)
* A MySQL/MariaDB database (tested with MySQL5.7 and MariaDB 10.4)

To run SQLSearch on your computer, there are several steps:
* Clone this folder
* Acquire an HTTPS certificate.  We used Java's `keytool` to generate a self-signed certificate, but any method for acquiring one works.  Guidance on how to generate your own is available [here](https://drissamri.be/blog/java/enable-https-in-spring-boot/)
* Copy the `keystore.p12` file into the `sqlsearcher` directory
* Configure your database credentials in `SQLExecutor` and `hibernate.cfg.xml`.  By default, we use a database running on the application server (`localhost`) although this is not a requirement.
* Run the `DBBuilder` and `HibernateDataGenerator` classes to populate the database with the set of problems we wrote and the tables corresponding to each one.
* Run the application: `mvn clean spring-boot:run`

The application will now be available at `localhost:8080` or `localhost:8443`.  Participants can now begin solving the problems from our PDF above.