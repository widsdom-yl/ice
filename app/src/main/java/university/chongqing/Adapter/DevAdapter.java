package university.chongqing.Adapter;

import java.util.List;

import university.chongqing.Activity.R;
import university.chongqing.model.WDeviceModel;

public class DevAdapter extends BaseAdapter<WDeviceModel>{

    public DevAdapter( List<WDeviceModel> list) {
        super(R.layout.list_device, list);
    }
    protected void convert(final BaseHolder holder, WDeviceModel model, final int position)
    {
        super.convert(holder, model, position);
        holder.setText(R.id.title_text, model.nme);
    }

}
