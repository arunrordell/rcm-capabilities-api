/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;

/**
 * Configuration interface.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public interface IDellFwuConfiguration
{
    /**
     * This returns the <code>IDellFwuAmqpConsumer</code> bean from the
     * application context.
     *
     * @return The <code>IDellFwuAmqpConsumer</code> bean.
     * @since 1.0
     */
    IDellFwuAmqpConsumer getDellFwuConsumer();

    /**
     * This returns the <code>IDellFwuAmqpProducer</code> bean from the
     * application context.
     *
     * @return The <code>IDellFwuAmqpProducer</code> bean.
     * @since 1.0
     */
    IDellFwuAmqpProducer getDellFwuProducer();
}
