FROM docker.elastic.co/beats/metricbeat:6.5.4

ADD metricbeat.yml /usr/share/metricbeat/metricbeat.yml

ADD system.yml /usr/share/metricbeat/modules.d/system.yml

USER root
RUN chmod 644 /usr/share/metricbeat/metricbeat.yml
USER metricbeat

ENTRYPOINT tail -f /dev/null
