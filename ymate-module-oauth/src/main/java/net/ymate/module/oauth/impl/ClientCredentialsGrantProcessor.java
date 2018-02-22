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
import net.ymate.module.oauth.OAuthEvent;
import net.ymate.module.oauth.base.OAuthClientBean;
import net.ymate.module.oauth.base.OAuthTokenBean;
import net.ymate.module.oauth.support.OAuthResponseUtils;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/16 下午11:56
 * @version 1.0
 */
public class ClientCredentialsGrantProcessor extends AbstractGrantProcessor {

    public ClientCredentialsGrantProcessor(IOAuth owner) {
        super(owner);
    }

    @Override
    public OAuthResponse process(HttpServletRequest request) throws Exception {
        OAuthResponse _response;
        try {
            OAuthTokenRequest _tokenRequest = new OAuthTokenRequest(request);
            OAuthClientBean _client = getClient(_tokenRequest.getClientId());
            if (_client == null) {
                _response = buildError(IOAuth.ErrorType.INVALID_CLIENT);
            } else if (!_client.checkSecret(_tokenRequest.getClientSecret())) {
                _response = buildError(IOAuth.ErrorType.UNAUTHORIZED_CLIENT);
            } else {
                _client.setLastAccessToken(_client.getAccessToken());
                _client.setAccessToken(getOwner().getModuleCfg().getTokenGenerator().accessToken());
                _client.setExpiresIn(getOwner().getModuleCfg().getAccessTokenExpireIn());
                //
                OAuthTokenBean _tokenBean = saveOrUpdateToken(_client);
                //
                getOwner().getOwner().getEvents().fireEvent(new OAuthEvent(getOwner(), OAuthEvent.EVENT.CLIENT_CREDENTIALS).setEventSource(_client));
                //
                OAuthASResponse.OAuthTokenResponseBuilder _builder = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(_tokenBean.getAccessToken())
                        .setExpiresIn(String.valueOf(_tokenBean.getExpiresIn()));
                _response = OAuthResponseUtils.appendParams(_client.getAttributes(), OAuthResponseUtils.appendParams(getParams(), _builder)).buildJSONMessage();
            }
        } catch (OAuthProblemException e) {
            _response = buildError(e);
        }
        return _response;
    }
}
