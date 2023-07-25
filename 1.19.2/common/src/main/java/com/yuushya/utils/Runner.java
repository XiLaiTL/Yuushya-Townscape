package com.yuushya.utils;

import java.util.Objects;

/**
 * Represents an operation that accepts no input argument and returns no result.
 */

public interface Runner{
    void run();
    default Runner andThen(Runner after){
        Objects.requireNonNull(after);
        return ()->{run();after.run();};
    }
}
