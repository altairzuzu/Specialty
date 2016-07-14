package ru.solomatin.specialty;

import android.app.Application;
import android.support.v4.util.LruCache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import ru.solomatin.specialty.Dependencies.NetworkComponent;
import ru.solomatin.specialty.Model.ApiResponse;
import ru.solomatin.specialty.Model.Person;
import ru.solomatin.specialty.Model.PersonDeserializer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 *
 */
public class NetworkService{

    //private static String baseUrl ="http://65apps.com";
    private static String baseUrl ="http://www.json-generator.com";
    //private static String baseUrl ="http://192.168.1.33:3000/";
    private NetworkAPI networkAPI;
    //private OkHttpClient okHttpClient;
    private LruCache<Class<?>, Observable<?>> cacheObservables;
    //@Inject Gson gson;
    @Inject Retrofit retrofit;
    @Inject Application application;

    public NetworkService(){
        this(baseUrl);
    }

    public NetworkService(String baseUrl){

//        OkHttpClient client = new OkHttpClient();
//        // Enable caching for OkHttp
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(getApplicationContext().getCacheDir(), cacheSize);
//        client.setCache(cache);

//        // Used for caching authentication tokens
//        SharedPreferences sharedPrefeences = PreferenceManager.getDefaultSharedPreferences(this);

        // Instantiate Gson
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Person.class, new PersonDeserializer())
//                .create();
        ((RxApplication) application).getNetworkComponent().inject(this);

        int lruCacheSize = 1024 * 1024;
        cacheObservables = new LruCache<>(lruCacheSize);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                //.client(client)
//                .build();

//        networkAPI = retrofit.create(NetworkAPI.class);
        networkAPI = retrofit.create(NetworkAPI.class);
    }

    public NetworkAPI getAPI(){
        return  networkAPI;
    }

    /**
     * Возвращает кэшированный Observable или подготавлвиает новый
     *
     * @param unPreparedObservable Неподготовленный Observable
     * @param clazz Используется, как ключ в LruCache
     * @return Observable, подготовленная для подписки
     */
    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable, Class<?> clazz) {
        Observable<?> preparedObservable = cacheObservables.get(clazz);
        if (preparedObservable != null) {
            return preparedObservable;
        }
        preparedObservable = unPreparedObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .cache();
        cacheObservables.put(clazz, preparedObservable);
        return preparedObservable;
    }

    /**
     * Описывает интерфейс API
     */
    public interface NetworkAPI {
        //@GET("/images/testTask.json")
        //@GET("/api/json/get/clQwzudaOG?indent=2")
        @GET("/testTask.json")
        //@GET("/api/json/get/cglkPcGEzm?indent=2")
        //@GET("/images/testTask.json")
        Observable<ApiResponse> getPersonsObservable();
    }

}

