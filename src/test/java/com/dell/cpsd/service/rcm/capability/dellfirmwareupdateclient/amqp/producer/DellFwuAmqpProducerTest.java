/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer;

import com.dell.cpsd.service.rcm.capability.CommandParameter;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuServiceException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @version 1.0
 * @since TBD
 */
public class DellFwuAmqpProducerTest
{
    private static final RabbitTemplate rabbitTemplate = new RabbitTemplate();
    private static final Exchange       exchange       = new Exchange()
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
    private static final String         routingKey     = "testKey";
    private static final String         hostname       = "testHostName";

    private static final DellFwuAmqpProducer producer = new DellFwuAmqpProducer(rabbitTemplate, exchange, routingKey, hostname);

    @BeforeClass
    public static void setUp()
    {
        System.setProperty("log4j.configuration", "dell-fwu-amqp-producer-log4j.xml");

    }

    @Test(expected = DellFwuServiceException.class)
    public void testPublishValidateAllComponentsNullValues() throws Exception
    {
        producer.publishDellFwuComponent(null, null, null, null, null);
    }

    @Test(expected = DellFwuServiceException.class)
    public void testPublishValidateAllComponents() throws Exception
    {
        CommandParameter cmdParameter = new CommandParameter();
        List<CommandParameter> parameters = Arrays.asList(cmdParameter);
        producer.publishDellFwuComponent("testTimeStamp", "testCorrelationId", "testKey", "testResponse", parameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRoutingKeyNull()
    {
        producer.setRoutingKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExchangeNull()
    {
        producer.setExchange(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRabbitTemplateNull()
    {
        producer.setRabbitTemplate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHostnameNull()
    {
        producer.setHostname(null);
    }

    @Test
    public void testGetValues()
    {
        DellFwuAmqpProducer producer = new DellFwuAmqpProducer(rabbitTemplate, exchange, routingKey, hostname);
        assertEquals(routingKey, producer.getRoutingKey());
        assertEquals(exchange, producer.getExchange());
        assertEquals(rabbitTemplate, producer.getRabbitTemplate());
        assertEquals(hostname, producer.getHostname());
    }

}
