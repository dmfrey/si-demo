package io.pivotal.dmfrey.sidemo.config;

import io.pivotal.dmfrey.sidemo.persistence.DataRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jpa.core.JpaExecutor;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;

import javax.persistence.EntityManager;

@Configuration
public class IntegrationConfig {

    @Bean
    public JpaExecutor jpaExecutor( final ApplicationContext context, final EntityManager entityManager ) {

        JpaExecutor jpaExecutor = new JpaExecutor( entityManager );
        jpaExecutor.setEntityClass( DataRecord.class );
        jpaExecutor.setBeanFactory( context );
        jpaExecutor.afterPropertiesSet();

        return jpaExecutor;
    }

    @Bean
    public JpaPollingChannelAdapter jpaPollingChannelAdapter( final JpaExecutor jpaExecutor ) {

        return new JpaPollingChannelAdapter( jpaExecutor );
    }

}
