package university.chongqing.View.marker;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

import university.chongqing.Activity.R;

/**
 * @author Lai
 * @time 2018/5/13 17:32
 * @describe describe
 */

public class DetailsMarkerView extends MarkerView {

    public TextView mTvMonth;
    public TextView mTvChart1;
    public List<String>xValues;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    public DetailsMarkerView(Context context) {
        super(context, R.layout.item_chart_des_marker_item);
        mTvMonth = findViewById(R.id.tv_chart_month);
        mTvChart1 = findViewById(R.id.tv_chart_1);
    }
    public void setxValues(List<String>values){
        xValues = values;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        try {
            if (e.getY() == 0) {
                mTvChart1.setText("");
            } else {
                mTvChart1.setText(e.getY()+"℃");
            }
            if (xValues != null){
                mTvMonth.setText(xValues.get((int)e.getX())+":00");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }




}
