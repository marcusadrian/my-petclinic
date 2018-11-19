package org.adrian.mypetclinic.transform;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GenericTransformer<S, T> implements Function<S, T>, BiConsumer<S, T> {

    private Supplier<T> targetSupplier;
    private BiConsumer<S, T> updater;

    public GenericTransformer(Supplier<T> targetSupplier, BiConsumer<S, T> updater) {
        super();
        this.targetSupplier = targetSupplier;
        this.updater = updater;
    }

    /**
     * Updates target with the source attributes.
     *
     * @param source not {@code null}
     * @param target not {@code null}
     * @return {@code null} if source is {@code null}, otherwise the transformed object
     */
    @Override
    public void accept(S source, T target) {
        Objects.requireNonNull(source, "source == null");
        Objects.requireNonNull(target, "target == null");
        doTransform(source, target);
    }

    /**
     * Transforms a single source object into a target object.
     *
     * @param source might be {@code null}
     * @return {@code null} if source is {@code null}, otherwise the transformed object
     */
    @Override
    public T apply(S source) {
        if (source == null) {
            return null;
        } else {
            T target = targetSupplier.get();
            doTransform(source, target);
            return target;
        }
    }

    /**
     * Transforms a collection of source objects into target objects.
     *
     * @param sources might be {@code null}
     * @param sort    order function for the target list
     * @return never {@code null} unless sources is {@code null}
     */
    public List<T> apply(Collection<? extends S> sources, Comparator<? super T> sort) {
        if (sources == null) {
            return null;
        }
        List<T> target = apply(sources, new ArrayList<>());
        if (sort != null) {
            target.sort(sort);
        }
        return target;
    }

    /**
     * Transforms a collection of source objects into target objects.
     *
     * @param sources might be {@code null}
     * @return never {@code null} unless sources is {@code null}
     */
    public List<T> apply(Collection<? extends S> sources) {
        return apply(sources, (Comparator<? super T>) null);
    }

    /**
     * Transforms the sources objects and adds them to the passed in targets collection.
     *
     * @param sources might be {@code null}
     * @param targets not {@code null}
     * @return the passed in {@code targets} parameter to enable a fluent programming style
     */
    public <C extends Collection<? super T>> C apply(Collection<? extends S> sources, C targets) {
        Objects.requireNonNull(targets, "targets == null");
        if (sources != null) {
            for (S source : sources) {
                targets.add(apply(source));
            }
        }
        return targets;
    }

    private void doTransform(S source, T target) {
        updater.accept(source, target);
    }
}
