#!/usr/bin/env bash
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 \
  -v /opt/elasticsearch/data -e"discovery.type=single-node" \
  --network	mynet --network-alias  elasticsearch elasticsearchik:7.8.0