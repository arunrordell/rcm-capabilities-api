/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

import com.dell.cpsd.service.rcm.capability.UpdateFirmwareErrorMessage;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @version 1.0
 * @since TBD
 */
public class DellFwuAmqpConsumerTest
{
    private static final String                     routingKey = "testKey";
    private static final DellFwuAmqpConsumer        consumer   = new DellFwuAmqpConsumer(routingKey);
    private static final IDellFwuAmqpMessageHandler handler    = new IDellFwuAmqpMessageHandler()
    {
        @Override
        public void handleDellFwuResponse(UpdateFirmwareResponse message)
        {
            message = null;
        }

        @Override
        public void handleDellFwuError(UpdateFirmwareErrorMessage message)
        {
            message = null;
        }

    };

    @BeforeClass
    public static void setUp()
    {
        System.setProperty("log4j.configuration", "dell-fwu-amqp-consumer-log4j.xml");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorKeyNull()
    {
        DellFwuAmqpConsumer nullConsumer = new DellFwuAmqpConsumer(null);
    }

    @Test
    public void testHandleMessageNull()
    {
        UpdateFirmwareResponse nullMessage = null;
        UpdateFirmwareErrorMessage nullErrorMessage = null;
        consumer.handleMessage(nullMessage);
        consumer.handleMessage(nullErrorMessage);
    }

    @Test
    public void testHandleMessage()
    {
        UpdateFirmwareResponse message = new UpdateFirmwareResponse();
        UpdateFirmwareErrorMessage errorMessage = new UpdateFirmwareErrorMessage();
        consumer.setDellFwuMessageHandler(handler);
        assertEquals(handler, consumer.getDellFwuMessageHandler());

        consumer.handleMessage(message);
        consumer.handleMessage(errorMessage);
    }

    @Test
    public void testHandleMessageHandlerNull()
    {
        DellFwuAmqpConsumer consumer = new DellFwuAmqpConsumer(routingKey);
        assertEquals(routingKey, consumer.getRoutingKey());
        assertEquals(null, consumer.getDellFwuMessageHandler());
        UpdateFirmwareResponse message = new UpdateFirmwareResponse();
        UpdateFirmwareErrorMessage errorMessage = new UpdateFirmwareErrorMessage();
        consumer.handleMessage(message);
        consumer.handleMessage(errorMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHandlerNull()
    {
        consumer.setDellFwuMessageHandler(null);
    }

}
