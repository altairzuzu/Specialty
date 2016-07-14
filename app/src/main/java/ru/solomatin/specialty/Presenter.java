package ru.solomatin.specialty;

import javax.inject.Inject;

import ru.solomatin.specialty.Model.ApiResponse;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Presenter
 */
@SuppressWarnings("unchecked")
public class Presenter {

    private MainActivity view;
    private NetworkService networkService;
    private Subscription subscription;

//    @Inject
    public Presenter(MainActivity view, NetworkService networkService){
        this.view = view;
        this.networkService = networkService;
    }

//    public Presenter(MainActivity view){
//        this.view = view;
//
//        //this.networkService = networkService;
//    }

    public void loadRxData(){
        view.showRxInProcess();
        // Подготавливаем Observable к подписке либо извлекаем из кэша
        Observable<ApiResponse> apiResponseObservable = (Observable<ApiResponse>)
                networkService.getPreparedObservable(networkService.getAPI().getPersonsObservable(),
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

}


