package com.bmvl.lk.ui.create_order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Material;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.TreeViewHolder.ProfileHolder;
import com.bmvl.lk.data.TreeViewHolder.SelectableHeaderHolder;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.Objects;

import static com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment.Materials;

public class ChoceMaterialDialogFragment extends DialogFragment {
    private TextInputEditText SearchField;
    private AutoCompleteTextView autoCompleteTextView;
    private String LastValue;
    private TextView SelectNode;
    private AndroidTreeView tView;

    private TreeNode StandartTreeNode;

    public ChoceMaterialDialogFragment(AutoCompleteTextView textView) {
        this.autoCompleteTextView = textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));
        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View MyView = inflater.inflate(R.layout.dialog_choce_material, null, false);
        ViewGroup containerView = MyView.findViewById(R.id.container);

        SearchField = MyView.findViewById(R.id.TextInput);
        SelectNode = MyView.findViewById(R.id.LastNode);

        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                new MyTask(s.toString().toLowerCase()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        tView = new AndroidTreeView(getActivity(), getRoot(null));
        tView.setDefaultNodeClickListener(nodeClickListener);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return builder
                .setTitle("Выбор материала")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(MyView)
                .setPositiveButton("Выбрать", (dialog, which) -> {
                    autoCompleteTextView.setText(LastValue);
                    dismiss();
                })
                .create();
    }

    private TreeNode getRoot(String name) {
        TreeNode root = TreeNode.root();
        TreeNode parent, child1, child2;
        for (Material CurrentMaterial : Materials) {
            if (CurrentMaterial.getParent().equals("#")) {
                parent = new TreeNode(CurrentMaterial.getText()).setViewHolder(new SelectableHeaderHolder(getActivity()));

                for (Material Lvl1 : Materials) {
                    if (Lvl1.getParent().equals(String.valueOf(CurrentMaterial.getId()))) {
                        child1 = new TreeNode(Lvl1.getText()).setViewHolder(new SelectableHeaderHolder(getActivity()));

                        for (Material Lvl2 : Materials) {
                            if (Lvl2.getParent().equals(String.valueOf(Lvl1.getId()))) {

                                if (name != null && !Lvl2.getText().toLowerCase().contains(name) && !Lvl1.getText().toLowerCase().contains(name))
                                    continue;

                                child2 = new TreeNode(Lvl2.getText()).setViewHolder(new SelectableHeaderHolder(getActivity()));
                                child1.addChild(child2);
                            }
                        }

                        if (name != null && !Lvl1.getText().toLowerCase().contains(name) && child1.size() == 0 && !CurrentMaterial.getText().toLowerCase().contains(name))
                            continue;
                        parent.addChild(child1);
                    }
                }
                if (name != null && !CurrentMaterial.getText().toLowerCase().contains(name) && parent.size() == 0)
                    continue;

                root.addChild(parent);
            }
        }
        StandartTreeNode = root;
        return root;
    }

    private void setSelectedNode(TreeNode node) {
        //  SelectableHeaderHolder itemLevel0,itemLevel1,itemLevel2;
        for (TreeNode n : StandartTreeNode.getChildren()) {
            ((SelectableHeaderHolder) n.getViewHolder()).changeSelected(node == n);

            for (TreeNode Level1 : n.getChildren()) {
                ((SelectableHeaderHolder) Level1.getViewHolder()).changeSelected(node == Level1);

                for (TreeNode Level2 : Level1.getChildren()) {
                    ((SelectableHeaderHolder) Level2.getViewHolder()).changeSelected(node == Level2);
                }
            }

        }
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            LastValue = value.toString();
            SelectNode.setText(String.format("Выбор: %s", LastValue));
            setSelectedNode(node);
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    private void Search(String name) {
        boolean Size, ParentSize;
        for (TreeNode n : StandartTreeNode.getChildren()) {
            ParentSize = false;
            for (TreeNode Level1 : n.getChildren()) {
                Size = false;
                for (TreeNode Level2 : Level1.getChildren()) {
                    if (name != null && !((SelectableHeaderHolder) Level2.getViewHolder()).isContaintsText(name) && !((SelectableHeaderHolder) Level1.getViewHolder()).isContaintsText(name))
                        ((SelectableHeaderHolder) Level2.getViewHolder()).setVisibilityNode(false, name);
                    else {
                        ((SelectableHeaderHolder) Level2.getViewHolder()).setVisibilityNode(true, name);
                        Size = true;
                    }
                }

                if (name != null && !((SelectableHeaderHolder) Level1.getViewHolder()).isContaintsText(name) && !Size && !((SelectableHeaderHolder) n.getViewHolder()).isContaintsText(name))
                    ((SelectableHeaderHolder) Level1.getViewHolder()).setVisibilityNode(false, name);
                else {
                    ((SelectableHeaderHolder) Level1.getViewHolder()).setVisibilityNode(true, name);
                    ParentSize = true;
                }

            }

            if (name != null && !((SelectableHeaderHolder) n.getViewHolder()).isContaintsText(name) && !ParentSize)
                ((SelectableHeaderHolder) n.getViewHolder()).setVisibilityNode(false, name);
            else
                ((SelectableHeaderHolder) n.getViewHolder()).setVisibilityNode(true, name);
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        private String s;

        MyTask(String text) {
            this.s = text;
        }

        @Override
        protected Void doInBackground(Void... params) {
//            if (s.length() > 0) {
//                // containerView.removeAllViews();
////                    containerView.removeViewAt(0);
////                    tView = new AndroidTreeView(getActivity(), getRoot(s.toString().toLowerCase()));
////                    tView.setDefaultNodeClickListener(nodeClickListener);
////                    containerView.addView(tView.getView());
//                Search(s);
//             //   tView.expandAll();
//                // setTextFont();
//            } else {
//                Search("");
//            //    tView.collapseAll();
//                //   containerView.removeAllViews();
////                    containerView.removeViewAt(0);
////                    tView = new AndroidTreeView(getActivity(), getRoot(null));
////                    tView.setDefaultNodeClickListener(nodeClickListener);
////                    containerView.addView(tView.getView());
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (s.length() > 0) {
                Search(s);
                tView.expandAll();
            }
            else {
                Search("");
                tView.collapseAll();
            }
            SearchField.clearFocus();
        }
    }
}