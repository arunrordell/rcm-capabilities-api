/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.logging.LoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.i18n.RRSMessageBundle;

import java.util.ResourceBundle;

;

/**
 * This is the logging manager for the RCM compliance data client.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
public class RRSLoggingManager
{
    /*
     * The message resource bundle used for logging.
     */
    private static ResourceBundle MESSAGE_BUNDLE = ResourceBundle.getBundle(RRSMessageBundle.class.getName());

    private static LoggingManager LOGGING_MANAGER = new LoggingManager();

    /**
     * RRSLoggingManager constructor.
     *
     * @since SINCE-TBD
     */
    public RRSLoggingManager()
    {
        super();
    }

    /**
     * This returns an <code>ILogger</code> using the name of the specified 
     * class as the logger name.
     *
     * @param   clazz  The <code>Class</code> used for the logger.
     *
     * @return The <code>ILogger</code> with the name of the class.
     *
     * @throws IllegalArgumentException  Thrown if the class is null.
     *
     * @since SINCE-TBD
     */
    public static ILogger getLogger(Class clazz)
    {
        return LOGGING_MANAGER.getLogger(clazz, MESSAGE_BUNDLE);
    }

    /**
     * This returns an <code>ILogger</code> using the specified name as the 
     * logger name.
     *
     * @param   name  The name of the logger.
     *
     * @return The <code>ILogger</code> with the specified name.
     *
     * @since SINCE-TBD
     */
    public static ILogger getLogger(String name)
    {
        return LOGGING_MANAGER.getLogger(name, MESSAGE_BUNDLE);
    }
}
