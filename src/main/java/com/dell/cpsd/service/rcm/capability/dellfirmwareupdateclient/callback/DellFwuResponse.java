/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.callback;

import com.dell.cpsd.service.common.client.callback.ServiceResponse;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;

/**
 * Dell Firmware update response.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */

public class DellFwuResponse extends ServiceResponse<UpdateFirmwareResponse>
{
    /**
     * DellFwuResponse constructor
     *
     * @param requestId The request identifier.
     * @param response  The reponse data.
     * @since 1.0
     */
    public DellFwuResponse(final String requestId, final UpdateFirmwareResponse response)
    {
        super(requestId, response, "");
    }
}
