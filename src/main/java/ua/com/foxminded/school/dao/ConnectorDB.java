package ua.com.foxminded.school.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectorDB {

    private final HikariDataSource hikariDataSource;

    @Inject
    public ConnectorDB(@Named("PathToDatabaseProperty") String filename) {
        ResourceBundle resource = ResourceBundle.getBundle(filename);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(resource.getString("db.url"));
        config.setUsername(resource.getString("db.user"));
        config.setPassword(resource.getString("db.password"));
        hikariDataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

}
