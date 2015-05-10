#!/bin/bash

SW_DIR=/vagrant/.install

echo Installing Java
[ -d /usr/java ] || mkdir /usr/java

echo Installing Oracle Java 8
if [ ! -d /usr/java/jdk6 ];then
        JDK_INSTALLER=jdk-8u45-linux-x64.tar.gz
        JDK_DIR=jdk1.8.0_45
	if [ ! -f $SW_DIR/$JDK_INSTALLER ]; then
		echo Missing Java Installer 
		exit 1
	fi

	if [ -f $SW_DIR/$JDK_INSTALLER -a ! -d "/usr/java/$JDK_DIR" ]; then
		echo Installing JDK 8...
                tar xzvf $SW_DIR/$JDK_INSTALLER
		mv $JDK_DIR /usr/java/$JDK_DIR
		ln -sv /usr/java/$JDK_DIR /usr/java/jdk8
	else 
		echo Unable to Install JDK 8, has not been downloaded.
	fi
fi

cp -v /vagrant/OS/default/java /etc/default
cp -v /vagrant/OS/profile/java.csh /etc/profile.d
cp -v /vagrant/OS/profile/java.sh /etc/profile.d

