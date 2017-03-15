/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.i18n;

import java.util.ListResourceBundle;

/**
 * This is the resource bundle for RCM compliance data client.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
public class RRSMessageBundle extends ListResourceBundle
{
    /*
     * The content of this message resource bundle.
     */
    private static final Object[][] CONTENTS = {{"RRS2001E", "RRS2001E Rabbit consumer [{0}] threw an error. Reason [{1}]"},
            {"RRS2002E", "RRS2002E Unable to consume message after retries. Reason [{0}]"},
            {"RRS2003I", "RRS2003I The compliance data producer routing key : [{0}]"},
            {"RRS2004I", "RRS2004I The compliance data consumer queue name : [{0}]"},
            {"RRS2005I", "RRS2005I The compliance data consumer binding key: [{0}]"},
            {"RRS2006E", "RRS2006E Error publishing compliance data message [{0}]. Reason [{1}]"},
            {"RRS2007W", "RRS2007W The compliance data message to handle is null."},
            {"RRS2008W", "RRS2008W The compliance error message to handle is null."},
            {"RRS2009W", "RRS2009W The compliance data message handler is not set."},
            {"RRS2010E", "RRS2010E The service request [{0}] has timed out after [{1}] msec."},
            {"RRS2011E", "RRS2011E Could not publish the message. Reason [{0}]"},
            {"RRS2012E", "RRS2012E Unexpected error on callback [{0}]. Reason [{1}]"}, {"RRS2013E", "RRS2013E "},
            {"RRS2014E", "RRS2014E ."}, {"RRS2015E", "RRS2015E ."},
            {"RRS2016E", "RRS2016E The client is shutting down. Requests are not being accepted."}, {"RRS2017E", "RRS2017E "},
            {"RRS2018I", "RRS2018I Shutting down the scheduled executor service."},
            {"RRS2019I", "RRS2019I Waiting for pending requests to complete."}, {"RRS2020I", "RRS2020I ."},
            {"RRS2021E", "RRS2021E Unexpected error on request timeout checking. Reason [{0}]"},
            {"RRS2023E", "RRS2023E Workflow already in progress or binary possibly already current version"}};

    /**
     * RRSMessageBundle constructor.
     *
     * @since SINCE-TBD
     */
    public RRSMessageBundle()
    {
        super();
    }

    /**
     * This returns the messages for this resource bundle.
     *
     * @return The messages for this resource bundle.
     *
     * @since SINCE-TBD
     */
    @Override
    protected Object[][] getContents()
    {
        return CONTENTS;
    }
}
