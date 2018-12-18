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

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/06 下午 15:26
 * @version 1.0
 */
public interface IOAuthClientModuleCfg {

    String ACCOUNT_PROVIDER_CLASS = "account_provider_class";

    String TOKEN_STORAGE_ADAPTER_CLASS = "token_storage_adapter_class";

    /**
     * @return OAuth客户端帐号信息提供者接口实现类, 默认值: net.ymate.module.oauth.client.impl.DefaultOAuthClientAccountProvider
     */
    IOAuthClientAccountProvider getAccountProvider();

    /**
     * @return OAuth客户端令牌信息存储适配器接口实现类, 默认值: net.ymate.module.oauth.client.impl.DefaultOAuthClientTokenStorageAdapter
     */
    IOAuthClientTokenStorageAdapter getTokenStorageAdapter();
}