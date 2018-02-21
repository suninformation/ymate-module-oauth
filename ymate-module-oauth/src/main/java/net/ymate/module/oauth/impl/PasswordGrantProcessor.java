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
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/16 下午11:52
 * @version 1.0
 */
public class PasswordGrantProcessor extends AbstractGrantProcessor {

    public PasswordGrantProcessor(IOAuth owner) {
        super(owner);
    }

    @Override
    public OAuthResponse process(HttpServletRequest request) throws Exception {
        OAuthResponse _response;
        try {
            OAuthTokenRequest _tokenRequest = new OAuthTokenRequest(request);
            //
            Set<String> _scopes = _tokenRequest.getScopes();
            if (_scopes.contains(IOAuth.Const.SCOPE_SNSAPI_BASE)) {
                _scopes.remove(IOAuth.Const.SCOPE_SNSAPI_BASE);
            }
            if (!getOwner().getScopeNames().containsAll(_scopes)) {
                _response = buildError(IOAuth.ErrorType.INVALID_SCOPE);
            } else {
                OAuthClientBean _client = getClient(_tokenRequest.getClientId());
                if (_client == null) {
                    _response = buildError(IOAuth.ErrorType.INVALID_CLIENT);
                } else if (!_client.checkSecret(_tokenRequest.getClientSecret())) {
                    _response = buildError(IOAuth.ErrorType.UNAUTHORIZED_CLIENT);
                } else {
                    OAuthClientUserBean _clientUser = getClientUser(_tokenRequest.getClientId(), _tokenRequest.getUsername(), _tokenRequest.getPassword());
                    if (_clientUser == null) {
                        _response = buildError(IOAuth.ErrorType.INVALID_USER);
                    } else {
                        _clientUser.setClientId(_tokenRequest.getClientId());
                        _clientUser.setScope(_tokenRequest.getParam(OAuth.OAUTH_SCOPE));
                        _clientUser.setAuthorized(true);
                        _clientUser.setAccessToken(getOwner().getModuleCfg().getTokenGenerator().accessToken());
                        _clientUser.setExpiresIn(getOwner().getModuleCfg().getAccessTokenExpireIn());
                        _clientUser.setRefreshCount(0);
                        _clientUser.setRefreshToken(getOwner().getModuleCfg().getTokenGenerator().refreshToken());
                        //
                        _clientUser = saveOrUpdateToken(_clientUser, false);
                        //
                        OAuthResponse.OAuthResponseBuilder _builder = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                                .setAccessToken(_clientUser.getAccessToken())
                                .setExpiresIn(String.valueOf(_clientUser.getExpiresIn()))
                                .setRefreshToken(_clientUser.getRefreshToken())
                                .setScope(_clientUser.getScope())
                                .setParam(IOAuth.Const.OPEN_ID, _clientUser.getOpenId());
                        _response = OAuthResponseUtils.appendParams(_clientUser.getAttributes(), OAuthResponseUtils.appendParams(getParams(), _builder)).buildJSONMessage();
                    }
                }
            }
        } catch (OAuthProblemException e) {
            _response = buildError(e);
        }
        return _response;
    }
}
