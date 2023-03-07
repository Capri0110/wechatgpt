# wechatgpt

#### 介绍
基于chatGPT开发微信公众号智能机器人

由于最近访问人数有点多，免费额度有限，暂时对公众号进行限制，每人每天免费两条提问 :sob: 希望大家能够理解
如果觉得项目可以，大家点一下小心心

#### 软件架构
主要的是 springboot + redis + wxJava


#### 安装教程

将项目导入到idea中，初始化项目，然后准备好下面的信息

1.  需要准备一台redis ,在application.yml中配置reids的信息
2.  需要准备chatGPTopenApi的密钥,在application.yml中配置
3.  需要申请一个微信公众号，获取公众号的token信息以及密钥信息，在application-dev.yml和application-test.yml中配置对应的信息

最后启动项目    

项目演示效果

![输入图片说明](7996a55080c6d357250d461197b3fb9.png)

