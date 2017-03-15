/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer;

import com.dell.cpsd.service.rcm.capability.CommandParameter;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuServiceException;

import java.util.List;

;

/**
 * This interface should be implemented by a producer of compliance data
 * request messages.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
public interface IDellFwuAmqpProducer
{
    public void publishDellFwuComponent(final String timestamp, final String correlationId, final String HalRoutingKey,
            final String responseMessage, List<CommandParameter> parameters) throws DellFwuServiceException;

}
