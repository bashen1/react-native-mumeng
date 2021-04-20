
#import "RNReactNativeMumeng.h"
#import <UMCommon/UMCommon.h>
#import <UMCommon/MobClick.h>

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

RCT_EXPORT_METHOD(onEvent:(NSString *)eventId)
{
  if (eventId == nil || [eventId isKindOfClass:[NSNull class]]) {
    return;
  }
  [MobClick event:eventId];
}

RCT_EXPORT_METHOD(onEventWithLabel:(NSString *)eventId eventLabel:(NSString *)eventLabel)
{
  if (eventId == nil || [eventId isKindOfClass:[NSNull class]]) {
    return;
  }
  if ([eventLabel isKindOfClass:[NSNull class]]) {
    eventLabel = nil;
  }
  [MobClick event:eventId label:eventLabel];

}

RCT_EXPORT_METHOD(onEventWithMap:(NSString *)eventId parameters:(NSDictionary *)parameters)
{
  if (eventId == nil || [eventId isKindOfClass:[NSNull class]]) {
    return;
  }
  if (parameters == nil && [parameters isKindOfClass:[NSNull class]]) {
    parameters = nil;
  }
  [MobClick event:eventId attributes:parameters];
}

RCT_EXPORT_METHOD(onEventWithMapAndCount:(NSString *)eventId parameters:(NSDictionary *)parameters eventNum:(int)eventNum)
{
  if (eventId == nil || [eventId isKindOfClass:[NSNull class]]) {
    return;
  }
  if (parameters == nil && [parameters isKindOfClass:[NSNull class]]) {
    parameters = nil;
  }
  
  [MobClick event:eventId attributes:parameters counter:eventNum];
}

RCT_EXPORT_METHOD(onPageBegin:(NSString *)pageName)
{
  if (pageName == nil || [pageName isKindOfClass:[NSNull class]]) {
    return;
  }
  [MobClick beginLogPageView:pageName];
}

RCT_EXPORT_METHOD(onPageEnd:(NSString *)pageName)
{
  if (pageName == nil || [pageName isKindOfClass:[NSNull class]]) {
    return;
  }
  [MobClick endLogPageView:pageName];
}

@end
  
