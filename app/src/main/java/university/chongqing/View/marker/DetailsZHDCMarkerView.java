package university.chongqing.View.marker;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class DetailsZHDCMarkerView  extends DetailsMarkerView {


    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    public DetailsZHDCMarkerView(Context context) {
        super(context);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        try {
            if (e.getY() == 10|| e.getY() == 35 ||e.getY() == 110 ||e.getY() == 220 ) {
                super.mTvChart1.setText(e.getY()+"V");
            } else {
                mTvChart1.setText(e.getY()+"kV/m");
            }
            if (xValues != null){
                mTvMonth.setText(xValues.get((int)e.getX()));
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



}
