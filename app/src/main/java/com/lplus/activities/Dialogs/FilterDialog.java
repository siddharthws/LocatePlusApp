package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Adapters.CustomListAdapter;
import com.lplus.activities.Interfaces.CategorySelectedInterface;
import com.lplus.activities.Interfaces.CustomAdapterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai_Kameswari on 19-03-2018.
 */

public class FilterDialog implements CustomAdapterListener {

    private Context context;
    private Dialog filterSelectDialog;
    private Button apply, cancel;
    private ListView simpleList;
    private List<String> selectedCategories;
    private CategorySelectedInterface listener;
    private boolean flag = false;
    private ArrayList<String> categoriesList;
    public void setListener(CategorySelectedInterface listener)
    {
        this.listener = listener;
    }

    public FilterDialog(Context context, ArrayList categoriesList)
    {
        this.context = context;
        this.categoriesList = categoriesList;
        selectedCategories = new ArrayList<>();
        Init();
    }

    private void Init()
    {
        filterSelectDialog = new Dialog(context, R.style.CustomDialogTheme);
        filterSelectDialog.setContentView(R.layout.dialog_filter);
        filterSelectDialog.setCancelable(true);
        filterSelectDialog.setCanceledOnTouchOutside(true);
        apply = filterSelectDialog.findViewById(R.id.bt_apply);
        cancel = filterSelectDialog.findViewById(R.id.bt_cancel);

        simpleList =  filterSelectDialog.findViewById(R.id.filter_list);
        CustomListAdapter customAdapter = new CustomListAdapter(context, categoriesList);
        customAdapter.setListener(this);
        simpleList.setAdapter(customAdapter);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onApplyClick(selectedCategories);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClick(selectedCategories);
            }
        });
    }

    @Override
    public void onItemClick(String category)
    {
        for (int j = 0; j < selectedCategories.size(); j++)
        {
            if(selectedCategories.get(j).equals(category))
            {
                selectedCategories.remove(j);
                flag=true;
                break;
            }
        }
        if (flag==false)
        {
            selectedCategories.add(category);
        }
        else
        {
            flag=false;
        }
    }

    public void ShowDialog()
    {
        filterSelectDialog.show();
    }

    public void HideDialog()
    {
        filterSelectDialog.cancel();
    }



}
