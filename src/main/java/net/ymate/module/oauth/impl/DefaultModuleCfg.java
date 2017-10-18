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
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/26 上午 02:08
 * @version 1.0
 */
public class DefaultModuleCfg implements IOAuthModuleCfg {

    private int __accessTokenExpireIn;

    private String __cacheNamePrefix;

    private boolean __snsEnabled;

    private String __authorizationView;

    private IOAuthTokenGenerator __tokenGenerator;

    private IOAuthUserInfoAdapter __userInfoAdaptor;

    private IOAuthStorageAdapter __storageAdapter;

    public DefaultModuleCfg(YMP owner) {
        Map<String, String> _moduleCfgs = owner.getConfig().getModuleConfigs(IOAuth.MODULE_NAME);
        //
        __accessTokenExpireIn = BlurObject.bind(_moduleCfgs.get("access_token_expire_in")).toIntValue();
        if (__accessTokenExpireIn <= 0) {
            __accessTokenExpireIn = 7200;
        }
        //
        __cacheNamePrefix = StringUtils.trimToEmpty(_moduleCfgs.get("cache_name_prefix"));
        //
        __snsEnabled = BlurObject.bind(_moduleCfgs.get("sns_enabled")).toBooleanValue();
        if (__snsEnabled) {
            __authorizationView = StringUtils.defaultIfBlank(_moduleCfgs.get("authorization_view"), "_views/oauth2/sns-authorization");
            __userInfoAdaptor = ClassUtils.impl(_moduleCfgs.get("userinfo_adapter_class"), IOAuthUserInfoAdapter.class, getClass());
        }
        //
        __tokenGenerator = ClassUtils.impl(_moduleCfgs.get("token_generator_class"), IOAuthTokenGenerator.class, getClass());
        if (__tokenGenerator == null) {
            __tokenGenerator = new DefaultTokenGenerator();
        }
        //
        __storageAdapter = ClassUtils.impl(_moduleCfgs.get("storage_adapter_class"), IOAuthStorageAdapter.class, getClass());
    }

    public int getAccessTokenExpireIn() {
        return __accessTokenExpireIn;
    }

    public String getCacheNamePrefix() {
        return __cacheNamePrefix;
    }

    public boolean isSnsEnabled() {
        return __snsEnabled;
    }

    public String getAuthorizationView() {
        return __authorizationView;
    }

    public IOAuthTokenGenerator getTokenGenerator() {
        return __tokenGenerator;
    }

    public IOAuthUserInfoAdapter getUserInfoAdapter() {
        return __userInfoAdaptor;
    }

    public IOAuthStorageAdapter getTokenStorageAdapter() {
        return __storageAdapter;
    }
}