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
package net.ymate.module.oauth;

import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/26 上午 02:08
 * @version 1.0
 */
public interface IOAuthModuleCfg {

    /**
     * @return 返回AccessToken访问凭证超时时间, 单位(秒), 默认值: 7200(两小时)
     */
    int getAccessTokenExpireIn();

    /**
     * @return 返回AccessToken访问凭证最大刷新次数，若为0表示不限制
     */
    int getRefreshCountMax();

    /**
     * @return 返回RefreshToken刷新凭证超时时间, 单位(天), 默认值: 30
     */
    int getRefreshTokenExpireIn();

    /**
     * @return 返回授权码过期时间, 单位(分钟), 默认值: 5
     */
    int getAuthorizationCodeExpireIn();

    /**
     * @return 缓存名称前缀, 默认值: ""
     */
    String getCacheNamePrefix();

    /**
     * @return 设置开启的授权模式, 多个模式之间用'|'分隔, 默认值: none，可选值范围: [authorization_code|implicit|password|refresh_token|client_credentials|none]
     */
    Set<GrantType> getAllowGrantTypes();

    /**
     * @return Token生成器接口实现
     */
    IOAuthTokenGenerator getTokenGenerator();

    /**
     * @return 令牌存储适配器接口实现
     */
    IOAuthStorageAdapter getStorageAdapter();

    /**
     * @return 错误信息响应适配器接口实现
     */
    IOAuthErrorAdapter getErrorAdapter();
}