package university.chongqing.Adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import university.chongqing.Activity.R;
import university.chongqing.Bean.DevisionDetailBean;
import university.chongqing.Bean.EnvItemBean;

public class DevStatusAdapter extends RecyclerView.Adapter<university.chongqing.Adapter.BaseHolder> implements CompoundButton.OnCheckedChangeListener {
    List<DevisionDetailBean> mDivisionList;
    List<EnvItemBean> mEnvList ;

    public DevStatusAdapter(List<DevisionDetailBean> mDivisionList, List<EnvItemBean> mEnvList) {
        this.mDivisionList = mDivisionList;
        this.mEnvList = mEnvList;
    }
    public void reLoadData(List<DevisionDetailBean> mDivisionList, List<EnvItemBean> mEnvList){
        this.mDivisionList = mDivisionList;
        this.mEnvList = mEnvList;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder;
        if (0 == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_devision_header, parent, false);
            holder = new BaseHolder(view);
        }
        else  if (1 == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_devision_item, parent, false);
            holder = new BaseHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_env, parent, false);
            holder = new BaseHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        if (holder instanceof  BaseHolder){
            if (position >=1 && position <=4){
                final int index = position -1;
                DevisionDetailBean bean = mDivisionList.get(index);
                TextView text_title = holder.getView(R.id.text_title);
                TextView text_elec_current = holder.getView(R.id.text_elec_current);
                TextView text_history = holder.getView(R.id.text_history);


                text_history.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                text_history.getPaint().setAntiAlias(true);//抗锯齿
                text_history.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mHistoryClickListener.onHistoryClick(view,index);
                    }
                });

                text_title.setText(bean.getName());
                text_elec_current.setText(bean.getElecCurrent());

                CheckBox checkbox = holder.getView(R.id.checkbox);
                checkbox.setOnCheckedChangeListener(this);
                checkbox.setTag(""+(position-1));
               // text_history.setText(bean.getName());
            }
            else if (position >mDivisionList.size() ){
                final int index = position -1-mDivisionList.size();
                EnvItemBean bean =  mEnvList.get(index);
                TextView text_title = holder.getView(R.id.text_title);
                TextView text_detail = holder.getView(R.id.text_detail);
                text_title.setText(bean.getName());
                text_detail.setText(bean.getDetail());
                ((BaseHolder) holder).itemView.setOnClickListener(
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                mItemClickListener.onItemClickListener(v,index);

                            }
                        });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position){
            return 0;
        }
        else if(position<=mDivisionList.size())
            return  1;
        else
            return  2;
    }

    @Override
    public int getItemCount() {
        return 1+mDivisionList.size()+mEnvList.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String tag = (String) compoundButton.getTag();
        int tagid = Integer.parseInt(tag);
        if (mCheckBoxClickListener!= null){
            mCheckBoxClickListener.onCheckBoxClick(tagid,b);
        }
    }

    public interface OnHistoryClickListener
    {
        //条目被点击时触发的回调
        //tpe:0-item被点击 1-share被点击 2-回放被点击 3-设置被点击
        void onHistoryClick(View view, int position);


    }
    public interface OnItemClickListener
    {
        //条目被点击时触发的回调
        //tpe:0-item被点击 1-share被点击 2-回放被点击 3-设置被点击
        void onItemClickListener(View view, int position);


    }
    public interface OnIceSetClickListener
    {
        //条目被点击时触发的回调
        void onIceSetClick(View view, int position);


    }
    public interface OnCheckBoxClickListener
    {
        //条目被点击时触发的回调
        //tpe:0-item被点击 1-share被点击 2-回放被点击 3-设置被点击
        void onCheckBoxClick(int position,boolean on);


    }

    public void setCheckBoxClickListener(OnCheckBoxClickListener clickListener)
    {
        this.mCheckBoxClickListener = clickListener;
    }
    public void setOnHistoryClickListener(OnHistoryClickListener clickListener)
    {
        this.mHistoryClickListener = clickListener;
    }
    public void setOnItemClickListener(OnItemClickListener itemClickListener)
    {
        this.mItemClickListener = itemClickListener;
    }


    OnItemClickListener mItemClickListener;
    OnHistoryClickListener mHistoryClickListener;
    OnCheckBoxClickListener mCheckBoxClickListener;
}
