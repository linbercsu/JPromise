package net.zentertain.promise.impl;

import net.zentertain.promise.Promise;
import net.zentertain.promise.Then;

public class PromiseThen extends PromiseWrapper {
    private final Then then;

    public PromiseThen(Then then) {
        this.then = then;
        then.setWrapper(this);
        then.setPromise(this);
    }

    @Override
    public void cancel() {
        if (!isPending())
            return;

        then.performCancel();

        super.cancel();
    }

    @Override
    protected void transfer(Promise source, Object value) {
//        try {
            then.run(value);
//        } catch (Throwable throwable) {
//            reject(throwable);
//        }
    }

    @Override
    public void reject(Throwable throwable) {
        super.reject(throwable);

//        then.error(throwable);

        if (getWrapper() != null) {
            getWrapper().reject(throwable);
        }
    }
}
