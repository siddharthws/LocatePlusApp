package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.Interfaces.AddPlaceInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai_Kameswari on 18-03-2018.
 */

public class AddPlaceDialog implements AdapterView.OnItemSelectedListener {

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

        //select category
        Spinner spinner = addplacedialog.findViewById(R.id.category_spinner);
        List<String> list = new ArrayList<>();
        list.add("category one");              //delete these
        list.add("category two");
        list.add("category three");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);


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
        addplacedialog.cancel();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       addPlacelistener.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        addPlacelistener.onNothingSelected();
    }
}

