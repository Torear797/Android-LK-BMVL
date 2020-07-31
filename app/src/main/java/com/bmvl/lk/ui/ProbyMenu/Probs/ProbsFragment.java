package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerCountries;
import com.bmvl.lk.Rest.Material;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.models.SuggestionCountries;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.button.MaterialButton;
import com.unnamed.b.atv.model.TreeNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProbsFragment extends Fragment implements OnBackPressedListener {
    private List<Field> ProbFields = new ArrayList<>(); //Поля пробы
    private List<Field> SampleFields = new ArrayList<>(); //Поля Образцов
    public static List<Material> Materials = new ArrayList<>(); //Список материалов
    public static String[] Countries; //Список стран
    public static ProbAdapter adapter;

    public ProbsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (CreateOrderActivity.order.getProby().size() == 0)
//            AddProb();

//        ProbAdapter.OnProbClickListener onClickListener = new ProbAdapter.OnProbClickListener() {
//            @Override
//            public void onDeletedProb(short id) {
//                adapter.closeAllItems();
//                TreeMap<Short, ProbyRest> insertlist = new TreeMap<>(CreateOrderActivity.order.getProby());
//                insertlist.remove(id);
//                adapter.updateList(insertlist);
//
//                if (CreateOrderActivity.order_id == 4) {
//                    CreateOrderActivity.order.getFields().put((short) 7, String.valueOf(CreateOrderActivity.order.getProby().size()));
//                    CreateOrderActivity.adapter.notifyItemChanged(18);
//                }
//            }
//
//            @Override
//            public void onCopyProb() {
//                Toast.makeText(getContext(), "Копирование пробы", Toast.LENGTH_SHORT).show();
//            }
//        };

//        switch (CreateOrderActivity.order_id) {
//            case 1:
//                AddProbFieldsType1();
//                break;
//            case 3:
//                AddProbFieldsType3();
//                break;
//            case 4:
//                AddProbFieldsType4();
//                break;
//            case 8:
//                AddPatternFieldsType1();
//                break;
//            case 9:
//                AddPatternFieldsType4();
//                break;
//            case 10:
//                AddPatternFieldsType3();
//                break;
//        }
//        adapter = new ProbAdapter(getContext(), ProbFields, SampleFields, onClickListener);
//        (adapter).setMode(Attributes.Mode.Single);
    }

    public static ProbsFragment newInstance() {
        return new ProbsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        final RecyclerView recyclerView = MyView.findViewById(R.id.List);

        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        recyclerView.setAdapter(adapter);
//        NewProbListener(AddProbBtn, adapter, recyclerView);

        getMaterialsList();
        new MyTask(recyclerView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return MyView;
    }
    private void getCountries(){
        NetworkService.getInstance()
                .getJSONApi()
                .getCountries(App.UserAccessData.getToken(),"")
                .enqueue(new Callback<AnswerCountries>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerCountries> call, @NonNull Response<AnswerCountries> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Countries = new String[response.body().getSuggestions().size()];
                            for(int i = 0; i <response.body().getSuggestions().size();i++){
                                Countries[i] = response.body().getSuggestions().get(i).getValue();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerCountries> call, @NonNull Throwable t) {
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void getMaterialsList() {
        NetworkService.getInstance()
                .getJSONApi()
                .getMaterials(App.UserAccessData.getToken())
                .enqueue(new Callback<List<Material>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Material>> call, @NonNull Response<List<Material>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Materials = response.body();
                            Collections.sort(Materials, (obj1, obj2) -> {
                                return obj1.getText().compareToIgnoreCase(obj2.getText()); // To compare string values
                            });
                        }
                        getCountries();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Material>> call, @NonNull Throwable t) {
                    }
                });
    }

    private void NewProbListener(final MaterialButton AddProbBtn, final ProbAdapter adapter, final RecyclerView recyclerView) {
        AddProbBtn.setVisibility(View.VISIBLE);
        AddProbBtn.setOnClickListener(view -> {
            Map<Short, ProbyRest> insertlist = new HashMap<>();
            final short newid = getPositionKey(CreateOrderActivity.order.getProby().size() - 1, CreateOrderActivity.order.getProby());
            insertlist.put((short) (newid + 1), new ProbyRest(newid));
            adapter.insertdata(insertlist);

            if(CreateOrderActivity.order_id == 1)
            Objects.requireNonNull(CreateOrderActivity.order.getProby().get((short) (newid + 1))).addSample((short) 1, new SamplesRest((short) 0));

            if (CreateOrderActivity.order_id == 4) {
                CreateOrderActivity.order.getFields().put((short) 7, String.valueOf(CreateOrderActivity.order.getProby().size()));
                CreateOrderActivity.adapter.notifyItemChanged(18);
            }

            //adapter.notifyDataSetChanged();
           // recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        });
    }

    private Short getPositionKey(int position, Map<Short, ProbyRest> Probs) {
        if (Probs.size() > 0)
            return new ArrayList<>(Probs.keySet()).get(position);
        else return 0;
    }

    private void AddProb() {
        CreateOrderActivity.order.addProb((short) 1, new ProbyRest((short) 0));
        if(CreateOrderActivity.order_id == 1)
        Objects.requireNonNull(CreateOrderActivity.order.getProby().get((short) (1))).addSample((short) 1, new SamplesRest((short) 0));
    } //Создает первую пробу и первый образец

    private void AddSamplesForType4() {
        SampleFields.add(new Field(6, "", "Наименование доставленного биоматериала", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(112, "", "Инвентарный номер животного, кличка и т.д.", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(113, "", "Группа", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(114, "", "Возраст, масть", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(115, "", "Номер корпуса", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(102, "", "Вакцинация поголовья", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    } //Поля образцов

    private void AddSamplesForType1() {
        SampleFields.add(new Field(6, "", "Наименование образца, термическое состояние", InputType.TYPE_CLASS_TEXT));
        //SampleFields.add(new Field(22, "", "Дата выработки", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_date_range_black_24dp), true));
        SampleFields.add(new Field((byte) 8, 22, "", "Дата выработки"));
        SampleFields.add(new Field(40, "", "Масса/объем образца", InputType.TYPE_CLASS_NUMBER));
        SampleFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)41), 41, " "));
        SampleFields.add(new Field((byte) 5, 19, "НД на продукцию"));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    }

    private void AddProbFieldsType1() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1,  App.OrderInfo.getFieldValues().get((short)25), 25, "Вид упаковки, наличие маркировки"));
        AddStandartFieldPart2();
        ProbFields.add(new Field((byte) 9, 19));
        ProbFields.add(new Field((byte) 10, 19));

        AddSamplesForType1();
        ProbFields.add(new Field((byte) 7));
    } //Поля пробы на исследование пищевых продуктов

    private void AddProbFieldsType3() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)25), 25, "Пробы упакованы"));
        AddStandartFieldPart2();
        ProbFields.add(new Field(143, "", "Кадастровый номер участка", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(144, "", "Глубина отбора", InputType.TYPE_NULL));
       // ProbFields.add(new Field((byte) 12,144, "Глубина отбора"));

        ProbFields.add(new Field(145, "", "Площадь с которой отобрано", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)146), 146, " "));
        ProbFields.add(new Field(68, "", "Особые условия доставки проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(69, "", "Отклонения проб от нормального состояния", InputType.TYPE_CLASS_TEXT));
        AddSamplesForType3();
        ProbFields.add(new Field((byte) 7));
    } //Поля пробы Заявка на исследование семян, почв, удобрений

    private void AddProbFieldsType4() {
        AddStandartFieldPart1();
        ProbFields.add(new Field(131, "", "Вид животного", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)25), 25, "Пробы упакованы"));
        AddStandartFieldPart2();
        ProbFields.add(new Field((byte) 7));
        AddSamplesForType4();
    } //Поля пробы сопроводительного письма

    private void AddPatternFieldsType1() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)25), 25, "Вид упаковки, наличие маркировки"));
        AddStandartFieldPart3();
        AddSamplesForPatternType1();
        ProbFields.add(new Field((byte) 7));
    }

    private void AddPatternFieldsType3() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)25), 25, "Пробы упакованы"));
        AddStandartFieldPart3();
        ProbFields.add(new Field(143, "", "Кадастровый номер участка", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(144, "", "Глубина отбора", InputType.TYPE_NULL));
        ProbFields.add(new Field(145, "", "Площадь с которой отобрано", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)146), 146, " "));
        AddSamplesForType3();
        ProbFields.add(new Field((byte) 7));
    }

    private void AddPatternFieldsType4() {
        AddStandartFieldPart1();
        ProbFields.add(new Field(131, "", "Вид животного", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)25), 25, "Пробы упакованы"));
        AddStandartFieldPart3();
        AddSamplesForPatternType4();
        ProbFields.add(new Field((byte) 7));
    }

    private void AddStandartFieldPart1() {
        ProbFields.add(new Field((byte) 6, 5, "", "Вид материала"));
        ProbFields.add(new Field((byte) 5, 116, "На соответствие требованиям"));
        ProbFields.add(new Field(15, "", "Номер сейф пакета", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)32), 32, "Состояние образца"));
        ProbFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)60), 60, "Транспорт"));
        ProbFields.add(new Field(61, "", "", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(54, "", "Широта", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(55, "", "Долгота", InputType.TYPE_CLASS_TEXT));
    }

    private void AddStandartFieldPart2() {
        AddStandartFieldPart3();
        ProbFields.add(new Field((byte) 8, 12, "Дата и время отбора"));
    }

    private void AddStandartFieldPart3() {
        ProbFields.add(new Field(74, "", "Наименование организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(75, "", "Адрес организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
//        ProbFields.add(new Field(27, "", "Страна отбора", InputType.TYPE_CLASS_TEXT));
//        ProbFields.add(new Field(28, "", "Регион отбора", InputType.TYPE_CLASS_TEXT));
//        ProbFields.add(new Field(57, "", "Район отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte)11, 27,  "Страна отбора"));
        ProbFields.add(new Field((byte)11, 28,  "Регион отбора"));
        ProbFields.add(new Field((byte)11, 57,  "Район отбора"));

        ProbFields.add(new Field(21, "", "Место отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(18, "", "План и метод отбора образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(125, "", "Должность лица,проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(126, "", "ФИО лица, проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(17, "", "В присутствии", InputType.TYPE_CLASS_TEXT));
    }

    private void AddSamplesForType3() {
        SampleFields.add(new Field(6, "", "Наименование доставленного материала", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(112, "", "Инвентарный номер, описание", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(144, "", "Глубина отбора", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 6, 0));
    }

    private void AddSamplesForPatternType1() {
        SampleFields.add(new Field(6, "", "Наименование образца, термическое состояние", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 5, 19, "НД на продукцию"));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    }

    private void AddSamplesForPatternType4() {
        SampleFields.add(new Field(6, "", "Наименование доставленного биоматериала", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(102, "", "Вакцинация поголовья", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    }

    @Override
    public void onBackPressed() {
        ProbFields.clear();
        SampleFields.clear();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        private RecyclerView recyclerView;

        MyTask(RecyclerView con) {
            this.recyclerView = con;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (CreateOrderActivity.order.getProby().size() == 0)
                AddProb();

            ProbAdapter.OnProbClickListener onClickListener = new ProbAdapter.OnProbClickListener() {
                @Override
                public void onDeletedProb(short id) {
                    adapter.closeAllItems();
                    TreeMap<Short, ProbyRest> insertlist = new TreeMap<>(CreateOrderActivity.order.getProby());
                    insertlist.remove(id);
                    adapter.updateList(insertlist);

                    CreateOrderActivity.order.getProby().remove(id);
                   // adapter.notifyItemRemoved(pos);
                  //  adapter.notifyDataSetChanged();

                    //Изменение поля кол-во проб
                    if (CreateOrderActivity.order_id == 4) {
                        CreateOrderActivity.order.getFields().put((short) 7, String.valueOf(CreateOrderActivity.order.getProby().size()));
                        CreateOrderActivity.adapter.notifyItemChanged(18);
                    }
                }

                @Override
                public void onDownloadProtocol(String adres, final short id) {
                    NetworkService.getInstance()
                            .getJSONApi()
                            .DownloadProtocol(App.UserAccessData.getToken())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (writeResponseBodyToDisk(response.body(), id)) {
                                            Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            };

            switch (CreateOrderActivity.order_id) {
                case 1:
                    if (!CreateOrderActivity.IsPattern)
                        AddProbFieldsType1();
                    else
                        AddPatternFieldsType1();
                    break;
                case 3:
                    if (!CreateOrderActivity.IsPattern)
                        AddProbFieldsType3();
                    else
                        AddPatternFieldsType3();
                    break;
                case 4:
                    if (!CreateOrderActivity.IsPattern)
                        AddProbFieldsType4();
                    else
                        AddPatternFieldsType4();
                    break;
            }

            adapter = new ProbAdapter(ProbFields, SampleFields, onClickListener);
            (adapter).setMode(Attributes.Mode.Single);
            return null;
        }

        private boolean writeResponseBodyToDisk(ResponseBody body, short id) {
            try {
                File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "Protocol_" + id + ".pdf");

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    byte[] fileReader = new byte[4096];

                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(futureStudioIconFile);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);

                        fileSizeDownloaded += read;

                        //  Log.d("File Download: " , fileSizeDownloaded + " of " + fileSize);
                    }

                    outputStream.flush();

                    return true;
                } catch (IOException e) {
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setAdapter(adapter);
            final MaterialButton AddProbBtn = Objects.requireNonNull(getView()).findViewById(R.id.addProb);
            NewProbListener(AddProbBtn, adapter, recyclerView);
        }
    }
}