
package com.maochunjie.mumeng;

import android.content.Context;

import com.facebook.react.bridge.*;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.statistics.common.DeviceConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RNReactNativeMumengModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;

    public RNReactNativeMumengModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "RNReactNativeMumeng";
    }

    @ReactMethod // 初始化SDK
    public void initSDK(final ReadableMap data, final Promise p) {
        String appkey = data.getString("appKey");
        String debug = data.getString("debug");
        String channel = data.getString("channel");
        boolean isDebug = false;
        if (debug.equals("true")) {
            isDebug = true;
        }
        if (channel == null || channel.equals("")) {
            channel = "Umeng";
        }
        if (!appkey.equals("")) {
            UMConfigure.init(reactContext, appkey, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);
            UMConfigure.setLogEnabled(isDebug);
            WritableMap map = Arguments.createMap();
            map.putString("message", "success");
            map.putString("code", Integer.toString(0));
            p.resolve(map);
        } else {
            WritableMap map = Arguments.createMap();
            map.putString("message", "AppKey为空");
            map.putString("code", Integer.toString(1));
            p.resolve(map);
        }
    }

    @ReactMethod //获取信息
    public void getInfo(final Promise p) {
        String info = getDeviceInfo(reactContext);
        WritableMap map = Arguments.createMap();
        map.putString("message", info);
        map.putString("code", Integer.toString(1));
        p.resolve(map);
    }

    /********************************U-App统计*********************************/
    @ReactMethod
    public void onPageStart(String mPageName) {
        MobclickAgent.onPageStart(mPageName);
    }

    @ReactMethod
    public void onPageEnd(String mPageName) {
        MobclickAgent.onPageEnd(mPageName);
    }

    @ReactMethod
    public void onEvent(String eventId) {
        MobclickAgent.onEvent(reactContext, eventId);
    }

    @ReactMethod
    public void onEventWithLabel(String eventId, String eventLabel) {
        MobclickAgent.onEvent(reactContext, eventId, eventLabel);
    }

    @ReactMethod
    public void onEventWithMap(String eventId, ReadableMap map) {
        Map<String, String> rMap = new HashMap<String, String>();
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            if (ReadableType.Array == map.getType(key)) {
                rMap.put(key, map.getArray(key).toString());
            } else if (ReadableType.Boolean == map.getType(key)) {
                rMap.put(key, String.valueOf(map.getBoolean(key)));
            } else if (ReadableType.Number == map.getType(key)) {
                rMap.put(key, String.valueOf(map.getInt(key)));
            } else if (ReadableType.String == map.getType(key)) {
                rMap.put(key, map.getString(key));
            } else if (ReadableType.Map == map.getType(key)) {
                rMap.put(key, map.getMap(key).toString());
            }
        }
        MobclickAgent.onEvent(reactContext, eventId, rMap);
    }

    @ReactMethod
    public void onEventWithMapAndCount(String eventId, ReadableMap map, int value) {
        Map<String, String> rMap = new HashMap();
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            if (ReadableType.Array == map.getType(key)) {
                rMap.put(key, map.getArray(key).toString());
            } else if (ReadableType.Boolean == map.getType(key)) {
                rMap.put(key, String.valueOf(map.getBoolean(key)));
            } else if (ReadableType.Number == map.getType(key)) {
                rMap.put(key, String.valueOf(map.getInt(key)));
            } else if (ReadableType.String == map.getType(key)) {
                rMap.put(key, map.getString(key));
            } else if (ReadableType.Map == map.getType(key)) {
                rMap.put(key, map.getMap(key).toString());
            }
        }
        MobclickAgent.onEventValue(reactContext, eventId, rMap, value);
    }

    /********************************U-App统计*********************************/

    public static String getDeviceInfo(Context context) {
        try {
            JSONObject json = new JSONObject();
            if (context != null) {
                json.put("device_id", DeviceConfig.getDeviceIdForGeneral(context));
                json.put("mac", DeviceConfig.getMac(context));
            }
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void init(Context context, String appkey, String channel, String secret) {
        UMConfigure.init(context, appkey, channel, UMConfigure.DEVICE_TYPE_PHONE, secret);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);
    }

    public static void preInit(Context context, String appkey, String channel) {
        UMConfigure.preInit(context, appkey, channel);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onDestroy(Context context) {
        MobclickAgent.onKillProcess(context);
    }

    @Override
    public void onHostResume() {
        RNReactNativeMumengModule.onResume(reactContext);
    }

    @Override
    public void onHostPause() {
        RNReactNativeMumengModule.onPause(reactContext);
    }

    @Override
    public void onHostDestroy() {
        RNReactNativeMumengModule.onDestroy(reactContext);
    }
}