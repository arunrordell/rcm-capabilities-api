/*
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;
import com.dell.cpsd.service.rcm.capability.PlaceholderControlPlaneRequest;
import com.dell.cpsd.service.rcm.capability.RemediationErrorMessage;

/**
 * Dell Firmware update message handler interface.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public interface IDellFwuAmqpMessageHandler
{
    /**
     * This handles the processing of a <code>PlaceholderControlPlaneRequest</code>.
     *
     * @param message The <code>PlaceholderControlPlaneRequest</code> to process.
     * @since 1.0
     */
    void handleDellFwuRequest(PlaceholderControlPlaneRequest message);

    void handleDellFwuResponse(UpdateFirmwareResponse message);

    /**
     * This handles the processing of a <code>RemediationErrorMessage</code>.
     *
     * @param message The <code>RemediationErrorMessage</code> to process.
     * @since 1.0
     */
    void handleDellFwuError(RemediationErrorMessage message);
}
