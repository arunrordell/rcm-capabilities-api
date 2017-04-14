/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient;

/**
 * Dell Firmware Update Service client exception.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class DellFwuServiceException extends Exception
{
    /**
     * Default constructor.
     *
     * @since 1.0
     */
    public DellFwuServiceException()
    {
        super();
    }

    /**
     * DellFwuServiceException constructor.
     *
     * @param cause The cause of the exception.
     * @since 1.0
     */
    public DellFwuServiceException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * DellFwuServiceException constructor.
     *
     * @param message The exception message.
     * @since 1.0
     */
    public DellFwuServiceException(final String message)
    {
        super(message);
    }

    /**
     * DellFwuServiceException constructor.
     *
     * @param message The exception message.
     * @param cause   The cause of the exception.
     * @since 1.0
     */
    public DellFwuServiceException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
