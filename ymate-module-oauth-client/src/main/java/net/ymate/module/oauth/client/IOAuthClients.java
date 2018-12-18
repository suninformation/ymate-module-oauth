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
import net.ymate.platform.core.YMP;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/06 下午 15:26
 * @version 1.0
 */
public interface IOAuthClients {

    String MODULE_NAME = "module.oauth.client";

    /**
     * @return 返回所属YMP框架管理器实例
     */
    YMP getOwner();

    /**
     * @return 返回模块配置对象
     */
    IOAuthClientModuleCfg getModuleCfg();

    /**
     * @return 返回模块是否已初始化
     */
    boolean isInited();

    /**
     * @return 获取默认客户端帐号配置
     */
    OAuthAccount getDefaultAccount();

    /**
     * 获取指定主键的客户端帐号配置
     *
     * @param accountId 客户端帐号主键名称
     * @return 返回客户端帐号元数据对象，若不存在则返回null
     */
    OAuthAccount getAccount(String accountId);

    /**
     * @return 获取默认客户端帐号访问令牌信息
     */
    OAuthAccessToken getDefaultAccessToken();

    /**
     * 获取指定主键的客户端帐号访问令牌信息
     *
     * @param accountId 客户端帐号主键名称
     * @return 返回客户端访问令牌对象, 若不存在则返回null
     */
    OAuthAccessToken getAccessToken(String accountId);
}