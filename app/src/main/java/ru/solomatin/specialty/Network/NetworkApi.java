package ru.solomatin.specialty.Network;

import retrofit2.http.GET;
import ru.solomatin.specialty.Model.ApiResponse;
import rx.Observable;

/**
 * Описывает интерфейс API
 */
public interface NetworkApi {
    @GET("/images/testTask.json")
    Observable<ApiResponse> getPersonsObservable();
}
