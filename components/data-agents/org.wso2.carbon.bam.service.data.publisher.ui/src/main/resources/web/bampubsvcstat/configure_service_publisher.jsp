<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.bam.service.data.publisher.stub.conf.EventingConfigData" %>
<%@ page import="org.wso2.carbon.bam.service.data.publisher.stub.conf.Property" %>
<%@ page import="org.wso2.carbon.bam.service.data.publisher.ui.ServiceDataPublisherAdminClient" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%! public static final String PROPERTY_VALUES = "propertyValues";
    public static final String PROPERTY_KEYS = "propertyKeys";
%><fmt:bundle basename="org.wso2.carbon.bam.service.data.publisher.ui.i18n.Resources">

<carbon:breadcrumb
        label="bam.statistics"
        resourceBundle="org.wso2.carbon.bam.service.data.publisher.ui.i18n.Resources"
        topPage="true"
        request="<%=request%>"/>
<%
    String setConfig = request.getParameter("setConfig"); // hidden parameter to check if the form is being submitted
    String enableServiceStats = request.getParameter("enableServiceStats"); // String value is "on" of checkbox clicked, else null
    String enableActivityService = request.getParameter("enableActivityService");
    String url = request.getParameter("url");
    String userName = request.getParameter("user_name");
    String password = request.getParameter("password");

    String streamName = request.getParameter("stream_name");
    String version = request.getParameter("version");
    String nickName = request.getParameter("nick_name");
    String description = request.getParameter("description");

    String[] propertyKeys = request.getParameterValues(PROPERTY_KEYS);
    String[] propertyValues = request.getParameterValues(PROPERTY_VALUES);

    List<Property> properties = null;
    if (propertyKeys != null) {
        properties = new ArrayList<Property>();
        for (int i = 0; i < propertyKeys.length; i++) {
            Property property = new Property();
            String propertyKey = propertyKeys[i];
            String propertyValue = propertyValues[i];
            property.setKey(propertyKey);
            property.setValue(propertyValue);
            properties.add(property);
        }
    }


    String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    ServiceDataPublisherAdminClient client = new ServiceDataPublisherAdminClient(
            cookie, backendServerURL, configContext, request.getLocale());
    EventingConfigData eventingConfigData = null;

    if (setConfig != null) {    // form submitted request to set eventing config
        eventingConfigData = new EventingConfigData();
        if (enableServiceStats != null) {
            eventingConfigData.setServiceStatsEnable(true);
        } else {
            eventingConfigData.setServiceStatsEnable(false);
        }
        if (enableActivityService != null) {
            eventingConfigData.setMsgDumpingEnable(true);
        } else {
            eventingConfigData.setMsgDumpingEnable(false);
        }
        if (url != null) {
            eventingConfigData.setUrl(url);
        }
        if (userName != null) {
            eventingConfigData.setUserName(userName);
        }
        if (password != null) {
            eventingConfigData.setPassword(password);
        }

        if (properties != null) {
            eventingConfigData.setProperties(properties.toArray(new Property[properties.size()]));
        }

        if (streamName != null) {
            eventingConfigData.setStreamName(streamName);
        }

        if (version != null) {
            eventingConfigData.setVersion(version);
        }

        if (nickName != null) {
            eventingConfigData.setNickName(nickName);
        }

        if (description != null) {
            eventingConfigData.setDescription(description);
        }

        try {
            client.setEventingConfigData(eventingConfigData);

%>
<script type="text/javascript">
    jQuery(document).init(function () {
        function handleOK() {

        }

        CARBON.showInfoDialog("Eventing Configuration Successfully Updated!", handleOK);
    });
</script>
<%
} catch (Exception e) {
    if (e.getCause().getMessage().toLowerCase().indexOf("you are not authorized") == -1) {
        response.setStatus(500);
        CarbonUIMessage uiMsg = new CarbonUIMessage(CarbonUIMessage.ERROR, e.getMessage(), e);
        session.setAttribute(CarbonUIMessage.ID, uiMsg);
%>
<jsp:include page="../admin/error.jsp"/>
<%
        }
    }
} else {
    try {
        eventingConfigData = client.getEventingConfigData();
    } catch (Exception e) {
        if (e.getCause().getMessage().toLowerCase().indexOf("you are not authorized") == -1) {
            response.setStatus(500);
            CarbonUIMessage uiMsg = new CarbonUIMessage(CarbonUIMessage.ERROR, e.getMessage(), e);
            session.setAttribute(CarbonUIMessage.ID, uiMsg);
%>
<jsp:include page="../admin/error.jsp"/>
<%
            }
        }
    }


    boolean isServiceStatsEnable = eventingConfigData.getServiceStatsEnable();
    boolean isMsgDumpingEnable = eventingConfigData.getMsgDumpingEnable();

    if (url == null) {
        url = eventingConfigData.getUrl();
    }
    if (userName == null) {
        userName = eventingConfigData.getUserName();
    }
    if (password == null) {
        password = eventingConfigData.getPassword();
    }

    if (streamName == null) {
        streamName = eventingConfigData.getStreamName();
    }
    if (version == null) {
        version = eventingConfigData.getVersion();
    }
    if(nickName == null){
        nickName = eventingConfigData.getNickName();
    }
    if(description == null){
        description = eventingConfigData.getDescription();
    }

    if (properties == null) {
        Property[] propertiesDTO = eventingConfigData.getProperties();
        if (propertiesDTO != null) {
            properties = new ArrayList<Property>();
            for (int i = 0; i < propertiesDTO.length; i++) {
                Property property = propertiesDTO[i];
                properties.add(property);
            }
        }
    }

