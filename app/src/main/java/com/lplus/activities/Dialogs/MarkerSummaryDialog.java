package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;

import com.lplus.R;
import com.lplus.activities.Objects.MarkerObject;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkerSummaryDialog {

    private Context context;
    private Dialog markerSummaryDialog;
    private MarkerObject markerObject;

    public MarkerSummaryDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {
        markerSummaryDialog = new Dialog(context, R.style.CustomDialogTheme);
        markerSummaryDialog.setContentView(R.layout.dialog_marker_summary);
        markerSummaryDialog.setCancelable(true);
        markerSummaryDialog.setCanceledOnTouchOutside(true);


    }

    public void ShowDialog()
    {
        markerSummaryDialog.show();
    }

    public void HideDialog()
    {
        markerSummaryDialog.cancel();
    }
}
