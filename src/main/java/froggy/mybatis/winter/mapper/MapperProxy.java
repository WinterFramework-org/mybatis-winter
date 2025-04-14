package froggy.mybatis.winter.mapper;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MapperProxy implements InvocationHandler {
    private final Class<?> mapperInterface;

    public MapperProxy(Class<?> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        Configuration configuration = sqlSessionFactory.getConfiguration();
        String statementId = mapperInterface.getName() + "." + method.getName();

        MappedStatement ms = configuration.getMappedStatement(statementId);
        SqlSession session = sqlSessionFactory.openSession(true);

        if (ms == null) return null;

        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            return session.insert(statementId, args[0]);
        } else if (ms.getSqlCommandType() == SqlCommandType.SELECT) {
            // 단일 검색, 다중검색 로직 필요
            return session.selectList(statementId);
//            return session.selectOne(statementId);
        } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
            return session.update(statementId, args[0]);
        } else if (ms.getSqlCommandType() == SqlCommandType.DELETE) {
            return session.delete(statementId, args[0]);
        }

        return null;
    }
}
