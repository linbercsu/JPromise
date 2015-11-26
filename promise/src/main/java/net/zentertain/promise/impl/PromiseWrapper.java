package net.zentertain.promise.impl;

import net.zentertain.promise.Promise;
import net.zentertain.promise.Then;

import java.util.ArrayList;
import java.util.List;

public abstract class PromiseWrapper extends Promise {

    protected PromiseWrapper wrapper;
    protected List<PromiseWrapper> inners;

    protected PromiseWrapper() {
        inners = new ArrayList<>();
    }

    public Promise then(Then then) {
        PromiseThen promiseThen = new PromiseThen(then);
        this.wrapper = promiseThen;
        promiseThen.addInner(this);
        return promiseThen;
    }

    @Override
    public void cancel() {
        for (PromiseWrapper inner : inners) {
            inner.cancel();
        }
    }

    @Override
    public void resolve(Promise source, Object value) {
        super.resolve(source, value);

        if (getWrapper() != null) {
            getWrapper().transfer(this, value);
        }
    }

    protected void addInner(PromiseWrapper inner) {
        inners.add(inner);
    }

    protected PromiseWrapper getWrapper() {
        return wrapper;
    }

    protected abstract void transfer(Promise source, Object value);
}
