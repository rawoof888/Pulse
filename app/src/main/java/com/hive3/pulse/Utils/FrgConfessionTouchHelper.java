package com.hive3.pulse.Utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.hive3.pulse.adapters.RVAdapterFragmentConfession;

public class FrgConfessionTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RVAdapterFragmentConfession adapter;


    public FrgConfessionTouchHelper(RVAdapterFragmentConfession adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }


    public FrgConfessionTouchHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


    }
}




