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
import net.ymate.module.oauth.IOAuthStorageAdapter;
import net.ymate.module.oauth.base.*;
import net.ymate.module.oauth.model.OAuthClient;
import net.ymate.module.oauth.model.OAuthUser;
import net.ymate.module.oauth.support.UserAuthenticationException;
import net.ymate.platform.cache.Caches;
import net.ymate.platform.cache.ICache;
import net.ymate.platform.cache.ICacheLocker;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.jdbc.query.IDBLocker;
import net.ymate.platform.persistence.jdbc.transaction.Trade;
import net.ymate.platform.persistence.jdbc.transaction.Transactions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/2/11 上午2:23
 * @version 1.0
 */
public class DefaultOAuthStorageAdapter implements IOAuthStorageAdapter {

    private IOAuth __owner;

    private ICache __dataCache;

    private ICache __codeCache;

    @Override
    public void init(IOAuth owner) throws Exception {
        __owner = owner;
        __dataCache = Caches.get().getCacheProvider().getCache(owner.getModuleCfg().getCacheNamePrefix().concat("oauth_data"));
        __codeCache = Caches.get().getCacheProvider().getCache(owner.getModuleCfg().getCacheNamePrefix().concat("oauth_code"));
    }

    @Override
    public void destroy() throws Exception {
        __codeCache = null;
        __dataCache = null;
        __owner = null;
    }

    protected IOAuth getOwner() {
        return __owner;
    }

    protected OAuthClientBean toOAuthClientBean(OAuthClient client) {
        if (client == null) {
            return null;
        }
        //
        OAuthClientBean _clientBean = new OAuthClientBean();
        //
        _clientBean.setClientId(client.getId());
        _clientBean.setIconUrl(client.getIconUrl());
        _clientBean.setDomain(client.getDomain());
        _clientBean.setSecretKey(client.getSecretKey());
        _clientBean.setAccessToken(client.getAccessToken());
        _clientBean.setLastAccessToken(client.getLastAccessToken());
        _clientBean.setExpiresIn(client.getExpiresIn());
        _clientBean.setCreateTime(client.getCreateTime());
        _clientBean.setLastModifyTime(client.getLastModifyTime());
        //
        return _clientBean;
    }

    protected OAuthClientUserBean toOAuthClientUserBean(OAuthUser user) {
        if (user == null) {
            return null;
        }
        //
        OAuthClientUserBean _clientUserBean = new OAuthClientUserBean();
        //
        _clientUserBean.setOpenId(user.getId());
        _clientUserBean.setUid(user.getUid());
        _clientUserBean.setClientId(user.getClientId());
        _clientUserBean.setAccessToken(user.getAccessToken());
        _clientUserBean.setLastAccessToken(user.getLastAccessToken());
        _clientUserBean.setExpiresIn(user.getExpiresIn());
        _clientUserBean.setScope(user.getScope());
        _clientUserBean.setRefreshToken(user.getRefreshToken());
        _clientUserBean.setRefreshCount(user.getRefreshCount());
        _clientUserBean.setAuthorized(BlurObject.bind(user.getIsAuthorized()).toBooleanValue());
        _clientUserBean.setCreateTime(user.getCreateTime());
        _clientUserBean.setLastModifyTime(user.getLastModifyTime());
        //
        return _clientUserBean;
    }

    @SuppressWarnings("unchecked")
    protected <T> T __getCacheElement(CacheDataType dataType, String targetId) {
        return (T) __dataCache.get(dataType.name().concat(targetId));
    }

