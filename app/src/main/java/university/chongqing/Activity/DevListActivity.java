package university.chongqing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import university.chongqing.Adapter.BaseAdapter;
import university.chongqing.Adapter.DevAdapter;
import university.chongqing.model.ResponseModel;
import university.chongqing.model.WDeviceModel;
import university.chongqing.util.Config;
import university.chongqing.util.GsonUtil;

public class DevListActivity extends AppCompatActivity implements BaseAdapter.OnItemClickListener {

    int uid;
    DevAdapter adapter;
    RecyclerView rv ;
    List<WDeviceModel> deviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_list);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//        {
//            setCustomTitle(getString(R.string.string_devlist), false);
//        }

        if (actionBar != null)
        {
            // setCustomTitle(devName, true);
            actionBar.setTitle(getString(R.string.string_devlist));
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        initView();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            uid = bundle.getInt("uid",0);
            loadDevList();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish(); // back button
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void initView(){
        rv = findViewById(R.id.device_list_view);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


    public void loadDevList() {
        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uid", ""+uid)
                .build();
        final Request request = new Request.Builder()
                .url(Config.serverUrl+"device_list.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(DevListActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
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
                            String body = new Gson().toJson(model.data);
                            List<WDeviceModel> lists = GsonUtil.parseJsonArrayWithGson(body, WDeviceModel[].class);
                            if (lists != null){
                                deviceList = lists;
                                if (adapter == null){
                                    adapter = new DevAdapter(lists);
                                    adapter.setOnItemClickListener(DevListActivity.this);
                                    rv.setAdapter(adapter);
                                }
                            }
                        }

                    }
                });
            }
        });
    }
    @Override
    public void onLongClick(View view, int position) {

    }
    @Override
    public void onItemClick(View view, int position) {

        WDeviceModel model = deviceList.get(position);

//            String devid = model.devid;
//            Intent intent = new Intent(DevListActivity.this, ChartDetailActivety.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("devid", devid);
//            bundle.putString("devName", model.nme);
//            intent.putExtras(bundle);
//            startActivity(intent);

        Intent intent = new Intent(this,DevDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(param1,model.nme);
        bundle.putString(param2, model.devid);
        intent.putExtras(bundle);
        startActivity(intent);


    }



    LoadingDialog lod;
    static  final  String param1 = "devname";
    static  final  String param2 = "devid";


}
