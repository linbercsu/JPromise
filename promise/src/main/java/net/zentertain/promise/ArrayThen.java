package net.zentertain.promise;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ArrayThen<T> extends Then {
    @Override
    protected void then(Object value) throws Throwable {
        Object[] array = (Object[]) value;
        doRun(new WrapperList<T>(Arrays.asList(array)));
    }

    protected abstract void doRun(List<T> result);

    @SuppressWarnings({"unchecked", "NullableProblems"})
    private static class WrapperList<T> implements List<T> {
        final List inner;

        public WrapperList(List inner) {
            this.inner = inner;
        }

        @Override
        public void add(int location, T object) {
            inner.add(location, object);
        }

        @Override
        public boolean add(T object) {
            return inner.add(object);
        }

        @Override
        public boolean addAll(int location, Collection<? extends T> collection) {
            return inner.addAll(location, collection);
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            return inner.addAll(collection);
        }

        @Override
        public void clear() {
            inner.clear();
        }

        @Override
        public boolean contains(Object object) {
            return inner.contains(object);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return inner.containsAll(collection);
        }

        @Override
        public T get(int location) {
            return (T) inner.get(location);
        }

        @Override
        public int indexOf(Object object) {
            return inner.indexOf(object);
        }

        @Override
        public boolean isEmpty() {
            return inner.isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return inner.iterator();
        }

        @Override
        public int lastIndexOf(Object object) {
            return inner.lastIndexOf(object);
        }

        @Override
        public ListIterator<T> listIterator() {
            return inner.listIterator();
        }

        @Override
        public ListIterator<T> listIterator(int location) {
            return inner.listIterator(location);
        }

        @Override
        public T remove(int location) {
            return (T) inner.remove(location);
        }

        @Override
        public boolean remove(Object object) {
            return inner.remove(object);
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return inner.removeAll(collection);
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return inner.retainAll(collection);
        }

        @Override
        public T set(int location, T object) {
            return (T) inner.set(location, object);
        }

        @Override
        public int size() {
            return inner.size();
        }

        @Override
        public List<T> subList(int start, int end) {
            return inner.subList(start, end);
        }

        @Override
        public Object[] toArray() {
            return inner.toArray();
        }

        @Override
        public <T1> T1[] toArray(T1[] array) {
            return (T1[]) inner.toArray(array);
        }
    }
}
