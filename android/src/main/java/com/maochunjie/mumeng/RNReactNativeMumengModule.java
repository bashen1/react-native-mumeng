
package com.maochunjie.mumeng;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.facebook.react.bridge.*;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RNReactNativeMumengModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNReactNativeMumengModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNReactNativeMumeng";
    }

    @ReactMethod // 初始化SDK（废除）
    public void initSDK(final ReadableMap data, final Promise p) {
        String appkey = data.getString("appKey");
        String debug = data.getString("debug");
        String channel = data.getString("channel");
        Boolean isDebug = false;
        if (debug == "true") {
            isDebug = true;
        }
        if (channel == null || channel.equals("")) {
            channel = "Umeng";
        }
        if (!appkey.equals("")) {
            Application app = (Application) reactContext.getApplicationContext();
            UMConfigure.setLogEnabled(isDebug);
            UMConfigure.init(app, appkey, channel, UMConfigure.DEVICE_TYPE_PHONE, "");
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
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                    device_id = Settings.Secure.getString(
                            context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                }
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)) {
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;
    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName())
                        || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission",
                        String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static void init(Context context, String appkey, String channel, String secret) {
        UMConfigure.init(context, appkey, channel, UMConfigure.DEVICE_TYPE_PHONE, secret);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }
}