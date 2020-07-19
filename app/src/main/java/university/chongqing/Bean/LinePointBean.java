package university.chongqing.Bean;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

//线路节点，比如有三个设备安装在一条线路上，那么当前bean信息包含了线路节点（一般而言，是杆塔名称）和A项，B项，C项
public class LinePointBean implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<String>{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public List<String> getmList() {
        return mList;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    List<String> mList;
    @Override
    public int getChildCount() {
        return mList.size();
    }

    @Override
    public String getChildAt(int childIndex) {
        return mList.size() <= childIndex ? null : mList.get(childIndex);
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }
}
