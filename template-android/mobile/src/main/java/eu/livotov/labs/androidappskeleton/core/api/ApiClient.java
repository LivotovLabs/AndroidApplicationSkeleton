package eu.livotov.labs.androidappskeleton.core.api;

import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.livotov.labs.android.robotools.network.RTNetwork;
import eu.livotov.labs.android.robotools.settings.RTPrefs;
import eu.livotov.labs.androidappskeleton.core.App;
import eu.livotov.labs.androidappskeleton.core.api.adapter.ConnectivityInterceptor;
import eu.livotov.labs.androidappskeleton.core.api.adapter.DateTimeConverter;
import eu.livotov.labs.androidappskeleton.core.err.ApiAuthDisconnectedError;
import eu.livotov.labs.androidappskeleton.data.ApiError;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by dlivotov on 17/08/2016.
 */

public class ApiClient implements Authenticator
{

    private final static long SIZE_OF_CACHE = 100 * 1024 * 1024; // 100 MiB
    private final static String PREFS_KEY_TOKEN = "token";
    private final static String PREFS_KEY_REFRESH_TOKEN = "rtoken";

    private final String appId;
    private final String appVersion;
    private final String platformName;
    private final String platformVersion;
    private final String deviceId;
    private final String endpointUrl;
    private final boolean debugMode;
    private final String http_cache_control_header_in = "public, max-age=10800, max-stale=2419200";
    private final String http_cache_control_header_out_online = "public, max-age=10800";
    private final String http_cache_control_header_out_offline = "public, only-if-cached, max-stale=2419200";
    private String authToken;
    private String authRefreshToken;
    private ExampleService service;
    private Retrofit retrofit;
    private Cache cache;
    private RTPrefs prefs = new RTPrefs(App.getContext(), getClass().getCanonicalName(), true);

