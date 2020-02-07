import {
    NativeModules,
    Alert,
} from 'react-native';

const {RNReactNativeMumeng} = NativeModules;

/**
 * 初始化SDK（已废除）
 * @param params
 * {
 *     appKey: '',
 *     debug: true,
 *     channel: '',
 * }
 * @returns {Promise<InitResult|*|void>}
 */
export async function initSDK(params) {
    return await RNReactNativeMumeng.initSDK(params);
}

/**
 * 获取设备信息
 * @returns {Promise<*>}
 */
export async function getInfo() {
    return await RNReactNativeMumeng.getInfo();
}

/**
 * onPageStart
 * @param pageName string
 */
export function onPageBegin(pageName) {
    RNReactNativeMumeng.onPageBegin(pageName);
}

/**
 * onPageEnd
 * @param pageName string
 */
export function onPageEnd(pageName) {
    RNReactNativeMumeng.onPageEnd(pageName);
}

/**
 * onEvent
 * @param eventId string
 */
export function onEvent(eventId) {
    RNReactNativeMumeng.onEvent(eventId);
}

/**
 * onEventWithLable
 * @param eventId string
 * @param eventLabel string
 */
export function onEventWithLabel(eventId, eventLabel) {
    RNReactNativeMumeng.onEventWithLabel(eventId, eventLabel);
}

/**
 * onEventWithMap
 * @param eventId string
 * @param eventMap json object
 */
export function onEventWithMap(eventId, eventMap) {
    RNReactNativeMumeng.onEventWithMap(eventId, eventMap);
}

/**
 * onEventWithMapAndCount
 * @param eventId string
 * @param eventMap json object
 * @param eventCount int
 */
export function onEventWithMapAndCount(eventId, eventMap, eventCount) {
    RNReactNativeMumeng.onEventWithMapAndCount(eventId, eventMap, eventCount);
}
