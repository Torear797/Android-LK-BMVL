package com.bmvl.lk.ui.settings.fragments.EditNotify;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerNotifySettings;
import com.bmvl.lk.Rest.AnswerSearch;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.settings.ItemNotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingNotifyFragment extends Fragment implements OnBackPressedListener {
    private RecyclerView SettingsList;
    private List<ItemNotify> SettingsFields = new ArrayList<>();
    private ItemSettingAdapter adapter;

    public static Fragment newInstance() {
        return new SettingNotifyFragment();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.only_recyclerview, container, false);

        SettingsList = MyView.findViewById(R.id.RecyclerView);


        InitList();
        return MyView;
    }

    private void InitList(){
        SettingsFields.add(new ItemNotify((byte) 1));

        NetworkService.getInstance()
                .getJSONApi()
                .getNotifySetting(App.UserAccessData.getToken())
                .enqueue(new Callback<AnswerNotifySettings>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerNotifySettings> call, @NonNull Response<AnswerNotifySettings> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {

                                for(Map.Entry<String, Map<String, Byte>> entry : response.body().getUserNotifiactions().entrySet()) {
                                    SettingsFields.add(new ItemNotify(getNameField(entry.getKey()),entry.getKey(),
                                            getBooleanValue(entry.getValue().get("lk")),
                                            getBooleanValue(entry.getValue().get("email")),
                                            getBooleanValue(entry.getValue().get("sms"))
                                            ));
                                }

                                initRecyclerView();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerNotifySettings> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                        Log.d("TAG","PROBLEM",t);
                    }
                });
    }
    private String getNameField(String text){
        switch (text){
            case "newOrder":
                return "Создание заказа";
            case "changeOrderStatus":
                return "Изменение статуса заявки";
            case "addProtocol":
                return "Загрузка протокола";
            case "editFilial":
                return "Изменение филиала";
            case "editContactPerson":
                return "Изменение контактного лица";
            case "downloadProtocol":
                return "Скачивание протокола";
        }
        return "Error";
    }

    private boolean getBooleanValue(byte value){
        if(value == (byte)0) return false;
        else return true;
    }

    private void initRecyclerView() {
        SettingsList.addItemDecoration(new SpacesItemDecoration((byte) 30, (byte) 10));
        SettingsList.setItemAnimator(new DefaultItemAnimator());
        SettingsList.setHasFixedSize(true);

//        SettingsAdapter.OnClickListener onClickListener = group -> {
//            Intent intent = new Intent(SettingsActivity.this, SettingItemActivity.class);
//            intent.putExtra("type_id", group.getId());
//            intent.putExtra("name", group.getName());
//            startActivity(intent);
//        };

        adapter = new ItemSettingAdapter(getContext(), SettingsFields);
        SettingsList.setAdapter(adapter);
    }
}
