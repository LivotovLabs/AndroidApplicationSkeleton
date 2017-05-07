package eu.livotov.labs.androidappskeleton.core;

import android.support.v7.app.AppCompatDelegate;
import com.arellomobile.mvp.MvpFacade;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import eu.livotov.labs.android.robotools.network.RTNetwork;
import eu.livotov.labs.androidappskeleton.core.base.BaseApp;
import eu.livotov.labs.androidappskeleton.util.AppSettings;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;

public class App extends BaseApp
{
    protected AppSettings settings;

    public static AppSettings getSettings()
    {
        return ((App) getInstance()).settings;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Allow native VectorDrawable support for pre-lollipops
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        MvpFacade.init();

        // Init settings object
        settings = new AppSettings(this);

        // Init Picasso image loader with persistent cache.
        initImageLoader();
    }

    /**
     * This method configures defaut Picasso image loader instance to use okhttp as network downloader as well as
     * sets up the persistent image caching. In case your API requires you to authenticate image urls,
     * you can add this here too by uncommenting a single line below (see the comments inside this method).
     * <p>
     * Once this methos is called, you can use Picasso as usual using its singleton Picasso.from(....)
     */
    private void initImageLoader()
    {
        /*
            Adjust the cache lifetime here if necessary. All timings are in seconds.
         */
        final String http_cache_control_header_in = "public, max-age=28800, max-stale=2419200";
        final String http_cache_control_header_out_online = "public, max-age=28800";
        final String http_cache_control_header_out_offline = "public, only-if-cached, max-stale=2419200";

        Cache cache = new Cache(new File(App.getContext().getCacheDir(), "app-images-cache"), 500000000);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();

        /*
            This is used to dump image requetsts headers in debug mode.
         */
        if (isDebuggable())
        {
            httpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS));
        }

        httpClientBuilder.addNetworkInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                final Request request = chain.request();

                if ("get".equalsIgnoreCase(request.method()))
                {
                    final Response originalResponse = chain.proceed(request);

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
                    return chain.proceed(request).newBuilder().build();
                }
            }
        });

        httpClientBuilder.addInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                final Request request = chain.request();
                Request.Builder bulder = request.newBuilder();

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

                // Uncomment live below if you need to pass auth headers for your image requests
                // bulder.addHeader(Headers.Authorization, String.format("Bearer %s", App.getSettings().getSystemSettings().getAccessToken()));

                return chain.proceed(bulder.build());
            }
        });

        httpClientBuilder.cache(cache);
        httpClientBuilder.retryOnConnectionFailure(true);
        Picasso.setSingletonInstance(new Picasso.Builder(this).downloader(new OkHttp3Downloader(httpClientBuilder.build())).build());
    }

}
