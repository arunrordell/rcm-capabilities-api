/**

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

 * VCE Confidential/Proprietary Information

 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient;

/**

 *

 * <p>

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved. 

 * VCE Confidential/Proprietary Information

 * </p>

 *

 * @since Vision x.y.z

 */

public class DellFwuServiceException extends Exception
{

    /**
     * HalServiceException constructor.
     *
     * @since SINCE-TBD
     */
    public DellFwuServiceException()
    {
        super();
    }

    /**
     * HalServiceException constructor.
     *
     * @param   cause  The cause of the exception.
     *
     * @since SINCE-TBD
     */
    public DellFwuServiceException(Throwable cause)
    {
        super(cause);
    }

    /**
     * HalServiceException constructor.
     *
     * @param   message  The exception message.
     *
     * @since SINCE-TBD
     */
    public DellFwuServiceException(String message)
    {
        super(message);
    }

    /**
     * HalServiceException constructor.
     *
     * @param   message  The exception message.
     * @param   cause    The cause of the exception.
     *
     * @since SINCE-TBD
     */
    public DellFwuServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
