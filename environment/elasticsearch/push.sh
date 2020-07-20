#!/usr/bin/env bash
docker login --username=i@renfei.net registry.cn-beijing.aliyuncs.com
docker tag [ImageId] registry.cn-beijing.aliyuncs.com/ren-fei/elasticsearch:[镜像版本号]
docker push registry.cn-beijing.aliyuncs.com/ren-fei/elasticsearch:[镜像版本号]