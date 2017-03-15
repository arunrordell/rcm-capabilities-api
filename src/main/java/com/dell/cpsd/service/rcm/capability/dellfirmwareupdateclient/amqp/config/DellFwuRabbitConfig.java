/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessor;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessorCallback;
import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.service.common.client.context.IConsumerContextConfig;
import com.dell.cpsd.service.rcm.capability.ControlPlaneResponse;
import com.dell.cpsd.service.rcm.capability.PlaceholderControlPlaneRequest;
import com.dell.cpsd.service.rcm.capability.RemediationErrorMessage;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSMessageCode;
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
 * This is the configuration for the RabbitMQ artifacts used by a client.
 *
 * <p/>
 * CCopyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
@Configuration
public class DellFwuRabbitConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = RRSLoggingManager.getLogger(DellFwuRabbitConfig.class);

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
    private static final String BINDING_REMEDIATION_REQUEST = "com.dell.cpsd.remediation.request";

    /*
     * The fragment of the remediation message queue name.
     */
    private static final String QUEUE_REMEDIATION_REQUEST    = "queue.dell.cpsd.remediation.request";
    /*
 * The binding key to the remediation message queue.
 */
    private static final String BINDING_REMEDIATION_RESPONSE = "com.dell.cpsd.remediation.response";

    /*
     * The fragment of the remediation message queue name.
     */
    private static final String QUEUE_REMEDIATION_RESPONSE    = "queue.dell.cpsd.remediation.response";
    /*
     * The name of the remediation exchange.
     */
    private static final String EXCHANGE_REMEDIATION_RESPONSE = "exchange.dell.cpsd.remediation.response";

    /*
    * The routing key to the remediation request message queue.
    */
    public static final String ROUTING_REMEDIATION_REQUEST = "com.dell.cpsd.remediation.request";

    /*
     * The name of the remediation data request exchange.
     */
    private static final String EXCHANGE_REMEDIATION_REQUEST = "exchange.dell.cpsd.remediation.request";

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
     *
     * @since SINCE-TBD
     */
    @Bean
    RabbitTemplate rabbitTemplate()
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(RemediationMessageConverter());
        template.setRetryTemplate(retryTemplate());
        return template;
    }

    /**
     * This returns the <code>RetryTemplate</code> for the <code>RabbitTemplate
     * </code>.
     *
     * @return The <code>RetryTemplate</code>.
     *
     * @since SINCE-TBD
     */
    @Bean
    RetryTemplate retryTemplate()
    {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(MULTIPLIER);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Bean
    public MessageConverter RemediationMessageConverter()
    {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(RemediationClassMapper());
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
     *
     * @since SINCE-TBD
     */
    @Bean
    ClassMapper RemediationClassMapper()
    {
        //stub
        final DefaultClassMapper classMapper = new DefaultClassMapper();
        final Map<String, Class<?>> classMappings = new HashMap<>();

        List<Class<?>> messageClasses = new ArrayList<Class<?>>();

        messageClasses.add(PlaceholderControlPlaneRequest.class);
        messageClasses.add(ControlPlaneResponse.class);
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
     *
     * @since SINCE-TBD
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
     * This returns the <code>TopicExchange</code> for remediation
     * messages.
     *
     * @return The <code>TopicExchange</code> for remediation
     *
     * @since SINCE-TBD
     */
    @Bean
    TopicExchange RemediationExchangeResponse()
    {
        return new TopicExchange(EXCHANGE_REMEDIATION_RESPONSE);
    }

    /**
     * This returns the <code>TopicExchange</code> for messages from the
     * remediation service.
     *
     * @return The <code>TopicExchange</code> for message from the service.
     *
     * @since SINCE-TBD
     */
    @Bean
    TopicExchange DellFwuRequestExchange()
    {
        return new TopicExchange(EXCHANGE_REMEDIATION_REQUEST);
    }

    /**
     * This returns the <code>Queue</code> for for messages from the
     * remediation service.
     *
     * @return The <code>Queue</code> for remediation messages.
     *
     * @since SINCE-TBD
     */
    @Bean
    Queue RemediationQueue()
    {
        String bindingPostFix = this.getDellFwuPostfix();

        StringBuilder builder = new StringBuilder();

        builder.append(QUEUE_REMEDIATION_RESPONSE);
        builder.append(".");
        builder.append(bindingPostFix);

        String queueName = builder.toString();

        Object[] lparams = {queueName};
        LOGGER.info(RRSMessageCode.HAL_QUEUE_I.getMessageCode(), lparams);

        boolean stateful = this.consumerContextConfig.stateful();

        Queue queue = new Queue(queueName, true, false, !stateful);

        return queue;
    }

    /**
     * This returns the <code>Binding</code> for the remediation message
     * queue and exchange.
     *
     * @return The <code>Binding</code> for the remediation message queue.
     *
     * @since SINCE-TBD
     */
    @Bean
    public Binding RemediationQueueBinding()
    {
        String bindingPostFix = this.getDellFwuPostfix();

        StringBuilder builder = new StringBuilder();

        builder.append(BINDING_REMEDIATION_RESPONSE);
        builder.append("|");
        builder.append(bindingPostFix);

        String binding = builder.toString();

        Object[] lparams = {binding};
        LOGGER.info(RRSMessageCode.HAL_BINDING_I.getMessageCode());

        return BindingBuilder.bind(RemediationQueue()).to(RemediationExchangeResponse()).with(binding);
    }

    /**
     * The returns the routing key for the remediation request message queue.
     *
     * @return The routing key for the remediation request message queue.
     *
     * @since SINCE-TBD
     */

    /**
     * This returns the <code>AmqpAdmin</code> for the connection factory.
     *
     * @return The AMQP admin object for the connection factory.
     *
     * @since SINCE-TBD
     */
    @Bean
    AmqpAdmin amqpAdmin()
    {
        return new RabbitAdmin(rabbitConnectionFactory);
    }

    /*
     * This returns the generated postfix that is appended to the compliance
     * data message queue and exchange binding.
     *
     * @return  The generated compute result postfix.
     *
     * @since   SINCE-TBD
     */
    private String getDellFwuPostfix()
    {
        return this.consumerContextConfig.consumerUuid();
    }
}