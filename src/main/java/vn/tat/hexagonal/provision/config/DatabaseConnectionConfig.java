package vn.tat.hexagonal.provision.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:52
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "data-provision")
public class DatabaseConnectionConfig {

    private ConnectionConfig source;

    private ConnectionConfig target;

    @Data
    public static class ConnectionConfig{
        private String username;
        private String password;
        private String url;
    }
}