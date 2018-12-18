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
package net.ymate.module.oauth.impl;

import net.ymate.module.oauth.*;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.support.IConfigReader;
import net.ymate.platform.core.support.impl.MapSafeConfigReader;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/26 上午 02:08
 * @version 1.0
 */
public class DefaultOAuthModuleCfg implements IOAuthModuleCfg {

    private int __accessTokenExpireIn;

    private int __refreshCountMax;

    private int __refreshTokenExpireIn;

    private int __authorizationCodeExpireIn;

    private String __cacheNamePrefix;

    private Set<GrantType> __allowGrantTypes;

    private IOAuthTokenGenerator __tokenGenerator;

    private IOAuthStorageAdapter __storageAdapter;

    private IOAuthErrorAdapter __errorAdapter;

    public DefaultOAuthModuleCfg(YMP owner) {
        IConfigReader _moduleCfg = MapSafeConfigReader.bind(owner.getConfig().getModuleConfigs(IOAuth.MODULE_NAME));
        //
        __accessTokenExpireIn = _moduleCfg.getInt(ACCESS_TOKEN_EXPIRE_IN);
        if (__accessTokenExpireIn <= 0) {
            __accessTokenExpireIn = 7200;
        }
        //
        __refreshCountMax = _moduleCfg.getInt(REFRESH_COUNT_MAX);
        if (__refreshCountMax < 0) {
            __refreshCountMax = 0;
        }
        //
        __refreshTokenExpireIn = _moduleCfg.getInt(REFRESH_TOKEN_EXPIRE_IN);
        if (__refreshTokenExpireIn <= 0) {
            __refreshTokenExpireIn = 30;
        }
        //
        __authorizationCodeExpireIn = _moduleCfg.getInt(AUTHORIZATION_CODE_EXPIRE_IN);
        if (__authorizationCodeExpireIn <= 0) {
            __authorizationCodeExpireIn = 5;
        }
        //
        __cacheNamePrefix = StringUtils.trimToEmpty(_moduleCfg.getString(CACHE_NAME_PREFIX));
        //
        __allowGrantTypes = new HashSet<GrantType>();
        String _grantTypeStr = _moduleCfg.getString(ALLOW_GRANT_TYPES, "none");
        if (!StringUtils.containsIgnoreCase(_grantTypeStr, "none")) {
            String[] _types = StringUtils.split(_grantTypeStr, "|");
            if (ArrayUtils.isNotEmpty(_types)) {
                for (String _item : _types) {
                    try {
                        GrantType _type = GrantType.valueOf(StringUtils.upperCase(StringUtils.trimToEmpty(_item)));
                        __allowGrantTypes.add(_type);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }
        //
        if (!__allowGrantTypes.isEmpty()) {
            __tokenGenerator = _moduleCfg.getClassImpl(TOKEN_GENERATOR_CLASS, IOAuthTokenGenerator.class);
            if (__tokenGenerator == null) {
                __tokenGenerator = new DefaultTokenGenerator();
            }
            //
            __storageAdapter = _moduleCfg.getClassImpl(STORAGE_ADAPTER_CLASS, IOAuthStorageAdapter.class);
        }
        //
        __errorAdapter = _moduleCfg.getClassImpl(ERROR_ADAPTER_CLASS, IOAuthErrorAdapter.class);
        if (__errorAdapter == null) {
            __errorAdapter = new DefaultErrorAdapter();
        }
    }

    @Override
    public int getAccessTokenExpireIn() {
        return __accessTokenExpireIn;
    }

    @Override
    public int getRefreshCountMax() {
        return __refreshCountMax;
    }

    @Override
    public int getRefreshTokenExpireIn() {
        return __refreshTokenExpireIn;
    }

    @Override
    public int getAuthorizationCodeExpireIn() {
        return __authorizationCodeExpireIn;
    }

    @Override
    public String getCacheNamePrefix() {
        return __cacheNamePrefix;
    }

    @Override
    public Set<GrantType> getAllowGrantTypes() {
        return __allowGrantTypes;
    }

    @Override
    public IOAuthTokenGenerator getTokenGenerator() {
        return __tokenGenerator;
    }

    @Override
    public IOAuthStorageAdapter getStorageAdapter() {
        return __storageAdapter;
    }

    @Override
    public IOAuthErrorAdapter getErrorAdapter() {
        return __errorAdapter;
    }
}