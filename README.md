
# react-native-mumeng

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
  