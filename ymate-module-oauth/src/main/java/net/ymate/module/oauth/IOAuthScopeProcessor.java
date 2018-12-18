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
package net.ymate.module.oauth;

import net.ymate.module.oauth.base.OAuthTokenBean;
import net.ymate.platform.core.support.IInitializable;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * OAuth授权作用域处理器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2018/1/14 下午4:32
 * @version 1.0
 */
public interface IOAuthScopeProcessor extends IInitializable<IOAuth> {

    /**
     * @return 是否已初始化
     */
    boolean isInited();

    /**
     * @param request   HttpServletRequest请求对象
     * @param tokenBean 访问令牌对象
     * @return 返回执行结果对象
     * @throws Exception 可能产生的任何异常
     */
    OAuthResponse process(HttpServletRequest request, OAuthTokenBean tokenBean) throws Exception;

    /**
     * @param key   参数键名
     * @param value 参数值
     * @return 传入参数
     */
    IOAuthScopeProcessor setParam(String key, Object value);
}
