/*
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.config.RabbitMQPropertiesConfig;
import com.dell.cpsd.service.common.client.context.ConsumerContextConfig;
import com.dell.cpsd.service.common.client.context.IConsumerContextConfig;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Additional Spring beans creation to make wireing working.
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class TestConfig
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
        return new ConsumerContextConfig("testRcmDellFwuClient", false);
    }

}
