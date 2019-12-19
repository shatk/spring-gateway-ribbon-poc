
--- Spring Cloud Gateway  ---

> Versions 

spring-boot  	  			: 2.1.2.RELEASE
spring-cloud-gateway		: 2.1.1.BUILD-SNAPSHOT
spring-boot-starter-webFlux : 2.1.2.RELEASE
reactor-core 				: 3.2.5.RELEASE

> To build the project :

mvn clean install

> To run the project :

run GatewayApp using eclipse STS4. 

--- CURL Request ---

curl --location --request GET 'http://localhost:9090/ribbon/lb/hello/' \
--header 'Content-Type: application/json' \
--data-raw ''

--- API Response ---

200OK
Hello, Spring!
