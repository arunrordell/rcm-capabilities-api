/**

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

 * VCE Confidential/Proprietary Information

 */

package com.dell.cpsd.remediation.service.client.model;

/**

 *

 * <p>

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved. 

 * VCE Confidential/Proprietary Information

 * </p>

 *

 * @since Vision x.y.z

 */

public class ControlPlaneConstants
{

    public enum ComponentType
    {
        DELL_SERVER_R730("Dell server R730");

        private ComponentType(String name)
        {
            this.name = name;
        }

        private final String name;
    }

    public enum CommandType
    {

        FIRMWARE_UPDATE("UpdateFirmware"),
        FAKE_FIRMWARE_UPDATE("FakeFirmwareUpdate"),
        GET_STATUS("Status");

        private CommandType(String name)
        {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString()
        {
            return name;
        }

        public static CommandType findCommandTypeFromString(String type)
        {
            for (CommandType t : values())
            {
                if (t.toString().compareTo(type) == 0)
                {
                    return t;
                }
            }
            return null;
        }
    }

    public enum SubComponentType
    {
        BIOS("BIOS");
        private final String name;

        @Override
        public String toString()
        {
            return name;
        }

        private SubComponentType(String name)
        {
            this.name = name;
        }
    }

    public enum CommandParameterType
    {
        FILEPATH("FilePath"),
        INSTANCE_ID("InstanceId");
        private final String name;

        @Override
        public String toString()
        {
            return name;
        }

        private CommandParameterType(String name)
        {
            this.name = name;
        }
    }

}
