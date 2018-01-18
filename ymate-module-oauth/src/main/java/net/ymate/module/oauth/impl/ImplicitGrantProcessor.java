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
import net.ymate.module.oauth.base.OAuthCodeBean;
import net.ymate.module.oauth.support.NeedAuthorizationException;
import net.ymate.module.oauth.support.OAuthResponseUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/16 下午11:50
 * @version 1.0
 */
public class ImplicitGrantProcessor extends AbstractGrantProcessor {

    private final ResponseType __responseType;

    public ImplicitGrantProcessor(IOAuth owner, ResponseType responseType) {
        super(owner);
        //
        if (responseType == null) {
            throw new NullArgumentException("responseType");
        }
        __responseType = responseType;
    }

    @Override
    public OAuthResponse process(HttpServletRequest request) throws Exception {
        OAuthResponse _response;
        try {
            OAuthAuthzRequest _oauthRequest = new OAuthAuthzRequest(request);
            if (!StringUtils.equalsIgnoreCase(__responseType.toString(), _oauthRequest.getResponseType())) {
                _response = OAuthResponseUtils.badRequest(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE);
            } else {
                Set<String> _scopes = _oauthRequest.getScopes();
                if (!_scopes.contains(IOAuth.Const.SCOPE_SNSAPI_BASE)) {
                    _scopes.add(IOAuth.Const.SCOPE_SNSAPI_BASE);
                }
                //
                String _uid = getParam(IOAuth.Const.UID).toStringValue();
                String _redirectURI = _oauthRequest.getRedirectURI();
                //
                if (StringUtils.isBlank(_uid)) {
                    _response = OAuthResponseUtils.badRequest(IOAuth.Const.INVALID_USER);
                } else if (StringUtils.isBlank(_redirectURI)) {
                    _response = OAuthResponseUtils.badRequest(IOAuth.Const.INVALID_REDIRECT_URI);
                } else if (!getOwner().getScopeNames().containsAll(_scopes)) {
                    _response = OAuthResponseUtils.badRequest(OAuthError.CodeResponse.INVALID_SCOPE);
                } else {
                    OAuthClientBean _client = getClient(_oauthRequest.getClientId());
                    if (_client == null) {
                        _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.INVALID_CLIENT);
                    } else if (ResponseType.TOKEN.equals(__responseType) && !_client.checkSecret(_oauthRequest.getClientSecret())) {
                        _response = OAuthResponseUtils.unauthorizedClient();
                    } else {
                        String _scope = OAuthUtils.encodeScopes(_scopes);
                        String _state = _oauthRequest.getState();
                        if ("POST".equalsIgnoreCase(request.getMethod())) {
                            if (!getParam(IOAuth.Const.AUTHORIZED).toBooleanValue()) {
                                _response = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                                        .location(_redirectURI)
                                        .setParam(org.apache.oltu.oauth2.common.OAuth.OAUTH_STATE, _state)
                                        .buildQueryMessage();
                            } else {
                                _response = __doParseResponseType(request, _client.getClientId(), _redirectURI, _scope, _uid, _state);
                            }
                        } else {
                            OAuthClientUserBean _clientUser = getClientUser(_client.getClientId(), _uid, IdType.UID);
                            if (_scopes.size() > 1 && (_clientUser == null || !_clientUser.isAuthorized())) {
                                // 若需要用户授权则跳转
                                throw new NeedAuthorizationException(_client, _uid, _oauthRequest.getScopes());
                            } else {
                                _response = __doParseResponseType(request, _client.getClientId(), _redirectURI, _scope, _uid, _state);
                            }
                        }
                    }
                }
            }
        } catch (OAuthProblemException e) {
            _response = OAuthResponseUtils.badRequestError(e);
        }
        return _response;
    }

    private OAuthResponse __doParseResponseType(HttpServletRequest request, String clientId, String _redirectURI, String scope, String uid, String state) throws Exception {
        OAuthResponse _response;
        switch (__responseType) {
            case CODE:
                OAuthCodeBean _codeBean = new OAuthCodeBean(getOwner().getModuleCfg().getTokenGenerator().authorizationCode(), _redirectURI, clientId, uid, scope);
                _codeBean = saveOrUpdateCode(_codeBean);
                //
                OAuthASResponse.OAuthAuthorizationResponseBuilder _authBuilder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                        .location(_redirectURI)
                        .setCode(_codeBean.getCode());
                if (StringUtils.isNotBlank(state)) {
                    _authBuilder.setParam(org.apache.oltu.oauth2.common.OAuth.OAUTH_STATE, state);
                }
                _response = _authBuilder.buildQueryMessage();
                break;
            case TOKEN:
                OAuthClientUserBean _clientUser = getClientUser(clientId, uid, IdType.UID);
                if (_clientUser == null) {
                    _clientUser = new OAuthClientUserBean(clientId, uid, true, scope, getOwner().getModuleCfg().getTokenGenerator().accessToken(), null, getOwner().getModuleCfg().getTokenGenerator().refreshToken(), 0, getOwner().getModuleCfg().getAccessTokenExpireIn());
                } else {
                    _clientUser.setAuthorized(true);
                    _clientUser.setLastAccessToken(_clientUser.getAccessToken());
                    _clientUser.setAccessToken(getOwner().getModuleCfg().getTokenGenerator().accessToken());
                    _clientUser.setRefreshToken(getOwner().getModuleCfg().getTokenGenerator().refreshToken());
                    _clientUser.setScope(scope);
                    _clientUser.setRefreshCount(0);
                    _clientUser.setExpiresIn(getOwner().getModuleCfg().getAccessTokenExpireIn());
                }
                _clientUser = saveOrUpdateToken(_clientUser);
                //
                OAuthResponse.OAuthResponseBuilder _builder = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(_clientUser.getAccessToken())
                        .setExpiresIn(String.valueOf(_clientUser.getExpiresIn()))
                        .setRefreshToken(_clientUser.getRefreshToken())
                        .setScope(_clientUser.getScope())
                        .setParam(IOAuth.Const.OPEN_ID, _clientUser.getOpenId());
                if (StringUtils.isNotBlank(state)) {
                    _builder.setParam(org.apache.oltu.oauth2.common.OAuth.OAUTH_STATE, state);
                }
                _response = OAuthResponseUtils.appendParams(getParams(), _builder).buildJSONMessage();
                break;
            default:
                _response = OAuthResponseUtils.badRequest(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE);
        }
        return _response;
    }
}
