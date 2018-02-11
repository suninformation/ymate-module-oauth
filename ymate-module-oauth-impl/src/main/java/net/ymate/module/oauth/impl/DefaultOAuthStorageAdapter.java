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
import net.ymate.module.oauth.support.UserAuthenticationException;
import net.ymate.platform.cache.Caches;
import net.ymate.platform.cache.ICache;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/2/11 上午2:23
 * @version 1.0
 */
public class DefaultOAuthStorageAdapter implements IOAuthStorageAdapter {

    private IOAuth __owner;

    private ICache __dataCache;

    @Override
    public void init(IOAuth owner) throws Exception {
        __owner = owner;
        __dataCache = Caches.get().getCacheProvider().getCache(owner.getModuleCfg().getCacheNamePrefix().concat("oauth_data"));
    }

    @Override
    public void destroy() throws Exception {
        __dataCache = null;
        __owner = null;
    }

    @Override
    public OAuthClientBean findClient(String clientId) throws Exception {
        OAuthClient _client = OAuthClient.builder().id(clientId).build().load();
        if (_client != null) {
            OAuthClientBean _clientBean = new OAuthClientBean();
            _clientBean.setClientId(_client.getId());
            _clientBean.setIconUrl(_client.getIconUrl());
            _clientBean.setDomain(_client.getDomain());
            _clientBean.setSecretKey(_client.getSecretKey());
            _clientBean.setAccessToken(_client.getAccessToken());
            _clientBean.setLastAccessToken(_client.getLastAccessToken());
            _clientBean.setExpiresIn(_client.getExpiresIn());
            _clientBean.setCreateTime(_client.getCreateTime());
            _clientBean.setLastModifyTime(_client.getLastModifyTime());
            //
            _clientBean = fillClientData(_clientBean);
            //
            return _clientBean;
        }
        return null;
    }

    protected OAuthClientBean fillClientData(OAuthClientBean clientBean) {
        return clientBean;
    }

    @Override
    public OAuthClientBean findClientByAccessToken(String accessToken) throws Exception {
        return null;
    }

    @Override
    public OAuthTokenBean saveOrUpdateClientAccessToken(String clientId, String accessToken, String lastAccessToken, int expiresIn) throws Exception {
        return null;
    }

    @Override
    public OAuthCodeBean saveOrUpdateCode(String code, String clientId, String redirectUri, String uid, String scope) throws Exception {
        return null;
    }

    @Override
    public OAuthCodeBean findCode(String clientId, String code) throws Exception {
        return null;
    }

    @Override
    public boolean removeCode(String clientId, String code) throws Exception {
        return false;
    }

    @Override
    public OAuthClientUserBean findUser(String clientId, String uid) throws Exception {
        return null;
    }

    protected OAuthClientUserBean fillUserData(OAuthClientUserBean clientUserBean) {
        return clientUserBean;
    }

    @Override
    public OAuthClientUserBean findUserByAccessToken(String accessToken) throws Exception {
        return null;
    }

    @Override
    public OAuthClientUserBean findUserByRefreshToken(String clientId, String refreshToken) throws Exception {
        return null;
    }

    @Override
    public OAuthClientUserBean findUser(String clientId, String username, String password) throws UserAuthenticationException {
        return null;
    }

    @Override
    public OAuthUserInfoBean findUserInfo(String clientId, String openId) throws Exception {
        return null;
    }

    @Override
    public OAuthClientUserBean saveOrUpdateUserAccessToken(String clientId, String uid, String scope, String accessToken, String lastAccessToken, String refreshToken, int expiresIn) throws Exception {
        return null;
    }
}
