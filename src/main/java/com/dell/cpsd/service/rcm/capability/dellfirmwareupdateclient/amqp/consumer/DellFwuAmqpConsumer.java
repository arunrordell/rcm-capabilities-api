/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.consumer.UnhandledMessageConsumer;
import com.dell.cpsd.service.rcm.capability.PlaceholderControlPlaneRequest;
import com.dell.cpsd.service.rcm.capability.RemediationErrorMessage;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSMessageCode;

/**
 * This is the message consumer that handles responses from the Hal
 * service.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
public class DellFwuAmqpConsumer extends UnhandledMessageConsumer implements IDellFwuAmqpConsumer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = RRSLoggingManager.getLogger(DellFwuAmqpConsumer.class);

    /*
     * The <code>IAmqpRemediationMessageHandler</code> that handles messages.
     */
    private IDellFwuAmqpMessageHandler handler = null;

    /*
     * The routing key for the message queue for this consumer.
     */
    private String routingKey = null;

    /**
     * AmqpHalConsumer constructor
     *
     * @since SINCE-TBD
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
    public void setDellFwuMessageHandler(IDellFwuAmqpMessageHandler handler)
    {
        if (handler == null)
        {
            throw new IllegalArgumentException("The message handler is null.");
        }

        this.handler = handler;
    }

    /**
     * This handles the <code>ValidateComponentsResultMessage</code> that is
     * consumed from the Hal queue.
     *
     * @param   message  The <code>SystemComplianceDataMessage</code> to consume.
     *
     * @since SINCE-TBD
     */
    public void handleMessage(final PlaceholderControlPlaneRequest message)
    {
        if (message == null)
        {
            LOGGER.warn(RRSMessageCode.SYSTEM_COMPLIANCE_NULL_W.getMessageCode());
            return;
        }

        if (this.handler == null)
        {
            LOGGER.warn(RRSMessageCode.COMPLIANCE_HANDLER_NULL_W.getMessageCode());
            return;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" handleMessage : " + message);
        }

        this.handler.handleDellFwuRequest(message);
    }

    /**
     * This handles the <code>ComplianceDataErrorMessage</code> that is consumed
     * from the Hal queue.
     *
     * @param   message  The <code>ComplianceDataErrorMessage</code> to consume.
     *
     * @since SINCE-TBD
     */
    public void handleMessage(final RemediationErrorMessage message)
    {
        if (message == null)
        {
            LOGGER.warn(RRSMessageCode.COMPLIANCE_ERROR_NULL_W.getMessageCode());
            return;
        }

        if (this.handler == null)
        {
            LOGGER.warn(RRSMessageCode.COMPLIANCE_HANDLER_NULL_W.getMessageCode());
            return;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" handleMessage : " + message);
        }

        this.handler.handleDellFwuError(message);
    }
}