    public ApiClient()
    {
        endpointUrl = "https://somehost.com/api/v1/";
        debugMode = true;

        appId = "<your application id for api>";
        deviceId = Settings.Secure.getString(App.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        platformName = "Android";
        platformVersion = "" + Build.VERSION.SDK_INT;
        appVersion = "" + App.getVersionCode();

        authToken = prefs.getString(PREFS_KEY_TOKEN, null);
        authRefreshToken = prefs.getString(PREFS_KEY_REFRESH_TOKEN, null);

        rebuildAdapters();
    }

    public void setAuth(final String token)
    {
        setAuth(token, null);
    }

    public void setAuth(final String token, final String refreshToken)
    {
        authToken = token;
        authRefreshToken = refreshToken;
        prefs.setString(PREFS_KEY_TOKEN, token);
        prefs.setString(PREFS_KEY_REFRESH_TOKEN, refreshToken);
    }

    public void unsetAuth()
    {
        authToken = null;
        authRefreshToken = null;
        prefs.remove(PREFS_KEY_TOKEN);
        prefs.remove(PREFS_KEY_REFRESH_TOKEN);
    }

    private void rebuildAdapters()
    {
        cache = new Cache(new File(App.getContext().getCacheDir(), "api-cache"), SIZE_OF_CACHE);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();

        if (debugMode)
        {
            httpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        httpClientBuilder.connectionSpecs(Collections.singletonList(new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                                                                            .tlsVersions(TlsVersion.TLS_1_2)
                                                                            .cipherSuites(
                                                                                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                                                                                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                                                                                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                                                                                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                                                                                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                                                                                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                                                                                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                                                                                    CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                                                                                    CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                                                                                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                                                                                    CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                                                                                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
                                                                            .build()));

        // Remove this interceptor if your API server can return proper caching headers. Leave if not.
        httpClientBuilder.addNetworkInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                final Request request = chain.request();

                if ("get".equalsIgnoreCase(request.method()))
                {
                    Response originalResponse = chain.proceed(request);

                    if (originalResponse.isSuccessful())
                    {
                        return originalResponse.newBuilder().removeHeader("Access-Control-Allow-Origin").removeHeader("X-Cache").removeHeader("X-Cache-Hit").removeHeader("Cache-Control").removeHeader("Pragma").header("Cache-Control", http_cache_control_header_in).build();
                    } else
                    {
                        return originalResponse.newBuilder().build();
                    }
                } else
                {
                    Response originalResponse = chain.proceed(request);
                    return originalResponse.newBuilder().build();
                }
            }
        });

        httpClientBuilder.addInterceptor(new ConnectivityInterceptor(App.getContext()));
        httpClientBuilder.addInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                return appendHttpHeaders(chain);
            }
        });

        httpClientBuilder.cache(cache);
        httpClientBuilder.retryOnConnectionFailure(true);
        httpClientBuilder.authenticator(this);

        // Add more datatype converters here if necessary
        final Gson gson = new GsonBuilder()
                                  .registerTypeAdapter(Date.class, new DateTimeConverter())
                                  .create();

        retrofit = new Retrofit.Builder().baseUrl(endpointUrl).client(httpClientBuilder.build()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        service = retrofit.create(ExampleService.class);
    }

    private Response appendHttpHeaders(Interceptor.Chain chain) throws IOException
    {
        final Request request = chain.request();
        Request.Builder bulder = request.newBuilder();

        // This is to prevent gzip encoded responses from the server, so we'll be able to read dumped responses while in debug mode
        if (debugMode)
        {
            bulder.addHeader("Accept-Encoding", "");
        }

        // Ask for cached data first for GET requests. Remove this if you don't need cache at all.
        if ("get".equalsIgnoreCase(request.method()))
        {
            if (RTNetwork.isConnected(App.getContext()))
            {
                bulder.addHeader("Cache-Control", http_cache_control_header_out_online).build();
            } else
            {
                bulder.addHeader("Cache-Control", http_cache_control_header_out_offline).build();
            }
        }

        if (!TextUtils.isEmpty(authToken))
        {
            bulder.addHeader(Headers.Authorization, "Bearer " + authToken);
        }

        bulder.addHeader(Headers.OperatingSystemName, platformName);
        bulder.addHeader(Headers.OperatingSystemVersion, platformVersion);
        bulder.addHeader(Headers.ApplicationID, appId);
        bulder.addHeader(Headers.ApplicationVersion, appVersion);
        bulder.addHeader(Headers.ClientUDID, deviceId);

        return chain.proceed(bulder.build());
    }

    public ApiError getError(retrofit2.Response<?> response)
    {
        retrofit2.Converter<okhttp3.ResponseBody, ApiError> converter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError error;

        try
        {
            error = converter.convert(response.errorBody());

            if (TextUtils.isEmpty(error.getDescription()))
            {
                error.setDescription(response.message());
            }

            if (error.getCode() == 0)
            {
                error.setCode(response.code());
            }

        } catch (Throwable e)
        {
            error = new ApiError();
            error.setCode(response.code());
            error.setDescription(response.message());
        }

        return error;
    }

    public ExampleService getService()
    {
        return service;
    }

    public void flushCache()
    {
        try
        {
            cache.evictAll();
        } catch (IOException e)
        {
        }
    }

    public void flushCache(final String url)
    {
        try
        {
            Iterator<String> urls = cache.urls();
            while (urls.hasNext())
            {
                final String nextUrl = "" + urls.next();
                if (nextUrl.toLowerCase().startsWith(url))
                {
                    urls.remove();
                }
            }
        } catch (IOException err)
        {

        }
    }

    @Nullable public Request authenticate(final Route route, final Response response) throws IOException
    {
        synchronized (retrofit)
        {
            if (!route.address().url().toString().contains("your/api/token-refresh-url"))
            {
                if (!TextUtils.isEmpty(authRefreshToken))
                {
                    //todo: execute token refresh api call and get the new tokens pair:
                    boolean refreshSuccessfull = false;
                    String newToken = "";
                    String newRefreshToken = "";

                    if (refreshSuccessfull)
                    {
                        setAuth(newToken, newRefreshToken);
                        return response.request().newBuilder().header("Authorization", newToken).build();
                    } else
                    {
                        unsetAuth();
                        throw new ApiAuthDisconnectedError();
                    }
                } else
                {
                    unsetAuth();
                    throw new ApiAuthDisconnectedError();
                }
            } else
            {
                unsetAuth();
                throw new ApiAuthDisconnectedError();
            }
        }
    }

    public class Headers
    {

        public final static String Authorization = "Authorization";
        public final static String ApplicationID = "X-APP-ID";
        public final static String ApplicationVersion = "X-APP-VERSION";
        public final static String ClientUDID = "X-CLIENT";
        public final static String OperatingSystemName = "X-OS-NAME";
        public final static String OperatingSystemVersion = "X-OS-VERSION";
    }
}
