
# react-native-mumeng

## NPM 安装完以后，必须下载本仓库中的SDK（iOS），由于百川SDK中用了SystemLink，NPM中的百川SDK包不完整，Android不受影响

## 开始

`$ npm install react-native-mumeng --save`

### 自动配置

`$ react-native link react-native-mumeng`

### 手动配置


#### iOS

1. 打开XCode工程中, 右键点击 `Libraries` ➜ `Add Files to [your project's name]`
2. 去 `node_modules` ➜ `react-native-mumeng` 目录添加 `RNReactNativeMumeng.xcodeproj`
3. 在工程Build Phases ➜ Link Binary With Libraries 中添加 `libRNReactNativeMumeng.a`

#### Android

1. 打开 `android/app/src/main/java/[...]/MainActivity.java`
  - 在顶部添加 `import com.reactlibrary.RNReactNativeMumengPackage;`
  - 在 `getPackages()` 方法后添加 `new RNReactNativeMumengPackage()`
2. 打开 `android/settings.gradle` ，添加:
  	```
  	include ':react-native-mumeng'
  	project(':react-native-mumeng').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mumeng/android')
  	```
3. 打开 `android/app/build.gradle` ，添加:
  	```
      compile project(':react-native-mumeng')
  	```


### 其他配置

#### iOS

1. 在工程中引入 `node_modules/react-native-mumeng/ios/UmengSDK`


## 集成

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

1. `MainActivity.java`

```
import com.umeng.analytics.MobclickAgent;

···
protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
}

protected void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
}
```

2. `MainApplication.java`

```
import com.umeng.commonsdk.UMConfigure;

···
public void onCreate() {
  super.onCreate();
  UMConfigure.init(this, "XXXXX", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
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
  