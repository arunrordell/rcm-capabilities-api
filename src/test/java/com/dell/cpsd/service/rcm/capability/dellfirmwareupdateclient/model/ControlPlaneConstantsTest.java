/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.model;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @version 1.0
 * @since TBD
 */
public class ControlPlaneConstantsTest
{
    @BeforeClass
    public static void setUp()
    {
        System.setProperty("log4j.configuration", "control-plane-constants-test-log4j.xml");

    }

    @Test
    public void testControlPlaneConstants()
    {
        ControlPlaneConstants.ComponentType componentType = ControlPlaneConstants.ComponentType.DELL_SERVER_R730;
        assertEquals("Dell server R730", componentType.toString());

        assertEquals(null, ControlPlaneConstants.CommandType.findCommandTypeFromString("test"));
        assertEquals(ControlPlaneConstants.CommandType.FIRMWARE_UPDATE,
                ControlPlaneConstants.CommandType.findCommandTypeFromString("UpdateFirmware"));

        ControlPlaneConstants.SubComponentType subComponentType = ControlPlaneConstants.SubComponentType.BIOS;
        assertEquals("BIOS", subComponentType.toString());

        ControlPlaneConstants.CommandParameterType commandParameterType = ControlPlaneConstants.CommandParameterType.INSTANCE_ID;
        assertEquals("InstanceId", commandParameterType.toString());
    }

}
