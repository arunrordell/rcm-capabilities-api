/**

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

 * VCE Confidential/Proprietary Information

 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;

/**

 *

 * <p>

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved. 

 * VCE Confidential/Proprietary Information

 * </p>

 *

 * @since Vision x.y.z

 */

public interface IDellFwuConfiguration
{
    /**
     * This returns the <code>IAmqpHalConsumer</code> bean from the
     * application context.
     *
     * @return The <code>IAmqpHalConsumer</code> bean.
     *
     * @since 1.0
     */
    public IDellFwuAmqpConsumer getDellFwuConsumer();

    /**
     * This returns the <code>IAmqpHalProducer</code> bean from the
     * application context.
     *
     * @return The <code>IAmqpHalProducer</code> bean.
     *
     * @since 1.0
     */
    public IDellFwuAmqpProducer getDellFwuProducer();
}
