package net.zentertain.promise;

import com.promise.test.promise.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 18, constants = BuildConfig.class)
public class PromiseTest {
    public static final RuntimeException THROWABLE = new RuntimeException();
    public static final RuntimeException THROWABLE2 = new RuntimeException();

    @Test
    public void should_resolved() {
        Robolectric.getForegroundThreadScheduler().pause();

        Then then = Mockito.spy(new TestThen());

        Promise.promise("1").then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then).run("1");
    }

    @Test
    public void should_resolved_case_2() {
        Robolectric.getForegroundThreadScheduler().pause();

        Then then = Mockito.spy(new TestThen());

        Promise.promise("1").then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {
                resolve(value);
            }
        }).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then).run("1");
    }

    @Test
    public void should_reject() {
        Robolectric.getForegroundThreadScheduler().pause();

        Then then = Mockito.spy(new TestThen());

        Promise.promise("1")
                .then(new TestThen2())
                .then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then).error(THROWABLE);
    }

    @Test
    public void should_cancelled() {
        Robolectric.getForegroundThreadScheduler().pause();

        Then then = Mockito.spy(new TestThen());

        TestThen2 then1 = Mockito.spy(new TestThen2());
        Promise promise = Promise.promise("1")
                .then(then1)
                .then(then);

        promise.cancel();

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then, Mockito.never()).run(Mockito.any());

        verify(then).cancel();
        verify(then1).cancel();
    }

    @Test
    public void should_test_all() {
        Robolectric.getForegroundThreadScheduler().pause();

        Promise promise1 = Promise.promise("1");
        Promise promise2 = Promise.promise("2");

        TestThen3 then = Mockito.spy(new TestThen3());
        Promise.all(promise1, promise2).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then, Mockito.times(1)).run(Mockito.any());
        assertThat(then.getResult().size()).isEqualTo(2);
        assertThat(then.getResult().get(0)).isEqualTo("1");
        assertThat(then.getResult().get(1)).isEqualTo("2");
    }

    @Test
    public void should_test_all_2() {
        Robolectric.getForegroundThreadScheduler().pause();

        Promise promise1 = Promise.promise("1").then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {

            }
        });
        Promise promise2 = Promise.promise("2");

        TestThen3 then = Mockito.spy(new TestThen3());
        Promise.all(promise1, promise2).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then, Mockito.never()).run(Mockito.any());
    }

    @Test
    public void should_test_all_reject_1() {
        Robolectric.getForegroundThreadScheduler().pause();

        Promise promise1 = Promise.promise("1").then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {
                throw THROWABLE;
            }
        });
        Promise promise2 = Promise.promise("2");

        TestThen3 then = Mockito.spy(new TestThen3());
        Promise.all(promise1, promise2).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then).error(THROWABLE);
    }

    @Test
    public void should_test_all_reject_2() {
        Robolectric.getForegroundThreadScheduler().pause();

        Promise promise1 = Promise.promise("1");
        Promise promise2 = Promise.promise("2").then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {
                throw THROWABLE;
            }
        });

        TestThen3 then = Mockito.spy(new TestThen3());
        Promise.all(promise1, promise2).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then).error(THROWABLE);
    }

    @Test
    public void should_test_all_reject_3() {
        Robolectric.getForegroundThreadScheduler().pause();

        Promise promise1 = Promise.promise("1").then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {
                throw THROWABLE;
            }
        });
        Promise promise2 = Promise.promise("2").then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {
                throw THROWABLE2;
            }
        });

        TestThen3 then = Mockito.spy(new TestThen3());
        Promise.all(promise1, promise2).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then, Mockito.times(1)).error(THROWABLE);
    }

    @Test
    public void should_test_race() {
        Robolectric.getForegroundThreadScheduler().pause();

        Promise promise1 = Promise.promise("1");
        Promise promise2 = Promise.promise("2");

        TestThen then = Mockito.spy(new TestThen());
        Promise.race(promise1, promise2).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then, Mockito.times(1)).run("1");
    }

    @Test
    public void should_test_race_reject() {
        Robolectric.getForegroundThreadScheduler().pause();

        Promise promise1 = Promise.promise("1").then(new TestThen2());
        Promise promise2 = Promise.promise("2").then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {

            }
        });

        TestThen then = Mockito.spy(new TestThen());
        Promise.race(promise1, promise2).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then, Mockito.times(1)).error(THROWABLE);
    }

    @Test
    public void should_test_asyncThen() throws Throwable {
        Robolectric.getForegroundThreadScheduler().pause();

        TestThen then = Mockito.spy(new TestThen());

        Promise.promise("1").then(new AsyncThen<String>() {
            @Override
            protected Object doInBackground(String value) throws Throwable {
                return value;
            }
        }).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();

        verify(then, Mockito.times(1)).then("1");
    }

    @Test
    public void should_test_asyncThen_cancel() throws Throwable {
        Robolectric.getForegroundThreadScheduler().pause();
        Robolectric.getBackgroundThreadScheduler().pause();

        TestThen then = Mockito.spy(new TestThen());

        Promise promise = Promise.promise("1").then(new AsyncThen<String>() {
            @Override
            protected Object doInBackground(String value) throws Throwable {
                return value;
            }
        }).then(then);

        Robolectric.getForegroundThreadScheduler().unPause();
        promise.cancel();
        Robolectric.flushBackgroundThreadScheduler();

        verify(then, Mockito.never()).then(Mockito.anyString());
    }


    public static class TestThen extends Then<String> {

        @Override
        protected void then(String value) throws Throwable {

        }
    }

    public static class TestThen2 extends Then {

        @Override
        protected void then(Object value) throws Throwable {
            throw THROWABLE;
        }
    }

    public static class TestThen3 extends ArrayThen<String> {
        List<String> result;

        @Override
        protected void doRun(List<String> result) {
            this.result = result;
        }

        public List<String> getResult() {
            return result;
        }
    }

}