//
//  MainViewController.m
//  Js&Webview
//
//  Created by Kim on 11/19/15.
//  Copyright © 2015 Kim. All rights reserved.
//

#import "MainViewController.h"

static NSString *const CellIdentifier = @"CELL";

@interface MainViewController ()
@property (nonatomic ,strong) NSArray *dataSource;
@end

@implementation MainViewController


- (instancetype)init
{
    if (self = [super init]) {
        self.title = @"UIWebView&JavaScript";
        _dataSource = @[@"新闻分享"];
    }
    return self;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:CellIdentifier];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSource.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 50.0f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    
    cell.textLabel.text = _dataSource[indexPath.row];
    cell.textLabel.font = [UIFont systemFontOfSize:18];
    
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    [self.navigationController pushViewController:[NSClassFromString(@"DetailViewController") new] animated:YES];
    
}


@end
