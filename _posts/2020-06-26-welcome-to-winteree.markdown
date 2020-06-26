---
layout: post
title:  "欢迎使用 WinterEE！"
date:   2020-06-26 15:57:32 +0800
tags: 关于
color: rgb(255,90,90)
cover: 'https://cdn.renfei.net/upload/image/2020/20200626191634.png'
subtitle: '关于 WinterEE'
---
### WinterEE 是什么  
`WinterEE`是基于`Spring Cloud`的微服务前后端分离开发脚手架，其中包含用户、角色、权限、部门等几乎每个系统都会有的功能，为了避免每个项目重复的开发这些公共功能，我将其提取出来成为一个开发脚手架，你只需要开发属于自己功能的微服务模块插入进来即可。

### WinterEE 名字的含义  
要想了解`WinterEE`你需要先了解`Spring`的名字由来，J2EE对人们来说是一个漫长的冬季（winter），现在终于有了全新的框架体系，这个框架就是开发者们的春天，所以就取名`Spring`，而这套框架是基于`Spring`的，所以为了致敬`Spring`依然选择四季其中之一，这个`Spring`框架给开发者带来了春天，而我们希望把琐碎无聊的工作交给我们，由我们承担冬季（winter）的部分，所以取名`Winter`；而`EE`则是向J2EE的致敬，并且也是为企业应用服务，综上所述取名`WinterEE`。

### WinterEE 的设计理念  
从一开始我们就使用的是微服务、前后端分离的设计思想，使用`Restfull API`接口进行通信对接，服务模块之间可以相互调用，如果你的业务模块需要调用`WinterEE`内部的服务，可以使用`WinterEE-API`模块中暴露的`interface`进行访问，所以业务模块可以依赖`WinterEE`的服务，但反过来如果你的代码入侵到`WinterEE`造成`WinterEE`去依赖你的业务模块就是不对的，这会造成后续`WinterEE`升级时你的项目难以跟随`WinterEE`升级。

### WinterEE 的构成
![WinterEE的构成](https://cdn.renfei.net/upload/image/2020/20200626190217.png "WinterEE的构成")

