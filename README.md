#SOI Final Project

The **Virtual LifeCoach** project is a Service Oriented Architecture (SOA) that wants to provide a basic framework for the definition of a wide range of possible services and applications, offering a modular, flexible and reusable set of tools for the management of lifestyle and health profiles.

###Run the Tests  

For this assignment Maven has been used.  

For testing purposes we defined a stand-alone server that will provide access to all the REST resources (both Storage Service and LifeCoach Service).  

In order to start the server run the following command: 
    
    mvn compile -Ptest-server
    
This starts the new StandaloneServer accessible at http://localhost:5030/.  

For testing the HealthProfile Service is instead required to run the local publisher, note that this SOAP service make use of the Storage Service, so the stand-alone server previous cited has to be already started.   

In order to start the service publisher run the following command: 
    
    mvn compile -Ptest-publisher
    
This starts the new service at http://localhost:5031/ws/healthProfile.  


In order to start the test for the REST client run the following command: 

    mvn compile -Ptest-rest
    
For the SOAP one run instead: 

    mvn compile -Ptest-soap

In case of problems try the following commands to clean and recompile the source code:

    mvn clean
    mvn compile