package university.chongqing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;
import com.hgdendi.expandablerecycleradapter.ViewProducer;

import java.util.ArrayList;
import java.util.List;

import university.chongqing.Adapter.LinePointAdapter;
import university.chongqing.Bean.LinePointBean;

public class LinePointActivity extends BaseAppCompatActivity implements BaseExpandableRecyclerViewAdapter.ExpandableRecyclerViewOnClickListener<LinePointBean, String> {

    RecyclerView mListView;
    List<LinePointBean> mList=new ArrayList<LinePointBean>();
    LinePointAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_point);
        setCustomTitle(getString(R.string.devlist),false);

        mListView = findViewById(R.id.recyler_lineLevel);
        mListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mListView.setLayoutManager(new LinearLayoutManager(this));
        initValue();
    }
    void initValue(){
        LinePointBean bean = new  LinePointBean();
        bean.setName(getString(R.string.linex1point));
        List<String> items = new ArrayList<String>();
        items.add(getString(R.string.A));
        items.add(getString(R.string.B));
        items.add(getString(R.string.C));
        bean.setmList(items);
        mList.add(bean);

        LinePointBean bean1 = new  LinePointBean();
        bean1.setName(getString(R.string.linex2point));
        List<String> items1 = new ArrayList<String>();
        items1.add(getString(R.string.A));
        items1.add(getString(R.string.B));
        items1.add(getString(R.string.C));
        bean1.setmList(items1);
        mList.add(bean1);

        mAdapter =new LinePointAdapter(mList);
        mAdapter.setEmptyViewProducer(new ViewProducer() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                return new DefaultEmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.empty, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder) {

            }
        });
        mListView.setAdapter(mAdapter);
        mAdapter.setListener(this);

    }

    @Override
    public boolean onGroupLongClicked(LinePointBean groupItem) {
        return false;
    }

    @Override
    public boolean onInterceptGroupExpandEvent(LinePointBean groupItem, boolean isExpand) {
        return false;
    }

    @Override
    public void onGroupClicked(LinePointBean groupItem) {

    }

    @Override
    public void onChildClicked(LinePointBean groupItem, String childItem) {
        Intent intent = new Intent(this,DevDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(param1,groupItem.getName()+childItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    static  final  String param1 = "param1";
}
