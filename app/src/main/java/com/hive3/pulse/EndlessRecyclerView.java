package com.hive3.pulse;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class EndlessRecyclerView extends RecyclerView.OnScrollListener {


    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    int firstVisibleItem,visibleItemCount,totalItemCount;
    private int current_page = 1;
    private LinearLayoutManager mLinearLayoutManager;




    EndlessRecyclerView(LinearLayoutManager mLinearLayoutManager){
        this.mLinearLayoutManager = mLinearLayoutManager;
    }


    public abstract void onLoadMore(int current_page);


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading){
            if (totalItemCount > previousTotal){
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem+visibleThreshold)){

            current_page++;
            onLoadMore(current_page);
            loading = true;


        }

    }




}














