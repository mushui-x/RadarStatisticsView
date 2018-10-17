package com.jersay.radarstatistics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jersay.radarstatisticsview.RadarStatisticsView;


public class MainActivity extends AppCompatActivity {

    private RadarStatisticsView statisticsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        statisticsView = findViewById(R.id.radar_statistics_view);
    }

    private void initData() {
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
    }
}