%>

<script id="source" type="text/javascript">
    function showHideDiv(divId) {
        var theDiv = document.getElementById(divId);
        if (theDiv.style.display == "none") {
            theDiv.style.display = "";
        } else {
            theDiv.style.display = "none";
        }
    }

    var rowNum = 1;

    function addColumn() {
        rowNum++;
        /*var n =  + parseInt(trId.charAt(trId.length-1))+1;
         jQuery("#"+trId+" td div.addIcon").remove();*/
        //alert(n);
        var sId = "propertyTable_" + rowNum;
        //alert(sId);
        var tableContent = "<tr id=\"" + sId + "\">" +
                           "<td>\n" +
                           "                        <fmt:message key='property.name'/>\n" +
                           "                        <input type=\"text\" name=\"<%=PROPERTY_KEYS%>\" value=\"\">\n" +
                           "                    </td>\n" +
                           "                    <td>\n" +
                           "                        <fmt:message key='property.value'/>\n" +
                           "                        <input type=\"text\" name=\"<%=PROPERTY_VALUES%>\" value=\"\">\n" +
                           "                    </td>" +
                           "<td>\n" +
                           "<a onClick='javaScript:removeColumn(\"" + sId + "\")'" +
                           "style='background-image: url(../bampubsvcstat/images/delete.gif);'class='icon-link addIcon'>Remove Property</a>\n" +
                           "                    </td>" +
                           "</tr>";

        $("#propertyTable").append(tableContent);
    }

    function addMetaData(){
        var sId = "propertyTable_" + rowNum;
        var propertyTable = "<table id=\"propertyTable\" width=\"100%\" class=\"styledLeft\""+
                           " style=\"margin-left: 0px;\"><tr id=\"" + sId + "\">" +
                           "<td>\n" +
                           "                        <fmt:message key='property.name'/>\n" +
                           "                        <input type=\"text\" name=\"<%=PROPERTY_KEYS%>\" value=\"\">\n" +
                           "                    </td>\n" +
                           "                    <td>\n" +
                           "                        <fmt:message key='property.value'/>\n" +
                           "                        <input type=\"text\" name=\"<%=PROPERTY_VALUES%>\" value=\"\">\n" +
                           "                    </td>" +
                           "<td>\n" +
                           "<a onClick='javaScript:removeColumn(\"" + sId + "\")'" +
                           "style='background-image: url(../bampubsvcstat/images/delete.gif);'class='icon-link addIcon'>Remove Property</a>\n" +
                           "                    </td>" +
                           "</tr></table>";

        $("#propertyTablePlaceHolder").append(propertyTable);
    }

    function removeColumn(id) {
        $("#" + id).remove();
    }
</script>

