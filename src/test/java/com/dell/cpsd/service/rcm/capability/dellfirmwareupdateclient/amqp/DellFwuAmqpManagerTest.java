/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp;

import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import com.dell.cpsd.service.rcm.capability.CommandParameter;
import com.dell.cpsd.service.rcm.capability.MessageProperties;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareErrorMessage;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuServiceException;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.IDellFwuConfiguration;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpMessageHandler;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @since 1.1
 */
public class DellFwuAmqpManagerTest
{
    public IDellFwuConfiguration iDellFwuConfiguration = new IDellFwuConfiguration()
    {
        @Override
        public IDellFwuAmqpConsumer getDellFwuConsumer()
        {
            IDellFwuAmqpConsumer iDellFwuAmqpConsumer = new IDellFwuAmqpConsumer()
            {
                @Override
                public String getRoutingKey()
                {
                    return "dummyKey";
                }

                @Override
                public IDellFwuAmqpMessageHandler getDellFwuMessageHandler()
                {
                    return null;
                }

                @Override
                public void setDellFwuMessageHandler(final IDellFwuAmqpMessageHandler handler)
                {

                }
            };
            return iDellFwuAmqpConsumer;
        }

        @Override
        public IDellFwuAmqpProducer getDellFwuProducer()
        {
            IDellFwuAmqpProducer iDellFwuAmqpProducer = new IDellFwuAmqpProducer()
            {
                @Override
                public void publishDellFwuComponent(final String timestamp, final String correlationId, final String rcmDellFwuRoutingKey,
                        final String responseMessage, final List<CommandParameter> parameters) throws DellFwuServiceException
                {

                }
            };
            return iDellFwuAmqpProducer;
        }
    };

    IDellFwuConfiguration producerNull = new IDellFwuConfiguration()
    {
        @Override
        public IDellFwuAmqpConsumer getDellFwuConsumer()
        {
            IDellFwuAmqpConsumer iDellFwuAmqpConsumer = new IDellFwuAmqpConsumer()
            {
                @Override
                public String getRoutingKey()
                {
                    return "dummyKey";
                }

                @Override
                public IDellFwuAmqpMessageHandler getDellFwuMessageHandler()
                {
                    return null;
                }

                @Override
                public void setDellFwuMessageHandler(final IDellFwuAmqpMessageHandler handler)
                {

                }
            };
            return iDellFwuAmqpConsumer;
        }
        @Override
        public IDellFwuAmqpProducer getDellFwuProducer()
        {
            return null;
        }

    };

    IDellFwuConfiguration consumerNull = new IDellFwuConfiguration()
    {
        @Override
        public IDellFwuAmqpConsumer getDellFwuConsumer()
        {
            return null;
        }

        @Override
        public IDellFwuAmqpProducer getDellFwuProducer()
        {
            IDellFwuAmqpProducer iDellFwuAmqpProducer = new IDellFwuAmqpProducer()
            {
                @Override
                public void publishDellFwuComponent(final String timestamp, final String correlationId, final String rcmDellFwuRoutingKey,
                        final String responseMessage, final List<CommandParameter> parameters) throws DellFwuServiceException
                {

                }
            };
            return iDellFwuAmqpProducer;
        }
    };
    private              Calendar date       = new GregorianCalendar();
    private static final long     timeout    = 10000L;
    private static final String   randomUuid = "uuid";

    private final MessageProperties messageProperties = new MessageProperties(date.getTime(), "correlationId", "test");

    @Test(expected = IllegalArgumentException.class)
    public void testDellFwuAmqpManagerNullConstructor() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(null);
    }

    @Test
    public void testDellFwuAmqpManager() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        assertNotNull(dellFwuAmqpManager.getRcmDellFwuProducer());
        assertNotNull(dellFwuAmqpManager.getRcmDellFwuConsumer());
        assertEquals(dellFwuAmqpManager.getRoutingKey(), this.iDellFwuConfiguration.getDellFwuConsumer().getRoutingKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProducerNull() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(producerNull);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumerNull() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(consumerNull);
    }

    @Test(expected = ServiceTimeoutException.class)
    public void testGetDellFwu() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.getDellFwu(randomUuid, timeout);
    }

    @Test(expected = DellFwuServiceException.class)
    public void testGetDellFwuUuidNull() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.getDellFwu(null, timeout);
    }

    @Test
    public void testHandleDellFwuResponse() throws Exception
    {
        UpdateFirmwareResponse updateFirmwareResponse = new UpdateFirmwareResponse();
        updateFirmwareResponse.setMessageProperties(messageProperties);
        updateFirmwareResponse.setCommandParameters(null);
        updateFirmwareResponse.setControlPlaneCommand("dummyCtrlPlaneCmd");
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.handleDellFwuResponse(null);
        dellFwuAmqpManager.handleDellFwuResponse(updateFirmwareResponse);
    }

    @Test
    public void testHandleDellFwuError() throws Exception
    {
        UpdateFirmwareErrorMessage updateFirmwareErrorMessage = new UpdateFirmwareErrorMessage();
        updateFirmwareErrorMessage.setMessageProperties(messageProperties);
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.handleDellFwuError(null);
        dellFwuAmqpManager.handleDellFwuError(updateFirmwareErrorMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRoutingKeyNull() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.setRoutingKey(null);
    }

    @Test(expected = DellFwuServiceException.class)
    public void testGetDellFwuShutDownTrue() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = Mockito.spy(new DellFwuAmqpManager(iDellFwuConfiguration));
        Mockito.when(dellFwuAmqpManager.isShutDown()).thenReturn(true);
        dellFwuAmqpManager.getDellFwu(randomUuid, timeout);
    }

}