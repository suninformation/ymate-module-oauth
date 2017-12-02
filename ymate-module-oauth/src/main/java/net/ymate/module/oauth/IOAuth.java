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
package net.ymate.module.oauth;

import net.ymate.platform.core.YMP;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/26 上午 02:08
 * @version 1.0
 */
public interface IOAuth {

    String MODULE_NAME = "module.oauth";

    /**
     * @return 返回所属YMP框架管理器实例
     */
    YMP getOwner();

    /**
     * @return 返回模块配置对象
     */
    IOAuthModuleCfg getModuleCfg();

    /**
     * @return 返回模块是否已初始化
     */
    boolean isInited();

    IOAuthClientHelper clientHelper(String clientId, String clientSecret) throws Exception;

    IOAuthAuthzHelper authzHelper(String clientId, String uid) throws Exception;

    IOAuthTokenHelper tokenHelper(String clientId, String clientSecret, String code) throws Exception;

    IOAuthTokenHelper tokenHelper(String clientId, String clientSecret, String scope, String uid) throws Exception;

    IOAuthTokenHelper tokenHelper(String clientId, String clientSecret, String scope, String username, String passwd) throws Exception;

    IOAuthTokenHelper tokenHelper(String clientId, String refreshToken) throws Exception;

    IOAuthAccessResourceHelper resourceHelper(String accessToken) throws Exception;

    IOAuthAccessResourceHelper resourceHelper(String accessToken, String openId) throws Exception;

    /**
     * OAuth授权作用域
     */
    final class Scope {

        public static final String SNSAPI_BASE = "snsapi_base";

        public static final String SNSAPI_USERINFO = "snsapi_userinfo";

        public static boolean verified(String scope) {
            return StringUtils.equalsIgnoreCase(scope, IOAuth.Scope.SNSAPI_BASE)
                    || StringUtils.equalsIgnoreCase(scope, IOAuth.Scope.SNSAPI_USERINFO);
        }
    }

    final class Const {

        public static final String ACCESS_TOKEN = "access_token";

        public static final String SCOPE = "scope";

        public static final String OPEN_ID = "open_id";

        public static final String INVALID_USER = "invalid_user";

        public static final String INVALID_REDIRECT_URI = "invalid_redirect_uri";

        public static final String REDIRECT_URI_MISMATCH = "redirect_uri_mismatch";
    }

    interface IOAuthClientHelper {

        OAuthClient getOAuthClient();

        boolean checkClientId();

        boolean checkClientSecret();

        OAuthToken createOrUpdateAccessToken() throws Exception;
    }

    interface IOAuthAuthzHelper {

        OAuthClient getOAuthClient();

        OAuthClientUser getOAuthClientUser();

        boolean checkClientId();

        boolean checkUserNeedAuth();

        OAuthCode createOrUpdateAuthCode(String redirectUri, String scope) throws Exception;
    }

    interface IOAuthTokenHelper {

        OAuthClient getOAuthClient();

        OAuthClientUser getOAuthClientUser();

        OAuthCode getOAuthCode();

        boolean checkClientId();

        boolean checkClientSecret();

        boolean checkAuthCode();

        boolean checkAuthUser();

        boolean isExpiredRefreshToken();

        boolean checkRefreshToken();

        OAuthSnsToken refreshAccessToken() throws Exception;

        OAuthSnsToken createOrUpdateAccessToken() throws Exception;
    }

    interface IOAuthAccessResourceHelper {

        OAuthClient getOAuthClient();

        OAuthClientUser getOAuthClientUser();

        boolean isExpiredAccessToken();

        boolean checkAccessToken();

        boolean checkScope(String scope);
    }
}