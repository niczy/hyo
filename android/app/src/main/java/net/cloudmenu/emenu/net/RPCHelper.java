package net.cloudmenu.emenu.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.utils.UnitUtils;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;

import cn.com.cloudstone.menu.server.thrift.api.IMenuService;
import cn.com.cloudstone.menu.server.thrift.api.IOrderService;
import cn.com.cloudstone.menu.server.thrift.api.IPadInfoService;
import cn.com.cloudstone.menu.server.thrift.api.IProfileService;
import cn.com.cloudstone.menu.server.thrift.api.IWaiterService;

public class RPCHelper {
    private static final String TAG = "RPCHelper";
    
    // public static final String DEFAULT_HOST_IP = "192.168.0.106";
    public static final String DEFAULT_HOST_IP = "http://hummingbirdsystem.com/";

    private static final String DEFAULT_API_SUFFIX = ".thrift";

    private static final int SOCKET_TIME_OUT = 20000;

    private static final int CONNECTION_TIME_OUT = 20000;
    
    public static final long CACHE_TIME_5_MIN = 5 * UnitUtils.MINUTE;

    public static final long CACHE_TIME_LONG = 500 * 24 * 3600 * 1000;

    public static final long CACHE_TIME_SHORT = 300 * 24 * 3600 * 1000;

    public static final long CACHE_TIME_REFRESH = Long.MIN_VALUE;

    private static final int MAX_READ_LENGTH = 512 * 1024;

    public static String getHostIp(Context context) {
        SharedPreferences preference = PreferenceManager
                .getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pre_key_custom_api_addr);
        return DEFAULT_HOST_IP;
    }

    private static String getApiSuffix(Context context) {
        SharedPreferences preference = PreferenceManager
                .getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pre_key_custom_api_suffix);
        return preference.getString(key, DEFAULT_API_SUFFIX);
    }

    private static HttpClient getHttpCilent() {
        HttpParams param = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(param, CONNECTION_TIME_OUT);
        HttpConnectionParams.setSoTimeout(param, SOCKET_TIME_OUT);
        return new DefaultHttpClient(param);
    }

    private static TProtocol getTProtocol(Context context, String url)
            throws TException {
        HttpClient client = getHttpCilent();
        String finalUrl = getHostIp(context) + url + getApiSuffix(context);
        TTransport transport = new THttpClient(finalUrl, client);
        Log.i(TAG, "url = " + finalUrl);
        transport.open();
        TBinaryProtocol protocol = new TBinaryProtocol(transport);
        return protocol;
    }

    public static IPadInfoService.Client getPadInfoService(Context context)
            throws TException {
        return new IPadInfoService.Client(getTProtocol(context,
                "padinfoservice"));
    }

    public static IMenuService.Client getMenuService(Context context)
            throws TException {
        return new IMenuService.Client(getTProtocol(context, "menuservice"));
    }

    public static IProfileService.Client getProfileService(Context context)
            throws TException {
        return new IProfileService.Client(getTProtocol(context,
                "profileservice"));
    }

    public static IProfileService.Client getCachedProfileService(Context context)
            throws TException {
        return new IProfileService.Client(getTProtocol(context,
                "profileservice"));
    }

    public static IProfileService.Client getCachedProfileService(
            Context context, long cacheTime) throws TException {
        return new IProfileService.Client(getTProtocol(context,
                "profileservice"));
    }

    public static IOrderService.Client getOrderService(Context context)
            throws TException {
        return new IOrderService.Client(getTProtocol(context, "orderservice"));
    }

    public static IWaiterService.Client getWaiterService(Context context)
            throws TException {
        return new IWaiterService.Client(getTProtocol(context, "waiterservice"));
    }

    public static IWaiterService.Client getCachedWaiterService(Context context)
            throws TException {
        return getCachedWaiterService(context, CACHE_TIME_LONG);
    }

    public static IWaiterService.Client getCachedWaiterService(Context context,
            long cacheTime) throws TException {
        return new IWaiterService.Client(getTProtocol(context,
                "waiterservice"));
    }

    public static IMenuService.Client getCachedMenuService(Context context)
            throws TException {
        return getCachedMenuService(context, CACHE_TIME_LONG);
    }

    public static IMenuService.Client getCachedMenuService(Context context,
            long cacheTime) throws TException {
        return new IMenuService.Client(getTProtocol(context,
                "menuservice"));
    }

    public static IMenuService.Client getClearCacheMenuService(Context context)
            throws TException {
        return new IMenuService.Client(getTProtocol(context,
                "menuservice"));
    }

    public static void releaseClient(TServiceClient client) {
        if (client != null && client.getInputProtocol() != null
                && client.getInputProtocol().getTransport() != null)
            client.getInputProtocol().getTransport().close();
    }

}
