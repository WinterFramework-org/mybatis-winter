package froggy.mybatis.winter;

import froggy.winterframework.beans.factory.annotation.Autowired;
import froggy.winterframework.beans.factory.annotation.Value;
import froggy.winterframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

@Component
public class SqlSessionFactory extends DefaultSqlSessionFactory {

    private static final String MYBATIS_CONFIG = "mybatis-config.xml";

    @Autowired
    public SqlSessionFactory(
        @Value("winter.datasource.url") String url,
        @Value("winter.datasource.username") String username,
        @Value("winter.datasource.password") String password
    ) {
        super(load(url, username, password));
    }

    private static Configuration load(String url, String username, String password) {
        try (InputStream in = Resources.getResourceAsStream(MYBATIS_CONFIG)) {
            Configuration cfg = new XMLConfigBuilder(in).parse();

            PooledDataSource ds = (PooledDataSource) cfg.getEnvironment().getDataSource();
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);

            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
