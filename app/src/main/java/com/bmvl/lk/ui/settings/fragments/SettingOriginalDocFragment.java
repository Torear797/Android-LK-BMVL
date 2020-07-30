package com.bmvl.lk.ui.settings.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.Rest.UserInfo.OriginalDocument;
import com.bmvl.lk.data.FileUtils;
import com.bmvl.lk.data.OnBackPressedListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;
import java.text.MessageFormat;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SettingOriginalDocFragment extends Fragment implements OnBackPressedListener {
    public static Fragment newInstance() {
        return new SettingOriginalDocFragment();
    }

    private static final int SELECT_SCAN = 2;
    private TextView PathScan;
    private File ScanFile;

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == SELECT_SCAN) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ScanFile = FileUtils.getFile(getContext(), data.getData());
                    PathScan.setText(Objects.requireNonNull(ScanFile).getName());
                    PathScan.setTextColor(getResources().getColor(R.color.text_order_field_color));
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_setting_original_doc, container, false);

        final Spinner spiner = MyView.findViewById(R.id.spinner);
        final TextView HintSpiner = MyView.findViewById(R.id.hint);

        final TextInputEditText Email = MyView.findViewById(R.id.EmailInput);
        final TextInputEditText Adres = MyView.findViewById(R.id.AdresInput);

        final TextInputLayout LayoutAdres = MyView.findViewById(R.id.AdresLayout);
        final TextInputLayout LayoutEmail = MyView.findViewById(R.id.EmailLayout);

        final MaterialButton Save_Btn = MyView.findViewById(R.id.MaterialButton);

        final Group group = MyView.findViewById(R.id.group);
        final MaterialButton SelectScan_Btn = MyView.findViewById(R.id.select);
        PathScan = MyView.findViewById(R.id.path);

        ArrayAdapter<CharSequence> adapterOriginalDoc = ArrayAdapter.createFromResource(inflater.getContext(), R.array.DocList, android.R.layout.simple_spinner_item);
        adapterOriginalDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapterOriginalDoc);
        HintSpiner.setText("Оригиналы документов предоставлять");

        switch (App.OrderInfo.getOD_ID()) {
            case 52:
                spiner.setSelection(0);
                Adres.setText(App.OrderInfo.getFIO());
                group.setVisibility(View.VISIBLE);
                break;
            case 63:
                spiner.setSelection(1);
                Adres.setText(App.OrderInfo.getOD_Adres());
                group.setVisibility(View.GONE);
                break;
            case 64:
                spiner.setSelection(2);
                Adres.setText(App.OrderInfo.getOD_Adres());
                Email.setText(App.OrderInfo.getOD_Email());
                group.setVisibility(View.GONE);
                break;
        }

        if (App.OrderInfo.getURL_SCAN_FILE() != null) {
            PathScan.setText(android.text.Html.fromHtml("<u>Загруженный файл</u>"));
            PathScan.setTextColor(Color.parseColor("#0066cc"));

            PathScan.setOnClickListener(v -> {
                if (PathScan.getTextColors().getDefaultColor() == Color.parseColor("#0066cc"))
                    inflater.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MessageFormat.format("{0}{1}", NetworkService.getServerUrl(), App.OrderInfo.getURL_SCAN_FILE()))));
            });

        }

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {

                if (selectedItemPosition == 0 || selectedItemPosition == 1) {
                    Email.setVisibility(View.GONE);
                    LayoutEmail.setVisibility(View.GONE);
                }

                switch (selectedItemPosition) {
                    case 0:
                        LayoutAdres.setHint(parent.getContext().getResources().getString(R.string.Doc_Face));
                        group.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        LayoutAdres.setHint(parent.getContext().getResources().getString(R.string.adres));
                        group.setVisibility(View.GONE);
                        break;
                    case 2:
                        Email.setVisibility(View.VISIBLE);
                        LayoutEmail.setVisibility(View.VISIBLE);
                        LayoutAdres.setHint(parent.getContext().getResources().getString(R.string.adres));
                        group.setVisibility(View.GONE);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Save_Btn.setOnClickListener(v -> {

            OriginalDocument doc = new OriginalDocument();
            switch (spiner.getSelectedItemPosition()) {
                case 0:
                    doc.setField52(String.valueOf(Adres.getText()));
                    break;
                case 1:
                    doc.setField63_0(String.valueOf(Adres.getText()));
                    break;
                case 2:
                    doc.setField64(String.valueOf(Email.getText()));
                    doc.setField63_1(String.valueOf(Adres.getText()));
                    break;
            }


            //  final Gson gson = new Gson();
//                Log.d("TAG", gson.toJson(fields));
            if (ScanFile != null)
                LoadScanFile(doc);
            else
                NetworkService.getInstance()
                        .getJSONApi()
                        .setDefaultFields(App.UserAccessData.getToken(), new Gson().toJson(doc))
                        .enqueue(new Callback<StandardAnswer>() {
                            @Override
                            public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        Toast.makeText(getContext(), R.string.data_save_ok, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getContext(), R.string.data_save_error, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), R.string.data_save_error, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                            }
                        });

        });

        SelectScan_Btn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                startActivityForResult(Intent.createChooser(intent,
                        getString(R.string.ChoceFile)), SELECT_SCAN);
            } catch (android.content.ActivityNotFoundException ex) {

                Toast.makeText(getContext(), R.string.NoFileMeneger,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return MyView;
    }

    private void LoadScanFile(OriginalDocument doc) {
        RequestBody requestBody = RequestBody.create(ScanFile, MediaType.parse("*/*"));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("doverennost", ScanFile.getName(), requestBody);

        RequestBody Token = RequestBody.create(App.UserAccessData.getToken(), okhttp3.MultipartBody.FORM);

        RequestBody fields = RequestBody.create(new Gson().toJson(doc), okhttp3.MultipartBody.FORM);

        NetworkService.getInstance()
                .getJSONApi()
                .setDefaultFieldsWithScanFile(Token, fields, fileToUpload)
                .enqueue(new Callback<StandardAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(getContext(), R.string.data_save_ok, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getContext(), R.string.data_save_error, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext(), R.string.data_save_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
