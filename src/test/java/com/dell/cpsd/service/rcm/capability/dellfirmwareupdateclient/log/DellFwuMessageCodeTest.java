/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Dell Firmware Update client texts enum test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class DellFwuMessageCodeTest
{
    public static final String PREFIX = "FWU";

    @Test
    public void testUniqueIntCode() throws Exception
    {
        final Set<Integer> set = new HashSet<>();
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            final boolean previous = set.add(code.getErrorCode());
            if (!previous)
            {
                fail("Int code used multiple times: " + code.getErrorCode());
            }
        }
    }

    @Test
    public void testIntIncludedInCode() throws Exception
    {
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            final int iCode = code.getErrorCode();
            final String sCode = code.getMessageCode();
            assertTrue(sCode.contains("" + iCode));
        }
    }

    @Test
    public void testUniqueErrorText() throws Exception
    {
        final Set<String> set = new HashSet<>();
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            final boolean previous = set.add(code.getErrorText());
            if (!previous)
            {
                //This doesn't have to be an error
                fail("Error text used multiple times: " + code.getErrorText());
            }
        }
    }

    @Test
    public void testUniqueMessageCode() throws Exception
    {
        final Set<String> set = new HashSet<>();
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            final boolean previous = set.add(code.getMessageCode());
            if (!previous)
            {
                fail("Message code used multiple times: " + code.getErrorCode());
            }
        }
    }

    @Test
    public void testUniqueMessageText() throws Exception
    {
        final Set<String> set = new HashSet<>();
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            final boolean previous = set.add(code.getMessageText(null));
            if (!previous)
            {
                //This doesn't have to be an error
                fail("Message text used multiple times: " + code.getErrorText());
            }
        }
    }

    /**
     * Bundle returns code string if no entry found.
     */
    @Test
    public void testCodeHasBundle() throws Exception
    {
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            final String msgCode = code.getMessageCode().trim();
            final String msgText = code.getMessageText(null).trim();
            assertNotEquals(msgCode + " has no entry in bundle", msgCode, msgText);
        }
    }

    @Test
    public void testEnumSuffix() throws Exception
    {
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            final String enumSuffix = code.name().substring(code.name().length() - 1);
            final String codeSuffix = code.getMessageCode().substring(code.getMessageCode().length() - 1);
            assertEquals(enumSuffix, codeSuffix);
        }
    }

    @Test
    public void testCodePrefix() throws Exception
    {
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            assertTrue(code.getMessageCode().startsWith(PREFIX));
        }
    }

}
