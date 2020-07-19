package university.chongqing.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import university.chongqing.View.MyLineChart;
import university.chongqing.View.marker.DetailsMarkerView;
import university.chongqing.View.marker.DetailsTiltMarkerView;
import university.chongqing.View.marker.PositionMarker;
import university.chongqing.View.marker.RoundMarker;
import university.chongqing.model.ICEDataModel;
import university.chongqing.model.ResponseModel;
import university.chongqing.util.Config;
import university.chongqing.util.GsonUtil;

public class DivideChannelDetailActivity extends BaseAppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {
    static  final  String param2 = "devid";
    static  final  String param3 = "detail";
    static  final  String param4 = "key";
    MyLineChart chart_view_elec_current ;
    MyLineChart chart_view_temp;
    List<String> xList = new ArrayList<>();
    List<Integer> yElecList = new ArrayList<>();
    List<Float> yTempList = new ArrayList<>();
    String key;
    String devid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divide_channel_detail);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            devid = bundle.getString(param2);
            setCustomTitle(bundle.getString(param3),true);
            key = bundle.getString(param4);
        }
        initChartView();
        initTempChartView();
        initValue();




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void initValue(){
//        xList.add("0");
//        xList.add("3");
//        xList.add("6");
//        xList.add("9");
//        xList.add("12");
//        xList.add("15");
//        xList.add("18");
//        xList.add("21");
//
//        yElecList.add(200);
//        yElecList.add(201);
//        yElecList.add(199);
//        yElecList.add(198);
//        yElecList.add(200);
//        yElecList.add(202);
//        yElecList.add(203);
//        yElecList.add(204);



//        loadDataForChartElecView();
        //loadDataForTempChartView();

        loadHistoryData();
    }

    //加载历史数据
    //type 0：依次获取，type-1：每小时 type-2：每天
    void loadHistoryData(){
        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("did", devid)
                .add("tpe", ""+1)
                .build();

        final Request request = new Request.Builder()
                .url(Config.serverUrl+"sensor_data_13.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lod.dismiss();
                        Toast.makeText(DivideChannelDetailActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                final ResponseModel model  = GsonUtil.parseJsonWithGson(res,ResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lod.dismiss();
                        if (model != null && model.error_code==0){
                            String body = new Gson().toJson(model.data);
                            Log.e(tag,"HistoryData body is :"+body);
                            try {
                                List<ICEDataModel> lists = GsonUtil.parseJsonArrayWithGson(body, ICEDataModel[].class);
                                if (lists.size()>0){
                                    //reloadValue(lists.get(0));
                                    parseHistroyData(lists);
                                }

                                Log.e(tag,res);
                            }
                            catch (Exception e){
                                Log.e(tag,"Exception is :"+e.getLocalizedMessage());
                            }
                        }
                    }
                });
            }
        });


    }
    void parseHistroyData(List<ICEDataModel> lists){
        int size = lists.size();
        if(size>0){
            xList.clear();
            yElecList.clear();
            for(int i=size-1;i>=0;i--){
                ICEDataModel model = lists.get(i);

                xList.add(model.date);

                if (key.equals("a1")){
                    yElecList.add(model.a1);
                }
                else if(key.equals("a2")){
                    yElecList.add(model.a2);
                }
                else if(key.equals("a3")){
                    yElecList.add(model.a3);
                }
                else if(key.equals("a4")){
                    yElecList.add(model.a4);
                }

            }
            loadDataForChartElecView();


        }
    }

    /********************************setup chartView 电流***************************************/
    void initChartView(){
        chart_view_elec_current  = findViewById(R.id.chart_view_elec_current);
        chart_view_elec_current.setOnChartGestureListener(this);
        chart_view_elec_current.setOnChartValueSelectedListener(this);
        chart_view_elec_current.setDrawGridBackground(false);
        // 无描述文本
        chart_view_elec_current.getDescription().setEnabled(false);

        // 使能点击
        chart_view_elec_current.setTouchEnabled(true);

        // 使能拖动和缩放
        chart_view_elec_current.setDragEnabled(true);
        chart_view_elec_current.setScaleEnabled(true);

        // 如果为false，则x，y两个方向可分别缩放
        chart_view_elec_current.setPinchZoom(true);

        LimitLine ll1 = new LimitLine(50f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(-10f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        //设置x轴位置
        XAxis xAxis = chart_view_elec_current.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);




        //去除右边的y轴
        YAxis yAxisRight = chart_view_elec_current.getAxisRight();
        yAxisRight.setEnabled(false);



    }

    void loadDataForChartElecView(){
        //设置X轴值为字符串

        XAxis xAxis = chart_view_elec_current.getXAxis();



        xAxis.setValueFormatter(new IndexAxisValueFormatter(xList));

        //创建覆盖物
        createMakerView();

        LineData data = getData();
        if (data != null){
            chart_view_elec_current.setData(data);
            chart_view_elec_current.animateX(2000);
        }

    }

    private LineData getData(){

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

            ArrayList<Entry> yVals = new ArrayList<Entry>();


           for (int i=0;i<yElecList.size();++i){
               yVals.add(new Entry(i,yElecList.get(i)));
           }

            LineDataSet set =new LineDataSet(yVals, getString(R.string.eleccurrent)+"(A)");

            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线为圆滑的线
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);  //设置有圆点
            set.setLineWidth(2f);    //设置线的宽度
            int  color=  Color.rgb(255,100, 255);

            set.setColor(color);
            set.setCircleColor(color);
            dataSets.add(set); // add the datasets


        LineData data = new LineData(dataSets);
        return data;
    }

    /********************************setup chartView 温度***************************************/
    void initTempChartView(){
        chart_view_temp  = findViewById(R.id.chart_view_temp);
        chart_view_temp.setOnChartGestureListener(this);
        chart_view_temp.setOnChartValueSelectedListener(this);
        chart_view_temp.setDrawGridBackground(false);
        // 无描述文本
        chart_view_temp.getDescription().setEnabled(false);

        // 使能点击
        chart_view_temp.setTouchEnabled(true);

        // 使能拖动和缩放
        chart_view_temp.setDragEnabled(true);
        chart_view_temp.setScaleEnabled(true);

        // 如果为false，则x，y两个方向可分别缩放
        chart_view_temp.setPinchZoom(true);

        LimitLine ll1 = new LimitLine(50f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(-10f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        //设置x轴位置
        XAxis xAxis = chart_view_temp.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);




        //去除右边的y轴
        YAxis yAxisRight = chart_view_temp.getAxisRight();
        yAxisRight.setEnabled(false);



    }

    void loadDataForTempChartView(){
        //设置X轴值为字符串

        XAxis xAxis = chart_view_temp.getXAxis();



        xAxis.setValueFormatter(new IndexAxisValueFormatter(xList));

        //创建覆盖物
        createTempMakerView();

        LineData data = getTempData();
        if (data != null){
            chart_view_temp.setData(data);
            chart_view_temp.animateX(2000);
        }

    }

    private LineData getTempData(){

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        ArrayList<Entry> yVals = new ArrayList<Entry>();


        for (int i=0;i<yElecList.size();++i){
            yVals.add(new Entry(i,yTempList.get(i)));
        }

        LineDataSet set =new LineDataSet(yVals, getString(R.string.linetemp)+"(℃)");

        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线为圆滑的线
        set.setCubicIntensity(0.2f);
        set.setDrawCircles(true);  //设置有圆点
        set.setLineWidth(2f);    //设置线的宽度

        int  color=  Color.rgb(255,100, 255);

        set.setColor(color);
        set.setCircleColor(color);
        dataSets.add(set); // add the datasets


        LineData data = new LineData(dataSets);
        return data;
    }
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (chart_view_elec_current.isMarkerAllNull()) {
            //重新绑定覆盖物
            createMakerView();
            //并且手动高亮覆盖物
            chart_view_elec_current.highlightValue(h);
        }
//        if (chart_view_temp.isMarkerAllNull()) {
//            //重新绑定覆盖物
//            createTempMakerView();
//            //并且手动高亮覆盖物
//            chart_view_temp.highlightValue(h);
//        }

    }

    @Override
    public void onNothingSelected() {

    }
    /**
     * 创建覆盖物
     */
    public void createMakerView() {
        DetailsTiltMarkerView detailsMarkerView = new DetailsTiltMarkerView(this);
        detailsMarkerView.setxValues(xList);
        detailsMarkerView.setChartView(chart_view_elec_current);
        chart_view_elec_current.setDetailsMarkerView(detailsMarkerView);
        chart_view_elec_current.setPositionMarker(new PositionMarker(this));
        chart_view_elec_current.setRoundMarker(new RoundMarker(this));
    }
    /**
     * 创建覆盖物
     */
    public void createTempMakerView() {
        DetailsMarkerView detailsMarkerView = new DetailsMarkerView(this);
        detailsMarkerView.setxValues(xList);
        detailsMarkerView.setChartView(chart_view_temp);
        chart_view_temp.setDetailsMarkerView(detailsMarkerView);
        chart_view_temp.setPositionMarker(new PositionMarker(this));
        chart_view_temp.setRoundMarker(new RoundMarker(this));
    }

    LoadingDialog lod;
    final  static  String tag = "chartview";

}
