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
     * @return 缓存名称前缀, 默认值: ""
     */
    String getCacheNamePrefix();

    /**
     * @return 用户确认授权视图文件路径, 默认值: _views/oauth2/sns-authorization
     */
    String getAuthorizationView();

    /**
     * @return Token生成器接口实现
     */
    IOAuthTokenGenerator getTokenGenerator();

    /**
     * @return 用户身份信息适配器接口实现
     */
    IOAuthUserInfoAdapter getUserInfoAdapter();

    /**
     * @return 令牌存储适配器接口实现
     */
    IOAuthStorageAdapter getTokenStorageAdapter();
}