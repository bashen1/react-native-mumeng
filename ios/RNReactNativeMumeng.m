
#import "RNReactNativeMumeng.h"
#import <UMCommon/UMCommon.h>
#import <UMCommonLog/UMCommonLogManager.h>

@implementation RNReactNativeMumeng

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()


///////

//初始化SDK（已经废除）
RCT_EXPORT_METHOD(initSDK: (NSDictionary *)param resolve: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSString *AppKey = @"";
    NSString *debug = @"false";
    NSString *channel = @"";
    if ((NSString *)param[@"appKey"] != nil) {
        AppKey=(NSString *)param[@"appKey"];
    }
    if ((NSString *)param[@"debug"] != nil) {
        debug = (NSString *)param[@"debug"];
    }
    if ((NSString *)param[@"channel"] != nil) {
        channel = (NSString *)param[@"channel"];
    }
    if(![AppKey isEqual: @""]){
        Boolean isDebug = NO;
        if([debug isEqual:@"true"]){
            isDebug = YES;
        }
        [UMCommonLogManager setUpUMCommonLogManager];//开发者需要显式的调用此函数，日志系统才能工作
        [UMConfigure setLogEnabled:isDebug];//设置打开日志
        [UMConfigure initWithAppkey:AppKey channel:channel];
        NSDictionary *ret = @{@"code": @"0", @"message":@"success"};
        resolve(ret);
    }else{
        NSDictionary *ret = @{@"code": @"1", @"message":@"AppKey为空"};
        resolve(ret);
    }
}

//授权登录
RCT_EXPORT_METHOD(getInfo: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSString* deviceID =  [UMConfigure deviceIDForIntegration];
    NSDictionary *ret = @{@"code":@"1",@"message":deviceID};
    resolve(ret);
}

@end
  
