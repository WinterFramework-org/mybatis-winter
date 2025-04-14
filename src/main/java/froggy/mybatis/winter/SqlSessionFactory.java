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

    public SqlSessionFactory() throws IOException {
        this(loadConfiguration());
    }

    public SqlSessionFactory(Configuration configuration) {
        super(configuration);
    }

    private static Configuration loadConfiguration() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream);
        return parser.parse();
    }

}
