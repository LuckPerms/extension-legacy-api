package me.lucko.luckperms.extension.legacyapi.impl.misc;

import com.google.common.collect.ForwardingCollection;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class FakeSortedSet<E> extends ForwardingCollection<E> implements SortedSet<E> {

    public static <E> FakeSortedSet<E> backedByLinkedHashSet() {
        return new FakeSortedSet<>(new LinkedHashSet<>());
    }

    private final Collection<E> backing;

    public FakeSortedSet(Collection<E> backing) {
        this.backing = backing;
    }

    @Override
    protected Collection<E> delegate() {
        return this.backing;
    }

    @Override
    public Comparator<? super E> comparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E first() {
        for (E e : this.backing) {
            return e;
        }
        throw new NoSuchElementException();
    }

    @Override
    public E last() {
        E item = null;
        for (E e : this.backing) {
            item = e;
        }
        if (item == null) {
            throw new NoSuchElementException();
        }
        return item;
    }

}
