package university.chongqing.Activity;


import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;



public class LoadingDialog
{
    private Dialog loadingDialog;
    private TextView textView;

    public LoadingDialog(Context context)
    {
        loadingDialog = new Dialog(context, R.style.myDialogTheme);
        loadingDialog.setContentView(R.layout.view_loadingdlg);
        textView = (TextView) loadingDialog.findViewById(R.id.loading_message);
    }

    /**
     * @param message
     */
    public void setMessage(String message)
    {
        textView.setText(message);
    }

    /**
     *
     */
    public void dismiss()
    {
        loadingDialog.dismiss();
    }

    /**
     *
     */
    public void dialogShow()
    {
        loadingDialog.show();
    }
}
