
# react-native-mumeng

[![npm version](https://badge.fury.io/js/react-native-mumeng.svg)](https://badge.fury.io/js/react-native-mumeng)

## 注意点

|         版本        |   版本  |   文档  |
| :-----------------: | :---: | :---: |
| react-native < 0.60 | 3.0.0 | [点击](./README_0.5X.md) |

## 开始

`$ npm install react-native-mumeng --save`

### 自动配置

`$ react-native link react-native-mumeng`

### 手动配置


### Android
打开`android/bulid.gradle`，添加以下仓库
```
······
allprojects {
    repositories {
        ·····
        maven {
            url "https://dl.bintray.com/umsdk/release"
        }
    }
}
······
```

## 集成

以下初始化，可以不在app初始化的时候集成。在应用启动的时候自己选择初始化，用于隐私合规

### iOS

编辑`AppDelegate.m`

```
#import <UMCommon/UMCommon.h>

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  [UMConfigure initWithAppkey:@"XXXXXXXX" channel:@"App Store"];
}
```

### Android


1. `MainApplication.java`

```
import com.maochunjie.mumeng.RNReactNativeMumengModule;

···
public void onCreate() {
  super.onCreate();
  RNReactNativeMumengModule.init(this, "XXXXX", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
}
```

## 使用
```javascript
import * as mUmeng from 'react-native-react-native-mumeng';

// 初始化
initSDK = async ()=>{
    alert(JSON.stringify(await mUmeng.initSDK({
        appKey: '', //你的友盟appkey
        debug: 'true',
        channel: 'RNTest'
    })));
};

// 获取MAC地址
getInfo = async () => {
    alert(JSON.stringify(await mUmeng.getInfo()));
}
```
  