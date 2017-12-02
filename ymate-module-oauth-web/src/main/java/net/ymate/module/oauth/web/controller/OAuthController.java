/*
 * Copyright 2007-2017 the original author or authors.
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
package net.ymate.module.oauth.web.controller;

import net.ymate.module.oauth.IOAuth;
import net.ymate.module.oauth.OAuth;
import net.ymate.module.oauth.support.OAuthResponseUtils;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/3 上午1:43
 * @version 1.0
 */
@Controller
@RequestMapping("/oauth2")
public class OAuthController {

    /**
     * @return 返回访问凭证 (grant_type=client_credentials)
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/token", method = Type.HttpMethod.POST)
    public IView token() throws Exception {
        OAuthResponse _response = null;
        try {
            OAuthTokenRequest _oauthRequest = new OAuthTokenRequest(WebContext.getRequest());
            GrantType _grantType = GrantType.valueOf(StringUtils.upperCase(_oauthRequest.getParam(org.apache.oltu.oauth2.common.OAuth.OAUTH_GRANT_TYPE)));
            IOAuth.IOAuthClientHelper _clientHelper = OAuth.get().clientHelper(_oauthRequest.getClientId(), _oauthRequest.getClientSecret());
            if (!_clientHelper.checkClientId()) {
                _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.INVALID_CLIENT);
            } else if (!_clientHelper.checkClientSecret()) {
                _response = OAuthResponseUtils.unauthorizedClient();
            } else if (GrantType.CLIENT_CREDENTIALS.equals(_grantType)) {
                _response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(_clientHelper.createOrUpdateAccessToken().getAccessToken())
                        .setExpiresIn(String.valueOf(OAuth.get().getModuleCfg().getAccessTokenExpireIn()))
                        .buildJSONMessage();
            } else {
                _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE);
            }
        } catch (OAuthProblemException e) {
            _response = OAuthResponseUtils.badRequestError(e);
        } catch (IllegalArgumentException e) {
            _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.INVALID_GRANT);
        }
        return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
    }
}
