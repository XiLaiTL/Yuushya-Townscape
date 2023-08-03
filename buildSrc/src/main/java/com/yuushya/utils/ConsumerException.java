package com.yuushya.utils;
@FunctionalInterface
public interface ConsumerException<T> {
    void accept(T t) throws Exception;
}
