package eu.livotov.labs.androidappskeleton.core.api;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ExampleService
{
    @GET("cities/")
    Call<List<String>> getCities();

}
