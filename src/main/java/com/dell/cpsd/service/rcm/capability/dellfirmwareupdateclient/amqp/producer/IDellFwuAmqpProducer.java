/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer;

import com.dell.cpsd.service.rcm.capability.CommandParameter;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuServiceException;

import java.util.List;

/**
 * This interface should be implemented by a producer of remediation request messages.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * 
 * @since 1.0
 */
public interface IDellFwuAmqpProducer
{
    void publishDellFwuComponent(final String timestamp, final String correlationId, final String rcmDellFwuRoutingKey,
            final String responseMessage, final List<CommandParameter> parameters) throws DellFwuServiceException;
}
