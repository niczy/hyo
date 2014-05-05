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
package greendroid.image;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.LinkedHashMap;
import java.util.Map;

import greendroid.app.GDApplication.OnLowMemoryListener;
import greendroid.util.GDUtils;

/**
 * A very basing implementation of an Bitmap cache
 *
 * @author Cyril Mottier
 */
public class ImageCache implements OnLowMemoryListener {
    // Create cache
    final int MAX_ENTRIES = 30;

    private final Map<String, Bitmap> mSoftCache;
    public ImageCache(Context context) {
        mSoftCache = new LinkedHashMap<String, Bitmap>(MAX_ENTRIES+1, .75F, true) {
            // This method is called just after a new entry has been added
            public boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_ENTRIES;
            }
        };
        GDUtils.getGDApplication(context).registerOnLowMemoryListener(this);
    }

    public static ImageCache from(Context context) {
        return GDUtils.getImageCache(context);
    }

    public Bitmap get(String url) {
        return mSoftCache.get(url);
    }

    public void put(String url, Bitmap bitmap) {
        mSoftCache.put(url, bitmap);
    }

    public int size() {
        return mSoftCache.size();
    }

    public void flush() {
        mSoftCache.clear();
    }

    public void onLowMemoryReceived() {
        flush();
    }
}