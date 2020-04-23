package com.bmvl.lk.data;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private byte spaceTB;
    private byte spaceRL;

    public SpacesItemDecoration(byte spaceTB, byte spaceRL)
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
