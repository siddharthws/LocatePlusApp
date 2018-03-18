package com.lplus.activities.Interfaces;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Sai_Kameswari on 18-03-2018.
 */

public interface AddPlaceInterface {
    void onSaveClick();
    void onCancelClick();
    void onItemSelected(AdapterView<?> parent, View view, int pos, long id);
    void onNothingSelected();

}
