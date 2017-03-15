/**

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

 * VCE Confidential/Proprietary Information

 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer;

import com.dell.cpsd.service.rcm.capability.ControlPlaneResponse;
import com.dell.cpsd.service.rcm.capability.PlaceholderControlPlaneRequest;
import com.dell.cpsd.service.rcm.capability.RemediationErrorMessage;

/**

 *

 * <p>

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved. 

 * VCE Confidential/Proprietary Information

 * </p>

 *

 * @since Vision x.y.z

 */

public interface IDellFwuAmqpMessageHandler
{
    /**
     * This handles the processing of a <code>RemediationMessage</code>.
     *
     * @param   message  The <code>RemediationMessage</code> to process.
     *
     * @since SINCE-TBD
     */
    public void handleDellFwuRequest(PlaceholderControlPlaneRequest message);

    public void handleDellFwuResponse(ControlPlaneResponse message);

    /**
     * This handles the processing of a <code>DellFwuErrorMessage</code>.
     *
     * @param   message  The <code>DellFwuErrorMessage</code> to process.
     *
     * @since SINCE-TBD
     */

    public void handleDellFwuError(RemediationErrorMessage message);
}
