#!/bin/bash

WF_INSTALL_DIR=WildFly
SW_DIR=/vagrant/.install
WF_INSTALLER=wildfly-8.2.0.Final.tar.gz
WF_DIR=/opt/wildfly
MODULE_JDBC_MARIADB=mariadb-java-client-1.1.8.jar 

echo Configuring users...
id wildfly 2>/dev/null || groupadd -g 1100 wildfly && useradd -u 1100 -g 1100 -s /bin/bash -m -d /home/wildfly wildfly 

passwd wildfly << EOF
welcome1
welcome1
EOF

if [ -d /opt/wildfly-8.2.0.Final ] ; then
        echo WildFly Server already installed
else
	if [ ! -f $SW_DIR/$WF_INSTALLER ]; then
   		echo Missing WildFly installer
                exit 1
	fi
	echo Installing WildFly Server
        pwd
        tar xzvf $SW_DIR/$WF_INSTALLER -C /opt
        ln -s /opt/wildfly-8.2.0.Final $WF_DIR
fi

cp -v /vagrant/$WF_INSTALL_DIR/development.xml $WF_DIR/standalone/configuration/development.xml
chown wildfly:wildfly -R $WF_DIR /opt/wildfly-8.2.0.Final

[ -d /var/log/wildfly ] || mkdir /var/log/wildfly
chown -v root:wildfly /var/log/wildfly
chmod ug+rwx /var/log/wildfly

sudo service wildfly status || service wildfly start

#ADD USER
sudo -u wildfly -E /opt/wildfly/bin/add-user.sh -s wildfly welcome1

#INSTALL MODULES
if [ -f $SW_DIR/$MODULE_JDBC_MARIADB ]; then
  curl --silent --url http://central.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/1.1.8/$MODULE_JDBC_MARIADB --output $SW_DIR/$MODULE_JDBC_MARIADB
fi

#mkdir -p $WF_DIR/modules/org/mariadb/jdbc/main
#cp -v /vagrant/$MODULE_JDBC_MARIADB $WF_DIR/modules/org/mariadb/jdbc/main
#cp -v /vagrant/module_jdbc_mariadb.xml $WF_DIR/modules/org/mariadb/jdbc/main

[ -d /opt/wildfly/modules/org.mariadb/jdbc/main ] || /opt/wildfly/bin/jboss-cli.sh --file=/vagrant/WildFly/module_mariadb.cli
/opt/wildfly/bin/jboss-cli.sh --file=/vagrant/WildFly/install.cli


cp -v /vagrant/OS/systemd/system/wildfly.service /etc/systemd/system
cp -v /vagrant/$WF_INSTALL_DIR/wildfly.conf /etc/default

systemctl daemon-reload
systemctl start wildfly.service
systemctl status wildfly.service
systemctl enable wildfly.service
