#!/bin/bash
OS_INSTALL_DIR=OS

echo Configuring Security limits
if ! grep -qe "wildfly.*nofile.*" /etc/security/limits.conf; then
	cat /vagrant/$OS_INSTALL_DIR/limits.conf >> /etc/security/limits.conf
fi

