package io.pivotal.dmfrey.sidemo;

import io.pivotal.dmfrey.sidemo.config.FlowConfig;
import io.pivotal.dmfrey.sidemo.config.IntegrationConfig;
import io.pivotal.dmfrey.sidemo.persistence.DataRecord;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.integration.test.util.OnlyOnceTrigger;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith( SpringRunner.class )
@ContextConfiguration( classes = { FlowConfig.class, IntegrationManagementTests.Config.class } )
@SpringIntegrationTest
public class IntegrationManagementTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Autowired
    @Qualifier( "output-channel" )
    private QueueChannel results;

    @Autowired
    private ArgumentCaptor<Message<?>> argumentCaptorForOutputTest;

    @Autowired
    private ArgumentCaptor<Message<?>> messageArgumentCaptor;

    @MockBean
    private JpaPollingChannelAdapter jpaPollingChannelAdapter;

    @After
    public void tearDown() {

        this.mockIntegrationContext.resetBeans();
        this.results.purge(null );

    }

    @Test
    public void testMockMessageSource() {

        DataRecord fakeDataRecord = DataRecord.builder()
                .id( 1L )
                .value( "I should be uppercase" )
                .build();

        this.mockIntegrationContext
                .substituteMessageSourceFor("dataRecordFlow.input",
                MockIntegration.mockMessageSource( fakeDataRecord ) );

        Message<?> receive = this.results.receive( 10_000 );
        assertThat( receive ).isNotNull();
        assertThat( receive.getPayload() ).isEqualTo( "I SHOULD BE UPPERCASE" );

    }

    @Configuration
    public static class Config {

        @Bean
        public OnlyOnceTrigger onlyOnceTrigger() {

            return new OnlyOnceTrigger();
        }

        @Bean
        public ArgumentCaptor<Message<?>> messageArgumentCaptor() {

            return MockIntegration.messageArgumentCaptor();
        }

        @Bean
        public ArgumentCaptor<Message<?>> argumentCaptorForOutputTest() {

            return MockIntegration.messageArgumentCaptor();
        }

        @Bean( name = PollerMetadata.DEFAULT_POLLER )
        public PollerSpec defaultPoller() {

            return Pollers.fixedDelay( 10 );
        }

        @Bean( "output-channel" )
        public QueueChannel results() {

            return new QueueChannel();
        }

    }

}
