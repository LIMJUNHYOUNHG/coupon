package com.example.coupon.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DataBaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseConfig.class);
    private final DataSource dataSource;

    @PostConstruct
    public void printDatabaseSource() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String url = metaData.getURL();
            String userName = metaData.getUserName();
            logger.info("Database URL: {}", url);
            logger.info("Database User: {}", userName);
        } catch (SQLException e) {
            logger.error("Failed to get database connection", e);
        }
    }
}
