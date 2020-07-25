#!/usr/bin/env bash
#########################################
## Maven构建、Docker镜像制作、Docker仓库推送
## Author RenFei(i@renfei.net)
#########################################
# 以下内容需要替换成你自己的内容
# [Name]：镜像的名称
# [Version]：镜像版本，例如：1.0.0
# [User Name]：镜像仓库的用户名
# [Password]：镜像仓库的密码
# [Repositories Address]：镜像仓库的地址

mvn clean package && \
docker build -t [Name]:[Version] . && \
docker login --username=[User Name] --password=[Password] [Repositories Address] && \
docker tag `docker images -q --filter reference=[Name]:[Version]` [Repositories Address][Name]:[Version] && \
docker push [Repositories Address][Name]:[Version] && \
docker logout [Repositories Address]