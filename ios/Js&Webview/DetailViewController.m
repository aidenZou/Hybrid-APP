//
//  DetailViewController.m
//  Js&Webview
//
//  Created by Kim on 11/19/15.
//  Copyright © 2015 Kim. All rights reserved.
//  http://kingxl.cn


#import "DetailViewController.h"
#import <WebKit/WebKit.h>
#import "WKWebViewJavascriptBridge.h"
#import "UMSocial.h"
#import "UMSocialWechatHandler.h"

//define
#define WEB_URL @"http://aidenzou.github.io/Hybrid-APP/share.html"
#define SCREEN_WIDTH            [[UIScreen mainScreen] bounds].size.width
#define SCREEN_HEIGHT           [[UIScreen mainScreen] bounds].size.height



static NSTimeInterval const kWebviewTimeOut = 10;

@interface DetailViewController ()<WKNavigationDelegate,UMSocialUIDelegate>
@property (nonatomic, strong) WKWebView *webView;
@property (nonatomic, strong) UIProgressView *progressView;
@property (nonatomic, strong) WKWebViewJavascriptBridge *bridge;
@property (nonatomic, strong) NSDictionary *data;

- (void)p_clean; //clean webview

@end

@implementation DetailViewController

#pragma mark - Lifecycle

- (instancetype)init {
    if (self = [super init]) {
    }
    return self;
}

-(void)dealloc {
    //clean
    [self p_clean];
    
}

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    //setup the view.
    [self configureviews];
    
    // Bridge
    [self webBridge];
    
    // load request
    [self loadPage];
}

- (void)didReceiveMemoryWarning {
    
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
    
}


#pragma mark - Configure Views

- (void)configureviews {
    
    self.view.backgroundColor = [UIColor whiteColor];
    self.edgesForExtendedLayout = UIRectEdgeNone;
    
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[[UIImage imageNamed:@"nav_back"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] style:UIBarButtonItemStylePlain target:self action:@selector(back:)];
    

    
    self.title = @"加载中...";
    [self.view addSubview:self.webView];
    [self.view addSubview:self.progressView];
    
}


#pragma mark - webBridge

- (void)webBridge {
    
    //init
    
    self.bridge = [WKWebViewJavascriptBridge bridgeForWebView:self.webView webViewDelegate:self handler:^(id data, WVJBResponseCallback responseCallback) {
        
        NSLog(@"1:Message from web!");
        
        responseCallback(@"Response from Objc");
    }];
    
    
    //regist
    
    [self.bridge registerHandler:@"shareCallback" handler:^(id data, WVJBResponseCallback responseCallback) {
        
        NSLog(@"2:Message from web---%@",data);
        
        
        self.data = data;
        
        [self showShareList:data];
        
        responseCallback(@"Response from shareCallback");
        
    }];
    
}



#pragma mark - load
- (void)loadPage {
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:WEB_URL] cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:kWebviewTimeOut];
    [_webView loadRequest:request];

}



#pragma mark - Event response

- (void)back:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)share:(id)sender
{
    
}



#pragma mark - show share list
- (void)showShareList:(id)data
{
    if ([data isKindOfClass:[NSDictionary class]]) {
        
        NSString *shareText = data[@"desc"];
        
        
        
        [UMSocialWechatHandler setWXAppId:WXKEY appSecret:WXSEC url:data[@"link"]];
        [UMSocialData defaultData].extConfig.wechatSessionData.title = data[@"title"];
        [UMSocialData defaultData].extConfig.wechatTimelineData.title = data[@"title"];

        UMSocialUrlResource *resource = [UMSocialUrlResource new];
        resource.resourceType = UMSocialUrlResourceTypeImage;
        resource.url = data[@"imgUrl"];

        
        
        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"分享至" message:nil preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *friend = [UIAlertAction actionWithTitle:@"微信好友" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            
            [[UMSocialDataService defaultDataService]  postSNSWithTypes:@[UMShareToWechatSession] content:shareText image:nil location:nil urlResource:resource presentedController:self completion:^(UMSocialResponseEntity *response){
                
                if (response.responseCode == UMSResponseCodeSuccess) {
                    
                }
            }];

        }];
        
        UIAlertAction *session = [UIAlertAction actionWithTitle:@"微信朋友圈" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            
            [[UMSocialDataService defaultDataService]  postSNSWithTypes:@[UMShareToWechatTimeline] content:shareText image:nil location:nil urlResource:resource presentedController:self completion:^(UMSocialResponseEntity *response){
                
                if (response.responseCode == UMSResponseCodeSuccess) {
                    
                }
            }];

        }];

        [alertController addAction:friend];
        [alertController addAction:session];
        
        
        [self presentViewController:alertController animated:YES completion:nil];

    }
    
}

#pragma mark - WKNavigationDelegate

- (void)webView:(WKWebView *)webView didStartProvisionalNavigation:(WKNavigation *)navigation {
}

- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {
    
    [UIView animateWithDuration:0.3 delay:0 options:UIViewAnimationOptionCurveLinear animations:^{
        
        [self.progressView setProgress:1 animated:YES];
        
    } completion:^(BOOL finished) {
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.progressView.alpha = 0;
            self.title = self.webView.title;
        });
    }];
    
}


- (void)webView:(WKWebView *)webView didFailProvisionalNavigation:(WKNavigation *)navigation withError:(NSError *)error {
    //error tips
    NSLog(@"error=%@",error.localizedDescription);
    
}


- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
    
    decisionHandler(WKNavigationActionPolicyAllow);
    
}


#pragma mark - Private

- (void)p_clean {
    
    [self.webView stopLoading];
    self.webView.navigationDelegate = nil;
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    [self.webView removeObserver:self forKeyPath:@"loading"];
    [self.webView removeObserver:self forKeyPath:@"estimatedProgress"];
    self.webView = nil;
    
}


#pragma mark - KVO

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    
    if ([keyPath isEqualToString:@"loading"]) {
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:self.webView.loading];
    } else if ([keyPath isEqualToString:@"estimatedProgress"]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.progressView setProgress:self.webView.estimatedProgress animated:YES];
        });
    }
    
}


#pragma mark - Getter

- (WKWebView *)webView {
    
    if (!_webView) {
        _webView = [[WKWebView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT-self.navigationController.navigationBar.bounds.size.height)];
        _webView.navigationDelegate = self;
        [_webView addObserver:self forKeyPath:@"loading" options:NSKeyValueObservingOptionNew context:nil];
        [_webView addObserver:self forKeyPath:@"estimatedProgress" options:NSKeyValueObservingOptionNew context:nil]; //kvo监听
    }
    return _webView;
    
}

- (UIProgressView *)progressView {
    
    if (!_progressView) {
        _progressView = [[UIProgressView alloc] initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, 3)];
        _progressView.trackTintColor = [UIColor whiteColor];
    }
    return _progressView;
    
}


@end
