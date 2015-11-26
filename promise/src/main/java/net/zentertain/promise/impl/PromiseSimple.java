package net.zentertain.promise.impl;

import android.os.Handler;
import android.os.Message;

import net.zentertain.promise.Promise;

import java.lang.ref.WeakReference;

public class PromiseSimple extends PromiseWrapper {

    private final Object object;
    private final Handler handler;

    public PromiseSimple(Object object) {
        this.object = object;

        handler = new MyHandler(this);
        handler.sendEmptyMessage(0);
    }

    protected void resolve() {
        resolve(this, object);
    }

    @Override
    protected void transfer(Promise source, Object value) {

    }

    @Override
    public void cancel() {
        if (!isPending())
            return;

        handler.removeMessages(0);

        super.cancel();
    }

    static class MyHandler extends Handler {
        private WeakReference<PromiseSimple> ref;

        public MyHandler(PromiseSimple ref) {
            this.ref = new WeakReference<>(ref);
        }

        @Override
        public void handleMessage(Message msg) {
            PromiseSimple promiseSimple = ref.get();
            if (promiseSimple != null) {
                promiseSimple.resolve();
            }
        }
    }
}
