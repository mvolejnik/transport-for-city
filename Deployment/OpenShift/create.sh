#!/bin/bash

rhc app-create apps jboss-wildfly-8
rhc add-cartridge https://raw.github.com/openshift-cartridges/mariadb-cartridge/master/metadata/manifest.yml -a apps

