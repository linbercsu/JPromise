package net.zentertain.promise.impl;

import net.zentertain.promise.Promise;

public class PromiseRace extends PromiseWrapper {

    public PromiseRace(Promise... promiseWrappers) {
        for (int i = 0; i < promiseWrappers.length; i++) {
            PromiseWrapper promiseWrapper = (PromiseWrapper) promiseWrappers[i];
            inners.add(promiseWrapper);
            promiseWrapper.wrapper = this;
        }

    }

    @Override
    public void cancel() {
        if (!isPending())
            return;

        super.cancel();
    }

    @Override
    public void reject(Throwable throwable) {
        if (!isPending())
            return;

        super.reject(throwable);

        if (getWrapper() != null) {
            getWrapper().reject(throwable);
        }
    }

    @Override
    protected void transfer(Promise source, Object value) {
        if (!isPending())
            return;

        resolve(this, value);

    }
}
