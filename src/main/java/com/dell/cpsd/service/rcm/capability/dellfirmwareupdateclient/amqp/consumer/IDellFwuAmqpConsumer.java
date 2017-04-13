/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

/**
 * IDellFwuAmqpConsumer interface.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public interface IDellFwuAmqpConsumer
{
    /**
     * This returns the routing key for the remediation message queue for
     * this consumer.
     *
     * @return The routing key for the remediation message queue.
     * @since 1.0
     */
    String getRoutingKey();

    /**
     * This returns the <code>IAmqpRemediationMessageHandler</code> for the
     * consumer.
     *
     * @return The remediation message handler for the consumer.
     * @since 1.0
     */
    IDellFwuAmqpMessageHandler getDellFwuMessageHandler();

    /**
     * This sets the <code>IAmqpRemediationMessageHandler</code> for the
     * consumer.
     *
     * @param handler The compliance data message handler for the consumer.
     * @throws IllegalArgumentException Thrown if the handler is null.
     * @since 1.0
     */
    void setDellFwuMessageHandler(IDellFwuAmqpMessageHandler handler);
}
