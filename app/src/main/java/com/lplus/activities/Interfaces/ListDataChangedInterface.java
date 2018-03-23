package com.lplus.activities.Interfaces;

import com.lplus.activities.Objects.FavouriteObject;

/**
 * Created by CHANDEL on 3/19/2018.
 */

public interface ListDataChangedInterface {
    void onDataChanged();
    void onItemClicked(FavouriteObject favouriteObject);
}
