/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.DellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.DellFwuAmqpProducer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collection;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @since 1.1
 */
public class DellFwuConfigurationTest
{
    private static final RabbitTemplate rabbitTemplate = new RabbitTemplate();

    private static final Exchange exchange = new Exchange()
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

    private static final String routingKey = "testKey";
    private static final String hostname   = "hostname";

    IDellFwuAmqpConsumer dellFwuAmqpConsumer = new DellFwuAmqpConsumer(routingKey);
    IDellFwuAmqpProducer dellFwuAmqpProducer = new DellFwuAmqpProducer(rabbitTemplate, exchange, routingKey, hostname);

    @BeforeClass
    public static void setUp()
    {
        System.setProperty("log4j.configuration", "dell-fwu-configuration-test-log4j.xml");

    }

    @Test
    public void testDellFwuConfiguration()
    {
        DellFwuConfiguration dellFwuConfig = new DellFwuConfiguration(dellFwuAmqpProducer, dellFwuAmqpConsumer);
        assertEquals(dellFwuAmqpConsumer, dellFwuConfig.getDellFwuConsumer());
        assertEquals(dellFwuAmqpProducer, dellFwuConfig.getDellFwuProducer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDellFwuConsumerNull()
    {
        DellFwuConfiguration dellFwuConfig = new DellFwuConfiguration(dellFwuAmqpProducer, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDellFwuProducerNull()
    {
        DellFwuConfiguration dellFwuConfig = new DellFwuConfiguration(null, dellFwuAmqpConsumer);
    }

}
