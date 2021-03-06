/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.output.transport.adaptor.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.output.transport.adaptor.core.config.OutputTransportAdaptorConfiguration;
import org.wso2.carbon.output.transport.adaptor.core.message.MessageDto;
import org.wso2.carbon.output.transport.adaptor.core.message.config.OutputTransportAdaptorMessageConfiguration;

import java.util.List;

/**
 * This is a TransportAdaptor type. these interface let users to publish subscribe messages according to
 * some type. this type can either be local, jms or ws
 */
public abstract class AbstractOutputTransportAdaptor {

    private static final Log log = LogFactory.getLog(AbstractOutputTransportAdaptor.class);
    private OutputTransportAdaptorDto outputTransportAdaptorDto;
    private MessageDto messageDto;

    protected AbstractOutputTransportAdaptor() {

        init();

        this.outputTransportAdaptorDto = new OutputTransportAdaptorDto();
        this.messageDto = new MessageDto();
        this.outputTransportAdaptorDto.setTransportAdaptorTypeName(this.getName());
        this.outputTransportAdaptorDto.setSupportedMessageTypes(this.getSupportedOutputMessageTypes());

        this.messageDto.setAdaptorName(this.getName());

        outputTransportAdaptorDto.setAdaptorPropertyList(((this)).getOutputAdaptorProperties());
        messageDto.setMessageOutPropertyList(((this)).getOutputMessageProperties());
    }

    public OutputTransportAdaptorDto getOutputTransportAdaptorDto() {
        return outputTransportAdaptorDto;
    }

    public MessageDto getMessageDto() {
        return messageDto;
    }

    /**
     * returns the name of the output transport adaptor type
     *
     * @return transport adaptor type name
     */
    protected abstract String getName();

    /**
     * To get the information regarding supported message types transport adaptor
     *
     * @return List of supported output message types
     */
    protected abstract List<OutputTransportAdaptorDto.MessageType> getSupportedOutputMessageTypes();

    /**
     * any initialization can be done in this method
     */
    protected abstract void init();

    /**
     * the information regarding the adaptor related properties of a specific transport adaptor type
     *
     * @return List of properties related to output transport adaptor
     */
    protected abstract List<Property> getOutputAdaptorProperties();

    /**
     * to get message related output configuration details
     *
     * @return list of output message configuration properties
     */
    protected abstract List<Property> getOutputMessageProperties();

    /**
     * publish a message to a given connection.
     *
     * @param outputTransportAdaptorMessageConfiguration
     *                - message configuration transport adaptor to publish messages
     * @param message - message to send
     * @param outputTransportAdaptorConfiguration
     *                - transport adaptor configuration to be used
     */
    public abstract void publish(
            OutputTransportAdaptorMessageConfiguration outputTransportAdaptorMessageConfiguration,
            Object message,
            OutputTransportAdaptorConfiguration outputTransportAdaptorConfiguration);

    /**
     * publish test message to check the connection with the transport adaptor configuration.
     *
     * @param outputTransportAdaptorConfiguration
     *         - transport adaptor configuration to be used
     */
    public abstract void testConnection(
            OutputTransportAdaptorConfiguration outputTransportAdaptorConfiguration);


}
