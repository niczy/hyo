/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greendroid.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import net.cloudmenu.emenu.net.RPCHelper;
import net.cloudmenu.emenu.utils.MenuUtils;
import net.cloudmenu.emenu.utils.ProfileHolder;
import net.cloudmenu.emenu.utils.ThriftUtils;

import org.apache.thrift.TException;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import cn.buding.common.file.FileUtil;
import cn.buding.common.file.ImageBuffer;
import cn.buding.common.net.BaseHttpsManager;
import cn.com.cloudstone.menu.server.thrift.api.IPadInfoService;
import cn.com.cloudstone.menu.server.thrift.api.PadInfo;
import greendroid.image.ImageCache;

/**
 * Define various methods that should be overridden in order to style your
 * application.
 *
 * @author Cyril Mottier
 */
public class GDApplication extends Application {

    private static final String TAG = "Application";
    public static final String ROOT_PATH = ".emenu";
    public static final String IMAGE_BUFFER_PATH = ROOT_PATH + "/image2";

    @Override
    public void onCreate() {
        super.onCreate();
        onAppStart();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                String log = Log.getStackTraceString(ex);
                if (log != null) {
                    Log.d("Crash!", log);
                    File f = FileUtil.getFile(GDApplication.this, ROOT_PATH,
                            "crash.log");
                    FileUtil.writeFile(f, log);
                }
            }
        });
    }

    private static final int MAX_IMAGE_BUFFER_SIZE = 128 * 1024 * 1024;

    protected void onAppStart() {
        BaseHttpsManager.init(this);
        ImageBuffer.init(this, IMAGE_BUFFER_PATH, MAX_IMAGE_BUFFER_SIZE);
        ImageBuffer.getInstance().setMaxMemSize(8 * 1024 * 1024);
        new SendPadInfoThread(this).start();
        Log.i(TAG, "onAppStart");
    }

    private static class SendPadInfoThread extends Thread {
        private static final String TAG = "SendPadInfoThread";
        private Context mContext;
        private int mBatteryLevel = -1;
        private static final long SLEEP_INTERVAL = 60 * 1000;

        public SendPadInfoThread(Context context) {
            mContext = context;
            listenBatteryLevel();
        }

        private void listenBatteryLevel() {
            BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    mBatteryLevel = intent.getIntExtra("level", -1);
                }
            };
            IntentFilter batteryLevelFilter = new IntentFilter(
                    Intent.ACTION_BATTERY_CHANGED);
            mContext.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        }

        @Override
        public void run() {
            while (true) {
                IPadInfoService.Client client = null;
                try {
                    client = RPCHelper.getPadInfoService(mContext);
                    PadInfo info = new PadInfo();
                    info.setBatteryLevel(mBatteryLevel);
                    info.setIMEI(MenuUtils.getCustomIMEI(mContext));
                    String sid = ProfileHolder.getIns().getCurrentSid(mContext);
                    info.setSessionId(sid);
                    info.setRestaurentId(2);
                    client.submitPadInfo(info);
                } catch (TException e) {
                    Log.w(TAG, "Send padinfo error");
                } finally {
                    ThriftUtils.releaseClient(client);
                }
                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    /**
     * Used for receiving low memory system notification. You should definitely
     * use it in order to clear caches and not important data every time the
     * system needs memory.
     *
     * @author Cyril Mottier
     * @see GDApplication#registerOnLowMemoryListener(OnLowMemoryListener)
     * @see GDApplication#unregisterOnLowMemoryListener(OnLowMemoryListener)
     */
    public static interface OnLowMemoryListener {

        /**
         * Callback to be invoked when the system needs memory.
         */
        public void onLowMemoryReceived();
    }

    private static final int CORE_POOL_SIZE = 5;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "GreenDroid thread #" + mCount.getAndIncrement());
        }
    };

    private ExecutorService mExecutorService;
    private ImageCache mImageCache;
    private ArrayList<WeakReference<OnLowMemoryListener>> mLowMemoryListeners;

    /**
     * @hide
     */
    public GDApplication() {
        mLowMemoryListeners = new ArrayList<WeakReference<OnLowMemoryListener>>();
    }

    /**
     * Return an ExecutorService (global to the entire application) that may be
     * used by clients when running long tasks in the background.
     *
     * @return An ExecutorService to used when processing long running tasks
     */
    public ExecutorService getExecutor() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(CORE_POOL_SIZE, sThreadFactory);
        }
        return mExecutorService;
    }

    /**
     * Return this application {@link ImageCache}.
     *
     * @return The application {@link ImageCache}
     */
    public ImageCache getImageCache() {
        if (mImageCache == null) {
            mImageCache = new ImageCache(this);
        }
        return mImageCache;
    }

    /**
     * Return the class of the home Activity. The home Activity is the main
     * entrance point of your application. This is usually where the
     * dashboard/general menu is displayed.
     *
     * @return The Class of the home Activity
     */
    public Class<?> getHomeActivityClass() {
        return null;
    }

    /**
     * Each application may have an "application intent" which will be used when
     * the user clicked on the application button.
     *
     * @return The main application Intent (may be null if you don't want to use
     *         the main application Intent feature)
     */
    public Intent getMainApplicationIntent() {
        return null;
    }

    /**
     * Add a new listener to registered {@link OnLowMemoryListener}.
     *
     * @param listener The listener to unregister
     * @see OnLowMemoryListener
     */
    public void registerOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            mLowMemoryListeners.add(new WeakReference<OnLowMemoryListener>(listener));
        }
    }

    /**
     * Remove a previously registered listener
     *
     * @param listener The listener to unregister
     * @see OnLowMemoryListener
     */
    public void unregisterOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            int i = 0;
            while (i < mLowMemoryListeners.size()) {
                final OnLowMemoryListener l = mLowMemoryListeners.get(i).get();
                if (l == null || l == listener) {
                    mLowMemoryListeners.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        int i = 0;
        while (i < mLowMemoryListeners.size()) {
            final OnLowMemoryListener listener = mLowMemoryListeners.get(i).get();
            if (listener == null) {
                mLowMemoryListeners.remove(i);
            } else {
                listener.onLowMemoryReceived();
                i++;
            }
        }
    }
}