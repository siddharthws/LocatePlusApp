package com.lplus.activities.Dialogs;

import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogs {

    private Context context;
    private android.support.v7.app.AlertDialog alertDialog;
    private String dialog_text;

    public AlertDialogs(Context context, String dialog_text)
    {
        this.context = context;
        this.dialog_text = dialog_text;
        Init();
    }

    public AlertDialogs(Context context)
    {
        this.context = context;
        this.dialog_text = "";
        Init();
    }

    private void Init()
    {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(dialog_text);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {alertDialog.dismiss();}
                });

        //Show Dialog
        alertDialog = alertDialogBuilder.create();
    }

    public void ShowDialog()
    {
        alertDialog.show();
    }

    public void HideDialog()
    {
        alertDialog.dismiss();
    }
}
