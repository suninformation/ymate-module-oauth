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

import net.ymate.module.oauth.base.*;
import net.ymate.module.oauth.support.UserAuthenticationException;
import net.ymate.platform.core.support.IInitializable;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/6 上午12:14
 * @version 1.0
 */
public interface IOAuthStorageAdapter extends IInitializable<IOAuth> {

    /**
     * @param clientId 应用唯一标识
     * @return 返回应用基本信息和授权令牌信息，若不存在则返回空
     * @throws Exception 可能产生的任何异常
     */
    OAuthClientBean findClient(String clientId) throws Exception;

    /**
     * @param accessToken 接口访问凭证
     * @return 根据凭证获取应用基本信息和授权令牌信息，若不存在则返回空
     * @throws Exception 可能产生任何异常
     */
    OAuthClientBean findClientByAccessToken(String accessToken) throws Exception;

    /**
     * @param clientId        应用唯一标识
     * @param accessToken     授权令牌
     * @param lastAccessToken 上一次授权令牌
     * @param expiresIn       凭证超时时间, 单位(秒)
     * @return 存储令牌信息并返回令牌对象，若不存在则返回空
     * @throws Exception 可能产生的任何异常
     */
    OAuthTokenBean saveOrUpdateClientAccessToken(String clientId, String accessToken, String lastAccessToken, int expiresIn) throws Exception;

    boolean removeClientAccessToken(String clientId, String accessToken) throws Exception;

    // -----

    /**
     * @param code        授权码 (默认5分钟未被使用自动过期)
     * @param clientId    应用唯一标识
     * @param redirectUri 重定向URL地址
     * @param uid         用户主键
     * @param scope       应用授权作用域
     * @return 存储应用与用户关系并返回授权码对象
     * @throws Exception 可能产生的任何异常
     */
    OAuthCodeBean saveOrUpdateCode(String code, String clientId, String redirectUri, String uid, String scope) throws Exception;

    OAuthCodeBean findCode(String clientId, String code) throws Exception;

    boolean removeCode(String clientId, String code) throws Exception;

    // -----

    /**
     * @param clientId 应用唯一标识
     * @param uid      用户主键
     * @return 生成基于clientId唯一的OpenId值
     */
    String buildOpenId(String clientId, String uid);

    // -----

    OAuthClientUserBean findUser(String openId) throws Exception;

    OAuthClientUserBean findUser(String clientId, String uid) throws Exception;

    OAuthClientUserBean findUserByAccessToken(String accessToken) throws Exception;

    OAuthClientUserBean findUserByRefreshToken(String clientId, String refreshToken) throws Exception;

    /**
     * 填充授权用户自定义属性
     * <p>注意: 需考虑数据被频繁加载的情况, 可以通过添加自定义标识判断是否需要执行加载动作</p>
     *
     * @param clientUser 预填充用户授权对象
     * @throws Exception 可能产生的任何异常
     */
    void fillClientUserAttributes(OAuthClientUserBean clientUser) throws Exception;

    /**
     * @param clientId 应用唯一标识
     * @param username 用户标识 (用户名称或电子邮件地址或手机号码)
     * @param password 密码md5值
     * @return 验证用户身份合法性并返回令牌信息(其中openId和uid非空), 否则返回空
     * @throws UserAuthenticationException 产生的错误码或提示信息可通过异常对象返回
     */
    OAuthClientUserBean findUser(String clientId, String username, String password) throws UserAuthenticationException;

    OAuthUserInfoBean findUserInfo(String clientId, String openId) throws Exception;

    OAuthClientUserBean saveOrUpdateUserAccessToken(String clientId, String uid, String scope, String accessToken, String lastAccessToken, String refreshToken, int expiresIn, boolean refresh) throws Exception;

    boolean removeUserAccessToken(String clientId, String uid, String accessToken) throws Exception;
}
