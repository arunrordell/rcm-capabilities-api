/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp;

import com.dell.cpsd.service.rcm.capability.Error;
import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.service.common.client.callback.IServiceCallback;
import com.dell.cpsd.service.common.client.callback.ServiceCallback;
import com.dell.cpsd.service.common.client.callback.ServiceError;
import com.dell.cpsd.service.common.client.callback.ServiceResponse;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import com.dell.cpsd.service.common.client.manager.AbstractServiceCallbackManager;
import com.dell.cpsd.service.common.client.task.ServiceTask;
import com.dell.cpsd.service.rcm.capability.CommandParameter;
import com.dell.cpsd.service.rcm.capability.UpdateFirmwareResponse;
import com.dell.cpsd.service.rcm.capability.PlaceholderControlPlaneRequest;
import com.dell.cpsd.service.rcm.capability.RemediationErrorMessage;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuServiceException;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.IDellFwuConfiguration;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.config.DellFwuRabbitConfig;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpConsumer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.consumer.IDellFwuAmqpMessageHandler;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer.IDellFwuAmqpProducer;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.callback.DellFwuResponse;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.DellFwuMessageCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is responsible for sending and processing messages to and from
 * the Remediation service.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Component
public class DellFwuAmqpManager extends AbstractServiceCallbackManager implements IDellFwuAmqpMessageHandler
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = DellFwuLoggingManager.getLogger(DellFwuAmqpManager.class);

    /*
     * The routing key for the compute result message queue used by the consumer
     */
    private String routingKey = null;

    /*
     * The configuration used by this service manager
     */
    private IDellFwuConfiguration configuration = null;

    /*
     * The DellFwu message producer
     */
    private IDellFwuAmqpProducer rcmDellFwuProducer = null;

    /*
     * The DellFwu message consumer
     */
    private IDellFwuAmqpConsumer rcmDellFwuConsumer = null;

    /**
     * DellFwuAmqpManager constructor.
     *
     * @param configuration The configuration used by this manager.
     * @throws IllegalArgumentException Thrown if the configuration is null.
     * @since 1.0
     */
    public DellFwuAmqpManager(final IDellFwuConfiguration configuration)
    {
        super();

        if (configuration == null)
        {
            throw new IllegalArgumentException("The configuration is not set.");
        }

        this.configuration = configuration;
        this.setRcmDellFwuConsumer(configuration.getDellFwuConsumer());
        this.setRemediationProducer(configuration.getDellFwuProducer());
        this.setRoutingKey(this.rcmDellFwuConsumer.getRoutingKey());
        this.rcmDellFwuConsumer.setDellFwuMessageHandler(this);
    }

    /**
     * This returns the remediation data.
     *
     * @param systemUid The uid of the system.
     * @return The remediation response.
     * @throws DellFwuServiceException Thrown if the request fails.
     * @throws ServiceTimeoutException Thrown if the request fails.
     * @since 1.0
     */
    public DellFwuResponse getDellFwu(final String systemUid, final long timeout) throws DellFwuServiceException, ServiceTimeoutException
    {
        nullCheck("system", systemUid);

        this.shutdownCheck();

        // create a correlation identifier for the operation
        final String requestId = this.createRequestId();
        final ServiceCallback<DellFwuResponse> callback = createCallback(DellFwuResponse.class);
        this.createAndAddServiceTask(requestId, callback, timeout);

        // publish the list validation message to the service
        try
        {
            LOGGER.info("Sending these across Systemuid " + systemUid + "\nrequestid " + requestId + "\nroutingkey " + this.routingKey);
            List<CommandParameter> parameters = new ArrayList<>();
            this.rcmDellFwuProducer
                    .publishDellFwuComponent(systemUid, requestId, DellFwuRabbitConfig.ROUTING_REMEDIATION_REQUEST, "command", parameters);
        }
        catch (Exception exception)
        {
            // remove the compute callback if the message cannot be published
            this.removeServiceTask(requestId);

            logAndThrowException(exception);
        }

        // wait from the response from the service
        this.waitForServiceCallback(callback, requestId, timeout);
        this.checkForServiceError(callback);

        // if there was no compute error, then return the compute result
        return callback.getServiceResponse();
    }

    @Override
    public void handleDellFwuRequest(final PlaceholderControlPlaneRequest request)
    {

    }

    /**
     * This handles the processing of a <code>ControlPlaneResponse</code>.
     *
     * @param result The <code>ControlPlaneResponse</code> to process.
     * @since 1.0
     */
    @Override
    public void handleDellFwuResponse(final UpdateFirmwareResponse result)
    {
        if (result == null)
        {
            return;
        }

        final String correlationId = result.getMessageProperties().getCorrelationId();
        final IServiceCallback<?> callback = this.removeServiceCallback(correlationId);

        if (callback == null)
        {
            return;
        }

        final DellFwuResponse response = new DellFwuResponse(correlationId, result);

        // TODO : Take the callback processing off the message thread
        try
        {
            ((ServiceCallback<DellFwuResponse>) callback).handleServiceResponse(response);
        }
        catch (Exception exception)
        {
            // log the exception thrown by the compute callback
            final Object[] lparams = {"handleDellFwuResponse", exception.getMessage()};
            LOGGER.error(DellFwuMessageCode.ERROR_CALLBACK_FAIL_E.getMessageCode(), lparams, exception);
        }
    }

    /**
     * This handles the processing of a <code>RemediationErrorMessage</code>.
     *
     * @param message The <code>RemediationErrorMessage</code> to process.
     * @since 1.0
     */
    @Override
    public void handleDellFwuError(final RemediationErrorMessage message)
    {
        if (message == null)
        {
            return;
        }

        final String correlationId = message.getMessageProperties().getCorrelationId();

        final IServiceCallback<?> callback = this.removeServiceCallback(correlationId);

        if (callback == null)
        {
            return;
        }

        //TODO : Fix this error Handling
        final List<Error> errors = message.getErrors();

        final ServiceError error = new ServiceError(correlationId, "", "");

        // TODO : Take the callback processing off the message thread
        try
        {
            callback.handleServiceError(error);
        }
        catch (Exception exception)
        {
            // log the exception thrown by the callback
            final Object[] lparams = {"handleServiceError", exception.getMessage()};
            LOGGER.error(DellFwuMessageCode.ERROR_CALLBACK_FAIL_E.getMessageCode(), lparams, exception);
        }
    }

    /**
     * This releases any resources associated with this manager.
     *
     * @since 1.0
     */
    @Override
    public void release()
    {
        super.release();
        this.configuration = null;
    }

    /**
     * This returns the  routing key used by the service consumer to
     * consume responses or error messages.
     *
     * @return The routing key.
     * @since 1.0
     */
    public String getRoutingKey()
    {
        return this.routingKey;
    }

    /**
     * This sets the compute result routing key used by the service consumer to
     * consume responses or error messages.
     *
     * @param routingKey The routing key.
     * @throws IllegalArgumentException Thrown if the routing key is null.
     * @since 1.0
     */
    public void setRoutingKey(final String routingKey)
    {
        if (routingKey == null)
        {
            throw new IllegalArgumentException("The routing key is not set.");
        }

        this.routingKey = routingKey;
    }

    /**
     * This returns the Remediation message producer.
     *
     * @return The Remediation message producer.
     * @since 1.0
     */
    public IDellFwuAmqpProducer getRcmDellFwuProducer()
    {
        return this.rcmDellFwuProducer;
    }

    /**
     * This sets the Remediation message producer.
     *
     * @param rcmDellFwuProducer The Remediation producer.
     * @throws IllegalArgumentException Thrown if the producer is null.
     * @since 1.0
     */
    protected void setRemediationProducer(final IDellFwuAmqpProducer rcmDellFwuProducer)
    {
        if (rcmDellFwuProducer == null)
        {
            throw new IllegalArgumentException("The Remediation producer is null.");
        }

        this.rcmDellFwuProducer = rcmDellFwuProducer;
    }

    /**
     * This returns the Remediation message consumer.
     *
     * @return The Remediation message consumer.
     * @since 1.0
     */
    public IDellFwuAmqpConsumer getRcmDellFwuConsumer()
    {
        return this.rcmDellFwuConsumer;
    }

    /**
     * This sets the Remediation message consumer.
     *
     * @param rcmDellFwuConsumer The Remediation consumer.
     * @throws IllegalArgumentException Thrown if the consumer is null.
     * @since 1.0
     */
    protected void setRcmDellFwuConsumer(final IDellFwuAmqpConsumer rcmDellFwuConsumer)
    {
        if (rcmDellFwuConsumer == null)
        {
            throw new IllegalArgumentException("The Remediation consumer is null.");
        }

        this.rcmDellFwuConsumer = rcmDellFwuConsumer;
    }

    private void createAndAddServiceTask(final String requestId, final ServiceCallback<?> callback, final long timeout)
    {
        // the infinite timeout is used for the task because it is handled with
        // this synchronous call.
        final ServiceTask<IServiceCallback<?>> task = new ServiceTask<IServiceCallback<?>>(requestId, callback, timeout);

        // add the compute callback using the correlation identifier as key
        this.addServiceTask(requestId, task);
    }

    private void logAndThrowException(final Exception exception) throws DellFwuServiceException
    {
        final Object[] lparams = {exception.getMessage()};
        final String lmessage = LOGGER.error(DellFwuMessageCode.PUBLISH_MESSAGE_FAIL_E.getMessageCode(), lparams, exception);

        throw new DellFwuServiceException(lmessage, exception);
    }

    private String createRequestId()
    {
        return UUID.randomUUID().toString();
    }

    private <R extends ServiceResponse<?>> ServiceCallback<R> createCallback(final Class<R> callbackType)
    {
        return new ServiceCallback<R>();
    }

    private void checkForServiceError(final ServiceCallback<?> callback) throws DellFwuServiceException
    {
        // check to see if a compute error has been handled by the manager
        final ServiceError error = callback.getServiceError();

        // throw a compute exception using the message in the compute error
        if (error != null)
        {
            throw new DellFwuServiceException(error.getErrorMessage());
        }
    }

    private void logAndThrowTimeoutException(final String requestId, final long timeLimit) throws ServiceTimeoutException
    {
        final Object[] lparams = {requestId, "" + timeLimit};
        final String lmessage = LOGGER.error(DellFwuMessageCode.MESSAGE_TIMEOUT_E.getMessageCode(), lparams);

        throw new ServiceTimeoutException(lmessage);
    }

    private void shutdownCheck() throws DellFwuServiceException
    {
        if (this.isShutDown())
        {
            final String lmessage = LOGGER.error(DellFwuMessageCode.MANAGER_SHUTDOWN_E.getMessageCode());
            throw new DellFwuServiceException(lmessage);
        }
    }

    private void nullCheck(final String deviceType, final String uuidToCheck) throws DellFwuServiceException
    {
        if (uuidToCheck == null)
        {
            throw new DellFwuServiceException("The " + deviceType + " uuid is null");
        }
    }

}
