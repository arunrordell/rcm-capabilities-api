/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.amqp.producer;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.service.rcm.capability.CommandParameter;
import com.dell.cpsd.service.rcm.capability.ControlPlaneResponse;
import com.dell.cpsd.service.rcm.capability.MessageProperties;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.DellFwuServiceException;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSLoggingManager;
import com.dell.cpsd.service.rcm.capability.dellfirmwareupdateclient.log.RRSMessageCode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Calendar;
import java.util.List;

/**
 * This is the message producer that sends messages to the compliance data
 * service.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 *
 * @since SINCE-TBD
 */
public class DellFwuAmqpProducer implements IDellFwuAmqpProducer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = RRSLoggingManager.getLogger(DellFwuAmqpProducer.class);

    /*
     * The RabbitMQ template used by the producer.
     */
    private RabbitTemplate rabbitTemplate;

    /*
     * The compliance data request <code>Exchange</code>.
     */
    private Exchange exchange;

    /*
     * The routing key for the compliance data request queue.
     */
    private String routingKey = null;

    /*
     * The host name of the client.
     */
    private String hostname = null;

    /*
     * The <code>Calendar</code> used by this producer.
     */
    private Calendar calendar = null;

    /**
     * AmqpHalProducer constructor.
     *
     * @param   rabbitTemplate  The RabbitMQ template.
     * @param   exchange        The compliance data request exchange.
     * @param   routingKey      The compliance data request routing key.
     * @param   hostname        The host name of the client.
     *
     * @throws IllegalArgumentException  Thrown if the parameters are null.
     *
     * @since SINCE-TBD
     */
    public DellFwuAmqpProducer(RabbitTemplate rabbitTemplate, Exchange exchange, String routingKey, String hostname)
    {
        super();

        this.calendar = Calendar.getInstance();

        this.setRabbitTemplate(rabbitTemplate);

        this.setExchange(exchange);

        this.setHostname(hostname);

        this.setRoutingKey(routingKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishDellFwuComponent(final String timestamp, final String correlationId, final String routingKey,
            final String responseMessage, List<CommandParameter> parameters) throws DellFwuServiceException
    {
        MessageProperties message = new MessageProperties(this.calendar.getTime(), correlationId, "remediationapi." + this.hostname);
        ControlPlaneResponse wrappedMessage = new ControlPlaneResponse(message, responseMessage, parameters);

        this.routingKey = routingKey;

        if (LOGGER.isDebugEnabled())
        {
            logDebugPublishMessage("publishDellFwuComponent", message);
        }

        sendMessage(wrappedMessage);
    }

    /**
     *
     * @param messageType
     * @param message
     */
    private void logDebugPublishMessage(String messageType, MessageProperties message)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(" " + messageType + " : ");
        builder.append("exchange [").append(exchange.getName());
        builder.append("], routingKey [").append(this.routingKey);
        builder.append("], message [").append(message).append("]");

        LOGGER.debug(builder.toString());
    }

    private void sendMessage(final ControlPlaneResponse message) throws DellFwuServiceException
    {
        try
        {
            LOGGER.info("In AmqpHalProducer, sending message to " + this.routingKey + "at exchange " + this.getExchange());
            rabbitTemplate.convertAndSend(exchange.getName(), this.routingKey, message);
        }
        catch (Exception exception)
        {
            Object[] params = {message, exception.getMessage()};
            String emessage = LOGGER.error(RRSMessageCode.PRODUCER_PUBLISH_E.getMessageCode(), params, exception);

            throw new DellFwuServiceException(emessage, exception);
        }
    }

    /**
     * This returns the routing key for the compliance data request message
     * queue for this producer.
     *
     * @return The routing key for the compliance data request message queue.
     *
     * @since SINCE-TBD
     */
    public String getRoutingKey()
    {
        return this.routingKey;
    }

    /**
     * This sets the routing key for the compliance data request message queue
     * for this producer.
     *
     * @param   routingKey  The routing key for the message queue.
     *
     * @throws IllegalArgumentException  Thrown if the routing key is null.
     *
     * @since SINCE-TBD
     */
    public void setRoutingKey(String routingKey)

    {
        if (routingKey == null)
        {
            throw new IllegalArgumentException("The routing key is not set");
        }

        this.routingKey = routingKey;
    }

    /**
     * This returns the <code>Exchange</code> for this producer.
     *
     * @return The <code>Exchange</code> for this producer.
     *
     * @since SINCE-TBD
     */
    public Exchange getExchange()
    {
        return this.exchange;
    }

    /**
     * This sets the <code>Exchange</code> for this producer.
     *
     * @param   exchange  The <code>Exchange</code> for this producer.
     *
     * @throws IllegalArgumentException  Thrown if the exchange is null.
     *
     * @since SINCE-TBD
     */
    public void setExchange(Exchange exchange)
    {
        if (exchange == null)
        {
            throw new IllegalArgumentException("The exchange is not set.");
        }

        this.exchange = exchange;
    }

    /**
     * This returns the <code>RabbitTemplate</code> for this producer.
     *
     * @return The <code>RabbitTemplate</code> for this producer.
     *
     * @since SINCE-TBD
     */
    public RabbitTemplate getRabbitTemplate()
    {
        return this.rabbitTemplate;
    }

    /**
     * This sets the <code>RabbitTemplate</code> for this producer.
     *
     * @param   rabbitTemplate  The <code>RabbitTemplate</code> for this producer.
     *
     * @throws IllegalArgumentException  Thrown if the template is null.
     *
     * @since SINCE-TBD
     */
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate)
    {
        if (rabbitTemplate == null)
        {
            throw new IllegalArgumentException("The rabbit template is not set.");
        }

        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * This returns the host name of the client.
     *
     * @return The host name of the client.
     *
     * @since SINCE-TBD
     */
    public String getHostname()
    {
        return this.hostname;
    }

    /**
     * This sets the host name of the client.
     *
     * @param   hostname  The host name of the client.
     *
     * @throws IllegalArgumentException  Thrown if the host name is null.
     *
     * @since SINCE-TBD
     */
    public void setHostname(String hostname)
    {
        if (hostname == null)
        {
            throw new IllegalArgumentException("The host name is not set.");
        }

        this.hostname = hostname;
    }

}
