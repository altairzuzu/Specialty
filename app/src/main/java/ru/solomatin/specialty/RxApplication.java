package ru.solomatin.specialty;

import android.app.Application;

import ru.solomatin.specialty.Dependencies.AppModule;
import ru.solomatin.specialty.Dependencies.DaggerNetworkComponent;
import ru.solomatin.specialty.Dependencies.NetworkComponent;
import ru.solomatin.specialty.Dependencies.NetworkModule;

/**
 * Created by cteegarden on 1/26/16.
 */
public class RxApplication extends Application {

    private NetworkService networkService;
    private NetworkComponent networkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        networkService = new NetworkService();

        networkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                //.networkModule(new NetworkModule("http://65apps.com"))
                .networkModule(new NetworkModule("http://192.168.1.33:3000"))
                .build();
    }

    public NetworkService getNetworkService(){
        return networkService;
    }
    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }
}

