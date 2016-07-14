package ru.solomatin.specialty.Dependencies;

import javax.inject.Singleton;

import dagger.Component;
import ru.solomatin.specialty.MainActivity;
import ru.solomatin.specialty.NetworkService;
import ru.solomatin.specialty.Presenter;

/**
 * Created by altair on 15.07.2016.
 */
@Singleton
@Component (modules = { AppModule.class, NetworkModule.class})
public interface NetworkComponent {
    void inject (MainActivity activity);
    void inject (Presenter presenter);
    void inject (NetworkService networkService);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}
