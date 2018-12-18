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
package net.ymate.module.oauth.client.impl;

import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.module.oauth.client.IOAuthClientTokenStorageAdapter;
import net.ymate.module.oauth.client.IOAuthClients;
import net.ymate.module.oauth.client.base.OAuthAccessToken;
import net.ymate.module.oauth.client.base.OAuthAccount;
import net.ymate.platform.core.support.ReentrantLockHelper;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/8 上午11:36
 * @version 1.0
 */
public class DefaultOAuthClientTokenStorageAdapter implements IOAuthClientTokenStorageAdapter {

    private static final Log _LOG = LogFactory.getLog(DefaultOAuthClientTokenStorageAdapter.class);

    private static final Map<String, OAuthAccessToken> __TOKEN_CACHES = new ConcurrentHashMap<String, OAuthAccessToken>();

    @Override
    public void init(IOAuthClients owner) throws Exception {
    }

    @Override
    public void destroy() throws Exception {
    }

    private OAuthAccessToken __doGetAccessToken(String accountId) throws Exception {
        OAuthAccessToken _accessToken = __TOKEN_CACHES.get(accountId);
        if (_accessToken == null || _accessToken.isExpired()) {
            // TODO 尝试从缓存或数据库中加载
        }
        return null;
    }

    private OAuthAccessToken __doGetAccessToken(OAuthAccount account) throws Exception {
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("grant_type", "client_credentials");
        _params.put("client_id", account.getClientId());
        _params.put("client_secret", account.getClientSecret());
        //
        IHttpResponse _response = HttpClientHelper.create().post(account.getServiceUrl(), _params);
        if (_response != null) {
            OAuthAccessToken.Result _result = new OAuthAccessToken.Result(_response);
            if (_result.isOK()) {
                saveOrUpdateAccessToken(account, _result);
                return _result.getAccessToken();
            }
        }
        return null;
    }

    @Override
    public OAuthAccessToken loadAccessToken(OAuthAccount account) {
        OAuthAccessToken _accessToken = __TOKEN_CACHES.get(account.getId());
        if (_accessToken == null || _accessToken.isExpired()) {
            ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId().concat("_token"));
            _locker.lock();
            try {
                _accessToken = __doGetAccessToken(account.getId());
                if (_accessToken == null || _accessToken.isExpired()) {
                    _accessToken = __doGetAccessToken(account);
                }
            } catch (Exception e) {
                try {
                    _accessToken = __doGetAccessToken(account.getId());
                    if (_accessToken == null || _accessToken.isExpired()) {
                        _accessToken = __doGetAccessToken(account);
                    }
                } catch (Exception ex) {
                    _LOG.warn("Exception when loading access_token: ", RuntimeUtils.unwrapThrow(ex));
                }
            } finally {
                if (_locker.isLocked()) {
                    _locker.unlock();
                }
            }
        }
        return _accessToken;
    }

    @Override
    public void saveOrUpdateAccessToken(OAuthAccount account, OAuthAccessToken.Result accessToken) {
        __TOKEN_CACHES.put(account.getId(), accessToken.getAccessToken());
    }
}
