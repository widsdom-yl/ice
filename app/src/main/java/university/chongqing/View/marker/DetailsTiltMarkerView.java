package university.chongqing.View.marker;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import static university.chongqing.View.marker.DetailsTiltMarkerView.MarkType.MarkType_ELectric;
import static university.chongqing.View.marker.DetailsTiltMarkerView.MarkType.MarkType_Humi;
import static university.chongqing.View.marker.DetailsTiltMarkerView.MarkType.MarkType_Ice;
import static university.chongqing.View.marker.DetailsTiltMarkerView.MarkType.MarkType_Jibin;
import static university.chongqing.View.marker.DetailsTiltMarkerView.MarkType.MarkType_Temp;

/**
 * @author Lai
 * @time 2018/5/13 17:32
 * @describe describe
 */

public class DetailsTiltMarkerView extends DetailsMarkerView {

    public enum MarkType{
        MarkType_ELectric,
        MarkType_Jibin,
        MarkType_Ice,
        MarkType_Temp,
        MarkType_Humi
    }
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    MarkType markType;
    public DetailsTiltMarkerView(Context context) {
        super(context);
    }
    public void setMarkType(MarkType type){
        markType = type;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        try {
            if (markType == MarkType_ELectric) {

                mTvChart1.setText(e.getY()+"A");
            }
            else if (markType == MarkType_Ice) {
                mTvChart1.setText(e.getY()+"mm");
            }
            else if (markType == MarkType_Temp) {
                mTvChart1.setText(e.getY()+"℃");
            }
            else if (markType == MarkType_Jibin) {
                mTvChart1.setText(e.getY()+"g");
            }
            else if (markType == MarkType_Humi) {
                mTvChart1.setText(e.getY()+"%");
            }
            else{
                mTvChart1.setText(""+e.getY());
            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



}
