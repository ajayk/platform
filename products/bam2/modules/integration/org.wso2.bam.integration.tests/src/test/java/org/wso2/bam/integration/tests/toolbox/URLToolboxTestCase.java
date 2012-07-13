package org.wso2.bam.integration.tests.toolbox;

/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.bam.toolbox.deployer.stub.BAMToolboxDepolyerServiceStub;
import org.wso2.carbon.integration.framework.LoginLogoutUtil;
import org.wso2.carbon.integration.framework.utils.FrameworkSettings;

import static org.testng.Assert.assertTrue;


public class URLToolboxTestCase {
    private static final Log log = LogFactory.getLog(DefaultToolBoxTestCase.class);

    private LoginLogoutUtil util = new LoginLogoutUtil();

    private static final String TOOLBOX_DEPLOYER_SERVICE = "/services/BAMToolboxDepolyerService";

    private static final String SERVER_URL = "https://localhost:9443/";
    private BAMToolboxDepolyerServiceStub toolboxStub;
    private static String TOOLBOX_URL = "https://svn.wso2.org/repos/wso2/carbon/platform/trunk/products/" +
            "bam2/modules/samples/toolboxes/KPI_Phone_Retail_Store.bar";

    private String deployedToolBox = "";

    @BeforeMethod(groups = {"wso2.bam"})
    public void init() throws Exception {
        ConfigurationContext configContext = ConfigurationContextFactory.
                createConfigurationContextFromFileSystem(null);
        String EPR = "https://" + FrameworkSettings.HOST_NAME +
                ":" + FrameworkSettings.HTTPS_PORT + TOOLBOX_DEPLOYER_SERVICE;
        String loggedInSessionCookie = util.login();
        toolboxStub = new BAMToolboxDepolyerServiceStub(configContext, EPR);
        ServiceClient client = toolboxStub._getServiceClient();
        Options option = client.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                loggedInSessionCookie);
    }

    @Test(groups = {"wso2.bam"})
    public void urlToolBoxDeployment() throws Exception {
        toolboxStub.deployToolBoxFromURL(TOOLBOX_URL);
        int slashIndex = TOOLBOX_URL.lastIndexOf('/');
        deployedToolBox = TOOLBOX_URL.substring(slashIndex + 1);

        Thread.sleep(20000);

        //get List of deployed toolboxes
        BAMToolboxDepolyerServiceStub.ToolBoxStatusDTO statusDTO = toolboxStub.getDeployedToolBoxes("1", "");
        String[] deployed = statusDTO.getDeployedTools();

        assertTrue(deployed != null, "Status of Toolbox is null");

        String toolBoxname = deployedToolBox.replaceAll(".bar", "");
        boolean installed = false;

        for (String aTool : deployed) {
            aTool = aTool.replaceAll(".bar", "");
            if (aTool.equalsIgnoreCase(toolBoxname)) {
                installed = true;
                break;
            }
        }
        assertTrue(installed, "URL installation of toolbox :" + toolBoxname + " failed!!");
    }

     @Test(groups = {"wso2.bam"}, dependsOnMethods = "urlToolBoxDeployment")
    public void undeployURlToolBox() throws Exception {
         String toolBoxname = deployedToolBox.replaceAll(".bar", "");
        toolboxStub.undeployToolBox(new String[]{toolBoxname});

        Thread.sleep(20000);

        BAMToolboxDepolyerServiceStub.ToolBoxStatusDTO statusDTO = toolboxStub.getDeployedToolBoxes("1", "");
        String[] deployedTools = statusDTO.getDeployedTools();
        String[] undeployingTools = statusDTO.getToBeUndeployedTools();


        boolean unInstalled = true;
        if (null != undeployingTools) {
            for (String aTool : undeployingTools) {
                if (aTool.equalsIgnoreCase(toolBoxname)) {
                    unInstalled = false;
                    break;
                }
            }
        }

        if (null != deployedTools && unInstalled) {
            for (String aTool : deployedTools) {
                if (aTool.equalsIgnoreCase(toolBoxname)) {
                    unInstalled = false;
                    break;
                }
            }
        }
        assertTrue(unInstalled, "Uninstalling url toolbox" + deployedToolBox + " is not successful");
    }

}
