/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertTrue;

/**
 *
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 *
 * @since 1.0
 */
public class SpringConfigurationTest
{
    @Before
    public void setUp() throws Exception
    {

    }

    @Test
    public void testSpringConfiguration() throws Exception
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        int beansBefore = context.getBeanDefinitionCount();
        context.register(TestConfig.class, DellFwuRabbitConfig.class, DellFwuConsumerConfig.class, DellFwuProducerConfig.class);
        context.refresh();
        assertTrue(context.getBeanDefinitionCount() > beansBefore);
    }

}