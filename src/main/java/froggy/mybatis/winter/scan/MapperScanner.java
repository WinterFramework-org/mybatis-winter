package froggy.mybatis.winter.scan;

import froggy.mybatis.winter.annotation.Mapper;
import froggy.mybatis.winter.mapper.MapperFactory;
import froggy.winterframework.beans.factory.annotation.Autowired;
import froggy.winterframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import froggy.winterframework.beans.factory.support.BeanFactory;
import froggy.winterframework.stereotype.Component;
import froggy.winterframework.utils.WinterUtils;
import java.util.Set;
import org.apache.ibatis.session.SqlSessionFactory;

@Component
public class MapperScanner implements BeanDefinitionRegistryPostProcessor {

    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    public MapperScanner(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanFactory beanFactory) {
        registerMapper(beanFactory);
    }

    private void registerMapper(BeanFactory beanFactory) {
        String basePackage = beanFactory.resolveEmbeddedValue("basePackage");
        Set<Class<?>> mapperClasses = WinterUtils.scanTypesAnnotatedWith(Mapper.class,  basePackage);

        for (Class<?> mapper : mapperClasses) {
            beanFactory.registerSingleton(
                WinterUtils.resolveSimpleBeanName(mapper),
                MapperFactory.createMapper(mapper, sqlSessionFactory)
            );
        }
    }
}
