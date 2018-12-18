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

/**
 * OAuth客户端令牌信息存储适配器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2018/10/6 下午3:48
 * @version 1.0
 */
public interface IOAuthClientTokenStorageAdapter {

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
     * 加载指定客户端帐号主键名称的访问令牌信息
     *
     * @param account 客户端帐号信息对象
     * @return 返回客户端访问令牌对象, 若不存在则返回null
     */
    OAuthAccessToken loadAccessToken(OAuthAccount account);

    /**
     * 储存或更新客户端访问令牌信息
     *
     * @param account     客户端帐号信息对象
     * @param accessToken 客户端访问令牌信息对象
     */
    void saveOrUpdateAccessToken(OAuthAccount account, OAuthAccessToken.Result accessToken);
}
