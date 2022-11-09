package io.github.edsuns;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @date 2022/10/28 14:37
 */
@JsonSerialize(using = ArgStdSerializer.class)
public class Arg<T, E> implements Serializable {

    private static final long serialVersionUID = -5807924615070041358L;

    private static final Function<Object, RuntimeException> DEFAULT_ERROR_HANDLER =
        err -> new ArgException(err.toString());

    @JsonProperty
    private final T value;

    @JsonProperty
    private final E error;

    /**
     * just for deserialization
     */
    Arg() {
        this.value = null;
        this.error = null;
    }

    /**
     * @throws NullPointerException if both value and error is null
     */
    protected Arg(T value, E error) throws NullPointerException {
        // just notify "value == null" when error is null
        // because if both value and error are not null it will throw the error later
        if (value == null && error == null) throw new NullPointerException("value == null");
        if (value != null && error != null) error = null;
        this.value = value;
        this.error = error;
    }

    public static <T, E> Arg<T, E> of(T value, @Nonnull E error) {
        requireNonNull(error);
        return new Arg<>(value, error);
    }

    public static <T, E> Arg<T, E> notNull(@Nonnull T value) {
        return new Arg<>(value, null);
    }

    public static <T, E> Arg<T, E> error(@Nonnull E error) {
        return new Arg<>(null, error);
    }

    public boolean isError() {
        return error != null;
    }

    public E getError() {
        return requireNonNull(this.error);
    }

    @SuppressWarnings("unchecked")
    public <X extends Arg<?, E>> X cast() {
        if (!isError()) throw new IllegalStateException("can not cast a non-error Arg");
        return (X) this;
    }

    public T unwrap() throws RuntimeException {
        return unwrap(getDefaultErrorHandler());
    }

    public <X extends Throwable> T unwrap(Function<E, ? extends X> errorHandler) throws X {
        if (isError()) {
            throw requireNonNull(errorHandler.apply(this.error));
        }
        return this.value;
    }

    public <U, W> Arg<U, W> map(Function<? super T, ? extends U> mapper, W error) {
        requireNonNull(mapper);
        requireNonNull(error);
        if (isError()) {
            return error(error);
        } else {
            U mapped = mapper.apply(this.value);
            return mapped != null ? notNull(mapped) : new Arg<>(null, error);
        }
    }

    /**
     * map Arg value to other value with different type or not
     *
     * @param mapper required not null
     * @return mapped {@link Arg}
     * @throws NullPointerException if {@link Function#apply} of mapper returns null
     */
    public <U> Arg<U, E> map(Function<? super T, ? extends U> mapper) throws NullPointerException {
        requireNonNull(mapper);
        if (isError()) {
            return this.cast();
        } else {
            return notNull(mapper.apply(this.value));
        }
    }

    public <U, W> Arg<U, W> flatMap(Function<? super T, Arg<U, W>> mapper, W error) {
        requireNonNull(mapper);
        requireNonNull(error);
        if (isError()) {
            return error(error);
        } else {
            return requireNonNull(mapper.apply(this.value));
        }
    }

    public <U, X extends Arg<U, E>> X flatMap(Function<? super T, X> mapper) {
        requireNonNull(mapper);
        if (isError()) {
            return this.cast();
        } else {
            return requireNonNull(mapper.apply(this.value));
        }
    }

    @SuppressWarnings("unchecked")
    public Function<E, RuntimeException> getDefaultErrorHandler() {
        return (Function<E, RuntimeException>) DEFAULT_ERROR_HANDLER;
    }

    /**
     * Indicates whether some other object is "equal to" this Arg. The
     * other object is considered equal if:
     * <ul>
     * <li>it is also an {@code Arg} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Arg)) {
            return false;
        }

        Arg<?, ?> other = (Arg<?, ?>) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return this.value != null ? "Arg[value="+this.value+"]" : "Arg[error="+this.error+"]";
    }
}
