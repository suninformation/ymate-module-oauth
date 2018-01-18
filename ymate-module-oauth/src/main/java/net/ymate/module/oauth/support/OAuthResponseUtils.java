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
package net.ymate.module.oauth.support;

import net.ymate.platform.core.lang.BlurObject;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/9/6 下午4:07
 * @version 1.0
 */
public class OAuthResponseUtils {

    public static OAuthResponse badRequest(String error) throws OAuthSystemException {
        return OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(error)
                .buildJSONMessage();
    }

    public static OAuthResponse unauthorizedClient() throws OAuthSystemException {
        return OAuthASResponse
                .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                .buildJSONMessage();
    }

    public static OAuthResponse unauthorizedClient(String error) throws OAuthSystemException {
        return OAuthASResponse
                .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(error)
                .buildJSONMessage();
    }

    public static OAuthResponse badRequestError(OAuthProblemException e) throws OAuthSystemException {
        return OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .error(e)
                .buildJSONMessage();
    }

    public static OAuthResponse.OAuthResponseBuilder appendParams(Map<String, Object> params, OAuthResponse.OAuthResponseBuilder builder) {
        for (Map.Entry<String, Object> _entry : params.entrySet()) {
            builder.setParam(_entry.getKey(), BlurObject.bind(_entry.getValue()).toStringValue());
        }
        return builder;
    }
}
