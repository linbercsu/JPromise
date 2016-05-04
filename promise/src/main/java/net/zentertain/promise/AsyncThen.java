package net.zentertain.promise;

import android.os.AsyncTask;

public abstract class AsyncThen<T> extends Then<T> {

    private AsyncTask<T, Integer, Object> task;

    public AsyncThen() {
        task = new AsyncTask<T, Integer, Object>() {

            @Override
            protected Object doInBackground(T... params) {
                try {
                    return AsyncThen.this.doInBackground(params[0]);
                }catch (Throwable e) {
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (o instanceof Throwable) {
                    reject((Throwable) o);
                }else {
                    resolve(o);
                }
            }
        };
    }

    @Override
    protected void then(T value) throws Throwable {
        if (task != null) {
            task.execute(value);
        }
    }

    protected abstract Object doInBackground(T value)  throws Throwable;

    @Override
    protected void cancel() {
        super.cancel();

        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }
}
