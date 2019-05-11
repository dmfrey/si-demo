package io.pivotal.dmfrey.sidemo.config;

import io.pivotal.dmfrey.sidemo.persistence.DataRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;

import static org.springframework.integration.dsl.Pollers.fixedRate;

@Configuration
@EnableIntegration
@EnableIntegrationManagement
public class FlowConfig {

    @Bean
    public IntegrationFlow dataRecordFlow( final JpaPollingChannelAdapter jpaPollingChannelAdapter ) {

        return IntegrationFlows.from( jpaPollingChannelAdapter, c -> c.id( "dataRecordFlow.input" ).poller( fixedRate( 60000 ) ) )
                .split()
                .log()
                .<DataRecord, String>transform(dataRecord -> dataRecord.getValue().toUpperCase() )
                .log()
                .channel( "output-channel" )
                .get();
    }

}
