/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.DellFwuAmqpProducer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuMessageCode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RCM - Dell Firmware Update Client.
 * The configuration for the message producer.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class DellFwuProducerConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = DellFwuLoggingManager.getLogger(DellFwuProducerConfig.class);

    /*
     * The Spring RabbitMQ template.
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
     * The compliance data request exchange.
     */
    @Autowired
    @Qualifier("rcmDellFwuRequestExchange")
    private Exchange exchange;

    /*
     * The host name of the client.
     */
    @Autowired
    @Qualifier("hostName")
    private String hostname;

    /**
     * This returns the compliance data message producer that publishes to the
     * exchange.
     *
     * @return The compliance data message producer.
     * @since 1.0
     */
    @Bean
    IDellFwuAmqpProducer rcmDellFwuProducer()
    {
        // Object[] lparams = {requestRoutingKey};
        LOGGER.info(DellFwuMessageCode.PRODUCER_ROUTING_I.getMessageCode());

        return new DellFwuAmqpProducer(rabbitTemplate, exchange, DellFwuRabbitConfig.ROUTING_REMEDIATION_REQUEST, hostname);
    }
}
