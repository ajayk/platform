
/*
 * Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.governance.client.internal;

import org.apache.axis2.context.ConfigurationContext;
import org.wso2.carbon.governance.client.WSRegistrySearchUtils;
import org.wso2.carbon.utils.ConfigurationContextService;
/**
 * Service Component for Client to WS Search API.
 *
 * @scr.component name="registry.ws.client.component" immediate="true"
 * @scr.reference name="config.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService"
 * cardinality="1..1" policy="dynamic" bind="setConfigurationContextService"
 * unbind="unsetConfigurationContextService"
 */
public class WSRegistrySearchComponent {

    private ConfigurationContext configurationContext;

    protected void setConfigurationContextService(ConfigurationContextService contextService) {
        configurationContext = contextService.getClientConfigContext();
        WSRegistrySearchUtils.setConfigurationContext(contextService.getServerConfigContext());
    }

    protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
        configurationContext = null;
        WSRegistrySearchUtils.setConfigurationContext(null);
    }
}