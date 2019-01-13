FROM jenkins/jenkins:jdk11

USER root

# Apache Ant
RUN curl -s http://mirror.dkm.cz/apache/ant/binaries/apache-ant-1.10.5-bin.tar.gz | tar -xzC /opt/

# Apache Maven
RUN curl -s http://mirror.dkm.cz/apache/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz | tar -xzC /opt/

# Java
RUN curl -s https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz | tar -xzC /opt/

ENV JAVA_HOME=/opt/jdk-11.0.1

ENV MVN_HOME=/opt/apache-maven-3.6.0

ENV ANT_HOME=/opt/apache-ant-1.10.5

ENV PATH=$JAVA_HOME/bin:$MVN_HOME/bin:$ANT_HOME/bin:$PATH

