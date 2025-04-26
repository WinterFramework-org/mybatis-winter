package froggy.mybatis.winter.scan;

import froggy.mybatis.winter.SqlSessionFactory;
import froggy.mybatis.winter.annotation.Mapper;
import froggy.mybatis.winter.mapper.MapperFactory;
import froggy.winterframework.beans.factory.annotation.Autowired;
import froggy.winterframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import froggy.winterframework.beans.factory.support.BeanFactory;
import froggy.winterframework.stereotype.Component;
import froggy.winterframework.utils.WinterUtils;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
        Set<String> basePackageClassNames = getFullyQualifiedClassNamesByBasePackage();

        List<Class<?>> mapperClasses = findMapperClasses(basePackageClassNames);

        for (Class<?> mapper : mapperClasses) {
            beanFactory.registerSingleton(
                WinterUtils.resolveSimpleBeanName(mapper),
                MapperFactory.createMapper(mapper, sqlSessionFactory)
            );
        }
    }

    private java.util.Set<String> getFullyQualifiedClassNamesByBasePackage() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");

        File directory = new File(resource.getFile());

        Set<String> fullQualifiedClassNames = new LinkedHashSet<>();
        if (directory.exists() && directory.isDirectory()) {
            scanDirectory(directory, "", fullQualifiedClassNames);
        }
        return fullQualifiedClassNames;
    }

    private void scanDirectory(File directory, String packageName, Set<String> fullQualifiedClassNames) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + file.getName() + ".", fullQualifiedClassNames);
            }

            if (file.getName().endsWith(".class")) {
                String className = packageName + file.getName().replace(".class", "");
                fullQualifiedClassNames.add(className);
            }
        }
    }

    private List<Class<?>> findMapperClasses(Set<String> classNames) {
        List<Class<?>> mapperClasses = new ArrayList<>();

        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (WinterUtils.hasAnnotation(clazz, Mapper.class)) {
                    mapperClasses.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found: " + className, e);
            }
        }

        return mapperClasses;
    }
}
