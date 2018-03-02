package edu.avans.tjedrowald.foodmap.connection.interceptors;

import edu.avans.tjedrowald.foodmap.models.ApiKey;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    private ApiKey apiKey;

    public ApiKeyInterceptor(ApiKey apiKey){
        if(apiKey == null) {
            throw new IllegalArgumentException("apiKey");
        }
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        originalRequest = originalRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", apiKey.getTokenType() + " " + apiKey.getApiKey())
        .build();
        return chain.proceed(originalRequest);
    }
}

