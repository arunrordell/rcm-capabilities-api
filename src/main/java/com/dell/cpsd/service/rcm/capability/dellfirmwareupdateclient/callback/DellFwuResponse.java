/**

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

 * VCE Confidential/Proprietary Information

 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.callback;

import com.dell.cpsd.service.common.client.callback.ServiceResponse;
import com.dell.cpsd.service.rcm.capability.ControlPlaneResponse;

/**

 *

 * <p>

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved. 

 * VCE Confidential/Proprietary Information

 * </p>

 *

 * @since Vision x.y.z

 */

public class DellFwuResponse extends ServiceResponse<ControlPlaneResponse>
{
    /**
     * HalResponse constructor
     *
     * @param   requestId   The request identifier.
     * @param   response    The reponse data.
     *
     * @since SINCE-TDB
     */
    public DellFwuResponse(final String requestId, final ControlPlaneResponse response)
    {
        super(requestId, response, "");
    }
}
