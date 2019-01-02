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
package net.ymate.module.oauth.client;

import net.ymate.module.oauth.client.base.OAuthAccessToken;
import net.ymate.module.oauth.client.base.OAuthAccount;
import net.ymate.module.oauth.client.impl.DefaultOAuthClientModuleCfg;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/06 下午 15:26
 * @version 1.0
 */
@Module
public class OAuthClients implements IModule, IOAuthClients {

    private static final Log _LOG = LogFactory.getLog(OAuthClients.class);

    public static final Version VERSION = new Version(1, 0, 0, OAuthClients.class.getPackage().getImplementationVersion(), Version.VersionType.Release);

    private static volatile IOAuthClients __instance;

    private YMP __owner;

    private IOAuthClientModuleCfg __moduleCfg;

    private boolean __inited;

    public static IOAuthClients get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(OAuthClients.class);
                }
            }
        }
        return __instance;
    }

    @Override
    public String getName() {
        return IOAuthClients.MODULE_NAME;
    }

    @Override
    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-module-oauth-client-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultOAuthClientModuleCfg(owner);
            //
            __moduleCfg.getAccountProvider().init(this);
            __moduleCfg.getTokenStorageAdapter().init(this);
            //
            __inited = true;
        }
    }

    @Override
    public boolean isInited() {
        return __inited;
    }

    @Override
    public OAuthAccount getDefaultAccount() {
        return getAccount(IOAuthClientAccountProvider.DEFAULT_ACCOUNT);
    }

    @Override
    public OAuthAccount getAccount(String accountId) {
        return __moduleCfg.getAccountProvider().getAccount(accountId);
    }

    @Override
    public OAuthAccessToken getDefaultAccessToken() {
        return getAccessToken(IOAuthClientAccountProvider.DEFAULT_ACCOUNT);
    }

    @Override
    public OAuthAccessToken getAccessToken(String accountId) {
        OAuthAccount _account = getAccount(accountId);
        if (_account != null) {
            return __moduleCfg.getTokenStorageAdapter().loadAccessToken(_account);
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            __moduleCfg.getAccountProvider().destroy();
            __moduleCfg.getTokenStorageAdapter().destroy();
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
    public IOAuthClientModuleCfg getModuleCfg() {
        return __moduleCfg;
    }
}
