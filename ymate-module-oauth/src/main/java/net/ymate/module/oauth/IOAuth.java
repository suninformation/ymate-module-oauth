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

import net.ymate.platform.core.YMP;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/26 上午 02:08
 * @version 1.0
 */
public interface IOAuth {

    String MODULE_NAME = "module.oauth";

    /**
     * @return 返回所属YMP框架管理器实例
     */
    YMP getOwner();

    /**
     * @return 返回模块配置对象
     */
    IOAuthModuleCfg getModuleCfg();

    /**
     * @return 返回模块是否已初始化
     */
    boolean isInited();

    /**
     * 注册OAuth授权作用域处理器
     *
     * @param targetClass 目标类型
     * @throws Exception 可能产生的任何异常
     */
    void registerScopeProcessor(Class<? extends IOAuthScopeProcessor> targetClass) throws Exception;

    /**
     * @param name OAuth授权作用域处理器名称
     * @param <T>  实例对象类型
     * @return 获取指定名称的Scope处理器对象，若不存在则返回空
     */
    <T extends IOAuthScopeProcessor> T getScopeProcessor(String name);

    /**
     * @return 获取Scope名称集合
     */
    Set<String> getScopeNames();

    /**
     * @param grantType 授权模式枚举类型
     * @return 获取指定授权模式处理器接口实现
     */
    IOAuthGrantProcessor getGrantProcessor(GrantType grantType);

    /**
     * @param request HttpServletRequest请求对象
     * @return 验证客户端授权模式请求凭证是否有效，若无效则返回非空OAuthResponse对象
     * @throws Exception 可能产生的任何异常
     */
    OAuthResponse checkClientAccessToken(HttpServletRequest request) throws Exception;

    /**
     * @param request HttpServletRequest请求对象
     * @param scope   授权作用域，可选
     * @return 验证用户授权模式请求凭证是否有效，若无效则返回非空OAuthResponse对象
     * @throws Exception 可能产生的任何异常
     */
    OAuthResponse checkUserAccessToken(HttpServletRequest request, String scope) throws Exception;

    /**
     * 常量
     */
    final class Const {

        public static final String SCOPE_SNSAPI_BASE = "snsapi_base";

        public static final String SCOPE_SNSAPI_USERINFO = "snsapi_userinfo";

        public static final String ACCESS_TOKEN = "access_token";

        public static final String UID = "uid";

        public static final String OPEN_ID = "open_id";

        public static final String AUTHORIZED = "authorized";

        public static final String INVALID_USER = "invalid_user";

        public static final String INVALID_REDIRECT_URI = "invalid_redirect_uri";

        public static final String REDIRECT_URI_MISMATCH = "redirect_uri_mismatch";
    }

    /**
     * 错误类型
     */
    enum ErrorType {
        INVALID_TOKEN,
        EXPIRED_TOKEN,
        INVALID_REQUEST,
        INVALID_CLIENT,
        INVALID_GRANT,
        INVALID_SCOPE,
        INSUFFICIENT_SCOPE,
        INVALID_USER,
        INVALID_REDIRECT_URI,
        UNAUTHORIZED_CLIENT,
        UNSUPPORTED_GRANT_TYPE,
        UNSUPPORTED_RESPONSE_TYPE,
        TEMPORARILY_UNAVAILABLE,
        REDIRECT_URI_MISMATCH,
        ACCESS_DENIED,
        SERVER_ERROR
    }
}