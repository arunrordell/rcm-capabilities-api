/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.retrypolicy.RetryPolicyExceptionUnpackerDelegate;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.DellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSMessageCode;
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
 * The configuration for the message consumers.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
@Configuration
public class DellFwuConsumerConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = RRSLoggingManager.getLogger(DellFwuConsumerConfig.class);

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
    private MessageConverter DellFwuMessageConverter;

    /*
     * The queue for compliance data response messages.
     */
    @Autowired
    @Qualifier("RemediationQueue")
    private Queue RemediationQueue;

    /*
     * The message queue binding for compliance data messages.
     */
    @Autowired
    private Binding RemediationQueueBinding;

    /**
     * The compliance data response message listener container.
     *
     * @return The compliance data response message listener container.
     *
     * @since SINCE-TBD
     */
    @Bean
    SimpleMessageListenerContainer DellFwuListenerContainer()
    {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setQueues(RemediationQueue);
        container.setAdviceChain(new Advice[] {retryPolicy()});
        container.setMessageListener(new MessageListenerAdapter(DellFwuConsumer(), DellFwuMessageConverter));
        container.setErrorHandler(errorHandler("HalListenerContainer"));

        return container;
    }

    /**
     * The message consumer for compliance data responses.
     *
     * @return The message consumer for compliance data responses.
     *
     * @since SINCE-TBD
     */
    @Bean
    IDellFwuAmqpConsumer DellFwuConsumer()
    {
        final String routingKey = this.RemediationQueueBinding.getRoutingKey();

        return new DellFwuAmqpConsumer(routingKey);
    }

    /**
     * The error handler for the compliance data response listener container.
     *
     * @param   listenerName  The name of the listener.
     *
     * @return The error handler compliance data response listener container.
     *
     * @since SINCE-TBD
     */
    ErrorHandler errorHandler(final String listenerName)
    {
        return new ErrorHandler()
        {
            @Override
            public void handleError(Throwable cause)
            {
                Object[] lparams = {listenerName, cause.getMessage()};
                LOGGER.error(RRSMessageCode.RABBIT_AMQP_LISTENER_E.getMessageCode(), lparams, cause);
            }
        };
    }

    /**
     * The retry operations interceptor for the consumer.
     *
     * @return The retry operations interceptor for the consumer.
     *
     * @since SINCE-TBD
     */
    @Bean
    RetryOperationsInterceptor retryPolicy()
    {
        // Delegate handler that unpacks the throwable to get the root
        // offending exception so we can set a specific policy for it.
        RetryPolicyExceptionUnpackerDelegate retryPolicyExceptionUnpackerDelegate = new RetryPolicyExceptionUnpackerDelegate(
                ourExceptionRetryPolicy());

        RetryTemplate retryTemplate = new RetryTemplate();
        BackOffPolicy backoffPolicy = new ExponentialBackOffPolicy();
        retryTemplate.setBackOffPolicy(backoffPolicy);
        retryTemplate.setRetryPolicy(retryPolicyExceptionUnpackerDelegate);

        RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
        interceptor.setRetryOperations(retryTemplate);

        return interceptor;
    }

    /*
     * The returns an exception based retry policy.
     *
     * @return  A new instance of an exception based retry policy.
     *
     * @since   SINCE-TBD
     */
    private RetryPolicy ourExceptionRetryPolicy()
    {
        ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();

        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();

        policyMap.put(ClassNotFoundException.class, new NeverRetryPolicy());

        // Default max attempts is 3. After the third attempt, the message is logged.
        policyMap.put(Exception.class, new SimpleRetryPolicy());

        policyMap.put(ListenerExecutionFailedException.class, new SimpleRetryPolicy());

        policyMap.put(MessageConversionException.class, new NeverRetryPolicy());

        exceptionClassifierRetryPolicy.setPolicyMap(policyMap);

        return exceptionClassifierRetryPolicy;
    }
}
