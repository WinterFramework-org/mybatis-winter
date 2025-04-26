package froggy.mybatis.winter.mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MapperProxy implements InvocationHandler {
    private final Class<?> mapperInterface;
    private final SqlSessionFactory sqlSessionFactory;


    public MapperProxy(Class<?> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.mapperInterface = mapperInterface;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        String statementId = mapperInterface.getName() + "." + method.getName();
        MappedStatement ms = configuration.getMappedStatement(statementId);

        return executeSqlCommand(method, ms, args);
    }

    private Object executeSqlCommand(Method method, MappedStatement ms, Object[] args) {
        if (ms == null) return null;

        MapperMethod mapperMethod = new MapperMethod(mapperInterface, method,
            ms.getConfiguration());
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            return mapperMethod.execute(session, args);
        }
    }
}
