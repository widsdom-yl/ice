package university.chongqing.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import university.chongqing.Adapter.DevStatusAdapter;
import university.chongqing.Bean.DevisionDetailBean;
import university.chongqing.Bean.EnvItemBean;
import university.chongqing.model.ICEDataModel;
import university.chongqing.model.ResponseModel;
import university.chongqing.util.Config;
import university.chongqing.util.GsonUtil;
//融冰装置详细信息，包含每个分裂的状态信息，每个分裂的状态信息为实时电流，导线温度，融冰状态
public class DevDetailActivity extends BaseAppCompatActivity implements DevStatusAdapter.OnHistoryClickListener, DevStatusAdapter.OnCheckBoxClickListener, View.OnClickListener {

    static  final  String param1 = "devname";
    static  final  String param2 = "devid";
    static  final  String param3 = "detail";
    static  final  String param4 = "key";
    List<DevisionDetailBean> mDivisionList = new ArrayList<DevisionDetailBean>();
    List<EnvItemBean> mEnvList = new ArrayList<EnvItemBean>();
    RecyclerView mListView;
    DevStatusAdapter mAdapter;
    Button button_ice_thick;
    String title;
    String devid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_detail);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            title = bundle.getString(param1);
            devid = bundle.getString(param2);
            setCustomTitle(title,true);
        }

        mListView = findViewById(R.id.list_detail);
        button_ice_thick = findViewById(R.id.button_ice_thick);
        button_ice_thick.setOnClickListener(this);
        mListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mListView.setLayoutManager(new LinearLayoutManager(this));
        loadHistoryData();





    }
    void  reloadValue(ICEDataModel model){
        mDivisionList.clear();
        mEnvList.clear();
        DevisionDetailBean bean1 = new DevisionDetailBean(getString(R.string.division1),model.getA1()+"A","20℃");
        DevisionDetailBean bean2 = new DevisionDetailBean(getString(R.string.division2),model.getA2()+"A","21℃");
        DevisionDetailBean bean3 = new DevisionDetailBean(getString(R.string.division3),model.getA3()+"A","19℃");
        DevisionDetailBean bean4 = new DevisionDetailBean(getString(R.string.division4),model.getA4()+"A","20.5℃");

        mDivisionList.add(bean1);
        mDivisionList.add(bean2);
        mDivisionList.add(bean3);
        mDivisionList.add(bean4);
        EnvItemBean item1 = new EnvItemBean(getString(R.string.totalcurrent),model.getAllCurrent()+"A");
        EnvItemBean item2 = new EnvItemBean(getString(R.string.envtemp),model.getTemp()+"℃");
        EnvItemBean item3 = new EnvItemBean(getString(R.string.envhumidity),model.getHumidity()+"%");
        EnvItemBean item4 = new EnvItemBean(getString(R.string.icethick),model.icethickness+"mm");
        EnvItemBean item5 = new EnvItemBean(getString(R.string.battery),model.getBattery()+"V");
        EnvItemBean item6 = new EnvItemBean(getString(R.string.date),model.date);
        mEnvList.add(item1);
        mEnvList.add(item2);
        mEnvList.add(item3);
        mEnvList.add(item4);
        mEnvList.add(item5);
        mEnvList.add(item6);
        if(mAdapter == null){
            mAdapter = new DevStatusAdapter(mDivisionList,mEnvList);
            mListView.setAdapter(mAdapter);
            mAdapter.setOnHistoryClickListener(this);
            mAdapter.setCheckBoxClickListener(this);


        }
        else{
            mAdapter.reLoadData(mDivisionList,mEnvList);
            mAdapter.notifyDataSetChanged();
        }




    }
    @Override
    public void onDestroy(){
        super.onDestroy();

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



    /******************************************************************************************/
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
                .add("tpe", ""+0)
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
                        Toast.makeText(DevDetailActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
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
                                    reloadValue(lists.get(0));
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


    @Override
    public void onHistoryClick(View view, int position) {
        Intent intent = new Intent(this,DivideChannelDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(param2,devid);
        bundle.putString(param3,mDivisionList.get(position).getName()+"-"+getString(R.string.eleccurrent));
        switch (position){
            case 0:
                bundle.putString(param4,"a1");
                break;
            case 1:
                bundle.putString(param4,"a2");
                break;
            case 2:
                bundle.putString(param4,"a3");
                break;
            case 3:
                bundle.putString(param4,"a4");
                break;
            default:
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }




    @Override
    public void onCheckBoxClick(final int position, final boolean on) {



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("")
                .setMessage("控制当前状态？")
                .setPositiveButton("确认",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CmdBean cmd = new CmdBean();
                        cmd.devid=devid;
                        cmd.position = position;
                        cmd.on = on?1:0;
                        sndCmd(cmd);


                    }
                })
                .setNegativeButton("取消", null).
                show();


    }

    public void sndCmd(CmdBean cmd) {
        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(Config.serverUrl+"cmd_ice.php?did="+cmd.devid+"&pos="+cmd.position+"&on="+cmd.on)
                .get()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(DevDetailActivity.this, "control Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        lod.dismiss();
                        final ResponseModel model  = GsonUtil.parseJsonWithGson(res,ResponseModel.class);
                        if (model.error_code == 0){
                            Toast.makeText(DevDetailActivity.this, "Cmd sent!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        final EditText inputServer = new EditText(this);
        inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.string_ice_thick_set)).setView(inputServer)
                .setNegativeButton(getString(R.string.string_cancel), null);
        builder.setPositiveButton(R.string.string_set, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.e( tag,inputServer.getText().toString());
                int number = Integer.parseInt(inputServer.getText().toString()) ;
                if(number > 0){
                    CmdBean cmd = new CmdBean();
                    cmd.devid=devid;
                    cmd.position = -1;
                    cmd.on = number;
                    sndCmd(cmd);
                }
            }
        });
        builder.show();
    }

    public class CmdBean {
        String  devid;
        int position;
        int on;
        CmdBean(){
            devid = "123456";
        }
        public String toString() {
            JSONObject object = new JSONObject();
            try {
                object.put("position", position);
                object.put("on", on);
                return object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

    }
    LoadingDialog lod;
    final static  String tag = "DevDetailActivity";
}
