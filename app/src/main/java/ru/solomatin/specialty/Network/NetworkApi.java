package ru.solomatin.specialty.Network;

import retrofit2.http.GET;
import ru.solomatin.specialty.Model.ApiResponse;
import rx.Observable;

/**
 * Описывает интерфейс API
 */
public interface NetworkApi {
    //@GET("/images/testTask.json")
    //@GET("/api/json/get/clQwzudaOG?indent=2")
    @GET("/testTask.json")
    //@GET("/api/json/get/cglkPcGEzm?indent=2")
    //@GET("/images/testTask.json")
    Observable<ApiResponse> getPersonsObservable();
}
