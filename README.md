# i河畔(UESTC_BBS)

## 关于
电子科技大学官方论坛“清水河畔”(http://bbs.uestc.edu.cn/forum.php)的Android开源客户端，主要使用Kotlin开发。项目整体使用Mvp架构+Retrfit+Glide完成，如有建议或疑问可联系开发者:febers418@qq.com。

## 实现细节
### Mvp架构
本项目的整体包结构如下图，其中module包下有各功能模块。

![](http://febers.tech/wp-content/uploads/2018/11/1.png)

选取登录模块介绍，由下图可看到，login包下有四个子包——contract、model、presenter和view。其中，contract(契约)包放置定义该功能模块Mvp三方的行为和对象的契约类LoginContract。

![](http://febers.tech/wp-content/uploads/2018/11/2.png)


你可在源码中阅读注释获取更多细节。


## 使用截图
![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/Screenshot_1.png)
![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/Screenshot_2.png)
![](https://github.com/Febers/UESTC_BBS/blob/master/Screenshots/Screenshot_3.png)

## LICENSE

```
Copyright 2018 Febers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.











