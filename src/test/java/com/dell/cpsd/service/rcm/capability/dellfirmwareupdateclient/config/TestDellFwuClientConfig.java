/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.config;

import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.config.RabbitMQPropertiesConfig;
import com.dell.cpsd.service.common.client.context.ConsumerContextConfig;
import com.dell.cpsd.service.common.client.context.IConsumerContextConfig;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.DellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.DellFwuAmqpProducer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.Map;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @since 1.1
 */
public class TestDellFwuClientConfig
{
    @Bean
    @Qualifier("rabbitConnectionFactory")
    public ConnectionFactory connectionFactory()
    {
        return new CachingConnectionFactory("testhost");
    }

    @Bean
    @Qualifier("rabbitPropertiesConfig")
    public IRabbitMqPropertiesConfig propertiesConfig()
    {
        return new RabbitMQPropertiesConfig();
    }

    @Bean
    public IConsumerContextConfig consumerContextConfig()
    {
        return new ConsumerContextConfig("testRcmEvaluationClient", false);
    }

    @Bean
    public Environment environment()
    {
        return new Environment()
        {
            @Override
            public String[] getActiveProfiles()
            {
                return new String[0];
            }

            @Override
            public String[] getDefaultProfiles()
            {
                return new String[0];
            }

            @Override
            public boolean acceptsProfiles(String... strings)
            {
                return false;
            }

            @Override
            public boolean containsProperty(String s)
            {
                return false;
            }

            @Override
            public String getProperty(String s)
            {
                return null;
            }

            @Override
            public String getProperty(String s, String s1)
            {
                return null;
            }

            @Override
            public <T> T getProperty(String s, Class<T> aClass)
            {
                return null;
            }

            @Override
            public <T> T getProperty(String s, Class<T> aClass, T t)
            {
                return null;
            }

            @Override
            public <T> Class<T> getPropertyAsClass(String s, Class<T> aClass)
            {
                return null;
            }

            @Override
            public String getRequiredProperty(String s) throws IllegalStateException
            {
                return null;
            }

            @Override
            public <T> T getRequiredProperty(String s, Class<T> aClass) throws IllegalStateException
            {
                return null;
            }

            @Override
            public String resolvePlaceholders(String s)
            {
                return null;
            }

            @Override
            public String resolveRequiredPlaceholders(String s) throws IllegalArgumentException
            {
                return null;
            }
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate()
    {
        return new RabbitTemplate();
    }

    /*
     * The service request exchange.
     */
    @Bean
    @Qualifier("dellFwuRequestExchange")
    public Exchange exchange()
    {
        return new Exchange()
        {
            @Override
            public String getName()
            {
                return null;
            }

            @Override
            public String getType()
            {
                return null;
            }

            @Override
            public boolean isDurable()
            {
                return false;
            }

            @Override
            public boolean isAutoDelete()
            {
                return false;
            }

            @Override
            public Map<String, Object> getArguments()
            {
                return null;
            }

            @Override
            public boolean isDelayed()
            {
                return false;
            }

            @Override
            public boolean isInternal()
            {
                return false;
            }

            @Override
            public boolean shouldDeclare()
            {
                return false;
            }

            @Override
            public Collection<?> getDeclaringAdmins()
            {
                return null;
            }

            @Override
            public boolean isIgnoreDeclarationExceptions()
            {
                return false;
            }
        };
    }

    /*
     * The routing key for the service request message queue.
     */
    @Bean
    @Qualifier("dellFwuRequestRoutingKey")
    public String routingKey()
    {
        return new String("routingKey");
    }

    /*
     * The host name of the client.
     */
    @Bean
    @Qualifier("hostName")
    public String hostname()
    {
        return new String("hostName");
    }

    @Bean
    public MessageConverter dellFwuMessageConverter()
    {
        return new MessageConverter()
        {
            @Override
            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException
            {
                return null;
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException
            {
                return null;
            }
        };
    }

    /*
     * The queue for the service response messages.
     */
    @Bean
    @Qualifier("dellFwuResponseQueue")
    public Queue dellFwuResponseQueue()
    {
        return new Queue("queue");
    }

    /*
     * The message queue binding for the service response messages.
     */
    @Bean
    public Binding dellFwuResponseQueueBinding()
    {
        return new Binding("destination", Binding.DestinationType.QUEUE, "exchange", "routingKey", null);
    }

    @Bean
    public IDellFwuAmqpConsumer dellFwuConsumer()
    {
        return new DellFwuAmqpConsumer("routingKey");
    }

    /*
     * The RCM evaluation producer to use with the manager.
     */
    @Bean
    public IDellFwuAmqpProducer dellFwuProducer()
    {
        return new DellFwuAmqpProducer(this.rabbitTemplate(), this.exchange(), this.routingKey(), this.hostname());
    }

}
