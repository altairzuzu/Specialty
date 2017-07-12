package ru.solomatin.specialty;

import android.app.Application;

import ru.solomatin.specialty.Dependencies.AppModule;
import ru.solomatin.specialty.Dependencies.DaggerNetworkComponent;
import ru.solomatin.specialty.Dependencies.NetworkComponent;
import ru.solomatin.specialty.Dependencies.NetworkModule;

/**
 * Application. Инициализируем и храним здесь DaggerNetworkComponent
 */
public class RxApplication extends Application {

    private NetworkComponent networkComponent;


    // todo test

    @Override
    public void onCreate() {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("http://127.0.0.1"))
                .build();
    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }
}

