



1. Developer environment ignore ssl errors.
mvn -Dmaven.wagon.http.ssl.insecure=true clean install

2. start spring boot with 

mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8888"


