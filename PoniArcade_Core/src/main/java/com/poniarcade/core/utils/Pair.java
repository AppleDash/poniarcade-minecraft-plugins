//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.poniarcade.core.utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Objects;

public abstract class Pair<L, R> implements Entry<L, R>, Serializable {
    @Serial
    private static final long serialVersionUID = 4954918890077093841L;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new ImmutablePair<>(left, right);
    }

    public abstract L getLeft();

    public abstract R getRight();

    public final L getKey() {
        return this.getLeft();
    }

    public R getValue() {
        return this.getRight();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Entry<?, ?> other) {
            return Objects.equals(this.getKey(), other.getKey()) && Objects.equals(this.getValue(), other.getValue());
        }

        return false;
    }

    public int hashCode() {
        return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
    }

    public String toString() {
        return String.valueOf('(') + this.getLeft() + ',' + this.getRight() + ')';
    }

    public String toString(String format) {
        return String.format(format, this.getLeft(), this.getRight());
    }
}
