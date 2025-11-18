# RokidDemo

Rokid Glasses CXR-M SDK demo

## 技术栈

[开发文档](https://custom.rokid.com/prod/rokid_web/57e35cd3ae294d16b1b8fc8dcbb1b7c7/pc/cn/9d9dea4799ca4dd2a1176fedb075b6f2.html)
CXR-M SDK 是面向移动端的开发工具包，主要用于构建手机端与 Rokid Glasses 的控制和协同应用。开发者可以通过
CXR-M SDK 与眼镜建立稳定连接，实现数据通信、实时音视频获取以及场景自定义。
它适合需要在手机端进行界面交互、远程控制或与眼镜端配合完成复杂功能的应用。目前 CXR-M SDK 仅提供
Android 版本。

UI库 qmuilibrary：https://github.com/Tencent/QMUI_Android
QMUI Android 的设计目的是用于辅助快速搭建一个具备基本设计还原效果的 Android 项目
qmuilibrary：是打包出来的aar包，部分可能存在问题，

# Rokid Glasses 设备连接与管理

开发者可以使用CXR-M SDK 与Rokid Glasses 进行连接，并可以获取Rokid Glasses 设备状态，并进行管理。

# Rokid Glasses 自定义场景交互

开发者可以通过CXR-M SDK 快速接入YodaOS-Sprite 操作系统定义的场景交互流程。快速根据YodaOS-Sprite
定义的交互场景进行自定义动能开发。


因为sdk版本是1.0.3
搭配使用自己的眼睛的时候需要修改一下两处
1.res/raw/sn.lc 文件 修改为自己的鉴权文件
2.CommonModel中CLIENT_SECRET修改为自己的密钥

