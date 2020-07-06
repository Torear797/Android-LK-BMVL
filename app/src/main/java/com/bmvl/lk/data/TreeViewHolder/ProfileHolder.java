package com.bmvl.lk.data.TreeViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmvl.lk.R;
import com.unnamed.b.atv.model.TreeNode;

public class ProfileHolder extends TreeNode.BaseNodeViewHolder<String> {


    public ProfileHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, String value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_header, null, false);
        TextView tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value);

       // final ImageView iconView = view.findViewById(R.id.icon);
      //  iconView.setBackground(context.getDrawable(value.icon));

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }
}
