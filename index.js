import {
    NativeModules,
    Alert
} from 'react-native';

const {RNReactNativeMumeng} = NativeModules;

export async function initSDK(params) {
    return await RNReactNativeMumeng.initSDK(params);
}

export async function getInfo() {
    return await RNReactNativeMumeng.getInfo();
}
