package froggy.mybatis.winter;

import froggy.winterframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

@Component
public class SqlSessionFactory extends DefaultSqlSessionFactory {

    public SqlSessionFactory() {
        this(loadConfiguration());
    }

    public SqlSessionFactory(Configuration configuration) {
        super(configuration);
    }

    private static Configuration loadConfiguration() {
        String resource = "mybatis-config.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream);
            return parser.parse();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MyBatis configuration: " + resource, e);
        }
    }

}
