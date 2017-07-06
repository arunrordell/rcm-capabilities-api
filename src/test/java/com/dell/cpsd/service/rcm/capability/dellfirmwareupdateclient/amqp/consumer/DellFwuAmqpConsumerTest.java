/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

import com.dell.cpsd.service.rcm.capability.CommandParameter;
import com.dell.cpsd.service.rcm.capability.Error;
import com.dell.cpsd.service.rcm.capability.MessageProperties;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareErrorMessage;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @since 1.1
 */
public class DellFwuAmqpConsumerTest
{
    private static final String              routingKey          = "testKey";
    private static final DellFwuAmqpConsumer consumer            = new DellFwuAmqpConsumer(routingKey);
    private static final String              correlationId       = "dummyKey";
    private static final String              controlPlaneCommand = "TestCommand";
    private static final String              replyTo             = "test";
    private static final String              propertyName        = "testProperty";
    private static final String              commandParameterKey = "testKey";
    private static final String              commandParameterVal = "testVal";
    private static final String              errorCode           = "test001";
    private static final String              errorMsg            = "dummyError";
    private static final Calendar            date                = new GregorianCalendar();
    private static final MessageProperties   msgProperties       = new MessageProperties(date.getTime(), correlationId, replyTo);

    private static final IDellFwuAmqpMessageHandler handler = new IDellFwuAmqpMessageHandler()
    {
        @Override
        public void handleDellFwuResponse(UpdateFirmwareResponse message)
        {
            message.setAdditionalProperty(propertyName, null);

            message.setMessageProperties(msgProperties);

            List<CommandParameter> cmdParameterList = new ArrayList<CommandParameter>();
            cmdParameterList.add(new CommandParameter(commandParameterKey, commandParameterVal));
            message.setCommandParameters(cmdParameterList);

            message.setControlPlaneCommand(controlPlaneCommand);
        }

        @Override
        public void handleDellFwuError(UpdateFirmwareErrorMessage message)
        {
            message.setAdditionalProperty(propertyName, null);

            message.setMessageProperties(msgProperties);

            List<Error> errors = new ArrayList<Error>();
            errors.add(new Error(errorCode, errorMsg));
            message.setErrors(errors);

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
    public void testHandleMessage()
    {
        UpdateFirmwareResponse nullMessage = null;
        consumer.handleMessage(nullMessage);

        UpdateFirmwareResponse message = new UpdateFirmwareResponse();
        consumer.setDellFwuMessageHandler(handler);
        assertEquals(handler, consumer.getDellFwuMessageHandler());

        consumer.handleMessage(message);

        assertNull(message.getAdditionalProperties().get(propertyName));
        assertTrue(message.getCommandParameters().contains(new CommandParameter(commandParameterKey, commandParameterVal)));
        assertEquals(msgProperties, message.getMessageProperties());
        assertEquals(controlPlaneCommand, message.getControlPlaneCommand());
    }

    @Test
    public void testHandleErrorMessage()
    {
        UpdateFirmwareErrorMessage nullErrorMessage = null;
        consumer.handleMessage(nullErrorMessage);

        UpdateFirmwareErrorMessage errorMessage = new UpdateFirmwareErrorMessage();
        consumer.setDellFwuMessageHandler(handler);

        consumer.handleMessage(errorMessage);

        assertNull(errorMessage.getAdditionalProperties().get(propertyName));
        assertTrue(errorCode, errorMessage.getErrors().contains(new Error(errorCode, errorMsg)));
        assertEquals(msgProperties, errorMessage.getMessageProperties());
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
