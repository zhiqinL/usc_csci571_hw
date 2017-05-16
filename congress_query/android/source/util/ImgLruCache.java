package csci571.zhiqinliao.hw9.util;


import android.support.v4.util.LruCache;

import com.squareup.picasso.RequestCreator;

import java.io.IOException;

/**
 * Cache class used to catch legislator images.
 */

public class ImgLruCache extends LruCache<String, RequestCreator> {

    public ImgLruCache(int cacheSize) {
        super(cacheSize);
    }

    @Override
    protected int sizeOf(String key, RequestCreator requestCreator) {
        try {
            return requestCreator.get().getRowBytes() * requestCreator.get().getHeight() / 1024;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
