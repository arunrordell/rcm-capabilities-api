/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

import com.dell.cpsd.service.rcm.capability.UpdateFirmwareErrorMessage;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;

/**
 * Dell Firmware update message handler interface.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public interface IDellFwuAmqpMessageHandler
{
    /**
     * This handles the processing of a <code>UpdateFirmwareResponse</code>.
     *
     * @param message The <code>UpdateFirmwareResponse</code> to process.
     * @since 1.0
     */
    void handleDellFwuResponse(UpdateFirmwareResponse message);

    /**
     * This handles the processing of a <code>UpdateFirmwareErrorMessage</code>.
     *
     * @param message The <code>UpdateFirmwareErrorMessage</code> to process.
     * @since 1.0
     */
    void handleDellFwuError(UpdateFirmwareErrorMessage message);
}
