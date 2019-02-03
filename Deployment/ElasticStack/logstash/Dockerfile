FROM logstash:6.5.4
#FROM docker.elastic.co/logstash/logstash:6.5.4

RUN rm -f /usr/share/logstash/pipeline/logstash.conf

ADD pipeline/ /usr/share/logstash/pipeline/

ADD config/ /usr/share/logstash/config/

