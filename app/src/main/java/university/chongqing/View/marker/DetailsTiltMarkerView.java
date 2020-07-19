package university.chongqing.View.marker;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * @author Lai
 * @time 2018/5/13 17:32
 * @describe describe
 */

public class DetailsTiltMarkerView extends DetailsMarkerView {


    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    public DetailsTiltMarkerView(Context context) {
        super(context);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        try {
            if (e.getY() == 0) {
                super.mTvChart1.setText("");
            } else {
                mTvChart1.setText(e.getY()+"A");
            }
            if (xValues != null){
                mTvMonth.setText(xValues.get((int)e.getX())+":00");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



}
