package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SampleItemDialog extends DialogFragment {

    private List<Field> SampleFields; //Поля Образцов
    private ProbyRest CurrentProb;
    private List<Suggestion> suggestions; //Материалы;
    private Map<String, Integer> id_fields = new HashMap<>(); //id полей для обновлений
    private SamplesRest CurrentSample; //Текущий образец
    private byte SamplePosition;

    public SampleItemDialog(List<Field> sampleFields, ProbyRest prob, SamplesRest Sample, byte position, List<Suggestion> suggestions) {
        this.SampleFields = sampleFields;
        this.CurrentProb = prob;
        this.CurrentSample = Sample;
        this.SamplePosition = position;
        this.suggestions = suggestions;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View MyView = inflater.inflate(R.layout.dialog_item_sample, null, false);

        final RecyclerView SampleFieldList = MyView.findViewById(R.id.SampleFieldList);
        SampleFieldList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));

        InitSampleFields(SampleFieldList);

//        InputMethodManager imm = (InputMethodManager) SampleItemDialog.this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);

        //    private TextView ProbHeader;
        //    private TextView SamplesHeader;
        Dialog myDialog = builder
                .setTitle("Образец № " + SamplePosition)
                .setView(MyView)
                .setPositiveButton("ОК", (dialog, which) -> {
                    ProbAdapter.adapter.notifyItemChanged(ProbAdapter.adapter.getIdForField("SampleList"));
                    ProbAdapter.probAdapter.notifyDataSetChanged();
                    dismiss();
                })
                .create();

        SampleFieldList.post(() -> Objects.requireNonNull(myDialog.getWindow()).clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM));

        return myDialog;
    }

    private void InitSampleFields(RecyclerView SampleFieldList) {
        SamplesFieldAdapter adapter = new SamplesFieldAdapter(SampleFields, CurrentSample, CurrentProb.getFields(),
                CurrentProb, SamplePosition, suggestions, id_fields,
                null, null);

        SampleFieldList.setAdapter(adapter);

        if (CreateOrderActivity.order_id == 1 || CreateOrderActivity.order_id == 8)
            if (!CreateOrderActivity.IsPattern) {
                final GridLayoutManager mng_layout = new GridLayoutManager(SampleFieldList.getContext(), 2);
                mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position == 2 || position == 3)
                            return 1;
                        return 2;
                    }
                });
                SampleFieldList.setLayoutManager(mng_layout);
            }
    }
}