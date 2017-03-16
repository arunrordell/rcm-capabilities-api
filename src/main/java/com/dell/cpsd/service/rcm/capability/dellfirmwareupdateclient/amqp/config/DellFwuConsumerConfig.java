/*
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.retrypolicy.RetryPolicyExceptionUnpackerDelegate;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.DellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuMessageCode;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * RCM - Dell Firmware Update Client.
 * The configuration for the message consumers.
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class DellFwuConsumerConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = DellFwuLoggingManager.getLogger(DellFwuConsumerConfig.class);

    /*
     * The RabbitMQ connection factory.
     */
    @Autowired
    @Qualifier("rabbitConnectionFactory")
    private ConnectionFactory rabbitConnectionFactory;

    /*
     * The message converter to be used.
     */
    @Autowired
    private MessageConverter rcmDellFwuMessageConverter;

    /*
     * The queue for Dell Firmware update
     */
    @Autowired
    @Qualifier("rcmDellFwuQueue")
    private Queue rcmDellFwuQueue;

    /*
     * The message queue binding for compliance data messages.
     */
    @Autowired
    private Binding rcmDellFwuQueueBinding;

    /**
     * The compliance data response message listener container.
     *
     * @return The compliance data response message listener container.
     * @since 1.0
     */
    @Bean
    SimpleMessageListenerContainer rcmDellFwuListenerContainer()
    {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setQueues(rcmDellFwuQueue);
        container.setAdviceChain(new Advice[] {retryPolicy()});
        container.setMessageListener(new MessageListenerAdapter(rcmDellFwuConsumer(), rcmDellFwuMessageConverter));
        container.setErrorHandler(errorHandler("rcmCapabilitiesClientContainer"));

        return container;
    }

    /**
     * The message consumer for compliance data responses.
     *
     * @return The message consumer for compliance data responses.
     * @since 1.0
     */
    @Bean
    IDellFwuAmqpConsumer rcmDellFwuConsumer()
    {
        final String routingKey = this.rcmDellFwuQueueBinding.getRoutingKey();

        return new DellFwuAmqpConsumer(routingKey);
    }

    /**
     * The error handler for the compliance data response listener container.
     *
     * @param listenerName The name of the listener.
     * @return The error handler compliance data response listener container.
     * @since 1.0
     */
    ErrorHandler errorHandler(final String listenerName)
    {
        return cause ->
        {
            final Object[] lparams = {listenerName, cause.getMessage()};
            LOGGER.error(DellFwuMessageCode.RABBIT_AMQP_LISTENER_E.getMessageCode(), lparams, cause);
        };
    }

    /**
     * The retry operations interceptor for the consumer.
     *
     * @return The retry operations interceptor for the consumer.
     * @since 1.0
     */
    @Bean
    RetryOperationsInterceptor retryPolicy()
    {
        // Delegate handler that unpacks the throwable to get the root
        // offending exception so we can set a specific policy for it.
        final RetryPolicyExceptionUnpackerDelegate retryPolicyExceptionUnpackerDelegate = new RetryPolicyExceptionUnpackerDelegate(
                ourExceptionRetryPolicy());

        final RetryTemplate retryTemplate = new RetryTemplate();
        final BackOffPolicy backoffPolicy = new ExponentialBackOffPolicy();
        retryTemplate.setBackOffPolicy(backoffPolicy);
        retryTemplate.setRetryPolicy(retryPolicyExceptionUnpackerDelegate);

        final RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
        interceptor.setRetryOperations(retryTemplate);

        return interceptor;
    }

    /**
     * The returns an exception based retry policy.
     *
     * @return A new instance of an exception based retry policy.
     * @since 1.0
     */
    private RetryPolicy ourExceptionRetryPolicy()
    {
        final ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();

        final Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
        policyMap.put(ClassNotFoundException.class, new NeverRetryPolicy());
        // Default max attempts is 3. After the third attempt, the message is logged.
        policyMap.put(Exception.class, new SimpleRetryPolicy());
        policyMap.put(ListenerExecutionFailedException.class, new SimpleRetryPolicy());
        policyMap.put(MessageConversionException.class, new NeverRetryPolicy());
        exceptionClassifierRetryPolicy.setPolicyMap(policyMap);

        return exceptionClassifierRetryPolicy;
    }
}
