package ru.solomatin.specialty.Dependencies;

import android.app.Application;
import android.support.v4.util.LruCache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.solomatin.specialty.Model.Person;
import ru.solomatin.specialty.Model.PersonDeserializer;
import ru.solomatin.specialty.Network.NetworkApi;
import rx.Observable;

/**
 * Модуль предоставления сетевых зависимостей
 */
@Module
public class NetworkModule {

    String baseUrl;

    public NetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Person.class, new PersonDeserializer())
                .create();
        return gson;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    NetworkApi provideNetworkService(Retrofit retrofit) {
        return retrofit.create(NetworkApi.class);
    }

    @Provides
    @Singleton
    LruCache<Class<?>, Observable<?>> provideLruCache() {
        int lruCacheSize = 1024 * 1024;
        return new LruCache<>(lruCacheSize);
    }

}
