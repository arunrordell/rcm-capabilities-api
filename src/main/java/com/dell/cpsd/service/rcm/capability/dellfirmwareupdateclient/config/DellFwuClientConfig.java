/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.config;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuConfiguration;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.DellFwuAmqpManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config.DellFwuConsumerConfig;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config.DellFwuProducerConfig;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config.DellFwuRabbitConfig;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Client configuration.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
@Configuration
@Import({DellFwuRabbitConfig.class, DellFwuProducerConfig.class, DellFwuConsumerConfig.class})
public class DellFwuClientConfig
{
    @Autowired
    private IDellFwuAmqpProducer rcmDellFwuProducer;
    @Autowired
    private IDellFwuAmqpConsumer rcmDellFwuConsumer;

    @Bean
    DellFwuAmqpManager getDellFwuAmqpManager()
    {
        final DellFwuConfiguration configuration = new DellFwuConfiguration(rcmDellFwuProducer, rcmDellFwuConsumer);
        return new DellFwuAmqpManager(configuration);
    }
}