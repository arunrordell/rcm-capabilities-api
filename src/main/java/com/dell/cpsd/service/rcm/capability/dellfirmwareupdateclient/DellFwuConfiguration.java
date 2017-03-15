/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient;

import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;

/**

 *

 * <p>

 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved. 

 * VCE Confidential/Proprietary Information

 * </p>

 *

 * @since Vision x.y.z

 */

public class DellFwuConfiguration implements IDellFwuConfiguration
{
    /*
       * The <code>IAmqpHalConsumer</code> to use.
       */
    private IDellFwuAmqpConsumer dellFwuConsumer = null;

    /*
     * The <code>IAmqpHalProducer</code> to use.
     */
    private IDellFwuAmqpProducer dellFwuProducer = null;

    /**
     * HalConfiguration constructor
     *
     * @param   dellFwuProducer   The compliance data message producer.
     * @param   dellFwuConsumer   The compliance data message consumer.
     *
     * @since 1.0
     */
    public DellFwuConfiguration(final IDellFwuAmqpProducer dellFwuProducer, final IDellFwuAmqpConsumer dellFwuConsumer)
    {
        super();

        this.setDellFwuConsumer(dellFwuConsumer);
        this.setDellFwuProducer(dellFwuProducer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDellFwuAmqpConsumer getDellFwuConsumer()
    {
        return this.dellFwuConsumer;
    }

    /**
     * This sets the <code>IAmqpHalConsumer</code> that the manager
     * will use.
     *
     * @param   dellFwuConsumer   The compliance data message consumer.
     *
     * @throws IllegalArgumentException    Thrown if the producer is null.
     *
     * @since 1.0
     */
    public void setDellFwuConsumer(final IDellFwuAmqpConsumer dellFwuConsumer)
    {
        if (dellFwuConsumer == null)
        {
            throw new IllegalArgumentException("The RCM compliance consumer is null");
        }

        this.dellFwuConsumer = dellFwuConsumer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDellFwuAmqpProducer getDellFwuProducer()
    {
        return this.dellFwuProducer;
    }

    /**
     * This sets the <code>IAmqpHalProducer</code> that the manager
     * will use.
     *
     * @param   dellFwuProducer   The compliance data message producer.
     *
     * @throws IllegalArgumentException    Thrown if the producer is null.
     *
     * @since 1.0
     */
    public void setDellFwuProducer(final IDellFwuAmqpProducer dellFwuProducer)
    {
        if (dellFwuProducer == null)
        {
            throw new IllegalArgumentException("The remediation producer is null");
        }

        this.dellFwuProducer = dellFwuProducer;
    }
}
