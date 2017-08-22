/**
 * Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.validators;

import com.dell.cpsd.common.rabbitmq.validators.ValidationResult;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic message validator.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since TBD
 */
public abstract class GenericMessageValidator<R extends Object>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericMessageValidator.class);

    /**
     * Implementation of message validation.
     *
     * @param requestMessage the request message
     * @throws Throwable the throwable
     * @return validation results
     */
    protected abstract ValidationResult validateMessage(final R requestMessage) throws Throwable;

    /**
     * Validate the message
     *
     * @param requestMessage the request message
     * @throws DellFwuServiceException Error Exception
     * @return validation results
     */
    public ValidationResult validate(final R requestMessage) throws DellFwuServiceException
    {
        try
        {
            return validateMessage(requestMessage);
        }
        catch (Throwable e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new DellFwuServiceException(e.getMessage(), e);
        }
    }

}
