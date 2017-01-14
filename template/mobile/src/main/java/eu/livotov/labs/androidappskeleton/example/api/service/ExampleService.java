package eu.livotov.labs.androidappskeleton.example.api.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExampleService
{
    @GET("cities/")
    Call<List<String>> getCities();

}
