/*
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessor;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessorCallback;
import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.service.common.client.context.IConsumerContextConfig;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareRequested;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;
import com.dell.cpsd.service.rcm.capability.PlaceholderControlPlaneRequest;
import com.dell.cpsd.service.rcm.capability.RemediationErrorMessage;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuMessageCode;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuLoggingManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RCM - Dell Firmware Update.
 * Configuration for the RabbitMQ artifacts used by a client.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class DellFwuRabbitConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = DellFwuLoggingManager.getLogger(DellFwuRabbitConfig.class);

    /*
     * The retry template information for the client.
     */
    private static final int    MAX_ATTEMPTS     = 10;
    private static final int    INITIAL_INTERVAL = 100;
    private static final double MULTIPLIER       = 2.0;
    private static final int    MAX_INTERVAL     = 50000;

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";

    /*
     * The binding key to the remediation message queue.
     */
    private static final String BINDING_REMEDIATION_REQUEST = "dell.cpsd.service.rcm.capability.update.firmware.requested";

    /*
     * The fragment of the remediation message queue name.
     */
    private static final String QUEUE_REMEDIATION_REQUEST = "queue.dell.cpsd.service.rcm.capability.update.firmware.requested";

    /*
     * The binding key to the remediation message queue.
     */
    private static final String BINDING_REMEDIATION_RESPONSE = "dell.cpsd.service.rcm.capability.update.firmware.response";

    /*
     * The fragment of the remediation message queue name.
     */
    private static final String QUEUE_REMEDIATION_RESPONSE = "queue.dell.cpsd.service.rcm.capability.update.firmware.response";

    /*
     * The name of the remediation exchange.
     */
    private static final String EXCHANGE_RCM_CAPABILITY_RESPONSE = "exchange.dell.cpsd.service.rcm.capability.response";

    /*
     * The routing key to the remediation request message queue.
     */
    public static final String ROUTING_REMEDIATION_REQUEST = "dell.cpsd.service.rcm.capability.placeholder.controlpane.request";

    /*
     * The name of the remediation data request exchange.
     */
    private static final String EXCHANGE_RCM_CAPABILITY_REQUEST = "exchange.dell.cpsd.service.rcm.capability.request";

    /*
     * The RabbitMQ connection factory.
     */
    @Autowired
    @Qualifier("rabbitConnectionFactory")
    private ConnectionFactory rabbitConnectionFactory;

    /*
     * The configuration properties for the client.
     */
    @Autowired
    @Qualifier("rabbitPropertiesConfig")
    private IRabbitMqPropertiesConfig propertiesConfig;

    /*
     * The configuration properties for the client.
     */
    @Autowired
    private IConsumerContextConfig consumerContextConfig;

    /*
     * The environment properties.
     */
    @Autowired
    private Environment environment;

    /**
     * This returns the RabbitMQ template used by the producer.
     *
     * @return The RabbitMQ the rabbit template used by the producer.
     * @since 1.0
     */
    @Bean
    RabbitTemplate rabbitTemplate()
    {
        final RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(rcmDellFwuMessageConverter());
        template.setRetryTemplate(retryTemplate());
        return template;
    }

    /**
     * This returns the <code>RetryTemplate</code> for the <code>RabbitTemplate
     * </code>.
     *
     * @return The <code>RetryTemplate</code>.
     * @since 1.0
     */
    @Bean
    RetryTemplate retryTemplate()
    {
        final RetryTemplate retryTemplate = new RetryTemplate();

        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(MULTIPLIER);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Bean
    public MessageConverter rcmDellFwuMessageConverter()
    {
        final Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(rcmDellFwuClassMapper());
        messageConverter.setCreateMessageIds(true);

        final ObjectMapper objectMapper = new ObjectMapper();

        // use ISO8601 format for dates
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        messageConverter.setJsonObjectMapper(objectMapper);

        // ignore properties we don't need or aren't expecting
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        messageConverter.setJsonObjectMapper(objectMapper);

        return messageConverter;
    }

    /**
     * This returns the <code>ClassMapper</code> for the message converter.
     *
     * @return The <ocde>ClassMapper</code> for the message converter.
     * @since SINCE-TBD
     */
    @Bean
    ClassMapper rcmDellFwuClassMapper()
    {
        //stub
        final DefaultClassMapper classMapper = new DefaultClassMapper();
        final Map<String, Class<?>> classMappings = new HashMap<>();

        final List<Class<?>> messageClasses = new ArrayList<Class<?>>();

        messageClasses.add(PlaceholderControlPlaneRequest.class);
        messageClasses.add(UpdateFirmwareRequested.class);
        messageClasses.add(UpdateFirmwareResponse.class);
        messageClasses.add(RemediationErrorMessage.class);

        final MessageAnnotationProcessor messageAnnotationProcessor = new MessageAnnotationProcessor();

        messageAnnotationProcessor.process(new MessageAnnotationProcessorCallback()
        {
            @Override
            public void found(String messageType, Class messageClass)
            {
                classMappings.put(messageType, messageClass);
            }
        }, messageClasses);

        classMapper.setIdClassMapping(classMappings);

        return classMapper;
    }

    /**
     * This returns the host name for the client.
     *
     * @return The host name for the client.
     * @since 1.0
     */
    @Bean
    String hostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException("Unable to identify hostname", e);
        }
    }

    /**
     * This returns the <code>TopicExchange</code> for RCM capability messages.
     *
     * @return The <code>TopicExchange</code> for remediation
     * @since 1.0
     */
    @Bean
    TopicExchange rcmDellFwuResponseExchange()
    {
        return new TopicExchange(EXCHANGE_RCM_CAPABILITY_RESPONSE);
    }

    /**
     * This returns the <code>TopicExchange</code> for messages from the
     * RCM capability service.
     *
     * @return The <code>TopicExchange</code> for message from the service.
     * @since 1.0
     */
    @Bean
    TopicExchange rcmDellFwuRequestExchange()
    {
        return new TopicExchange(EXCHANGE_RCM_CAPABILITY_REQUEST);
    }

    /**
     * This returns the <code>Queue</code> for for messages from the
     * RCM capability service.
     *
     * @return The <code>Queue</code> for remediation messages.
     * @since 1.0
     */
    @Bean
    Queue rcmDellFwuQueue()
    {
        final String bindingPostFix = this.getDellFwuPostfix();

        final StringBuilder builder = new StringBuilder();

        builder.append(QUEUE_REMEDIATION_RESPONSE);
        builder.append(".");
        builder.append(bindingPostFix);

        final String queueName = builder.toString();

        final Object[] lparams = {queueName};
        LOGGER.info(DellFwuMessageCode.FWU_QUEUE_I.getMessageCode(), lparams);

        boolean stateful = this.consumerContextConfig.stateful();

        final Queue queue = new Queue(queueName, true, false, !stateful);

        return queue;
    }

    /**
     * This returns the <code>Binding</code> for the remediation message
     * queue and exchange.
     *
     * @return The <code>Binding</code> for the remediation message queue.
     * @since 1.0
     */
    @Bean
    public Binding rcmDellFwuQueueBinding()
    {
        final String bindingPostFix = this.getDellFwuPostfix();

        final StringBuilder builder = new StringBuilder();

        builder.append(BINDING_REMEDIATION_RESPONSE);
        builder.append("|");
        builder.append(bindingPostFix);

        final String binding = builder.toString();

        final Object[] lparams = {binding};
        LOGGER.info(DellFwuMessageCode.FWU_BINDING_I.getMessageCode());

        return BindingBuilder.bind(rcmDellFwuQueue()).to(rcmDellFwuResponseExchange()).with(binding);
    }

    /**
     * This returns the <code>AmqpAdmin</code> for the connection factory.
     *
     * @return The AMQP admin object for the connection factory.
     * @since 1.0
     */
    @Bean
    AmqpAdmin amqpAdmin()
    {
        return new RabbitAdmin(rabbitConnectionFactory);
    }

    /**
     * This returns the generated postfix that is appended to the compliance
     * data message queue and exchange binding.
     *
     * @return The generated compute result postfix.
     * @since 1.0
     */
    private String getDellFwuPostfix()
    {
        return this.consumerContextConfig.consumerUuid();
    }
}
