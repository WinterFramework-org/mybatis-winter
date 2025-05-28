package froggy.mybatis.winter.scan;

import froggy.mybatis.winter.SqlSessionFactory;
import froggy.mybatis.winter.annotation.Mapper;
import froggy.mybatis.winter.mapper.MapperFactory;
import froggy.winterframework.beans.factory.annotation.Autowired;
import froggy.winterframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import froggy.winterframework.beans.factory.support.BeanFactory;
import froggy.winterframework.stereotype.Component;
import froggy.winterframework.utils.WinterUtils;
import java.util.Set;

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
        // TODO: BasePackage 경로 어떻게 찾아올지 구상하기
        String basePackage = System.getProperty("basePackage");
        System.out.println("basePackage = " + basePackage);
        Set<Class<?>> mapperClasses = WinterUtils.scanTypesAnnotatedWith(Mapper.class,  basePackage);

        for (Class<?> mapper : mapperClasses) {
            beanFactory.registerSingleton(
                WinterUtils.resolveSimpleBeanName(mapper),
                MapperFactory.createMapper(mapper, sqlSessionFactory)
            );
        }
    }
}
