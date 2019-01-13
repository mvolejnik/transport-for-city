#/bin/bash

keytool=$JAVA_HOME/bin/keytool

$keytool -genkeypair\
 -alias tls\
 -keyalg EC\
 -keysize 256\
 -keystore keystore\
 -storetype pkcs12\
 -storepass password\
 -keypass password\
 -validity 3650\
 -dname "cn=Public Transport Delays Service,l=Prague,c=CZ"\
 -ext BasicConstraints=ca:false\
 -ext ExtendedkeyUsage=1.3.6.1.5.5.7.3.1\
 -ext SubjectAlternativeName=DNS:localhost,IP:127.0.0.1
