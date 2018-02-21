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
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.ClassUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/26 上午 02:08
 * @version 1.0
 */
public class DefaultModuleCfg implements IOAuthModuleCfg {

    private int __accessTokenExpireIn;

    private int __refreshCountMax;

    private int __refreshTokenExpireIn;

    private int __authorizationCodeExpireIn;

    private String __cacheNamePrefix;

    private Set<GrantType> __allowGrantTypes;

    private IOAuthTokenGenerator __tokenGenerator;

    private IOAuthStorageAdapter __storageAdapter;

    private IOAuthErrorAdapter __errorAdapter;

    public DefaultModuleCfg(YMP owner) {
        Map<String, String> _moduleCfgs = owner.getConfig().getModuleConfigs(IOAuth.MODULE_NAME);
        //
        __accessTokenExpireIn = BlurObject.bind(_moduleCfgs.get("access_token_expire_in")).toIntValue();
        if (__accessTokenExpireIn <= 0) {
            __accessTokenExpireIn = 7200;
        }
        //
        __refreshCountMax = BlurObject.bind(_moduleCfgs.get("refresh_count_max")).toIntValue();
        if (__refreshCountMax < 0) {
            __refreshCountMax = 0;
        }
        //
        __refreshTokenExpireIn = BlurObject.bind(_moduleCfgs.get("refresh_token_expire_in")).toIntValue();
        if (__refreshTokenExpireIn <= 0) {
            __refreshTokenExpireIn = 30;
        }
        //
        __authorizationCodeExpireIn = BlurObject.bind(_moduleCfgs.get("authorization_code_expire_in")).toIntValue();
        if (__authorizationCodeExpireIn <= 0) {
            __authorizationCodeExpireIn = 5;
        }
        //
        __cacheNamePrefix = StringUtils.trimToEmpty(_moduleCfgs.get("cache_name_prefix"));
        //
        __allowGrantTypes = new HashSet<GrantType>();
        String _grantTypeStr = StringUtils.defaultIfBlank(_moduleCfgs.get("allow_grant_types"), "none");
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
            __tokenGenerator = ClassUtils.impl(_moduleCfgs.get("token_generator_class"), IOAuthTokenGenerator.class, getClass());
            if (__tokenGenerator == null) {
                __tokenGenerator = new DefaultTokenGenerator();
            }
            //
            __storageAdapter = ClassUtils.impl(_moduleCfgs.get("storage_adapter_class"), IOAuthStorageAdapter.class, getClass());
        }
        //
        __errorAdapter = ClassUtils.impl(_moduleCfgs.get("error_adapter_class"), IOAuthErrorAdapter.class, getClass());
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