

docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -v elasticsearch_data:/usr/share/elasticsearch/data elasticsearch 

curl -X PUT --header "Content-Type: application/json"  -d "{\"persistent\": {\"xpack.monitoring.collection.enabled\": true}}"  http://localhost:9200/_cluster/settings

