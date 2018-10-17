# RadarStatisticsView
一个简单精美的雷达数据统计图
![雷达数据统计数](https://github.com/Grrsun/RadarStatisticsView/blob/master/images/RadarStatisticsView.png)

### Useage
#### 1、引入依赖
(```)
        implementation 'com.jersay.android:radarstatisticsview:1.0.0
(```)
#### 2、在XML中添加view
(```)
        <com.zxm.radarstatisticsview.RadarStatisticsView
        android:id="@+id/radar_statistics_view"
        android:layout_width="match_parent"
        android:layout_height="300dp" />
(```)
#### 3、设置数据和属性
(```)
        String[] titles = {"小米", "华为", "苹果", "三星", "OPPO"};
        double[] data = {45, 80, 25, 10, 60};
        statisticsView.setTitles(titles);
        statisticsView.setData(data);
        statisticsView.setMainPaintColor("#3C3F41");
        statisticsView.setValuePaintColor("#B0F566");
        statisticsView.setMaxValue(80);
        statisticsView.setTitlePaintColor("#EB5D0F");
        statisticsView.setTitleSize(28);
        statisticsView.setMainPaintStrokeWidth(2);
(```)


