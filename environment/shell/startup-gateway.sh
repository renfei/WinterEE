#!/usr/bin/env bash
# author:RenFei(i@renfei.net)
# 项目Jar包启动命令模板，可依据该模板修改成自己需要的启动命令

nohub java -jar ./WinterEE-Gateway-Serve.jar >>./WinterEE-Gateway-Startup.log 2>&1 &
tail -f ./WinterEE-Gateway-Startup.log
