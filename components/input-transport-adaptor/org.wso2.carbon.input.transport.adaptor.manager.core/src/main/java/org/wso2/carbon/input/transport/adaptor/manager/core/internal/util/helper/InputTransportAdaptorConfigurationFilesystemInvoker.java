package org.wso2.carbon.input.transport.adaptor.manager.core.internal.util.helper;
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

import org.apache.axiom.om.OMElement;
import org.apache.axis2.deployment.Deployer;
import org.apache.axis2.deployment.DeploymentEngine;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.input.transport.adaptor.manager.core.InputTransportAdaptorDeployer;
import org.wso2.carbon.input.transport.adaptor.manager.core.exception.InputTransportAdaptorManagerConfigurationException;
import org.wso2.carbon.input.transport.adaptor.manager.core.internal.util.InputTransportAdaptorManagerConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class used to do the file system related tasks
 */
public final class InputTransportAdaptorConfigurationFilesystemInvoker {

    private static final Log log = LogFactory.getLog(InputTransportAdaptorConfigurationFilesystemInvoker.class);

    private InputTransportAdaptorConfigurationFilesystemInvoker(){}

    public static void saveConfigurationToFileSystem(OMElement transportAdaptorElement,
                                                     String transportName, String pathInFileSystem,
                                                     AxisConfiguration axisConfiguration)
            throws InputTransportAdaptorManagerConfigurationException {

        InputTransportAdaptorConfigurationFilesystemInvoker.save(transportAdaptorElement.toString(), transportName, pathInFileSystem, axisConfiguration);
    }

    public static void save(String transportAdaptor, String transportName,
                            String transportPath, AxisConfiguration axisConfiguration)
            throws InputTransportAdaptorManagerConfigurationException {
        try {
            /* save contents to .xml file */
            BufferedWriter out = new BufferedWriter(new FileWriter(transportPath));
            out.write(new XmlFormatter().format(transportAdaptor));
            out.close();
            log.info("Transport adaptor configuration for " + transportName + " saved in the filesystem");

            InputTransportAdaptorDeployer deployer = (InputTransportAdaptorDeployer) getDeployer(axisConfiguration, InputTransportAdaptorManagerConstants.TM_ELE_DIRECTORY);
            deployer.addToDeployedEventFormatterFiles(transportPath);
            reload(transportPath,axisConfiguration);
        } catch (IOException e) {
            log.error("Error while saving " + transportName, e);
            throw new InputTransportAdaptorManagerConfigurationException("Error while saving ", e);
        }
    }

    public static void deleteTransportAdaptorFile(String filePath,
                                                  AxisConfiguration axisConfiguration)
            throws InputTransportAdaptorManagerConfigurationException {
        try {
            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
            File file = new File(filePath);
            if (file.exists()) {
                boolean fileDeleted = file.delete();
                if (!fileDeleted) {
                    log.error("Could not delete " + fileName);
                } else {
                    log.info(fileName + " is deleted from the file system");
                    InputTransportAdaptorDeployer deployer = (InputTransportAdaptorDeployer) getDeployer(axisConfiguration, InputTransportAdaptorManagerConstants.TM_ELE_DIRECTORY);
                    deployer.removeFromDeployedEventFormatterFiles(filePath);
                    deployer.processUndeploy(filePath);
                }
            }
        } catch (Exception e) {
            throw new InputTransportAdaptorManagerConfigurationException("Error while deleting the transport adaptor " + e.getMessage());
        }
    }

    private static Deployer getDeployer(AxisConfiguration axisConfig, String endpointDirPath) {
        // access the deployment engine through axis config
        DeploymentEngine deploymentEngine = (DeploymentEngine) axisConfig.getConfigurator();
        return deploymentEngine.getDeployer(endpointDirPath, "xml");
    }


    public static void reload(String transportPath, AxisConfiguration axisConfiguration)
            throws InputTransportAdaptorManagerConfigurationException {
        InputTransportAdaptorDeployer deployer = (InputTransportAdaptorDeployer) getDeployer(axisConfiguration, InputTransportAdaptorManagerConstants.TM_ELE_DIRECTORY);
        DeploymentFileData deploymentFileData = new DeploymentFileData(new File(transportPath));
        try {
            deployer.processDeploy(deploymentFileData, transportPath);
        } catch (DeploymentException e) {
            throw new InputTransportAdaptorManagerConfigurationException(e);
        }

    }
}
