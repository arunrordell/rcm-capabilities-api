/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.i18n.RRSMessageBundle;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This is the message code enum for the RCM compliance data client.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
public enum RRSMessageCode
{
    RABBIT_AMQP_LISTENER_E(2001, "RRS2001E"),
    RABBIT_AMQP_RECOVERY_E(2002, "RRS2002E"),
    PRODUCER_ROUTING_I(2003, "RRS2003I"),
    HAL_QUEUE_I(2004, "RRS2004I"),
    HAL_BINDING_I(2005, "RRS2005I"),
    PRODUCER_PUBLISH_E(2006, "RRS2006E"),
    SYSTEM_COMPLIANCE_NULL_W(2007, "RRS2007W"),
    COMPLIANCE_ERROR_NULL_W(2008, "RRS2008W"),
    COMPLIANCE_HANDLER_NULL_W(2009, "RRS2009W"),
    MESSAGE_TIMEOUT_E(2010, "RRS2010E"),
    PUBLISH_MESSAGE_FAIL_E(2011, "RRS2011E"),
    ERROR_CALLBACK_FAIL_E(2012, "RRS2012E"),
    REUSE_2013_E(2013, "RRS2013E"),
    REUSE_2014_E(2014, "RRS2014E"),
    REUSE_2015_E(2015, "RRS2015E"),
    MANAGER_SHUTDOWN_E(2016, "RRS2016E"),
    REUSE_2017_E(2017, "RRS2017E"),
    EXECUTOR_SHUTDOWN_I(2018, "RRS2018I"),
    WAIT_ON_REQUESTS_I(2019, "RRS2019I"),
    REUSE_2020_I(2020, "RRS2020I"),
    TIMEOUT_TASK_CHECK_E(2021, "RRS2021E"),
    DEVICE_COMPLIANCE_NULL_W(2022, "RRS2022W"),
    RACKHD_WORKFLOW_E(2023, "RRS2023E");

    /*
     * The path to the resource bundle
     */
    private static ResourceBundle BUNDLE = ResourceBundle.getBundle(RRSMessageBundle.class.getName());

    /*
     * The error code.
     */
    private final int errorCode;

    /*
     * The message code.
     */
    private final String messageCode;

    /**
     * RRSMessageCode constructor
     *
     * @param   errorCode    The error code.
     * @param   messageCode  The message code.
     *
     * @since SINCE-TBD
     */
    private RRSMessageCode(int errorCode, String messageCode)
    {
        this.errorCode = errorCode;
        this.messageCode = messageCode;
    }

    /**
     * This returns the message code.
     *
     * @return The message code.
     *
     * @since SINCE-TBD
     */
    public String getMessageCode()
    {
        return this.messageCode;
    }

    /**
     * This returns the error code.
     *
     * @return The error code.
     *
     * @since SINCE-TBD
     */
    public int getErrorCode()
    {
        return this.errorCode;
    }

    /**
     * This returns the error text.
     *
     * @return The error text.
     *
     * @since SINCE-TBD
     */
    public String getErrorText()
    {
        try
        {
            return BUNDLE.getString(this.messageCode);

        }
        catch (MissingResourceException exception)
        {
            return this.messageCode;
        }
    }

    /**
     * This formats the  message using the array of parameters.
     *
     * @param   params   The message parameters.
     *
     * @return The localized message populated with the parameters.
     *
     * @since SINCE-TBD
     */
    public String getMessageText(Object[] params)
    {
        String message = null;

        try
        {
            message = BUNDLE.getString(this.messageCode);

        }
        catch (MissingResourceException exception)
        {
            return this.messageCode;
        }

        if ((params == null) || (params.length == 0))
        {
            return message;
        }

        return MessageFormat.format(message, params);
    }

}
