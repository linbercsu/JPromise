package net.zentertain.promise;

import android.os.Handler;
import android.util.Log;

import net.zentertain.promise.impl.PromiseWrapper;

public abstract class Then<T> {
    private PromiseWrapper wrapper;
    private Promise promise;
    private Handler handler;
    private Runnable resolvedRunnable;
    private Runnable rejectRunnable;
    private volatile boolean cancel;

    abstract public void run(T value);

    protected Then() {
        handler = new Handler();
    }
//    public void error(Throwable throwable) {
//
//    }

    public synchronized void performCancel() {
        if (cancel)
            return;

        cancel = true;

        if (resolvedRunnable != null) {
            handler.removeCallbacks(resolvedRunnable);
            resolvedRunnable = null;
        }

        if (rejectRunnable != null) {
            handler.removeCallbacks(rejectRunnable);
            rejectRunnable = null;
        }

        cancel();
    }

    protected void cancel() {

    }

    protected synchronized void resolve(final Object value) {
        if (cancel)
            return;

        if (resolvedRunnable != null || rejectRunnable != null) {
            Log.e("promise", "error: resolve: resolvedRunnable or rejectRunnable should be null.");
            return;
        }

        resolvedRunnable = new Runnable() {
            @Override
            public void run() {
                resolvedRunnable = null;

                promise.resolve(wrapper, value);
            }
        };
        handler.post(resolvedRunnable);

    }

    protected synchronized void reject(final Throwable throwable) {
        if (cancel)
            return;

        if (resolvedRunnable != null || rejectRunnable != null) {
            Log.e("promise", "error: reject: resolvedRunnable or rejectRunnable should be null.");
            return;
        }

        rejectRunnable = new Runnable() {

            @Override
            public void run() {
                rejectRunnable = null;

                promise.reject(throwable);
            }
        };

        handler.post(rejectRunnable);
    }

    public void setPromise(Promise promise) {
        this.promise = promise;
    }

    public void setWrapper(PromiseWrapper wrapper) {
        this.wrapper = wrapper;
    }

}
