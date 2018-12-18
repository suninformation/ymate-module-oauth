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

import net.ymate.module.oauth.client.IOAuthClientAccountProvider;
import net.ymate.module.oauth.client.IOAuthClientModuleCfg;
import net.ymate.module.oauth.client.IOAuthClientTokenStorageAdapter;
import net.ymate.module.oauth.client.IOAuthClients;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.support.IConfigReader;
import net.ymate.platform.core.support.impl.MapSafeConfigReader;
import net.ymate.platform.core.util.ClassUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/06 下午 15:26
 * @version 1.0
 */
public class DefaultOAuthClientModuleCfg implements IOAuthClientModuleCfg {

    private IOAuthClientAccountProvider accountProvider;

    private IOAuthClientTokenStorageAdapter tokenStorageAdapter;

    public DefaultOAuthClientModuleCfg(YMP owner) {
        IConfigReader _moduleCfg = MapSafeConfigReader.bind(owner.getConfig().getModuleConfigs(IOAuthClients.MODULE_NAME));
        //
        String _accountProviderClass = StringUtils.trimToNull(_moduleCfg.getString(ACCOUNT_PROVIDER_CLASS));
        if (_accountProviderClass != null && !StringUtils.equals(_accountProviderClass, DefaultOAuthClientAccountProvider.class.getName())) {
            this.accountProvider = ClassUtils.impl(_accountProviderClass, IOAuthClientAccountProvider.class, getClass());
        }
        if (this.accountProvider == null) {
            this.accountProvider = new DefaultOAuthClientAccountProvider(_moduleCfg);
        }
        //
        this.tokenStorageAdapter = _moduleCfg.getClassImpl(TOKEN_STORAGE_ADAPTER_CLASS, DefaultOAuthClientTokenStorageAdapter.class);
    }

    @Override
    public IOAuthClientAccountProvider getAccountProvider() {
        return accountProvider;
    }

    @Override
    public IOAuthClientTokenStorageAdapter getTokenStorageAdapter() {
        return tokenStorageAdapter;
    }
}