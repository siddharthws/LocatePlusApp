package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.HomeActivity;
import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.Interfaces.RegisterClassInterface;

/**
 * Created by Sai_Kameswari on 18-03-2018.
 */

public class AddPlaceDialog {

    private Context context;
    private Dialog addplacedialog;
    private EditText place_name;
    private TextView address;
    private CardView save,cancel;

    private AddPlaceInterface addPlacelistener = null;
    public void SetListener(AddPlaceInterface addPlacelistener)
    {
        this.addPlacelistener = addPlacelistener;
    }

    public AddPlaceDialog(Context context)
    {
        this.context = context;
        Init();
    }

    private void Init()
    {
        addplacedialog = new Dialog(context, R.style.CustomDialogTheme);
        addplacedialog.setContentView(R.layout.dialog_place_add);
        addplacedialog.setCancelable(false);
        addplacedialog.setCanceledOnTouchOutside(false);
        addplacedialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        place_name = addplacedialog.findViewById(R.id.add_place_name);
        place_name.setText("Rest Rooms");

        address = addplacedialog.findViewById(R.id.address_add);
        address.setText("is the an appropriate facility ?");

        save = addplacedialog.findViewById(R.id.save_add);
        cancel = addplacedialog.findViewById(R.id.cancel_add);
        //TextView yes = dialog.findViewById(R.id.ok_text);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlacelistener.onSaveClick();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addPlacelistener.onCancelClick();
            }
        });
    }

    public void ShowDialog()
    {
        addplacedialog.show();
    }

    public void HideDialog()
    {
        addplacedialog.dismiss();
    }
}
