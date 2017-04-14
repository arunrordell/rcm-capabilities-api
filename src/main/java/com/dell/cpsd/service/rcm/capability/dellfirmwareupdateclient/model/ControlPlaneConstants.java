/*
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.model;

/**
 * Control Plane Constants / enums.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class ControlPlaneConstants
{

    public enum ComponentType
    {
        DELL_SERVER_R730("Dell server R730");

        private final String name;

        ComponentType(final String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public enum CommandType
    {
        FIRMWARE_UPDATE("UpdateFirmware"),
        FAKE_FIRMWARE_UPDATE("FakeFirmwareUpdate"),
        GET_STATUS("Status");

        private final String name;

        CommandType(final String name)
        {
            this.name = name;
        }

        public static CommandType findCommandTypeFromString(final String type)
        {
            for (final CommandType t : values())
            {
                if (t.toString().compareTo(type) == 0)
                {
                    return t;
                }
            }
            return null;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public enum SubComponentType
    {
        BIOS("BIOS");

        private final String name;

        SubComponentType(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public enum CommandParameterType
    {
        FILEPATH("serverFilePath"),
        INSTANCE_ID("InstanceId");

        private final String name;

        CommandParameterType(final String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

}
