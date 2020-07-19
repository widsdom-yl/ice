package university.chongqing.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

import university.chongqing.Activity.R;
import university.chongqing.Bean.LinePointBean;

//融冰装置的列表adapter,包含了融冰装置的线路杆塔名称，以及 A B C相导线
public class LinePointAdapter extends
        BaseExpandableRecyclerViewAdapter<LinePointBean,String,LinePointAdapter.GroupVH,LinePointAdapter.ChildVH>{
    private List<LinePointBean> mList;
    public LinePointAdapter(List<LinePointBean> list) {
        mList = list;
    }
    public void resetMList(List<LinePointBean> list){
        if (list != null){
            mList = list;
            this.notifyDataSetChanged();
        }
    }


    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public LinePointBean getGroupItem(int groupIndex) {
        return mList.get(groupIndex);
    }

    @Override
    public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
        return new GroupVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_line_point, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(GroupVH holder, LinePointBean groupBean, boolean isExpand) {
        holder.view_title.setText(groupBean.getName());

    }

    @Override
    public ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
        return new ChildVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adatper_line_item, parent, false));
    }

    @Override
    public void onBindChildViewHolder(ChildVH holder, LinePointBean groupBean, String s) {
        holder.text_linename.setText(s);
    }

    static class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder {
        ImageView foldIv;
        TextView view_title;


        GroupVH(View itemView) {
            super(itemView);
            // foldIv = (ImageView) itemView.findViewById(R.id.group_item_indicator);
            view_title = itemView.findViewById(R.id.text_title);
        }

        @Override
        protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
            //foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        }
    }

    static class ChildVH extends RecyclerView.ViewHolder {
        TextView text_linename;

        ChildVH(View itemView) {
            super(itemView);
            text_linename = (TextView) itemView.findViewById(R.id.text_title);
        }
    }
}
