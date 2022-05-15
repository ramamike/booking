![BlockWit booking](logo.png "BlockWit booking")

# BlockWit booking project

## Build
mvn install

## Start application
To start application you can use command:

java -jar /path/to/war/booking-0.0.1-SNAPSHOT.war --spring.config.location=/path/to/config/application.properties

Sometimes you should pass config through -D option, like this:  
java -jar /path/to/war/booking-0.0.1-SNAPSHOT.war -Dspring.config.location=/path/to/config/application.properties
