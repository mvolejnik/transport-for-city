# Logstash Docker Image

```
docker build -t logstash .

docker network create -d bridge devops-net

docker run -ti -p 9600:9600 --network devops-net --name logstash logstash # -v logstash_pipeline:/usr/share/logstash/pipeline/
```
