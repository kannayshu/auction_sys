基于C2C的PAIPAI拍卖平台

**应用介绍：**

本应用是一个基于C2C（Customer To
Customer）的拍卖网站，用户可以在这上面发布或参与商品拍卖。

基本业务流程：

![](media/beb1682676b57ca977afe353cca4fe28.png)

**技术使用：**

开发模式采用前后端分离的模式。

后台用Java编写，主要使用了Spring、Spring MVC 、Spring
Schedule、Mybaits、jedis等框架

服务器使用centos

服务器环境使用了nginx做代理服务器访问图片、redis存储应用缓存等

**主要模块：**

用户模块

商品模块

拍卖模块

订单模块

**用户模块包含**：登录注册、找回密码、信息更新、地址管理等功能

登录注册页面：

![](media/905ab1fd5049fd3d35ad2e14c7319c08.png)

![](media/3a07d02ae9e1d9e5ff608fb35a4a1ba8.png)

个人中心：

![](media/c5b2be2891820da6e0c235845d554011.png)

地址管理：

![](media/cd77530c83c676ef3e2ceda0f83c2ca5.png)

![](media/cab4f2d34aba01a4ab790125cb805a11.png)

**商品模块包含**：收藏、搜索、竞拍等功能

搜索页面：

主要包含关键词搜索以及分类搜索等搜索方式

![](media/76305c6978ec77420af39a2cec501b1f.png)

商品发布页面：

主要包含主图上传、用富文本编辑器simditor上传商品详情并发布

![](media/fe2d86441fc363c9bb4bee9203c8789b.png)

商品发布：

![](media/17fcb9af31905d0bc2d704c61d2ed47a.png)

商品详情：

商品会根据商品状态显示当前出于拍卖状态还是结束状态，并且有对应的拍卖记录

竞拍中：

![](media/99a8a886e3f065c1a6cfdb2782ae90d2.png)

竞拍结束：

![](media/0cdc4921ab18e8163bf8d5250e2e159f.png)

商品详情富文本内容：

![](media/cb05ccdfb5743c694a9c308b772ad4bd.png)

详情页下的竞买记录：

![](media/f356d680ea9e09e1837c35bc12634ba1.png)

收藏列表：

![](media/01c63a116b8625b117bc4faebe8276f9.png)

**拍卖模块包含：商品竞拍、拍品发布、定金支付、违约处理等**

商品竞拍：

![](media/589b38367ea76784565c5428375dfd8e.png)

定金支付：

![](media/7def41e241939a366d47d2b2f6cbbee7.png)

：

![C:\\Users\\DALIU\~1\\AppData\\Local\\Temp\\WeChat Files\\557137250607573039.jpg](media/72e50caa1f6a18e13a4907b8295fc49f.jpg)

![C:\\Users\\DALIU\~1\\AppData\\Local\\Temp\\WeChat Files\\403309517491937648.jpg](media/68ae0e0cd8fd8b781cdb115347c4370b.jpg)

**订单模块包含：订单管理、定时资金结算等主要业务功能**

包含对订单状态的管理并显示不同的操作。

订单列表：

![](media/5c3a51ec2388d0d76ef47c5bbc7c4451.png)

订单详情：

![](media/90e8c1e47a75925f01bda0d3dfb4c20d.png)
