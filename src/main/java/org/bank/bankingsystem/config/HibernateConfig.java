package org.bank.bankingsystem.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class HibernateConfig {

    @Bean
    public DataSource dataSource() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/bank_db");
        config.setUsername("root");
        config.setPassword("root123");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);

        return new HikariDataSource(config);
    }

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .applySetting("hibernate.show_sql", "true")
                .applySetting("hibernate.hbm2ddl.auto", "update")
                .applySetting("hibernate.connection.datasource", dataSource)
                .build();

        MetadataSources sources = new MetadataSources(registry);

        sources.addAnnotatedClass(org.bank.bankingsystem.model.Account.class);
        sources.addAnnotatedClass(org.bank.bankingsystem.model.Transaction.class);
        sources.addAnnotatedClass(org.bank.bankingsystem.model.TransactionEvent.class);
        sources.addAnnotatedClass(org.bank.bankingsystem.model.TransactionHistory.class);

        Metadata metadata = sources.getMetadataBuilder().build();

        return metadata.getSessionFactoryBuilder().build();
    }
}