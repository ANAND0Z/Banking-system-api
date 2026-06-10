package org.bank.bankingsystem.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.model.Transaction;
import org.bank.bankingsystem.model.Beneficiary;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

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

        Properties hibernateProps = new Properties();
        hibernateProps.put(
                "hibernate.dialect",
                "org.hibernate.dialect.MySQLDialect"
        );
        hibernateProps.put("hibernate.show_sql", "true");
        hibernateProps.put("hibernate.format_sql", "true");
        hibernateProps.put("hibernate.hbm2ddl.auto", "update");
        hibernateProps.put(
                "hibernate.current_session_context_class",
                "org.springframework.orm.hibernate5.SpringSessionContext"
        );


        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .applySettings(hibernateProps)
                        .applySetting("hibernate.connection.datasource", dataSource)
                        .build();

        MetadataSources sources = new MetadataSources(registry)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Transaction.class)
                .addAnnotatedClass(Beneficiary.class);

        return sources.buildMetadata().buildSessionFactory();
    }
}