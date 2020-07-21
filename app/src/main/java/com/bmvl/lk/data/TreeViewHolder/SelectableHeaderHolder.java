package com.bmvl.lk.data.TreeViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmvl.lk.R;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

public class SelectableHeaderHolder extends TreeNode.BaseNodeViewHolder<String> {
    private TextView tvValue;
    private ImageView arrowView;
    private RelativeLayout Field;

    public SelectableHeaderHolder(Context context) {
        super(context);
    }

    @Override
    public void setTreeViev(AndroidTreeView treeViev) {
        super.setTreeViev(treeViev);
    }

    @Override
    public View createNodeView(final TreeNode node, String value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_header, null, false);

        Field = view.findViewById(R.id.field);
        tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value);

        arrowView = view.findViewById(R.id.arrow_icon);

        if (!node.isLeaf()) {
            arrowView.setVisibility(View.VISIBLE);
            arrowView.setBackground(context.getDrawable(R.drawable.ic_baseline_keyboard_arrow_right_24));
        }

//        nodeSelector = (MaterialRadioButton) view.findViewById(R.id.node_selector);
//        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//               // getTreeView().deselectAll();
//                node.setSelected(isChecked);
////                for (TreeNode n : node.getChildren()) {
////                    getTreeView().selectNode(n, isChecked);
////                }
//            }
//        });
//        nodeSelector.setChecked(node.isSelected());
//        Field.setOnClickListener(v -> {
//
//        });

        return view;
    }

    public boolean getVisibilityNode() {
        return Field.getVisibility() == View.VISIBLE;
    }

    public boolean isContaintsText(String text) {
        if (tvValue != null)
            return tvValue.getText().toString().toLowerCase().contains(text.toLowerCase());
        return false;
    }

    public void setVisibilityNode(boolean isVisible, String text) {
        if (Field != null) {
            if (isVisible) {
                Field.setVisibility(View.VISIBLE);
                setBoldItalicText(text);
            } else
                Field.setVisibility(View.GONE);
        }
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setBackground(context.getDrawable(active ? R.drawable.ic_baseline_keyboard_arrow_down_24 : R.drawable.ic_baseline_keyboard_arrow_right_24));
    }

    public void setBoldItalicText(String text) {
        if (tvValue != null)
            if (text.equals("")) {
                tvValue.setTypeface(null, Typeface.NORMAL);
                tvValue.setTextColor(context.getResources().getColor(R.color.text_order_field_color));
            } else if (tvValue.getText().toString().toLowerCase().contains(text.toLowerCase())) {
                tvValue.setTypeface(null, Typeface.BOLD_ITALIC);
                tvValue.setTextColor(Color.RED);
            }
    }

    public void changeSelected(boolean selected) {
        if (Field != null)
            Field.setBackgroundColor(selected ? context.getResources().getColor(R.color.field) : Color.WHITE);
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }
}