package com.example.bloodapp.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://bloodapp-scy6.onrender.com/";
    private static Retrofit retrofit;
    private static String authToken;

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // Add auth header if token exists
                            if (authToken != null && !authToken.isEmpty()) {
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", "Bearer " + authToken)
                                        .method(original.method(), original.body());
                                return chain.proceed(requestBuilder.build());
                            }

                            return chain.proceed(original);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
