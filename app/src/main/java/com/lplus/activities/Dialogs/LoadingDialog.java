package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.lplus.R;

/**
 * Created by Sai_Kameswari on 16-03-2018.
 */

public class LoadingDialog {

    private Context context;
    private Dialog loadingDialog;
    private String dialog_text;

    public LoadingDialog(Context context, String dialog_text)
    {
        this.context = context;
        this.dialog_text = dialog_text;
        Init();
    }

    public LoadingDialog(Context context)
    {
        this.context = context;
        this.dialog_text = "";
        Init();
    }

    private void Init()
    {
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.loading_dialog);
        TextView tv_dialog = loadingDialog.findViewById(R.id.tv_dialog);
        if(dialog_text.length()>0)
        {
            tv_dialog.setText(dialog_text);
        }
    }

    public void ShowDialog()
    {
        loadingDialog.show();
    }

    public void HideDialog()
    {
        loadingDialog.dismiss();
    }
}
