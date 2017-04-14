/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.i18n.DellFwuMessageBundle;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This is the message code enum for the RCM compliance data client.
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public enum DellFwuMessageCode
{
    RABBIT_AMQP_LISTENER_E(2001, "FWU2001E"),
    RABBIT_AMQP_RECOVERY_E(2002, "FWU2002E"),
    PRODUCER_ROUTING_I(2003, "FWU2003I"),
    FWU_QUEUE_I(2004, "FWU2004I"),
    FWU_BINDING_I(2005, "FWU2005I"),
    PRODUCER_PUBLISH_E(2006, "FWU2006E"),
    SYSTEM_COMPLIANCE_NULL_W(2007, "FWU2007W"),
    FWU_ERROR_NULL_W(2008, "FWU2008W"),
    FWU_HANDLER_NULL_W(2009, "FWU2009W"),
    MESSAGE_TIMEOUT_E(2010, "FWU2010E"),
    PUBLISH_MESSAGE_FAIL_E(2011, "FWU2011E"),
    ERROR_CALLBACK_FAIL_E(2012, "FWU2012E"),
    REUSE_2013_E(2013, "FWU2013E"),
    REUSE_2014_E(2014, "FWU2014E"),
    REUSE_2015_E(2015, "FWU2015E"),
    MANAGER_SHUTDOWN_E(2016, "FWU2016E"),
    REUSE_2017_E(2017, "FWU2017E"),
    EXECUTOR_SHUTDOWN_I(2018, "FWU2018I"),
    WAIT_ON_REQUESTS_I(2019, "FWU2019I"),
    REUSE_2020_I(2020, "FWU2020I"),
    TIMEOUT_TASK_CHECK_E(2021, "FWU2021E"),
    DEVICE_COMPLIANCE_NULL_W(2022, "FWU2022W"),
    RACKHD_WORKFLOW_E(2023, "FWU2023E");

    /*
     * The path to the resource bundle
     */
    private static ResourceBundle BUNDLE = ResourceBundle.getBundle(DellFwuMessageBundle.class.getName());

    /*
     * The error code.
     */
    private final int errorCode;

    /*
     * The message code.
     */
    private final String messageCode;

    /**
     * DellFwuMessageCode constructor
     *
     * @param errorCode   The error code.
     * @param messageCode The message code.
     * @since 1.0
     */
    DellFwuMessageCode(final int errorCode, final String messageCode)
    {
        this.errorCode = errorCode;
        this.messageCode = messageCode;
    }

    /**
     * This returns the message code.
     *
     * @return The message code.
     * @since 1.0
     */
    public String getMessageCode()
    {
        return this.messageCode;
    }

    /**
     * This returns the error code.
     *
     * @return The error code.
     * @since 1.0
     */
    public int getErrorCode()
    {
        return this.errorCode;
    }

    /**
     * This returns the error text.
     *
     * @return The error text.
     * @since 1.0
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
     * @param params The message parameters.
     * @return The localized message populated with the parameters.
     * @since 1.0
     */
    public String getMessageText(final Object[] params)
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
