package appskeleton.example.api;

import android.os.Build;
import android.provider.Settings;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Iterator;

import appskeleton.core.App;
import appskeleton.example.api.model.api.in.ApiError;
import appskeleton.example.api.service.ExampleService;
import eu.livotov.labs.android.robotools.network.RTNetwork;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dlivotov on 17/08/2016.
 */

public class ApiClient
{
    private final static long SIZE_OF_CACHE = 100 * 1024 * 1024; // 100 MiB
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
    private ExampleService service;
    private Retrofit retrofit;
    private Cache cache;

    public ApiClient()
    {
        endpointUrl = "https://somehost.com/api/v1/";
        debugMode = true;

        appId = "<your application id for api>";
        deviceId = Settings.Secure.getString(App.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        platformName = "Android";
        platformVersion = "" + Build.VERSION.SDK_INT;
        appVersion = "" + App.getVersionCode();

        rebuildAdapters();
    }

    private void rebuildAdapters()
    {
        cache = new Cache(new File(App.getContext().getCacheDir(), "api-cache"), SIZE_OF_CACHE);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();

        if (debugMode)
        {
            httpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

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
                    }
                    else
                    {
                        return originalResponse.newBuilder().build();
                    }
                }
                else
                {
                    Response originalResponse = chain.proceed(request);
                    return originalResponse.newBuilder().build();
                }
            }
        });

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

        retrofit = new Retrofit.Builder().baseUrl(endpointUrl).client(httpClientBuilder.build()).addConverterFactory(GsonConverterFactory.create()).build();
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
            }
            else
            {
                bulder.addHeader("Cache-Control", http_cache_control_header_out_offline).build();
            }
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
        }
        catch (Throwable e)
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
        }
        catch (IOException e)
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
        }
        catch (IOException err)
        {

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
