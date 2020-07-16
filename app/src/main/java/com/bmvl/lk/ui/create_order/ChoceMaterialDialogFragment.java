package com.bmvl.lk.ui.create_order;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Material;
import com.bmvl.lk.data.TreeViewHolder.ProfileHolder;
import com.bmvl.lk.data.TreeViewHolder.SelectableHeaderHolder;
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
    private ViewGroup containerView;

    public ChoceMaterialDialogFragment(AutoCompleteTextView textView) {
        this.autoCompleteTextView = textView;
    }

    private String State;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));
        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View MyView = inflater.inflate(R.layout.dialog_choce_material, null, false);
        containerView = MyView.findViewById(R.id.container);

        SearchField = MyView.findViewById(R.id.TextInput);
        SelectNode = MyView.findViewById(R.id.LastNode);


        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    containerView.removeAllViews();
                    tView = new AndroidTreeView(getActivity(), getRoot(s.toString().toLowerCase()));
                    tView.setDefaultNodeClickListener(nodeClickListener);
                    containerView.addView(tView.getView());
                } else {
                    containerView.removeAllViews();
                    tView = new AndroidTreeView(getActivity(), getRoot(null));
                    tView.setDefaultNodeClickListener(nodeClickListener);
                    containerView.addView(tView.getView());
                }
                SearchField.clearFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


//        TreeNode root = TreeNode.root();
//        root.addChildren(ProbsFragment.root.getChildren());

        tView = new AndroidTreeView(getActivity(), getRoot(null));
       // tView = new AndroidTreeView(getActivity(), ProbsFragment.root);
        tView.setDefaultNodeClickListener(nodeClickListener);
        State = tView.getSaveState();

//        if(tView.getView().getParent() == null) {
//           // ((ViewGroup)tView.getView().getParent()).removeView(tView.getView()); // <- fix
//            containerView.addView(tView.getView());
//        }

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
                .setPositiveButton("Выбрать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        autoCompleteTextView.setText(LastValue);
                        dismiss();
                    }
                })
                .create();
    }

    private TreeNode getRoot(String name) {
        TreeNode root = TreeNode.root();
        TreeNode parent, child1, child2;
        for (Material CurrentMaterial : Materials) {
            if (CurrentMaterial.getParent().equals("#")) {
               // if(name != null && !CurrentMaterial.getText().toLowerCase().contains(name)) continue;
                parent = new TreeNode(CurrentMaterial.getText()).setViewHolder(new SelectableHeaderHolder(getActivity()));

                for (Material Lvl1 : Materials) {
                    if (Lvl1.getParent().equals(String.valueOf(CurrentMaterial.getId()))) {
                      //  if(name != null && !Lvl1.getText().toLowerCase().contains(name)) continue;
                        child1 = new TreeNode(Lvl1.getText()).setViewHolder(new SelectableHeaderHolder(getActivity()));

                        for (Material Lvl2 : Materials) {
                            if (Lvl2.getParent().equals(String.valueOf(Lvl1.getId()))) {
                                if(name != null && !Lvl2.getText().toLowerCase().contains(name)) continue;
                                child2 = new TreeNode(Lvl2.getText()).setViewHolder(new ProfileHolder(getActivity()));
                                child1.addChild(child2);
                            }
                        }

                        if(name != null && !Lvl1.getText().toLowerCase().contains(name) && child1.size() == 0) continue;
                        parent.addChild(child1);
                    }
                }
                if(name != null && !CurrentMaterial.getText().toLowerCase().contains(name) && parent.size() == 0) continue;
                root.addChild(parent);
            }
        }
        return root;
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            LastValue = value.toString();
            SelectNode.setText(String.format("Выбор: %s", LastValue));
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

}