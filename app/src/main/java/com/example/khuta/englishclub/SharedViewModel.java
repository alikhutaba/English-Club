package com.example.khuta.englishclub;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Player> selected = new MutableLiveData<Player>();

    public void select(Player player) {
        selected.setValue(player);
    }

    public LiveData<Player> getSelected() {
        return selected;
    }

}
