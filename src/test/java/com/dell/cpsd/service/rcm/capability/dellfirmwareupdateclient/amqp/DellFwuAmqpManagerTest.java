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
import com.oracle.deploy.update.UpdateCheck;
import org.junit.Test;

import java.util.List;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @version 1.0
 * @since TBD
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

    private static final String randomUuid = "uuid";
    private static final long timeout = 10000L;
    private static final MessageProperties messageProperties = new MessageProperties(null,"correlationId",null);


    @Test(expected = IllegalArgumentException.class)
    public void testDellFwuAmqpManagerNullConstructor() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(null);
    }

    @Test(expected = ServiceTimeoutException.class)
    public void testDellFwuAmqpManager() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.getDellFwu(randomUuid, timeout);
    }

    @Test(expected = DellFwuServiceException.class)
    public void testDellFwuAmqpManagerUuidNull() throws Exception
    {
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.getDellFwu(null, timeout);
    }

    @Test
    public void testDellFwuResponse() throws Exception
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
    public void testhandleDellFwuError() throws Exception
    {
        UpdateFirmwareErrorMessage updateFirmwareErrorMessage = new UpdateFirmwareErrorMessage();
        updateFirmwareErrorMessage.setMessageProperties(messageProperties);
        DellFwuAmqpManager dellFwuAmqpManager = new DellFwuAmqpManager(iDellFwuConfiguration);
        dellFwuAmqpManager.handleDellFwuError(null);
        dellFwuAmqpManager.handleDellFwuError(updateFirmwareErrorMessage);
    }
}
