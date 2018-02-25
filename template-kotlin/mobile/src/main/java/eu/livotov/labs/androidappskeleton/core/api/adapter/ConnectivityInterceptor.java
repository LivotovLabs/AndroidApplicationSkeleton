package eu.livotov.labs.androidappskeleton.core.api.adapter;

import android.content.Context;

import java.io.IOException;

import eu.livotov.labs.android.robotools.network.RTNetwork;
import eu.livotov.labs.androidappskeleton.core.err.NoConnectivityException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 31/01/2018
 */
public class ConnectivityInterceptor implements Interceptor
{

    private Context mContext;

    public ConnectivityInterceptor(Context context)
    {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        if (!RTNetwork.isConnected(mContext))
        {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}
