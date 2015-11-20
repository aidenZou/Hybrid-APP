# IOS 说明

1 Webview绑定

    self.bridge = [WKWebViewJavascriptBridge bridgeForWebView:self.webView webViewDelegate:self handler:^(id data, WVJBResponseCallback responseCallback) {
        
        NSLog(@"1:Message from web!");
        
        responseCallback(@"Response from Objc");
    }];


2 注册事件

    
    [self.bridge registerHandler:@"shareCallback" handler:^(id data, WVJBResponseCallback responseCallback) {
        
        NSLog(@"2:Message from web---%@",data);
        
        
        self.data = data;
        
        [self showShareList:data];
        
        responseCallback(@"Response from shareCallback");
        
    }];
