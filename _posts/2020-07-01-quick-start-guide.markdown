---
layout: post
title:  "快速开始使用 WinterEE"
date:   2020-07-01 16:15:33 +0800
tags: 开发
color: rgb(0,112,162)
cover: 'https://cdn.renfei.net/upload/image/2020/20200701161419.png'
subtitle: '快速安装使用指南'
---
### 基础环境  
- 【必须】JDK 8 (推荐 OpenJDK 1.8.0_242)
- 【必须】Apache Maven (推荐3.3.9)
- 【必须】MySQL (推荐5.7.17，也可以是MariaDB)
- 【可选】RabbitMQ (推荐3.7.26，消息队列用于BUS消息总线，通知服务配置更新，重新拉取配置)
- 【可选】Zipkin (推荐2.21.1，用于服务链路跟踪)
- 【可选】Redis
- 【推荐】Docker (容器化会更方便自动化部署和运维)

### 获取 WinterEE   
- 代码：[github.com/renfei-net/WinterEE][github]
- 主页：[winteree.renfei.net][home]
- 论坛：[bbs.renfei.net][bbs]
- 作者：[www.renfei.net][renfei]

代码分支说明：项目使用Git Flow工作流分支管理

- master: 发布版本的分支
- develop: 开发分支，开发人员的基线分支
- feature/*: 功能分支，各种开发中的功能模块在这里完成开发
- gh-pages: 项目主页分支，项目主页网站页面在这里
- winteree-config: 配置文件分支，用于演示配置在此处

### WinterEE 的模块说明  
- WinterEE-API：WinterEE的内部服务通过这个包对外发布，包括接口地址、入参对象和出参对象
- WinterEE-Config-Serve：微服务中的配置中心，所有服务的配置文件在这里统一管理，通过BUS消息总线通知各个服务拉取最新配置，实现配置文件修改不停机热更新
- WinterEE-Core-Serve：核心服务，WinterEE的业务逻辑在这里实现
- WinterEE-Eureka-Serve：服务注册与发现，这里负责让各个服务相互看的见找得到
- WinterEE-Gateway-Serve：网关服务，整个微服务架构的入口，负责转发请求，同时可以在这里做限流等操作
- WinterEE-UAA-Serve：用户的认证鉴权，这里颁发Token凭证和校验凭证，各个微服务会拿着Token来这里查询令牌的有效性
- Your-Module-Serve：用于演示你的程序如何集成，你可以直接复制这个模块，然后修改包名就可以开始写自己的业务逻辑了

### WinterEE 的启动
由于服务之间有依赖关系，启动时有一定的顺序要求，否则会启动失败，以下是推荐的启动顺序：  
WinterEE-Config-Serve -> WinterEE-Eureka-Serve -> WinterEE-Core-Serve -> WinterEE-UAA-Serve -> [你的业务服务们] -> WinterEE-Gateway-Serve

注意事项：在启动`WinterEE-Config-Serve -> WinterEE-Eureka-Serve`以后需要等待以下，等待WinterEE-Config-Serve的服务注册到WinterEE-Eureka-Serve中，因为服务注册发现是有心跳间隔的，不能马上立刻发现，需要等待一下，如果直接启动其他服务会因为找不到配置中心而启动失败。

[github]: https://github.com/renfei-net/WinterEE
[home]:   https://winteree.renfei.net
[bbs]:    https://bbs.renfei.net
[renfei]: https://www.renfei.net