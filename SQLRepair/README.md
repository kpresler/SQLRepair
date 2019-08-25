# SQLRepair

This part of the project contains SQLRepair, our tool for automated repair of SQL statements.

## Requirements

SQLRepair is a Spring Boot project written in Java.  In order to run it, your system needs:
* Java 8+ (tested on Java 8 and 11, should work on any other recent version)
* Maven 3.5+ (tested with 3.5.3)
* Microsoft Z3 installed.  Instructions are available in [Microsoft's README](https://github.com/Z3Prover/z3).  Z3 runs on Linux, Windows, and MacOS.  From our experience, the Linux installation is the most straightforward and least prone to errors.

## Setup

Clone this repository, and ensure that you have the requirements from above installed.

Microsoft does not make their Java API for Z3 available in any centralised Maven repository.  Thus, after ensuring that the above requirements are set, you must add the Microsoft Z3 jar file to your local Maven repository.  The installation process will give you a jar that is the Java API, called `com.microsoft.z3.jar`.  Then, run `mvn install:install-file -Dfile=/path/to/com.microsoft.z3.jar -DgroupId=com.microsoft -DartifactId=z3 -Dversion=1.0 -Dpackaging=jar`, replacing `/path/to` with wherever the jar is located on your local filesystem.  The group, artifact, and version must match exactly, regardless of the version that you have built, because these versions are expected in our `pom.xml` file.

## Execution

SQLRepair can be run in two modes: test mode, and interactive mode.  

Test mode runs all of ourprovided tests for the project, which you can use to ensure that everything is working properly and as regression tests if you've extended the system at all.  To run SQLRepair in test mode, cd into the `sqlrepair` folder and run `mvn clean test`.  The process is quick, and after approximately twenty seconds you should see a message in the console indicating that everything passed.

Interactive mode launches SQLRepair as a web application so that you can use it for repairing SQL queries.  We encourage that you use the provided web UI that we have written, but the API can also be used directly from tools such as [Postman](https://www.getpostman.com/).  To launch SQLRepair in interactive mode, cd into the `sqlrepair` folder and run `mvn clean spring-boot:run`.  You can then access the application at `localhost:8080`

## How-To

To use SQLRepair, enter in one or more pairs of (source, destination) tables and the query to attempt a repair on.  Then, click the Submit Example to attempt a repair.

Below we have several examples demonstrating how SQLRepair can fix mistakes in provided queries

### Simple Repair

Enter in the source table:
```
a
5
4
3
2
1
```

And destination table:
```
a
5
4
```

And query to repair:
`select * from table WHERE a = 10;`

Click submit, and you should receive a repaired query similar to: `SELECT * FROM table WHERE a > 3`


### Syntax Repair
Enter in the source table:
```
a
5
4
3
2
1
```

And destination table:
```
a
5
4
```

And the query to repair:
`select * from table WHERE a = 10 && a != 4`

Click submit, and you should receive a repaired query similar to: `SELECT * FROM table WHERE a = 5 AND a = 5`.  SQLRepair automatically corrects the incorrect syntax `&&` and fixes the incorrect logic as well.


### Column Repair
Enter in the source table:
```
a b
5 4
4 3
3 2
2 1
1 0
```

And destination table:
```
a 
5
```
And the query to repair:
`select * from table WHERE a = 10;`

Click submit, and you should receive the repaird query `SELECT a FROM table WHERE a = 5`.  SQLRepair recognises that the set of columns provided is wrong and corrects this in addition to the incorrect WHERE clause.


### Partial Repairs
SQLRepair cannot fix all mistakes, due to unsupported functionality or wildly broken syntax.  However, it can still make partial syntax repairs even in such cases.