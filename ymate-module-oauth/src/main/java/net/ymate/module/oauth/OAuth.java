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

import net.ymate.module.oauth.annotation.OAuthScope;
import net.ymate.module.oauth.base.OAuthClientUserBean;
import net.ymate.module.oauth.base.OAuthTokenBean;
import net.ymate.module.oauth.handle.OAuthScopeHandler;
import net.ymate.module.oauth.impl.*;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.BeanMeta;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private Map<String, Class<? extends IOAuthScopeProcessor>> __scopeProcessors = new HashMap<String, Class<? extends IOAuthScopeProcessor>>();

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

    @Override
    public String getName() {
        return IOAuth.MODULE_NAME;
    }

    @Override
    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-module-oauth-" + VERSION);
            //
            __owner = owner;
            __owner.getEvents().registerEvent(OAuthEvent.class);
            __owner.registerHandler(OAuthScope.class, new OAuthScopeHandler(this));
            __moduleCfg = new DefaultModuleCfg(owner);
            //
            if (__moduleCfg.getStorageAdapter() != null) {
                __moduleCfg.getStorageAdapter().init(this);
            }
            //
            __inited = true;
        }
    }

    @Override
    public boolean isInited() {
        return __inited;
    }

    @Override
    public void registerScopeProcessor(Class<? extends IOAuthScopeProcessor> targetClass) throws Exception {
        IOAuthScopeProcessor _scopeProc = targetClass.newInstance();
        if (StringUtils.isNotBlank(_scopeProc.getName())) {
            _scopeProc.init(this);
            __scopeProcessors.put(_scopeProc.getName(), targetClass);
            __owner.registerBean(BeanMeta.create(_scopeProc, targetClass));
            //
            if (__owner.getConfig().isDevelopMode()) {
                _LOG.debug("--> Registered scope processor [" + _scopeProc.getName() + "] - " + targetClass.getName());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IOAuthScopeProcessor> T getScopeProcessor(String name) {
        if (StringUtils.isNotBlank(name) && __scopeProcessors.containsKey(name)) {
            return (T) YMP.get().getBean(__scopeProcessors.get(name));
        }
        return null;
    }

    @Override
    public Set<String> getScopeNames() {
        return Collections.unmodifiableSet(__scopeProcessors.keySet());
    }

    @Override
    public IOAuthGrantProcessor getGrantProcessor(GrantType grantType) {
        IOAuthGrantProcessor _process = null;
        if (grantType != null) {
            if (!__moduleCfg.getAllowGrantTypes().isEmpty() && __moduleCfg.getAllowGrantTypes().contains(grantType)) {
                switch (grantType) {
                    case AUTHORIZATION_CODE:
                        _process = new AuthorizationCodeGrantProcessor(this);
                        break;
                    case IMPLICIT:
                        _process = new ImplicitGrantProcessor(this, ResponseType.TOKEN);
                        break;
                    case PASSWORD:
                        _process = new PasswordGrantProcessor(this);
                        break;
                    case REFRESH_TOKEN:
                        _process = new RefreshTokenGrantProcessor(this);
                        break;
                    case CLIENT_CREDENTIALS:
                        _process = new ClientCredentialsGrantProcessor(this);
                        break;
                    default:
                        _process = IOAuthGrantProcessor.UNSUPPORTED_GRANT_TYPE;
                }
            }
        }
        return _process;
    }

    @Override
    public OAuthResponse checkClientAccessToken(HttpServletRequest request) throws Exception {
        OAuthResponse _response = null;
        try {
            OAuthAccessResourceRequest _oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            OAuthTokenBean _tokenBean = __moduleCfg.getStorageAdapter().findClientByAccessToken(_oauthRequest.getAccessToken());
            //
            if (_tokenBean == null) {
                _response = __moduleCfg.getErrorAdapter().onError(ErrorType.INVALID_TOKEN);
            } else if (!_tokenBean.checkAccessToken()) {
                _response = __moduleCfg.getErrorAdapter().onError(ErrorType.EXPIRED_TOKEN);
            } else {
                request.setAttribute(OAuthTokenBean.class.getName(), _tokenBean);
                request.setAttribute(Const.ACCESS_TOKEN, _oauthRequest.getAccessToken());
            }
        } catch (OAuthProblemException e) {
            _response = __moduleCfg.getErrorAdapter().onError(e);
        }
        return _response;
    }

    @Override
    public OAuthResponse checkUserAccessToken(HttpServletRequest request, String scope) throws Exception {
        OAuthResponse _response = null;
        try {
            OAuthAccessResourceRequest _oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            OAuthClientUserBean _tokenBean = __moduleCfg.getStorageAdapter().findUserByAccessToken(_oauthRequest.getAccessToken());
            //
            if (_tokenBean == null) {
                _response = __moduleCfg.getErrorAdapter().onError(ErrorType.INVALID_TOKEN);
            } else if (!_tokenBean.checkAccessToken()) {
                _response = __moduleCfg.getErrorAdapter().onError(ErrorType.EXPIRED_TOKEN);
            } else if (StringUtils.isNotBlank(scope) && !_tokenBean.containsScope(Collections.singleton(scope))) {
                _response = __moduleCfg.getErrorAdapter().onError(ErrorType.INSUFFICIENT_SCOPE);
            } else {
                request.setAttribute(OAuthTokenBean.class.getName(), _tokenBean);
                request.setAttribute(Const.ACCESS_TOKEN, _oauthRequest.getAccessToken());
            }
        } catch (OAuthProblemException e) {
            _response = __moduleCfg.getErrorAdapter().onError(e);
        }
        return _response;
    }

    @Override
    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            if (__moduleCfg.getStorageAdapter() != null) {
                __moduleCfg.getStorageAdapter().destroy();
            }
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    @Override
    public YMP getOwner() {
        return __owner;
    }

    @Override
    public IOAuthModuleCfg getModuleCfg() {
        return __moduleCfg;
    }
}
