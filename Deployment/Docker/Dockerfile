# Docker image based on jetty < openjdk < alpine
FROM jetty:9.4.5-alpine

ADD tmp/tfc-ws-rest.war /var/lib/jetty/webapps/
ADD tmp/tfc-web-scheduler.war /var/lib/jetty/webapps/

EXPOSE 8080
ENTRYPOINT /docker-entrypoint.sh
