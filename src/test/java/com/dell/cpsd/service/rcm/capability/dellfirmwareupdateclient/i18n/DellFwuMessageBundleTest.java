/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.i18n;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuMessageCode;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Dell Firmware Update client texts bundle test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class DellFwuMessageBundleTest
{
    private static final DellFwuMessageBundle bundle = new DellFwuMessageBundle();

    @Test
    public void testUniqueKey() throws Exception
    {
        final Set<Object> set = new HashSet<>();
        for (final Object[] row : bundle.getContents())
        {
            final Object key = row[0];
            final boolean previous = set.add(key);
            if (!previous)
            {
                fail("Key used multiple times: " + key);
            }
        }
    }

    /**
     * Find messages without enum.
     */
    @Test
    public void testKeyHasEnumEntry() throws Exception
    {
        final Set<String> set = new HashSet<>(DellFwuMessageCode.values().length);
        for (final DellFwuMessageCode code : DellFwuMessageCode.values())
        {
            set.add(code.getMessageCode());
        }

        for (final Object[] row : bundle.getContents())
        {
            final String key = (String) row[0];
            assertTrue("Key doesn't have enum entry: " + key, set.contains(key));
        }
    }

}
