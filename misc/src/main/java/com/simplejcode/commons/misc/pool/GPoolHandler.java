package com.simplejcode.commons.misc.pool;

public interface GPoolHandler<T> {

    T create() throws Exception;

    void destroy(T object) throws Exception;

}
