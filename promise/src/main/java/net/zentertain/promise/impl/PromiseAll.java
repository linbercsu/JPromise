package net.zentertain.promise.impl;

import net.zentertain.promise.Promise;

public class PromiseAll extends PromiseWrapper {

    private Object[] values;

    public PromiseAll(Promise... promiseWrappers) {
        for (int i = 0; i < promiseWrappers.length; i++) {
            PromiseWrapper promiseWrapper = (PromiseWrapper) promiseWrappers[i];
            inners.add(promiseWrapper);
            promiseWrapper.wrapper = this;
        }

        values = new Object[promiseWrappers.length];
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

        for (int i = 0; i < inners.size(); i++) {
            if(inners.get(i).equals(source)) {
                values[i] = value;
                break;
            }
        }

        boolean resolved = true;

        for (Promise promise : inners) {
            if (promise.isPending()) {
                resolved = false;
                break;
            }
        }

        if (resolved) {
            resolve(this, values);
        }
    }
}
