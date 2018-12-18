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
import net.ymate.module.oauth.client.IOAuthClients;
import net.ymate.module.oauth.client.base.OAuthAccount;
import net.ymate.platform.core.support.IConfigReader;
import net.ymate.platform.core.support.impl.MapSafeConfigReader;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/8 上午11:34
 * @version 1.0
 */
public class DefaultOAuthClientAccountProvider implements IOAuthClientAccountProvider {

    private static final Map<String, OAuthAccount> __CACHES = new ConcurrentHashMap<String, OAuthAccount>();

    public DefaultOAuthClientAccountProvider(IConfigReader moduleCfg) {
        String[] _accountNames = StringUtils.split(moduleCfg.getString(ACCOUNT_NAME_LIST, DEFAULT_ACCOUNT), "|");
        for (String _name : _accountNames) {
            IConfigReader _accountCfg = MapSafeConfigReader.bind(moduleCfg.getMap(_name + "."));
            OAuthAccount _account = new OAuthAccount(_name, _accountCfg.getString(SERVICE_URL), _accountCfg.getString(CLIENT_ID), _accountCfg.getString(CLIENT_SECRET), _accountCfg.getString(REDIRECT_URI));
            Map<String, String> _params = _accountCfg.getMap(PARAMS_PREFIX);
            if (!_params.isEmpty()) {
                _account.getAttributes().putAll(_params);
            }
            __CACHES.put(_name, _account);
        }
    }

    @Override
    public void init(IOAuthClients owner) throws Exception {
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public boolean hasAccount(String accountId) {
        return __CACHES.containsKey(accountId);
    }

    @Override
    public OAuthAccount getAccount(String accountId) {
        return __CACHES.get(accountId);
    }
}
