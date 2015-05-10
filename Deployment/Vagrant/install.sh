#!/bin/bash

OS_INSTALL_DIR=OS
UPDATE_OS=true

echo Fixing TimeZone... \(CentOS 6\)
#if [ ! -h /etc/localtime ]; then
#	mv /etc/localtime /etc/localtime.bak
#	ln -s /usr/share/zoneinfo/Europe/Prague /etc/localtime
#fi

IP=$(ifconfig eth1 |awk 'BEGIN { FS = "[ :]*" } {if ( $2 == "inet" ) { print $4 }}')
HOSTNAME_SHORT=$(hostname -s)
HOSTNAME_LONG=$(hostname)

#disable SELinux
if [ -f /etc/selinux/config ]; then
  echo 0 >/selinux/enforce
  cp -v /etc/selinux/config /etc/selinux/config.orig
  sed 's/SELINUX=enforcing/SELINUX=permissive/' /etc/selinux/config.orig > /etc/selinux/config
fi

#disable iptables
service iptables stop
chkconfig iptables off

service ip6tables stop
chkconfig ip6tables off

echo Configuring hosts settings
if ! grep -q "$IP" /etc/hosts; then
	mv -v /etc/hosts /etc/hosts.backup
        cp -v /vagrant/$OS_INSTALL_DIR/hosts /etc/hosts
	echo -e "$IP    $HOSTNAME_LONG $HOSTNAME_SHORT" >> /etc/hosts
fi

echo Installing Linux packages...
yum -y install man-pages

if $UPDATE_OS; then
  yum check-update
  yum -y update
fi

yum -y install mc
yum -y install telnet
yum -y install sysstat lsof
yum -y install gdb
yum -y install zip unzip
yum -y install dos2unix
yum -y install strace
yum -y install libXtst-devel libXrender-devel xauth


