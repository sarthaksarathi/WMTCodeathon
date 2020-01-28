# WMTCodeathon
WMTCodeathon source control and deployment repo

Project Setup
-------------
1. Download/Clone the repository
2. In Eclipse, import existing maven projects and point the root of the project to the root of the downloaded/cloned repo
3. To generate a jar for deployment, right click on the project and
    a. Run as maven clean
    b. Run as maven install
    c. jar will be generate in the target folder
    d. Upload the jar to the deploy branch by drag & drop on github UI
    
Project Config
--------------
1. Change the config in the application.properties file to your respective azure db config

Test Run
---------
1. Right click on the project and run as Java application ( Select the class with main function i.e. Spring boot class)
2. Go to localhost:8080/hello-world-bean to test the web app
3. Call /get-inventory to test the API and DB connection


ILA TEST