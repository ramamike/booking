package com.blockwit.booking.service.Utils;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class WithOptional {

    public static <T, R> R process(Optional<T> optional, Supplier<R> error, Function<T, R> success) {
        return optional.isPresent() ? success.apply(optional.get()) : error.get();
    }
}
