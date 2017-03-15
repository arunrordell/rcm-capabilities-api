/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.DellFwuAmqpProducer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSMessageCode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for the compliance data message producer.
 *
 * <p/>
 * Copyright © 2015-2016 VCE Company, LLC. All rights reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
@Configuration
public class DellFwuProducerConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = RRSLoggingManager.getLogger(DellFwuProducerConfig.class);

    /*
     * The Spring RabbitMQ template.
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
     * The compliance data request exchange.
     */
    @Autowired
    @Qualifier("DellFwuRequestExchange")
    private Exchange exchange;

    /*
     * The routing key for the compliance data request message queue.
     */
    /*
     * The host name of the client.
     */
    @Autowired
    @Qualifier("hostName")
    private String hostname;

    /**
     * This returns the compliance data message producer that publishes to the
     *  exchange.
     *
     * @return The compliance data message producer.
     *
     * @since SINCE-TBD
     */
    @Bean
    IDellFwuAmqpProducer DellFwuProducer()
    {
        // Object[] lparams = {requestRoutingKey};
        LOGGER.info(RRSMessageCode.PRODUCER_ROUTING_I.getMessageCode());

        return new DellFwuAmqpProducer(rabbitTemplate, exchange, DellFwuRabbitConfig.ROUTING_REMEDIATION_REQUEST, hostname);
    }
}
