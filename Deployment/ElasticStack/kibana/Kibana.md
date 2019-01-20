

docker network create -d bridge devops-net

docker run -p 5601:5601 -e "ENV SERVER_NAME=kibana" -e "ELASTICSEARCH_URL=http://elasticsearch:9200" --name kibana --network devops-net kibana
