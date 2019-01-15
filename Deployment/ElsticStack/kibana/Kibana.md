

docker run -p 5601:5601 -e "ENV SERVER_NAME=kibana.docker.pri" -e "ELASTICSEARCH_URL=http://es.docker.pri" --name kibana --link elasticsearch kibana
