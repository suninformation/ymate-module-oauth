/*
 * Copyright 2007-2018 the original author or authors.
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
package net.ymate.module.oauth.impl;

import net.ymate.module.oauth.AbstractGrantProcessor;
import net.ymate.module.oauth.IOAuth;
import net.ymate.module.oauth.base.OAuthClientBean;
import net.ymate.module.oauth.base.OAuthClientUserBean;
import net.ymate.module.oauth.support.OAuthResponseUtils;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/16 下午11:54
 * @version 1.0
 */
public class RefreshTokenGrantProcessor extends AbstractGrantProcessor {

    public RefreshTokenGrantProcessor(IOAuth owner) {
        super(owner);
    }

    @Override
    public OAuthResponse process(HttpServletRequest request) throws Exception {
        OAuthResponse _response;
        try {
            OAuthTokenRequest _tokenRequest = new OAuthTokenRequest(request);
            //
            OAuthClientBean _client = getClient(_tokenRequest.getClientId());
            OAuthClientUserBean _clientUser = getClientUser(_tokenRequest.getClientId(), _tokenRequest.getRefreshToken(), IdType.REFRESH_TOKEN);
            if (_client == null) {
                _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.INVALID_CLIENT);
            } else if (_clientUser == null) {
                _response = OAuthResponseUtils.badRequest(OAuthError.ResourceResponse.INVALID_TOKEN);
            } else {
                int _refreshCountMax = getOwner().getModuleCfg().getRefreshCountMax();
                int _refreshTokenExpireIn = getOwner().getModuleCfg().getRefreshTokenExpireIn();
                //
                if (!_clientUser.checkRefreshToken(_refreshCountMax, _refreshTokenExpireIn)) {
                    _response = OAuthResponseUtils.badRequest(OAuthError.ResourceResponse.EXPIRED_TOKEN);
                } else if (!_clientUser.checkScope(_tokenRequest.getParam(org.apache.oltu.oauth2.common.OAuth.OAUTH_SCOPE))) {
                    _response = OAuthResponseUtils.badRequest(OAuthError.ResourceResponse.INSUFFICIENT_SCOPE);
                } else {
                    _clientUser.setLastAccessToken(_clientUser.getAccessToken());
                    _clientUser.setAccessToken(getOwner().getModuleCfg().getTokenGenerator().accessToken());
                    _clientUser.setExpiresIn(getOwner().getModuleCfg().getAccessTokenExpireIn());
                    _clientUser.setRefreshToken(getOwner().getModuleCfg().getTokenGenerator().refreshToken());
                    //
                    _clientUser = saveOrUpdateToken(_clientUser, true);
                    //
                    OAuthResponse.OAuthResponseBuilder _builder = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                            .setAccessToken(_clientUser.getAccessToken())
                            .setExpiresIn(String.valueOf(_clientUser.getExpiresIn()))
                            .setRefreshToken(_clientUser.getRefreshToken())
                            .setScope(_clientUser.getScope())
                            .setParam(IOAuth.Const.OPEN_ID, _clientUser.getOpenId());
                    _response = OAuthResponseUtils.appendParams(getParams(), _builder).buildJSONMessage();
                }
            }
        } catch (OAuthProblemException e) {
            _response = OAuthResponseUtils.badRequestError(e);
        }
        return _response;
    }
}
