package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertTrue;

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