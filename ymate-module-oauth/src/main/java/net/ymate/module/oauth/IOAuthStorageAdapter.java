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
 * @author 刘镇 (suninformation@163.com) on 17/3/6 上午12:14
 * @version 1.0
 */
public interface IOAuthStorageAdapter {

    void init(IOAuth owner);

    void destroy();

    /**
     * @param clientId 应用唯一标识
     * @return 返回应用基本信息和授权令牌信息
     * @throws Exception 可能产生的任何异常
     */
    OAuthClient findClientById(String clientId) throws Exception;

    /**
     * @param accessToken 接口访问凭证
     * @return 根据凭证获取应用基本信息和授权令牌信息
     * @throws Exception 可能产生任何异常
     */
    OAuthClient findClientByAccessToken(String accessToken) throws Exception;

    /**
     * @param clientId    应用唯一标识
     * @param accessToken 授权令牌
     * @param expiresIn   凭证超时时间, 单位(秒)
     * @return 存储令牌信息并返回令牌对象
     * @throws Exception 可能产生的任何异常
     */
    OAuthToken saveOrUpdateClientAccessToken(String clientId, String accessToken, int expiresIn) throws Exception;

    // -----

    /**
     * @param code        授权码(5分钟未被使用自动过期)
     * @param redirectUri 重定向URL地址
     * @param clientId    应用唯一标识
     * @param uid         用户主键
     * @param scope       应用授权作用域
     * @return 存储应用与用户关系并返回授权码对象
     * @throws Exception 可能产生的任何异常
     */
    OAuthCode saveOrUpdateAuthCode(String code, String redirectUri, String clientId, String uid, String scope) throws Exception;

    OAuthCode findAuthCode(String clientId, String authzCode) throws Exception;

    OAuthSnsToken saveOrUpdateAccessToken(String clientId, String uid, String scope, String accessToken, String refreshToken, int expiresIn, boolean refresh) throws Exception;

    OAuthClientUser findUser(String clientId, String uid) throws Exception;

    OAuthClientUser findUserByAccessToken(String accessToken) throws Exception;

    OAuthClientUser findUserByRefreshToken(String clientId, String refreshToken) throws Exception;
}
