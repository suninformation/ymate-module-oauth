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

import net.ymate.module.oauth.IOAuth;
import net.ymate.module.oauth.IOAuthErrorAdapter;
import net.ymate.module.oauth.support.OAuthResponseUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/2/22 上午12:58
 * @version 1.0
 */
public class DefaultErrorAdapter implements IOAuthErrorAdapter {

    @Override
    public OAuthResponse onError(IOAuth.ErrorType errorType) throws Exception {
        OAuthResponse _response;
        switch (errorType) {
            case INVALID_CLIENT:
                _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.INVALID_CLIENT);
                break;
            case UNAUTHORIZED_CLIENT:
                _response = OAuthResponseUtils.unauthorized(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT);
                break;
            case INVALID_TOKEN:
                _response = OAuthResponseUtils.badRequest(OAuthError.ResourceResponse.INVALID_TOKEN);
                break;
            case EXPIRED_TOKEN:
                _response = OAuthResponseUtils.unauthorized(OAuthError.ResourceResponse.EXPIRED_TOKEN);
                break;
            case INVALID_SCOPE:
                _response = OAuthResponseUtils.badRequest(OAuthError.CodeResponse.INVALID_SCOPE);
                break;
            case INSUFFICIENT_SCOPE:
                _response = OAuthResponseUtils.unauthorized(OAuthError.ResourceResponse.INSUFFICIENT_SCOPE);
                break;
            case INVALID_USER:
                _response = OAuthResponseUtils.badRequest(IOAuth.Const.INVALID_USER);
                break;
            case INVALID_REQUEST:
                _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.INVALID_REQUEST);
                break;
            case INVALID_REDIRECT_URI:
                _response = OAuthResponseUtils.badRequest(IOAuth.Const.INVALID_REDIRECT_URI);
                break;
            case REDIRECT_URI_MISMATCH:
                _response = OAuthResponseUtils.badRequest(IOAuth.Const.REDIRECT_URI_MISMATCH);
                break;
            case UNSUPPORTED_GRANT_TYPE:
                _response = OAuthResponseUtils.badRequest(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE);
                break;
            case UNSUPPORTED_RESPONSE_TYPE:
                _response = OAuthResponseUtils.badRequest(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE);
                break;
            default:
                _response = OAuthResponseUtils.badRequest(errorType.name().toLowerCase());
        }
        return _response;
    }

    @Override
    public OAuthResponse onError(OAuthProblemException e) throws Exception {
        return OAuthResponseUtils.badRequestError(e);
    }
}
