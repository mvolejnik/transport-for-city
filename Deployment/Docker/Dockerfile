# Docker image based on jetty < openjdk < alpine
FROM jetty:9.4.5-alpine

ADD tmp/ptd-ws-rest.war /var/lib/jetty/webapps/
ADD tmp/ptd-web-scheduler.war /var/lib/jetty/webapps/
ADD tmp/keystore /var/lib/jetty/etc/
ADD start.ini /var/lib/jetty/
ADD jetty-ssl-context.xml /var/lib/jetty/etc

EXPOSE 8080 8443
ENTRYPOINT /docker-entrypoint.sh
