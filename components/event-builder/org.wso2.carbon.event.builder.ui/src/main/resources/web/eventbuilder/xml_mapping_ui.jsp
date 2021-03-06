<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  ~ Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
  ~
  ~  WSO2 Inc. licenses this file to you under the Apache License,
  ~  Version 2.0 (the "License"); you may not use this file except
  ~  in compliance with the License.
  ~  You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~  Unless required by applicable law or agreed to in writing,
  ~  software distributed under the License is distributed on an
  ~  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~  KIND, either express or implied.  See the License for the
  ~  specific language governing permissions and limitations
  ~  under the License.
  --%>

<fmt:bundle basename="org.wso2.carbon.event.builder.ui.i18n.Resources">
    <link type="text/css" href="../eventbuilder/css/cep.css" rel="stylesheet"/>
    <script type="text/javascript" src="../eventbuilder/js/event_builders.js"></script>

    <table class="styledLeft noBorders spacer-bot"
           style="width:100%">
        <tbody>
        <tr fromElementKey="inputXmlMapping">
            <td colspan="2" class="middle-header">
                <fmt:message key="event.builder.mapping.xml"/>
            </td>
        </tr>
        <tr fromElementKey="inputXmlMapping">
            <td colspan="2">

                <h4><fmt:message key="xpath.prefix.header"/></h4>
                <table class="styledLeft noBorders spacer-bot" id="inputXpathPrefixTable"
                       style="display:none">
                    <thead>
                    <th class="leftCol-med"><fmt:message
                            key="event.builder.xpath.prefix"/></th>
                    <th class="leftCol-med"><fmt:message
                            key="event.builder.xpath.ns"/></th>
                    <th><fmt:message key="event.builder.mapping.actions"/></th>
                    </thead>
                    <tbody id="inputXpathPrefixTBody"></tbody>
                </table>
                <div class="noDataDiv-plain" id="noInputPrefixes">
                    No XPath definitions available
                </div>
                <table id="addXpathDefinition" class="normal">
                    <tbody>
                    <tr>
                        <td class="col-small"><fmt:message key="event.builder.xpath.prefix"/> :
                        </td>
                        <td>
                            <input type="text" id="inputPrefixName"/>
                        </td>
                        <td class="col-small"><fmt:message
                                key="event.builder.xpath.ns"/> :
                        </td>
                        <td>
                            <input type="text" id="inputXpathNs"/>
                        </td>
                        <td><input type="button" class="button"
                                   value="<fmt:message key="add"/>"
                                   onclick="addInputXpathDef()"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>

        <tr fromElementKey="inputXmlMapping">
            <td colspan="2">

                <h4><fmt:message key="xpath.expression.header"/></h4>
                <table class="styledLeft noBorders spacer-bot"
                       id="inputXpathExprTable" style="display:none">
                    <thead>
                    <th class="leftCol-med"><fmt:message
                            key="event.builder.property.xpath"/></th>
                    <th class="leftCol-med"><fmt:message
                            key="event.builder.property.mappedto"/></th>
                    <th class="leftCol-med"><fmt:message
                            key="event.builder.property.type"/></th>
                    <th class="leftCol-med"><fmt:message
                            key="event.builder.property.default"/></th>
                    <th><fmt:message key="event.builder.mapping.actions"/></th>
                    </thead>
                    <tbody id="inputXpathExprTBody"></tbody>
                </table>
                <div class="noDataDiv-plain" id="noInputProperties">
                    No XPath expressions properties Defined
                </div>
                <table id="addXpathExprTable" class="normal">
                    <tbody>
                    <tr>
                        <td class="col-small"><fmt:message
                                key="event.builder.property.xpath"/> :
                        </td>
                        <td>
                            <input type="text" id="inputPropertyValue"/>
                        </td>
                        <td class="col-small"><fmt:message key="event.builder.property.mappedto"/> :
                        </td>
                        <td>
                            <input type="text" id="inputPropertyName"/>
                        </td>
                        <td><fmt:message key="event.builder.property.type"/>:
                            <select id="inputPropertyType">
                                <option value="java.lang.Integer">Integer</option>
                                <option value="java.lang.Long">Long</option>
                                <option value="java.lang.Double">Double</option>
                                <option value="java.lang.Float">Float</option>
                                <option value="java.lang.String">String</option>
                                <option value="java.lang.Boolean">Boolean</option>
                            </select>
                        </td>
                        <td class="col-small"><fmt:message
                                key="event.builder.property.default"/></td>
                        <td><input type="text" id="inputPropertyDefault"/></td>
                        <td><input type="button" class="button"
                                   value="<fmt:message key="add"/>"
                                   onclick="addInputXmlProperty()"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table class="normal">
                    <tbody>
                    <tr>
                        <td><fmt:message key="event.builder.batchprocessing.enabled"/></td>
                        <td><input type="checkbox" id="batchProcessingEnabled"></td>
                    </tr>
                    </tbody>
                </table>
        </tr>


        </tbody>
    </table>
</fmt:bundle>