<div id="middle">
    <h2>
        <fmt:message key="bam.service.data.publisher.config"/>
    </h2>

    <div id="workArea">
        <div id="result"></div>
        <p>&nbsp;</p>

        <form action="configure_service_publisher.jsp" method="post">
            <input type="hidden" name="setConfig" value="on"/>
            <table width="100%" class="styledLeft" style="margin-left: 0px;">
                <thead>
                <tr>
                    <th colspan="4">
                        <fmt:message key="service.configuration"/>
                    </th>
                </tr>
                </thead>
                <tr>
                    <td>
                        <% if (isServiceStatsEnable) { %>
                        <input type="checkbox" name="enableServiceStats"
                               checked="true">&nbsp;&nbsp;&nbsp;&nbsp;
                        <% } else { %>
                        <input type="checkbox" name="enableServiceStats">&nbsp;&nbsp;&nbsp;&nbsp;
                        <% } %>
                        <fmt:message key="enable.service.stats"/>

                    </td>
                </tr>

                <thead>
                <tr>
                    <th colspan="4">
                        <fmt:message key="activity.service.configuration"/>
                    </th>
                </tr>
                </thead>
                <tr>
                    <td>
                        <% if (isMsgDumpingEnable) { %>
                        <input type="checkbox" name="enableActivityService"
                               checked="true">&nbsp;&nbsp;&nbsp;&nbsp;
                        <% } else { %>
                        <input type="checkbox" name="enableActivityService"
                                >&nbsp;&nbsp;&nbsp;&nbsp;
                        <% } %>
                        <fmt:message key="enable.activity.service"/>

                    </td>
                </tr>

                <thead>
                <tr>
                    <th colspan="4">
                        <fmt:message key="stream.definition.configuration"/>
                    </th>
                </tr>
                </thead>

                <tr>
                    <td><fmt:message key="stream.name"/></td>
                    <td><input type="text" name="stream_name" value="<%=streamName%>"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="version"/></td>
                    <td><input type="text" name="version" value="<%=version%>"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="nick.name"/></td>
                    <td><input type="text" name="nick_name" value="<%=nickName%>"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="description"/></td>
                    <td><input type="text" name="description" value="<%=description%>"/></td>
                </tr>


                    <%--                    <% if (isServiceStatsEnable || isMsgDumpingEnable) { %>--%>
                <thead>
                <tr>
                    <th colspan="4">
                        <fmt:message key="bam.credential"/>
                    </th>
                </tr>
                </thead>
                <tr>
                    <td><fmt:message key="bam.url"/></td>
                    <td><input type="text" name="url" value="<%=url%>"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="username"/></td>
                    <td><input type="text" name="user_name" value="<%=userName%>"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="password"/></td>
                    <td><input type="password" name="password" value="<%=password%>"/></td>
                </tr>

                <thead>
                <tr>
                    <th colspan="4">
                        <fmt:message key="properties"/>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td id="propertyTablePlaceHolder" colspan="2">

                        <% if (properties != null) { %>
                        <table id="propertyTable" width="100%" class="styledLeft"
                           style="margin-left: 0px;">
                            <tr>
                                <td colspan="3">
                                    <a onClick='javaScript:addColumn()' style='background-image:
                                    url(../bampubsvcstat/images/add.gif);' class='icon-link addIcon'>Add
                                                                                                     Property</a>
                                </td>
                            </tr>
                            <% int i = 1;
                            for (Property property : properties) {

                            %>
                            <tr id="propertyTable_<%=i%>">
                                <td>
                                    <fmt:message key="property.name"/>
                                    <input type="text" name="<%=PROPERTY_KEYS%>"
                                           value="<%=property.getKey()%>">
                                </td>
                                <td>
                                    <fmt:message key="property.value"/>
                                    <input type="text" name="<%=PROPERTY_VALUES%>"
                                           value="<%=property.getValue()%>">
                                </td>

                                <td>
                                    <a onClick='javaScript:removeColumn("propertyTable_<%=i%>")' style='background-image:
                                    url(../bampubsvcstat/images/delete.gif);' class='icon-link addIcon'>Remove
                                                                                                        Property</a>
                                </td>


                            </tr>
                            <script type="text/javascript">
                                rowNum++;
                            </script>
                            <% i++;
                            }
                            %>

                        </table>
                        <%
                        } else { %>
                            <table width="100%" class="styledLeft" style="margin-left: 0px;">
                                <tr>
                                    <td colspan="3">
                                    <a onClick='javaScript:addMetaData()'
                                    style='background-image: url(../bampubsvcstat/images/add.gif);'
                                    class='icon-link addIcon'>Add Property</a>
                                    </td>
                                </tr>
                            </table>

                        <% } %>
                    </td>
                </tr>
                </tbody>
                <tr>
                    <td colspan="4" class="buttonRow">
                        <input type="submit" class="button" value="<fmt:message key="update"/>"
                               id="updateStats"/>&nbsp;&nbsp;
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>


</fmt:bundle>

