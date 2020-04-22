package com.bmvl.lk.data;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceTB;
    private int spaceRL;

    public SpacesItemDecoration(int spaceTB, int spaceRL)
    {
        this.spaceTB = spaceTB;
        this.spaceRL = spaceRL;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        //добавить переданное кол-во пикселей отступа снизу
        outRect.top = spaceTB;
        //outRect.bottom = spaceTB;
        outRect.left = spaceRL;
        outRect.right = spaceRL;
    }
}
