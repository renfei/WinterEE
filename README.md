## WinterEE
[![Build Status](https://travis-ci.org/renfei-net/WinterEE.svg?branch=master)](https://travis-ci.org/renfei-net/WinterEE)
[![Coverage Status](https://coveralls.io/repos/github/renfei-net/WinterEE/badge.svg?branch=master)](https://coveralls.io/github/renfei-net/WinterEE?branch=master)
[![codebeat badge](https://codebeat.co/badges/c9071c17-7646-4790-831f-d57ec130efd5)](https://codebeat.co/projects/github-com-renfei-net-winteree-master)
[![Build status](https://ci.appveyor.com/api/projects/status/7nnjlgf4a7cppn9f/branch/master?svg=true)](https://ci.appveyor.com/project/NeilRen/winteree/branch/master)

WinterEE是基于SpringCloud的开发脚手架，它名字的由来就要先了解Spring名字的由来，Spring的名字是代表了传统J2EE的“冬天”之后新的开始成为春天；WinterEE只是一个半成品，还需要开发者去进一步二次开发，所以我们希望把冬天留给WinterEE来完成，所以取冬天“Winter”，同时使用四季的名称是对Spring的致敬；而 EE 是指“Enterprise Edition”，为企业环境开发，同时也是是对 J2EE 的致敬。

### 项目简介
WinterEE是基于SpringCloud的开发脚手架，我的习惯是每年折腾一个事，之前每年大更新我的个人网站，随着学习的深入和理解，开始尝试使用微服务架构，从而
诞生了这个项目，这个项目也代表了我目前2020年的最高技术水平，目前我知道的，我能用的最先进的技术都使用到了。
##### 技术关键词
Spring Cloud / Spring Boot / Spring Security / OAuth2 / JwtToken / Feign / Spring Cloud Config / Spring Cloud Eureka / 
Spring Cloud Zuul / Zipkin / Redis / RabbitMQ / Spring Cloud Bus / ribbon / hystrix

### 前端项目
此项目是后端项目，后台前端项目请访问[WinterEE-Front](https://github.com/renfei-net/WinterEE-Front) ，后台前端项目使用的是Vue.js，
使用了 vue、vue-router、vue-i18n、vuetifyjs、axios等。其他前端类型类如APP、小程序需要您自行开发。

### 文件结构
```
WinterEE
├── WinterEE-API （统一接口）
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── winteree
│       │   │           └── api
│       │   │               ├── entity （全局统一实体类）
│       │   │               ├── event （全局事件类）
│       │   │               ├── exception （全局异常类）
│       │   │               ├── service （全局服务接口）
│       │   │               └── utils （全局工具）
│       │   └── resources
│       │       └── i18n （国际化语言翻译，仅后端）
│       └── test
│           └── java （单元测试）
├── WinterEE-Config-Serve （配置中心）
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── winteree
│       │   │           └── config （启动类）
│       │   └── resources
│       │       └── config （本地配置）
│       └── test
│           └── java （单元测试）
├── WinterEE-Core-Serve （核心服务）
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── winteree
│       │   │           └── core
│       │   │               ├── config
│       │   │               ├── controller
│       │   │               ├── dao
│       │   │               │   └── entity
│       │   │               ├── filter
│       │   │               ├── security
│       │   │               └── service
│       │   └── resources
│       │       └── mapper
│       └── test
│           ├── java （单元测试）
│           └── resources
├── WinterEE-Eureka-Serve （服务注册中心）
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── winteree
│       │   │           └── eureka
│       │   └── resources
│       └── test
│           └── java （单元测试）
├── WinterEE-Gateway-Serve （服务网关）
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── winteree
│       │   │           └── gateway
│       │   │               ├── client
│       │   │               ├── config
│       │   │               ├── filter
│       │   │               └── service
│       │   └── resources
│       │       └── mapper
│       └── test
│           └── java （单元测试）
├── WinterEE-UAA-Serve （认证授权中心）
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── winteree
│       │   │           └── uaa
│       │   │               ├── aop
│       │   │               ├── client
│       │   │               ├── config
│       │   │               ├── controller
│       │   │               ├── dao
│       │   │               │   └── entity
│       │   │               ├── entity
│       │   │               ├── filter
│       │   │               ├── granter
│       │   │               └── service
│       │   └── resources
│       │       └── mapper
│       └── test
│           └── java （单元测试）
└── environment（运行环境）
    ├── db
    ├── rabbitmq
    ├── redis
    └── zipkin
```
### 编码规约
编码规范按照阿里巴巴的《Java开发手册》执行，其中约定了变量命名、数据库命名、小数比较等规则。
### GitFlow工作流
项目代码采用 GitFlow 工作流管理：
* master: 主分支，保存发布版本
* develop: 开发分支，开发人员在这个分支上工作
* feature/: 功能分支，新增的功能开发在这个分支下开发，完成后合并到 develop 分支
* release: 发布分支，新版本在这个分支进行发布
* hotfix: 补丁分支，重大漏洞快速修复将在这个分支上进行修复

### 启动项目
由于项目有依赖关系，启动的时候有顺序要求，否则会报错，我建议的顺序是：基础运行环境->WinterEE-Config-Serve->WinterEE-Eureka-Serve
->WinterEE-Core-Serve->WinterEE-UAA-Serve->你自己建立的模块->WinterEE-Gateway-Serve，这样的顺序启动。

### 代码结构约定
##### Controller
Controller层实现 WinterEE-API 中的 interface 接口，并且在Controller层做权限拦截，使用的注解是```@PreAuthorize("hasAnyAuthority('signed')")```
Controller层作为控制层只决定调用哪个 Service ，而不处理业务逻辑，禁止直接在Controller层写业务逻辑代码。
##### Service
Service里先定义 interface，然后在 impl 包中实现，这里主要处理业务逻辑。在实现 interface 的同时应该集成 BaseService，例如：
```java
@Service
public class I18nMessageServiceImpl extends BaseService implements I18nMessageService {
    protected I18nMessageServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                     AccountService accountService) {
        super(accountService, wintereeCoreConfig);
    }
}
```