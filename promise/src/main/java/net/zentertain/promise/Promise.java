package net.zentertain.promise;

import net.zentertain.promise.impl.PromiseAll;
import net.zentertain.promise.impl.PromiseRace;
import net.zentertain.promise.impl.PromiseSimple;

public abstract class Promise {

    private Status status;

    public static Promise promise(Object object) {
        return new PromiseSimple(object);
    }

    public static Promise all(Promise... promises) {
        return new PromiseAll(promises);
    }

    public static Promise race(Promise...promises) {
        return new PromiseRace(promises);
    }

    public abstract Promise then(Then then);

    public abstract void cancel();

    public boolean isPending() {
        return status == Status.Pending;
    }

    public boolean isRejected() {
        return status == Status.Rejected;
    }

    public boolean isResolved() {
        return status == Status.Resolved;
    }

    protected Promise() {
        status = Status.Pending;
    }

    public void resolve(Promise wrapper, Object value) {
        if (status != Status.Pending)
            throw new RuntimeException();

        status = Status.Resolved;
    }
    public void reject(Throwable throwable) {
        if (status != Status.Pending)
            throw new RuntimeException();

        status = Status.Rejected;
    }

    protected static enum Status {
        Pending,
        Resolved,
        Rejected
    }
}
