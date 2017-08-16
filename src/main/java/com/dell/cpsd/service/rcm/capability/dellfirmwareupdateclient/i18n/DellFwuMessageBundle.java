/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.i18n;

import java.util.ListResourceBundle;

/**
 * This is the resource bundle for RCM Remediation service client.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class DellFwuMessageBundle extends ListResourceBundle
{
    /*
     * The content of this message resource bundle.
     */
    private static final Object[][] CONTENTS = {{"FWU2001E", "FWU2001E Rabbit consumer [{0}] threw an error. Reason [{1}]"},
            {"FWU2002E", "FWU2002E Unable to consume message after retries. Reason [{0}]"},
            {"FWU2003I", "FWU2003I The Dell Firmware Update client routing key : [{0}]"},
            {"FWU2004I", "FWU2004I The Dell Firmware Update client queue name : [{0}]"},
            {"FWU2005I", "FWU2005I The Dell Firmware Update client binding key: [{0}]"},
            {"FWU2006E", "FWU2006E Error publishing compliance data message [{0}]. Reason [{1}]"},
            {"FWU2007W", "FWU2007W The Dell Firmware Update client message to handle is null."},
            {"FWU2008W", "FWU2008W The Dell Firmware Update error message to handle is null."},
            {"FWU2009W", "FWU2009W The Dell Firmware Update client message handler is not set."},
            {"FWU2010E", "FWU2010E The service request [{0}] has timed out after [{1}] msec."},
            {"FWU2011E", "FWU2011E Could not publish the message. Reason [{0}]"},
            {"FWU2012E", "FWU2012E Unexpected error on callback [{0}]. Reason [{1}]"},
            {"FWU2013E", "FWU2013E ."},
            {"FWU2014E", "FWU2014E ."},
            {"FWU2015E", "FWU2015E ."},
            {"FWU2016E", "FWU2016E The client is shutting down. Requests are not being accepted."},
            {"FWU2017E", "FWU2017E ."},
            {"FWU2018I", "FWU2018I Shutting down the scheduled executor service."},
            {"FWU2019I", "FWU2019I Waiting for pending requests to complete."},
            {"FWU2020I", "FWU2020I ."},
            {"FWU2022W", "FWU2022W Device compliance is null."},
            {"FWU2021E", "FWU2021E Unexpected error on request timeout checking. Reason [{0}]"},
            {"FWU2023E", "FWU2023E Workflow already in progress or binary possibly already current version"}};

    /**
     * Default constructor.
     *
     * @since 1.0
     */
    public DellFwuMessageBundle()
    {
        super();
    }

    /**
     * This returns the messages for this resource bundle.
     *
     * @return The messages for this resource bundle.
     * @since 1.0
     */
    @Override
    protected Object[][] getContents()
    {
        return CONTENTS;
    }
}
