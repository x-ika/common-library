package com.simplejcode.commons.misc._pattern.pool;

public interface IPoolHandler<T> {

    T create() throws Exception;

    void destroy(T object) throws Exception;

}
