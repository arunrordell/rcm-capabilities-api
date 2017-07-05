/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.config;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config.DellFwuConsumerConfig;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config.DellFwuProducerConfig;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config.DellFwuRabbitConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertTrue;

/**
 * <p/>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @version 1.0
 * @since TBD
 */
public class DellFwuClientConfigTest
{
    @Before
    public void setUp() throws Exception
    {

    }

    @Test
    public void testDellFwuClientConfig()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        int beansBefore = context.getBeanDefinitionCount();
        context.register(DellFwuClientConfig.class, TestDellFwuClientConfig.class, DellFwuConsumerConfig.class, DellFwuProducerConfig.class,
                DellFwuRabbitConfig.class);
        context.refresh();
        assertTrue(context.getBeanDefinitionCount() > beansBefore);
    }
}
