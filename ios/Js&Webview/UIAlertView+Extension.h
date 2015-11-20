//
//  UIAlertView+Extension.h
//  Easywork
//
//  Created by Kingxl on 11/4/14.
//  Copyright (c) 2014 Jin. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef  void(^FinishBlock)(NSInteger buttonIndex);

@interface UIAlertView (Extension)

- (void)showAlertWithBlock:(FinishBlock)block;

@end
