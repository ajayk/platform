/*
*Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/

package org.wso2.carbon.identity.oauth.ui.client;

import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.common.AuthenticationException;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.identity.oauth.common.OAuth2ErrorCodes;
import org.wso2.carbon.identity.oauth.ui.OAuth2Parameters;
import org.wso2.carbon.identity.oauth.ui.OAuthConstants;
import org.wso2.carbon.identity.oauth.ui.internal.OAuthUIServiceComponentHolder;
import org.wso2.carbon.identity.oauth.ui.util.OAuthUIUtil;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2AuthorizeReqDTO;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2AuthorizeRespDTO;
import org.wso2.carbon.ui.CarbonUIUtil;
import org.wso2.carbon.ui.CarbonUIAuthenticator;
import org.wso2.carbon.ui.tracker.AuthenticatorRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.rmi.RemoteException;

public class OAuth2AuthzClient {

    private static Log log = LogFactory.getLog(OAuth2AuthzClient.class);

    public String handleAuthorizationRequest(HttpServletRequest request,
                                             HttpServletResponse response) throws OAuthSystemException {

		OAuth2Parameters oauth2Params =
		                                (OAuth2Parameters) request.getSession()
		                                                          .getAttribute(OAuthConstants.OAUTH2_PARAMS);

		// user has denied the authorization. Send back the error code.
		if ("true".equals(request.getParameter("deny"))) {
			return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
			                      .setError(OAuth2ErrorCodes.ACCESS_DENIED)
			                      .location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState())
			                      .buildQueryMessage().getLocationUri();
		}

        try {
            OAuth2AuthorizeRespDTO authzRespDTO = authorize(request, oauth2Params);
            // Authentication Failure, send back to the login page
            if (!authzRespDTO.getAuthenticated()) {
            	String loginPageInSession = (String) request.getSession().getAttribute("loginPage");
            	return loginPageInSession+"&auth_status=failed";
            }
			OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
			                                                            OAuthASResponse.authorizationResponse(request,
                                                                                                              HttpServletResponse.SC_FOUND);
            OAuthResponse oauthResponse;
            // user is authorized.
            if (authzRespDTO.getAuthorized()) {

				if (ResponseType.CODE.toString().equals(oauth2Params.getResponseType())) {
					builder.setCode(authzRespDTO.getAuthorizationCode());
				} else if (ResponseType.TOKEN.toString().equals(oauth2Params.getResponseType())) {
					builder.setAccessToken(authzRespDTO.getAccessToken());
					builder.setExpiresIn(String.valueOf(60 * 60));
				}

                builder.setParam("state", oauth2Params.getState());
                String redirectURL = authzRespDTO.getCallbackURI();
                oauthResponse = builder.location(redirectURL).buildQueryMessage();

            } else {
				OAuthProblemException oauthException =
				                                       OAuthProblemException.error(authzRespDTO.getErrorCode(),
				                                                                   authzRespDTO.getErrorMsg());
				oauthResponse =
				                OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
				                               .error(oauthException).location(authzRespDTO.getCallbackURI())
				                               .setState(oauth2Params.getState()).buildQueryMessage();
            }
            response.setStatus(HttpServletResponse.SC_FOUND);
            request.getSession().removeAttribute(OAuthConstants.OAUTH2_PARAMS);
            return handleOpenIDConnectParams(request, oauthResponse.getLocationUri(), oauth2Params);

        } catch (OAuthProblemException e) {
            log.error(e.getError(), e.getCause());
            return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .error(e)
                    .location(oauth2Params.getRedirectURI()).buildQueryMessage().getLocationUri();
        }
    }

	private String handleOpenIDConnectParams(HttpServletRequest request, String redirectUrl,
	                                         OAuth2Parameters oauth2Params) {

		if ("true".equals(request.getSession()
		                         .getAttribute(OAuthConstants.OIDCSessionConstant.OIDC_REQUEST))) {
			// store the logged in user in session to support prompt = none
			String loggedInUser = request.getParameter(OAuthConstants.REQ_PARAM_OAUTH_USER_NAME);
			if (loggedInUser != null) {
				request.getSession()
				       .setAttribute(OAuthConstants.OIDCSessionConstant.OIDC_LOGGED_IN_USER,
				                     loggedInUser);
			} else {
				loggedInUser =
				               (String) request.getSession()
				                               .getAttribute(OAuthConstants.OIDCSessionConstant.OIDC_LOGGED_IN_USER);
			}
			// load the users approved applications
			String appName = oauth2Params.getApplicationName();
			boolean isRpInStore =
			                      OAuthUIServiceComponentHolder.getInstance()
			                                                   .getOauth2UserAppsStore()
			                                                   .isUserRPInStore(loggedInUser,
			                                                                    appName);
			
			if(isRpInStore && oauth2Params.getPrompt() != null && oauth2Params.getPrompt().contains("none")) {
				return redirectUrl; // should not prompt for consent
			}
			redirectUrl = OAuthUIUtil.getUserConsentURL(request, null, oauth2Params, loggedInUser, redirectUrl);
			// store the response and forward for user consent
			request.getSession().setAttribute(OAuthConstants.OIDCSessionConstant.OIDC_RESPONSE, redirectUrl);
		}
		return redirectUrl;
	}

    private OAuth2AuthorizeRespDTO authorize(HttpServletRequest req, OAuth2Parameters oauth2Params)
            throws OAuthProblemException {
        try {
            // authenticate and issue the authorization code
            String backendServerURL = CarbonUIUtil.getServerURL(ServerConfiguration.getInstance());
            ConfigurationContext configContext =
                    (ConfigurationContext) req.getSession()
                            .getServletContext()
                            .getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
            OAuth2ServiceClient oauth2ServiceClient =
                    new OAuth2ServiceClient(backendServerURL, configContext);

            OAuth2AuthorizeReqDTO authzReqDTO = new OAuth2AuthorizeReqDTO();
            authzReqDTO.setCallbackUrl(oauth2Params.getRedirectURI());
            authzReqDTO.setConsumerKey(oauth2Params.getClientId());
            authzReqDTO.setResponseType(oauth2Params.getResponseType());
            authzReqDTO.setScopes(oauth2Params.getScopes().toArray(
                    new String[oauth2Params.getScopes().size()]));
            String username = req.getParameter(OAuthConstants.REQ_PARAM_OAUTH_USER_NAME);
            if (username == null) {
                username = (String) req.getSession().getAttribute(OAuthConstants.OIDCSessionConstant.OIDC_LOGGED_IN_USER);
            }
            authzReqDTO.setUsername(username);
            authzReqDTO.setPassword(req.getParameter(OAuthConstants.REQ_PARAM_OAUTH_USER_PASSWORD));
            CarbonUIAuthenticator authenticator = AuthenticatorRegistry.getCarbonAuthenticator(req);
            try {
                if (authenticator != null) {
                    authenticator.authenticate(req);
                    authzReqDTO.setUserAuthenticated(true);
                    return oauth2ServiceClient.authorize(authzReqDTO);
                }
            } catch (AuthenticationException e) {
                log.error(e.getMessage());
            }
            log.error("Error when Authenticating User.");

            OAuth2AuthorizeRespDTO authorizeRespDTO = new OAuth2AuthorizeRespDTO();
            authorizeRespDTO.setCallbackURI(authzReqDTO.getCallbackUrl());
            return authorizeRespDTO;

        } catch (RemoteException e) {
            log.error("Error when invoking the OAuth2Service to perform authorization.", e);
            throw OAuthProblemException.error(OAuth2ErrorCodes.SERVER_ERROR,
                                              "Error when invoking the OAuth2Service to perform authorization.");
        }
    }
}
