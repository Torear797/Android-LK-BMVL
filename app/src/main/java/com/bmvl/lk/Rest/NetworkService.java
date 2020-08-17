package com.bmvl.lk.Rest;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    //При смене BASE_URL необходимо изменить XML файл CreateOrderActivity
   private static final String BASE_URL = "http://192.168.0.69";
   // private static final String BASE_URL = "http://192.168.0.100";
    //private static final String BASE_URL = "http://lk.belmvl.ru";

    private Retrofit mRetrofit;


    private NetworkService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);


        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public static String getServerUrl(){
        return BASE_URL;
    }

    public ServerApi getJSONApi() {
        return mRetrofit.create(ServerApi.class);
    }
}