    protected void __putCacheElement(CacheDataType dataType, OAuthTokenBean tokenBean) {
        if (dataType != null && tokenBean != null) {
            String _targetId;
            switch (dataType) {
                case CLIENT:
                    _targetId = tokenBean.getClientId();
                    break;
                default:
                    _targetId = ((OAuthClientUserBean) tokenBean).getOpenId();
            }
            if (StringUtils.isNotBlank(_targetId)) {
                String _cacheName = dataType.name().concat(_targetId);
                ICacheLocker _locker = __dataCache.acquireCacheLocker();
                if (_locker != null) {
                    _locker.writeLock(_cacheName);
                }
                try {
                    __dataCache.put(_cacheName, tokenBean);
                    if (StringUtils.isNotBlank(tokenBean.getAccessToken())) {
                        __dataCache.put(dataType.name().concat(tokenBean.getAccessToken()), _targetId);
                    }
                    if (StringUtils.isNotBlank(tokenBean.getLastAccessToken())) {
                        __dataCache.put(dataType.name().concat(tokenBean.getLastAccessToken()), _targetId);
                    }
                } finally {
                    if (_locker != null) {
                        _locker.releaseWriteLock(_cacheName);
                    }
                }
            }
        }
    }

    protected void __cleanCacheElement(CacheDataType dataType, String targetId, String accessToken, String lastAccessToken) {
        if (dataType != null) {
            if (StringUtils.isNotBlank(targetId)) {
                __dataCache.remove(dataType.name().concat(targetId));
            }
            if (StringUtils.isNotBlank(accessToken)) {
                __dataCache.remove(dataType.name().concat(accessToken));
            }
            if (StringUtils.isNotBlank(lastAccessToken)) {
                __dataCache.remove(dataType.name().concat(lastAccessToken));
            }
        }
    }

