/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.logging.LoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.i18n.DellFwuMessageBundle;

import java.util.ResourceBundle;

/**
 * This is the logging manager for the RCM compliance data client.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class DellFwuLoggingManager
{
    /*
     * The message resource bundle used for logging.
     */
    private static final ResourceBundle MESSAGE_BUNDLE  = ResourceBundle.getBundle(DellFwuMessageBundle.class.getName());
    private static final LoggingManager LOGGING_MANAGER = new LoggingManager();

    /**
     * DellFwuLoggingManager constructor.
     *
     * @since 1.0
     */
    public DellFwuLoggingManager()
    {
        super();
    }

    /**
     * This returns an <code>ILogger</code> using the name of the specified
     * class as the logger name.
     *
     * @param clazz The <code>Class</code> used for the logger.
     * @return The <code>ILogger</code> with the name of the class.
     * @throws IllegalArgumentException Thrown if the class is null.
     * @since 1.0
     */
    public static ILogger getLogger(Class clazz)
    {
        return LOGGING_MANAGER.getLogger(clazz, MESSAGE_BUNDLE);
    }

    /**
     * This returns an <code>ILogger</code> using the specified name as the
     * logger name.
     *
     * @param name The name of the logger.
     * @return The <code>ILogger</code> with the specified name.
     * @since 1.0
     */
    public static ILogger getLogger(String name)
    {
        return LOGGING_MANAGER.getLogger(name, MESSAGE_BUNDLE);
    }
}
