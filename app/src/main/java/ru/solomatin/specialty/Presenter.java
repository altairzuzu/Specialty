package ru.solomatin.specialty;

import android.support.v4.util.LruCache;

import javax.inject.Inject;

import ru.solomatin.specialty.Model.ApiResponse;
import ru.solomatin.specialty.Network.NetworkApi;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Presenter
 */
@SuppressWarnings("unchecked")
public class Presenter {

    private MainActivity view;
    private Subscription subscription;
    @Inject NetworkApi networkService;
    @Inject LruCache<Class<?>, Observable<?>> cacheObservables;

//    @Inject
    public Presenter(MainActivity view){
        this.view = view;
        ((RxApplication) view.getApplication()).getNetworkComponent().inject(this);
    }

    public void loadRxData(){
        view.showRxInProcess();
        // Подготавливаем Observable к подписке либо извлекаем из кэша
        Observable<ApiResponse> apiResponseObservable = (Observable<ApiResponse>)
                getPreparedObservable(networkService.getPersonsObservable(),
                        ApiResponse.class);
        subscription = apiResponseObservable.subscribe(new Observer<ApiResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                view.showRxFailure(e);
            }

            @Override
            public void onNext(ApiResponse response) {
                // Передаем полученный json-ответ в MainActivity
                view.showRxResults(response);
            }
        });
    }

    public void rxUnSubscribe(){
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
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

}


