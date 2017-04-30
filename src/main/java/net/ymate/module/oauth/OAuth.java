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

import net.ymate.module.oauth.impl.DefaultModuleCfg;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/26 上午 02:08
 * @version 1.0
 */
@Module
public class OAuth implements IModule, IOAuth {

    private static final Log _LOG = LogFactory.getLog(OAuth.class);

    public static final Version VERSION = new Version(1, 0, 0, OAuth.class.getPackage().getImplementationVersion(), Version.VersionType.Alphal);

    private static volatile IOAuth __instance;

    private YMP __owner;

    private IOAuthModuleCfg __moduleCfg;

    private boolean __inited;

    public static IOAuth get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(OAuth.class);
                }
            }
        }
        return __instance;
    }

    public String getName() {
        return IOAuth.MODULE_NAME;
    }

    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-module-oauth-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultModuleCfg(owner);
            __moduleCfg.getTokenStorageAdapter().init(this);
            if (__moduleCfg.getUserInfoAdapter() != null) {
                __moduleCfg.getUserInfoAdapter().init(this);
            }
            //
            __inited = true;
        }
    }

    public boolean isInited() {
        return __inited;
    }

    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            if (__moduleCfg.getUserInfoAdapter() != null) {
                __moduleCfg.getUserInfoAdapter().destroy();
            }
            __moduleCfg.getTokenStorageAdapter().destroy();
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    public YMP getOwner() {
        return __owner;
    }

    public IOAuthModuleCfg getModuleCfg() {
        return __moduleCfg;
    }

    public IOAuthClientHelper bindClientHelper(final String clientId, final String clientSecret) throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("clientId");
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new NullArgumentException("clientSecret");
        }
        //
        return new IOAuthClientHelper() {

            private OAuthClient _clientVO = __moduleCfg.getTokenStorageAdapter().findClientById(clientId);

            public OAuthClient getOAuthClient() {
                return _clientVO;
            }

            public boolean checkClientId() {
                return _clientVO != null;
            }

            public boolean checkClientSecret() {
                return _clientVO != null && StringUtils.equals(clientSecret, _clientVO.getSecretKey());
            }

            public OAuthToken createOrUpdateAccessToken() throws Exception {
                if (_clientVO != null) {
                    return __moduleCfg.getTokenStorageAdapter().saveOrUpdateClientAccessToken(clientId, __moduleCfg.getTokenGenerator().accessToken(), OAuth.get().getModuleCfg().getAccessTokenExpireIn());
                }
                return null;
            }
        };
    }

    //

    public IOAuthAuthzHelper bindAuthzHelper(final String clientId, final String uid) throws Exception {
        return new IOAuthAuthzHelper() {

            private OAuthClient _clientVO = __moduleCfg.getTokenStorageAdapter().findClientById(clientId);

            private OAuthClientUser _clientUserVO = __moduleCfg.getTokenStorageAdapter().findUser(clientId, uid);

            public OAuthClient getOAuthClient() {
                return _clientVO;
            }

            public OAuthClientUser getOAuthClientUser() {
                return _clientUserVO;
            }

            public boolean checkClientId() {
                return _clientUserVO != null && StringUtils.isNotBlank(_clientUserVO.getClientId());
            }

            public boolean checkUserNeedAuth() {
                return _clientUserVO == null || !BlurObject.bind(_clientUserVO.getIsAuthorized()).toBooleanValue();
            }

            public OAuthCode createOrUpdateAuthCode(String redirectUri, String scope) throws Exception {
                if (_clientUserVO != null) {
                    return __moduleCfg.getTokenStorageAdapter().saveOrUpdateAuthCode(__moduleCfg.getTokenGenerator().authorizationCode(), redirectUri, clientId, uid, scope);
                }
                return null;
            }
        };
    }

    //

    public IOAuthTokenHelper bindTokenHelper(final String clientId, final String clientSecret, final String code) throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("clientId");
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new NullArgumentException("clientSecret");
        }
        if (StringUtils.isBlank(code)) {
            throw new NullArgumentException("code");
        }
        //
        return new IOAuthTokenHelper() {

            private OAuthClient _clientVO = __moduleCfg.getTokenStorageAdapter().findClientById(clientId);

            private OAuthCode _authzCode = __moduleCfg.getTokenStorageAdapter().findAuthCode(clientId, code);

            public OAuthClient getOAuthClient() {
                return _clientVO;
            }

            public OAuthClientUser getOAuthClientUser() {
                throw new UnsupportedOperationException();
            }

            public OAuthCode getOAuthCode() {
                return _authzCode;
            }

            public boolean checkClientId() {
                return _clientVO != null;
            }

            public boolean checkClientSecret() {
                return _clientVO != null && StringUtils.equals(clientSecret, _clientVO.getSecretKey());
            }

            public boolean checkAuthCode() {
                return _authzCode != null;
            }

            public boolean checkAuthUser() {
                throw new UnsupportedOperationException();
            }

            public boolean isExpiredRefreshToken() {
                throw new UnsupportedOperationException();
            }

            public boolean checkRefreshToken() {
                throw new UnsupportedOperationException();
            }

            public OAuthSnsToken refreshAccessToken() {
                throw new UnsupportedOperationException();
            }

            public OAuthSnsToken createOrUpdateAccessToken() throws Exception {
                if (_clientVO != null && _authzCode != null) {
                    return __moduleCfg.getTokenStorageAdapter()
                            .saveOrUpdateAccessToken(clientId, _authzCode.getUid(), _authzCode.getScope(),
                                    __moduleCfg.getTokenGenerator().accessToken(),
                                    __moduleCfg.getTokenGenerator().refreshToken(),
                                    OAuth.get().getModuleCfg().getAccessTokenExpireIn(), false);
                }
                return null;
            }
        };
    }

    public IOAuthTokenHelper bindTokenHelper(final String clientId, final String clientSecret, final String scope, final String username, final String passwd) throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("clientId");
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new NullArgumentException("clientSecret");
        }
        if (StringUtils.isBlank(scope)) {
            throw new NullArgumentException("scope");
        }
        if (StringUtils.isBlank(username)) {
            throw new NullArgumentException("username");
        }
        if (StringUtils.isBlank(passwd)) {
            throw new NullArgumentException("passwd");
        }
        //
        return new IOAuthTokenHelper() {

            private OAuthClient _clientVO = __moduleCfg.getTokenStorageAdapter().findClientById(clientId);

            private String _uid;

            public OAuthClient getOAuthClient() {
                return _clientVO;
            }

            public OAuthClientUser getOAuthClientUser() {
                throw new UnsupportedOperationException();
            }

            public OAuthCode getOAuthCode() {
                throw new UnsupportedOperationException();
            }

            public boolean checkClientId() {
                return _clientVO != null;
            }

            public boolean checkClientSecret() {
                return _clientVO != null && StringUtils.equals(clientSecret, _clientVO.getSecretKey());
            }

            public boolean checkAuthCode() {
                throw new UnsupportedOperationException();
            }

            public boolean checkAuthUser() {
                if (_uid == null) {
                    try {
                        _uid = __moduleCfg.getUserInfoAdapter().verify(username, passwd);
                    } catch (Exception e) {
                        _LOG.warn("", RuntimeUtils.unwrapThrow(e));
                    }
                }
                return _uid != null;
            }

            public boolean isExpiredRefreshToken() {
                throw new UnsupportedOperationException();
            }

            public boolean checkRefreshToken() {
                throw new UnsupportedOperationException();
            }

            public OAuthSnsToken refreshAccessToken() {
                throw new UnsupportedOperationException();
            }

            public OAuthSnsToken createOrUpdateAccessToken() throws Exception {
                if (_clientVO != null && _uid != null) {
                    return __moduleCfg.getTokenStorageAdapter()
                            .saveOrUpdateAccessToken(clientId, _uid, scope,
                                    __moduleCfg.getTokenGenerator().accessToken(),
                                    __moduleCfg.getTokenGenerator().refreshToken(),
                                    OAuth.get().getModuleCfg().getAccessTokenExpireIn(), false);
                }
                return null;
            }
        };
    }

    public IOAuthTokenHelper bindTokenHelper(final String clientId, final String refreshToken) throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("clientId");
        }
        if (StringUtils.isBlank(refreshToken)) {
            throw new NullArgumentException("refreshToken");
        }
        //
        return new IOAuthTokenHelper() {

            private OAuthClientUser _clientUserVO = __moduleCfg.getTokenStorageAdapter().findUserByRefreshToken(clientId, refreshToken);

            public OAuthClient getOAuthClient() {
                throw new UnsupportedOperationException();
            }

            public OAuthClientUser getOAuthClientUser() {
                return _clientUserVO;
            }

            public OAuthCode getOAuthCode() {
                throw new UnsupportedOperationException();
            }

            public boolean checkClientId() {
                return _clientUserVO != null;
            }

            public boolean checkClientSecret() {
                throw new UnsupportedOperationException();
            }

            public boolean checkAuthCode() {
                throw new UnsupportedOperationException();
            }

            public boolean checkAuthUser() {
                throw new UnsupportedOperationException();
            }

            public boolean isExpiredRefreshToken() {
                if (_clientUserVO != null) {
                    int _dayNum = 90;
                    switch (_clientUserVO.getRefreshCount()) {
                        case 0:
                            _dayNum = 7;
                            break;
                        case 1:
                            _dayNum = 30;
                            break;
                    }
                    return System.currentTimeMillis() - _clientUserVO.getLastModifyTime() >= DateTimeUtils.DAY * _dayNum;
                }
                return true;
            }

            public boolean checkRefreshToken() {
                return _clientUserVO != null;
            }

            public OAuthSnsToken refreshAccessToken() throws Exception {
                if (_clientUserVO != null) {
                    if (System.currentTimeMillis() - _clientUserVO.getLastModifyTime() < _clientUserVO.getExpiresIn() * 1000) {
                        return __moduleCfg.getTokenStorageAdapter()
                                .saveOrUpdateAccessToken(clientId, _clientUserVO.getUid(), null, null, __moduleCfg.getTokenGenerator().refreshToken(), 0, true);
                    } else {
                        return __moduleCfg.getTokenStorageAdapter()
                                .saveOrUpdateAccessToken(clientId, _clientUserVO.getUid(), _clientUserVO.getScope(),
                                        __moduleCfg.getTokenGenerator().accessToken(),
                                        __moduleCfg.getTokenGenerator().refreshToken(),
                                        OAuth.get().getModuleCfg().getAccessTokenExpireIn(), true);
                    }
                }
                return null;
            }

            public OAuthSnsToken createOrUpdateAccessToken() {
                throw new UnsupportedOperationException();
            }
        };
    }

    //

    public IOAuthAccessResourceHelper bindAccessResourceHelper(final String accessToken) throws Exception {
        return new IOAuthAccessResourceHelper() {

            private OAuthClient _clientVO = __moduleCfg.getTokenStorageAdapter().findClientByAccessToken(accessToken);

            public OAuthClient getOAuthClient() {
                return _clientVO;
            }

            public OAuthClientUser getOAuthClientUser() {
                throw new UnsupportedOperationException();
            }

            public boolean isExpiredAccessToken() {
                return System.currentTimeMillis() - _clientVO.getLastModifyTime() >= _clientVO.getExpiresIn() * 1000;
            }

            public boolean checkAccessToken() {
                return _clientVO != null;
            }

            public boolean checkScope(String scope) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public IOAuthAccessResourceHelper bindAccessResourceHelper(final String accessToken, final String openId) throws Exception {
        return new IOAuthAccessResourceHelper() {

            private OAuthClientUser _clientUserVO = __moduleCfg.getTokenStorageAdapter().findUserByAccessToken(accessToken);

            public OAuthClient getOAuthClient() {
                throw new UnsupportedOperationException();
            }

            public OAuthClientUser getOAuthClientUser() {
                return _clientUserVO;
            }

            public boolean isExpiredAccessToken() {
                return System.currentTimeMillis() - _clientUserVO.getLastModifyTime() >= _clientUserVO.getExpiresIn() * 1000;
            }

            public boolean checkAccessToken() {
                return _clientUserVO != null && StringUtils.equals(_clientUserVO.getId(), openId);
            }

            public boolean checkScope(String scope) {
                return StringUtils.contains(_clientUserVO.getScope(), scope);
            }
        };
    }
}
