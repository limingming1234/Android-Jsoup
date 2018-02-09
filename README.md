# Android-Jsoup
2018/2/6
一款Android阅读app，运用Jsoup抓取网站内容，Picasso加载图片，基于sqlite数据库实现历史记录查询和收藏功能。
1.0版本：Jsoup抓取网站内容：知乎日报，果壳，简书，网易创业CLUB。
        利用sqlite数据库实现收藏文章和查询历史记录功能，让历史记录保持15条和不断更新。
        app安装包在Jsoup/app/release目录下。
第一次上传android app项目，如有问题恳请批评指正。
未来修改的方向：
1.UI不够漂亮，有时间再改。
2.没有实现翻页功能，按钮更新太死板。
3.可以添加用户系统及私信功能。
2018/2/7
使用开源项目LeakCanary查找内存泄漏问题，发现在activity销毁时数据库对象没有关闭，通过重写onDestroy()方法解决。千里之堤毁于蚁穴，今后编程中需注意。
2018/2/8
修复历史记录更新bug，优化代码。
2018/2/9
1.SwipeRefreshLayout+TabLayout+ViewPager+Fragment实现滑动翻页和下拉刷新功能。
