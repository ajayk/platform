<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%--
  ~  Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
  ~
  ~  Licensed under the Apache License, Version 2.0 (the "License");
  ~  you may not use this file except in compliance with the License.
  ~  You may obtain a copy of the License at
  ~
  ~        http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~  Unless required by applicable law or agreed to in writing, software
  ~  distributed under the License is distributed on an "AS IS" BASIS,
  ~  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~  See the License for the specific language governing permissions and
  ~  limitations under the License.
  --%>

<%@ page import="org.wso2.carbon.output.transport.adaptor.manager.stub.OutputTransportAdaptorManagerAdminServiceStub" %>
<%@ page
        import="org.wso2.carbon.output.transport.adaptor.manager.stub.types.OutputTransportAdaptorPropertyDto" %>
<%@ page
        import="org.wso2.carbon.output.transport.adaptor.manager.ui.OutputTransportAdaptorUIUtils" %>

<%
    String transportAdaptorName = request.getParameter("transportAdaptorName");
    String action = request.getParameter("action");    

    if (transportAdaptorName != null && action != null) {
        OutputTransportAdaptorManagerAdminServiceStub stub = OutputTransportAdaptorUIUtils.getOutputTransportManagerAdminService(config, session, request);
        if ("enableStat".equals(action)) {
            stub.enableStatistics(transportAdaptorName);
        } else if ("disableStat".equals(action)) {
            stub.disableStatistics(transportAdaptorName);
        } else if ("enableTracing".equals(action)) {
            stub.enableTracing(transportAdaptorName);
        } else if ("disableTracing".equals(action)) {
            stub.disableTracing(transportAdaptorName);
        }
    }

%>