package froggy.mybatis.winter.mapper;

import java.lang.reflect.Proxy;

public class MapperFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createMapper(Class<T> mapperInterface) {
        return (T) Proxy.newProxyInstance(
            mapperInterface.getClassLoader(),
            new Class[]{mapperInterface},
            new MapperProxy(mapperInterface)
        );
    }
}