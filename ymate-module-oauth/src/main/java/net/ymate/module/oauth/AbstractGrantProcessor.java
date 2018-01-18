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
package net.ymate.module.oauth;

import net.ymate.module.oauth.base.OAuthClientBean;
import net.ymate.module.oauth.base.OAuthClientUserBean;
import net.ymate.module.oauth.base.OAuthCodeBean;
import net.ymate.module.oauth.base.OAuthTokenBean;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/16 下午11:42
 * @version 1.0
 */
public abstract class AbstractGrantProcessor implements IOAuthGrantProcessor {

    private static final Log _LOG = LogFactory.getLog(AbstractGrantProcessor.class);

    private IOAuth __owner;

    private Map<String, Object> __params;

    private OAuthClientBean __clientBean;

    private OAuthClientUserBean __clientUserBean;

    public AbstractGrantProcessor(IOAuth owner) {
        __owner = owner;
        __params = new HashMap<String, Object>();
    }

    protected IOAuth getOwner() {
        return __owner;
    }

    protected Map<String, Object> getParams() {
        return __params;
    }

    protected BlurObject getParam(String key) {
        return BlurObject.bind(__params.get(key));
    }

    protected OAuthClientBean getClient(String clientId) {
        if (__clientBean == null) {
            try {
                __clientBean = __owner.getModuleCfg().getTokenStorageAdapter().findClient(clientId);
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
        }
        return __clientBean;
    }

    protected OAuthClientUserBean getClientUser(String clientId, String id, IdType idType) {
        if (__clientUserBean == null) {
            try {
                switch (idType) {
                    case REFRESH_TOKEN:
                        __clientUserBean = __owner.getModuleCfg().getTokenStorageAdapter().findUserByRefreshToken(clientId, id);
                        break;
                    case ACCESS_TOKEN:
                        __clientUserBean = __owner.getModuleCfg().getTokenStorageAdapter().findUserByAccessToken(id);
                        break;
                    case UID:
                        __clientUserBean = __owner.getModuleCfg().getTokenStorageAdapter().findUser(clientId, id);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
        }
        return __clientUserBean;
    }

    protected OAuthClientUserBean getClientUser(String clientId, String username, String password) {
        if (__clientUserBean == null) {
            try {
                __clientUserBean = __owner.getModuleCfg().getTokenStorageAdapter().findUser(clientId, username, password);
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
        }
        return __clientUserBean;
    }

    protected OAuthCodeBean getCode(String clientId, String code) throws Exception {
        return __owner.getModuleCfg().getTokenStorageAdapter().findCode(clientId, code);
    }

    private void __doCheckTokenBean(OAuthTokenBean tokenBean) {
        if (tokenBean == null || StringUtils.isBlank(tokenBean.getClientId())) {
            throw new NullArgumentException("client");
        }
        if (StringUtils.isBlank(tokenBean.getAccessToken())) {
            throw new NullArgumentException("accessToken");
        }
        if (tokenBean.getExpiresIn() <= 0) {
            throw new IllegalArgumentException("expiresIn");
        }
    }

    protected OAuthTokenBean saveOrUpdateToken(OAuthClientBean client) throws Exception {
        __doCheckTokenBean(client);
        //
        client.setLastModifyTime(System.currentTimeMillis());
        //
        return __owner.getModuleCfg().getTokenStorageAdapter().saveOrUpdateClientAccessToken(client.getClientId(), client.getAccessToken(), client.getLastAccessToken(), client.getExpiresIn());
    }

    protected OAuthClientUserBean saveOrUpdateToken(OAuthClientUserBean clientUser) throws Exception {
        __doCheckTokenBean(clientUser);
        //
        if (StringUtils.isBlank(clientUser.getUid())) {
            throw new NullArgumentException("uid");
        }
        if (StringUtils.isBlank(clientUser.getRefreshToken())) {
            throw new NullArgumentException("refreshToken");
        }
        //
        return __owner.getModuleCfg().getTokenStorageAdapter().saveOrUpdateUserAccessToken(clientUser.getClientId(), clientUser.getUid(), clientUser.getScope(), clientUser.getAccessToken(), clientUser.getLastAccessToken(), clientUser.getRefreshToken(), clientUser.getExpiresIn());
    }

    protected OAuthCodeBean saveOrUpdateCode(OAuthCodeBean codeBean) throws Exception {
        return __owner.getModuleCfg().getTokenStorageAdapter().saveOrUpdateCode(codeBean.getCode(), codeBean.getClientId(), codeBean.getRedirectUri(), codeBean.getUid(), codeBean.getScope());
    }

    @Override
    public IOAuthGrantProcessor setParam(String key, Object value) {
        __params.put(key, value);
        return this;
    }

    public enum IdType {
        UID, REFRESH_TOKEN, ACCESS_TOKEN
    }
}
