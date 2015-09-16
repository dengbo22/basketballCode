# basketballCode

2015.8.20
添加StickyHeaderListView
添加PullToRefreshStickyListView —— 完成StickyHeaderListView与PullToRefresh结合功能

2015.8.22
添加PullToRefreshListView
完成PullToRefresh下载功能以及图片加载功能

2015.8.23
添加SQLite数据库用于缓存上一次最新的数据
在无网络连接的情况下延迟一秒调用onRefreshComplete()修正了HeaderView不消失的BUG

2015.8.24
直播、赛事页面SQLite数据添加完成
修改直播页面的请求逻辑以及赛事页面的请求逻辑

2015.8.25
创建LiveDetailActivity，添加加载内容
修改CompetitionFragment中的Header逻辑从CheckBox到RadioGroup

2015.8.26
修改LiveDetailActivity的TopView，创建CheckBox的响应逻辑
添加从LiveFragment到LiveDetailFragment的数据请求以及Intent返回

2015.8.31
修改LiveFragment的逻辑，删除原用的StickyHeaderListView+PullToRefresh
改用Ultra-Pull-To-Refresh框架结构

2015.9.5
创建StatisticsFragment,使用RecyclerView创建
修改对应的Adapter逻辑
修复LiveDetailActivity的返回导致数据增加的BUG

2015.9.6
添加微信登陆

2015.9.7
添加底部弹出窗口给出分享提示
添加微信分享、好友分享功能

2015.9.9
添加赛事内部的球队以及战报页面
添加直播内部的各个页面的为NULL图像

2015.9.11
修改评论页面逻辑，完成Online功能

2015.9.12
添加ScheduleFragment页面，TeamFragment与服务器同步完成

2015.9.14
添加短信注册模块并完成注册流程

2015.9.15
添加注册页面以及修改密码页面并完成交互响应
修改登陆页面逻辑，使得可以在任意页面启动并解耦合

2015.9.16
修改支持球队的逻辑，完成Online
添加Bugly的App跟踪反馈
