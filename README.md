# i河畔(UESTC_BBS)
![](https://github.com/Febers/UESTC_BBS/blob/master/app/src/main/ic_launcher-web.png)

[前往下载页面](https://github.com/Febers/UESTC_BBS/releases)

## 关于

电子科技大学官方论坛“清水河畔”(http://bbs.uestc.edu.cn/forum.php) 的Android开源客户端，主要使用Kotlin开发。项目整体使用Mvp架构+Retrfit+Glide完成，如有建议或疑问可联系开发者:febers418@qq.com。


![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/UESTC_BBS%E6%B5%B7%E6%8A%A5.png)

### 功能列表

- [x] 登录、看帖、发帖、回复、私信等基本功能。
- [x] 图片保存、分享、发帖添加图片附件。
- [x] 夜间模式、任意颜色主题切换。
- [x] 查看用户收藏、回复的帖子。
- [x] 应用内打开web界面
- [x] 多彩图标选择。
- [x] 多用户切换。
- [x] 河畔表情包。
- [x] 帖子搜索。
- [ ] 等等

## 实现细节

### Mvp架构
本项目的整体包结构如下，其中module包下有各功能模块。
```
-base //包含项目的所有基类，包括baseActivity、BaseModel等

-entity	//包含所有的JavaBean类，大多为解析服务器返回的json的数据类

-home	//主界面包

-http	//网络工具包

-io	 //文件保存读取包

-module  //项目功能模块包

-utils	//项目工具包

-view	//跟视图绘制有关的所有类

-GlideModule.kt  //Glide的Model

-MyApp.kt  //自定义Application类
```
比如在登录模块中，可以看到，login包下有四个子包——contract、model、presenter和view。其中，contract(契约)包放置定义该功能模块Mvp三方的行为和对象的契约类LoginContract。
```
-contract  //包含模块契约类，联系M-V-P三者

-model	//事务具体类，包括数据的存取

-presenter  //Presenter类，充当M和V之间的桥梁

-view  //包含所有的视图
```

### 功能实现方法
表情包功能魔改了[PandaEmoView](https://github.com/PandaQAQ/PandaEmoView)，界面整体的框架布局使用了[Fragmentation](https://github.com/YoKeyword/Fragmentation)，图片加载使用[Glide](https://github.com/bumptech/glide)库，其他开源库可打开应用内的 关于-开源项目 查看。

你可在源码中阅读注释获取更多细节。


## 使用截图

![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/screen0.png)
![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/screen1.png)
![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/screen2.png)
![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/screen3.png)

## LICENSE

```
Copyright 2019 Febers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
