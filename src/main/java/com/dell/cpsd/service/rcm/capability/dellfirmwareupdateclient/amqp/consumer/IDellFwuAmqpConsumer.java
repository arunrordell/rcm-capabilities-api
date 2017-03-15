/**

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

 * VCE Confidential/Proprietary Information

 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

/**
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since Vision x.y.z
 */

public interface IDellFwuAmqpConsumer
{
    /**
     * This returns the routing key for the remediation message queue for
     * this consumer.
     *
     * @return The routing key for the remediation message queue.
     *
     * @since SINCE-TBD
     */
    public String getRoutingKey();

    /**
     * This returns the <code>IAmqpRemediationMessageHandler</code> for the
     * consumer.
     *
     * @return The remediation message handler for the consumer.
     *
     * @since SINCE-TBD
     */
    public IDellFwuAmqpMessageHandler getDellFwuMessageHandler();

    /**
     * This sets the <code>IAmqpRemediationMessageHandler</code> for the
     * consumer.
     *
     * @param   handler  The compliance data message handler for the consumer.
     *
     * @throws IllegalArgumentException  Thrown if the handler is null.
     *
     * @since SINCE-TBD
     */
    public void setDellFwuMessageHandler(IDellFwuAmqpMessageHandler handler);
}
