package froggy.mybatis.winter.mapper;

import java.lang.reflect.Proxy;
import org.apache.ibatis.session.SqlSessionFactory;

public class MapperFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createMapper(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        return (T) Proxy.newProxyInstance(
            mapperInterface.getClassLoader(),
            new Class[]{mapperInterface},
            new MapperProxy(mapperInterface, sqlSessionFactory)
        );
    }
}