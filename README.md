# Challenge Project for X Company
Assignment Project For X Company

# Assignment 
Backend project with java codebase including API rest endpoints, database communication and a short description of the Project
Frontend project with ReactJS codebase (if available) including a short description of the project

#Description
Backend Project is very basic user order management system

Frontend Project is includes login , signUp, and profile and home pages are created.



### Tech

Application uses a number of open source projects to work properly:

## Backend
* Java 8- Primary Language
* Spring boot (Spring Data,Spring Security (Basic Auth), Spring Web) - An open source Java-based framework used to create a micro Service.
* Maven - Dependency Management
* H2 - For the sake of simplicity, h2 embedded relational database is used
* JUnit 1.4, Spring boot Test is used for - tests
* JaCoCo - Maven plugin to generate a code coverage report for a Java project.


## Frontend

*React, Redux, React testing library, React thunk and axios are used

### Cloning Repository
```sh
$ git clone https://github.com/mehmetfatihustdag/challenge.git
```

### Installation
 

After cloning or downloading the repository. Follow to steps to test and run the project.

Backend : 

You will need to be go to  folder where pom.xml located.
  ```sh
     $ cd challange/backend
  ```


Steps to Test : 
```sh
$ mvn test
```
Steps to Run :
```sh
$ mvn spring-boot:run
```

Project will run under the http://localhost:8080 
Note : 8080 is configurable port it can be changed via application.yml file


Frontend : 


 ```sh
     $ cd challange/frontend
  ```
  
Steps to Test :
```sh
$  npm test
```  

Steps to Test :
```sh
$  npm start
```  

Project will run under the http://localhost:3000 

### Test Reports :

After running the test, you can find the test coverage reports which JaCoCo plugin provided by opening the file which is under target/site/jacoco/index.html

### Suggestion For Improvements :  
 - Write MORE Tests
 - For High traffic request environment , project can be designed as a micro service based architecture. User management module and order management module may be seperated as a different microservice. 
 Queue technologies (ActiveMQ, RabbitMQ, Kafka,...) could be used for communication between micro service. 
 - I used H2 Database for demonstration purpose. Real ones can be used . 
 - Development, test , and production profiles can be used. Spring profile can be hired for configuration for different environment.  
 --Basic auth is used . JWT , OAuth2 , OpenId can be hired for advanced level security implementation.
 --Once more endpoints added, swagger integration could be used for rest api documentation
 