    @Override
    public OAuthClientBean findClient(String clientId) throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("clientId");
        }
        OAuthClientBean _clientBean = __getCacheElement(CacheDataType.CLIENT, clientId);
        if (_clientBean == null) {
            OAuthClient _client = OAuthClient.builder().id(clientId).status(0).build().load();
            if (_client != null) {
                _clientBean = toOAuthClientBean(_client);
                __putCacheElement(CacheDataType.CLIENT, _clientBean);
            }
        }
        return _clientBean;
    }

    @Override
    public OAuthClientBean findClientByAccessToken(String accessToken) throws Exception {
        if (StringUtils.isBlank(accessToken)) {
            throw new NullArgumentException("accessToken");
        }
        String _clientId = __getCacheElement(CacheDataType.CLIENT, accessToken);
        OAuthClientBean _clientBean = null;
        if (StringUtils.isNotBlank(_clientId)) {
            _clientBean = __getCacheElement(CacheDataType.CLIENT, _clientId);
        }
        if (_clientBean == null) {
            OAuthClient _client = OAuthClient.builder().accessToken(accessToken).status(0).build().findFirst();
            if (_client != null) {
                _clientBean = toOAuthClientBean(_client);
                __putCacheElement(CacheDataType.CLIENT, _clientBean);
            }
        }
        return _clientBean;
    }

    @Override
    public OAuthTokenBean saveOrUpdateClientAccessToken(final String clientId, final String accessToken, final String lastAccessToken, final int expiresIn) throws Exception {
        return Transactions.execute(new Trade<OAuthTokenBean>() {
            @Override
            public void deal() throws Throwable {
                OAuthClient _client = OAuthClient.builder().id(clientId).build().load(IDBLocker.DEFAULT);
                if (_client != null) {
                    __cleanCacheElement(CacheDataType.CLIENT, clientId, _client.getAccessToken(), _client.getLastAccessToken());
                    //
                    _client = new OAuthClient.OAuthClientBuilder(_client)
                            .accessToken(accessToken)
                            .lastAccessToken(lastAccessToken)
                            .expiresIn(expiresIn)
                            .lastModifyTime(System.currentTimeMillis()).build()
                            .update(Fields.create(OAuthClient.FIELDS.ACCESS_TOKEN,
                                    OAuthClient.FIELDS.LAST_ACCESS_TOKEN,
                                    OAuthClient.FIELDS.EXPIRES_IN,
                                    OAuthClient.FIELDS.LAST_MODIFY_TIME));
                }
                setReturns(toOAuthClientBean(_client));
            }
        });
    }

    @Override
    public boolean removeClientAccessToken(String clientId, String accessToken) throws Exception {
        if (StringUtils.isNotBlank(accessToken)) {
            if (StringUtils.isBlank(clientId)) {
                clientId = __getCacheElement(CacheDataType.CLIENT, accessToken);
            }
            //
            OAuthClientBean _clientBean = null;
            if (StringUtils.isNotBlank(clientId)) {
                _clientBean = __getCacheElement(CacheDataType.CLIENT, clientId);
            }
            __cleanCacheElement(CacheDataType.CLIENT, clientId, accessToken, _clientBean == null ? null : _clientBean.getLastAccessToken());
            //
            if (_clientBean != null) {
                OAuthClient.builder().id(_clientBean.getClientId()).lastModifyTime(System.currentTimeMillis()).build()
                        .update(Fields.create(OAuthClient.FIELDS.ACCESS_TOKEN,
                                OAuthClient.FIELDS.LAST_ACCESS_TOKEN,
                                OAuthClient.FIELDS.EXPIRES_IN,
                                OAuthClient.FIELDS.LAST_MODIFY_TIME));
            }
            //
            return true;
        }
        return false;
    }

    @Override
    public OAuthCodeBean saveOrUpdateCode(String code, String clientId, String redirectUri, String uid, String scope) throws Exception {
        OAuthCodeBean _codeBean = new OAuthCodeBean(code, redirectUri, clientId, uid, scope);
        __codeCache.put(code, _codeBean);
        //
        return _codeBean;
    }

    @Override
    public OAuthCodeBean findCode(String clientId, String code) throws Exception {
        OAuthCodeBean _codeBean = (OAuthCodeBean) __codeCache.get(code);
        if (_codeBean != null) {
            removeCode(clientId, code);
            if (StringUtils.equals(_codeBean.getClientId(), clientId)) {
                return _codeBean;
            }
        }
        return null;
    }

    @Override
    public boolean removeCode(String clientId, String code) throws Exception {
        __codeCache.remove(code);
        return true;
    }

    public String buildOpenId(String clientId, String uid) {
        return DigestUtils.md5Hex(clientId + uid);
    }

    @Override
    public OAuthClientUserBean findUser(String clientId, String uid) throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("clientId");
        }
        if (StringUtils.isBlank(uid)) {
            throw new NullArgumentException("uid");
        }
        String _targetId = buildOpenId(clientId, uid);
        OAuthClientUserBean _clientUserBean = __getCacheElement(CacheDataType.CLIENT_USER, _targetId);
        if (_clientUserBean == null) {
            OAuthUser _user = OAuthUser.builder().id(_targetId).build().load();
            if (_user != null) {
                _clientUserBean = toOAuthClientUserBean(_user);
                __putCacheElement(CacheDataType.CLIENT_USER, _clientUserBean);
            }
        }
        return _clientUserBean;
    }

    @Override
    public OAuthClientUserBean findUserByAccessToken(String accessToken) throws Exception {
        if (StringUtils.isBlank(accessToken)) {
            throw new NullArgumentException("accessToken");
        }
        String _targetId = __getCacheElement(CacheDataType.CLIENT_USER, accessToken);
        OAuthClientUserBean _userBean = null;
        if (StringUtils.isNotBlank(_targetId)) {
            _userBean = __getCacheElement(CacheDataType.CLIENT_USER, _targetId);
        }
        if (_userBean == null) {
            OAuthUser _user = OAuthUser.builder().accessToken(accessToken).build().findFirst();
            if (_user != null) {
                _userBean = toOAuthClientUserBean(_user);
                __putCacheElement(CacheDataType.CLIENT_USER, _userBean);
            }
        }
        return _userBean;
    }

    @Override
    public OAuthClientUserBean findUserByRefreshToken(String clientId, String refreshToken) throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("clientId");
        }
        if (StringUtils.isBlank(refreshToken)) {
            throw new NullArgumentException("refreshToken");
        }
        OAuthUser _user = OAuthUser.builder().clientId(clientId).refreshToken(refreshToken).build().findFirst();
        if (_user != null) {
            return toOAuthClientUserBean(_user);
        }
        return null;
    }

    @Override
    public OAuthClientUserBean findUser(String clientId, String username, String password) throws UserAuthenticationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuthUserInfoBean findUserInfo(String clientId, String openId) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuthClientUserBean saveOrUpdateUserAccessToken(final String clientId, final String uid, final String scope, final String accessToken, final String lastAccessToken, final String refreshToken, final int expiresIn, final boolean refresh) throws Exception {
        return Transactions.execute(new Trade<OAuthClientUserBean>() {
            public void deal() throws Throwable {
                String _targetId = buildOpenId(clientId, uid);
                OAuthUser _user = OAuthUser.builder().id(_targetId).build().load(IDBLocker.DEFAULT);
                long _now = System.currentTimeMillis();
                if (_user == null) {
                    _user = OAuthUser.builder()
                            .id(_targetId)
                            .uid(uid)
                            .clientId(clientId)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .refreshCount(0)
                            .scope(scope)
                            .isAuthorized(1)
                            .expiresIn(expiresIn)
                            .createTime(_now)
                            .lastModifyTime(_now)
                            .build().save();
                } else {
                    __cleanCacheElement(CacheDataType.CLIENT_USER, _targetId, _user.getAccessToken(), _user.getLastAccessToken());
                    //
                    int _refreshCount = BlurObject.bind(_user.getRefreshCount()).toIntValue();
                    if (refresh) {
                        _refreshCount++;
                    }
                    _user = new OAuthUser.OAuthUserBuilder(_user)
                            .accessToken(accessToken)
                            .lastAccessToken(lastAccessToken)
                            .refreshToken(refreshToken)
                            .refreshCount(_refreshCount)
                            .scope(scope)
                            .isAuthorized(1)
                            .lastModifyTime(_now)
                            .build().update(Fields.create(OAuthUser.FIELDS.ACCESS_TOKEN,
                                    OAuthUser.FIELDS.LAST_ACCESS_TOKEN,
                                    OAuthUser.FIELDS.REFRESH_TOKEN,
                                    OAuthUser.FIELDS.REFRESH_COUNT,
                                    OAuthUser.FIELDS.SCOPE,
                                    OAuthUser.FIELDS.IS_AUTHORIZED,
                                    OAuthUser.FIELDS.LAST_MODIFY_TIME));
                }
                //
                this.setReturns(toOAuthClientUserBean(_user));
            }
        });
    }

    @Override
    public boolean removeUserAccessToken(String clientId, String uid, String accessToken) throws Exception {
        if (StringUtils.isNotBlank(accessToken)) {
            String _targetId;
            if (StringUtils.isBlank(clientId) || StringUtils.isBlank(uid)) {
                _targetId = __getCacheElement(CacheDataType.CLIENT_USER, accessToken);
            } else {
                _targetId = buildOpenId(clientId, uid);
            }
            OAuthClientUserBean _clientUserBean = null;
            if (StringUtils.isNotBlank(_targetId)) {
                _clientUserBean = __getCacheElement(CacheDataType.CLIENT_USER, _targetId);
            }
            __cleanCacheElement(CacheDataType.CLIENT_USER, clientId, accessToken, _clientUserBean == null ? null : _clientUserBean.getLastAccessToken());
            //
            if (_clientUserBean != null) {
                OAuthUser.builder().id(_targetId).lastModifyTime(System.currentTimeMillis()).build()
                        .update(Fields.create(OAuthUser.FIELDS.ACCESS_TOKEN,
                                OAuthUser.FIELDS.LAST_ACCESS_TOKEN,
                                OAuthUser.FIELDS.REFRESH_TOKEN,
                                OAuthUser.FIELDS.REFRESH_COUNT,
                                OAuthUser.FIELDS.SCOPE,
                                OAuthUser.FIELDS.LAST_MODIFY_TIME));
            }
            //
            return true;
        }
        return false;
    }

    enum CacheDataType {
        CLIENT, CLIENT_USER
    }
}
