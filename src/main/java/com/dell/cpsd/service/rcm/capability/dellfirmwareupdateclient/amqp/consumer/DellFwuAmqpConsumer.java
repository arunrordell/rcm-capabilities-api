/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.consumer.UnhandledMessageConsumer;
import com.dell.cpsd.service.rcm.capability.RemediationErrorMessage;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuMessageCode;

/**
 * This is the message consumer that handles responses from the Remediation service.
 * service.
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @version 1.0
 * @since 1.0
 */
public class DellFwuAmqpConsumer extends UnhandledMessageConsumer implements IDellFwuAmqpConsumer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = DellFwuLoggingManager.getLogger(DellFwuAmqpConsumer.class);

    /*
     * The <code>IAmqpRemediationMessageHandler</code> that handles messages.
     */
    private IDellFwuAmqpMessageHandler handler = null;

    /*
     * The routing key for the message queue for this consumer.
     */
    private String routingKey = null;

    /**
     * DellFwuAmqpConsumer constructor
     *
     * @since 1.0
     */
    public DellFwuAmqpConsumer(final String routingKey)
    {
        super();

        if (routingKey == null)
        {
            throw new IllegalArgumentException("The routing key is not set.");
        }

        this.routingKey = routingKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoutingKey()
    {
        return this.routingKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDellFwuAmqpMessageHandler getDellFwuMessageHandler()
    {
        return this.handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDellFwuMessageHandler(final IDellFwuAmqpMessageHandler handler)
    {
        if (handler == null)
        {
            throw new IllegalArgumentException("The message handler is null.");
        }

        this.handler = handler;
    }

    /**
     * This handles the <code>UpdateFirmwareResponse</code> that is
     * consumed from the Remediation queue.
     *
     * @param message The <code>UpdateFirmwareResponse</code> to consume.
     * @since 1.1
     */
    public void handleMessage(final UpdateFirmwareResponse message)
    {
        if (message == null)
        {
            LOGGER.warn(DellFwuMessageCode.SYSTEM_COMPLIANCE_NULL_W.getMessageCode());
            return;
        }

        if (this.handler == null)
        {
            LOGGER.warn(DellFwuMessageCode.FWU_HANDLER_NULL_W.getMessageCode());
            return;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" handleMessage : " + message);
        }

        this.handler.handleDellFwuResponse(message);
    }

    /**
     * This handles the <code>RemediationErrorMessage</code> that is consumed
     * from the Remediation queue.
     *
     * @param message The <code>RemediationErrorMessage</code> to consume.
     * @since 1.0
     */
    public void handleMessage(final RemediationErrorMessage message)
    {
        if (message == null)
        {
            LOGGER.warn(DellFwuMessageCode.FWU_ERROR_NULL_W.getMessageCode());
            return;
        }

        if (this.handler == null)
        {
            LOGGER.warn(DellFwuMessageCode.FWU_HANDLER_NULL_W.getMessageCode());
            return;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" handleMessage : " + message);
        }

        this.handler.handleDellFwuError(message);
    }
}
