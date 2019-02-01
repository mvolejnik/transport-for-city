FROM docker.elastic.co/beats/metricbeat:6.5.4

ADD --chown=root:metricbeat metricbeat.yml /usr/share/metricbeat/metricbeat.yml

ADD --chown=root:metricbeat system.yml /usr/share/metricbeat/modules.d/system.yml

USER root
RUN chmod 644 /usr/share/metricbeat/metricbeat.yml
RUN chmod 644 /usr/share/metricbeat/modules.d/system.yml
USER metricbeat

ENTRYPOINT tail -f /dev/null
