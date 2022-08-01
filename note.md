mvn install
ssh ramamike@ramamike.blockwit.io
ps aux | grep booking
kill <pid>
~/javaProjects/tasks/booking/target$ scp booking-0.0.1-SNAPSHOT.war ramamike@ramamike.blockwit.io:
./start.sh

# Upload path for pictures
upload.path=/home/ramamike/service-booking

spring.servlet.multipart.enabled=true
# Threshold after which files are written to disk
spring.servlet.multipart.file-size-threshold=2KB
# Max file size
spring.servlet.multipart.max-file-size=200MB
# Max Request Size
spring.servlet.multipart.max-request-size=251MB
