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

import net.ymate.module.oauth.client.base.OAuthAccount;

/**
 * OAuth客户端帐号信息提供者接口
 *
 * @author 刘镇 (suninformation@163.com) on 2018/10/6 下午3:50
 * @version 1.0
 */
public interface IOAuthClientAccountProvider {

    /**
     * 默认客户端帐号主键名称
     */
    String DEFAULT_ACCOUNT = "default";

    String ACCOUNT_NAME_LIST = "account_name_list";

    String SERVICE_URL = "service_url";

    String CLIENT_ID = "client_id";

    String CLIENT_SECRET = "client_secret";

    String REDIRECT_URI = "redirect_uri";

    String PARAMS_PREFIX = "params.";

    /**
     * 初始化
     *
     * @param owner 所属模块对象
     * @throws Exception 可能产生的任何异常
     */
    void init(IOAuthClients owner) throws Exception;

    /**
     * 销毁
     *
     * @throws Exception 可能产生的任何异常
     */
    void destroy() throws Exception;

    /**
     * 判断是否包含指定id的客户端帐号配置
     *
     * @param accountId 客户端帐号主键名称
     * @return 若存在则返回true，否则返回false
     */
    boolean hasAccount(String accountId);

    /**
     * 获取指定id的客户端帐号信息
     *
     * @param accountId 客户端帐号主键名称
     * @return 返回OAuth客户端帐号元数据对象，若不存在则返回null
     */
    OAuthAccount getAccount(String accountId);
}
