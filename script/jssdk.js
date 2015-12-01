/**
 * Created by aidenZou on 15/11/24.
 */
!function (w) {
    //IOS
    function connectWebViewJavascriptBridge(callback) {
        if (window.WebViewJavascriptBridge) {
            callback(WebViewJavascriptBridge)
        } else {
            document.addEventListener('WebViewJavascriptBridgeReady', function () {
                callback(WebViewJavascriptBridge)
            }, false)
        }
    }

    connectWebViewJavascriptBridge(function (bridge) {
        bridge.init(function (message, responseCallback) {
            log('JS got a message', message)
            var data = {'Javascript Responds': 'Wee!'}
            log('JS responding with', data)
            responseCallback(data)
        })
        bridge.registerHandler('testJavascriptHandler', function (data, responseCallback) {
            log('ObjC called testJavascriptHandler with', data)
            var responseData = {'Javascript Says': 'Right back atcha!'}
            log('JS responding with', responseData)
            responseCallback(responseData)
        })

        window.bridge = bridge;
        bridgeInit(bridge);
        log('IOS bridge 注入完毕')
    })

    //Android
    //function connectWebViewJavascriptBridge4Android() {
    //    if (window.bridge) {
    //        alert('bridge')
    //        bridgeInit(bridge);
    //    } else {
    //        document.addEventListener('WebViewJavascriptBridgeReady4Android', function () {
    //            alert('WebViewJavascriptBridgeReady4Android 触发')
    //            bridgeInit(bridge);
    //        }, false)
    //    }
    //}
    //connectWebViewJavascriptBridge4Android();

    w.bridgeSDK = {
        /**
         * 分享
         * @param data(暂时只支持前面4个参数)
         * {
         * title: '', // 分享标题
         * desc: '', // 分享描述
         * link: '', // 分享链接
         * imgUrl: '', // 分享图标
         *
         * type: '', // 分享类型,music、video或link，不填默认为link
         * dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
         * success: function () {
         * // 用户确认分享后执行的回调函数
         * },
         * cancel: function () {
         * // 用户取消分享后执行的回调函数
         * }
         */
        share: function (data) {
            //var eventKey = 'shareCallback';
            var eventKey = 'onShare';
            var _data = {
                'title': data.title,    // 分享标题
                'desc': data.desc,      // 分享描述
                'link': data.link,      // 分享链接
                'imgUrl': data.imgUrl   // 分享图标
            };

            //IOS
            if (app.browser.ios) {
                bridge.callHandler(eventKey, _data, function (response) {    //TODO Ios 采用 callback 方式
                    //alert(response)
                    //alert(response.key)
                    log('Ios分享回调', response)
                    //data.success(response);
                })
            } else if (app.browser.android) {    //Android
                var dataStr = JSON.stringify(_data);
                bridge.callHandler(eventKey, dataStr);

                //TODO Android采用 return 方式
                var result = bridge.callHandler(eventKey, dataStr);
                log('Adroid分享回调', result);
                //data.success(result);
            } else {    //微信 等

            }
        }
    };
}(window);
