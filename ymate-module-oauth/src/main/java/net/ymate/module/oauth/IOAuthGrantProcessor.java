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

import net.ymate.module.oauth.support.OAuthResponseUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * OAuth授权处理器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2018/1/16 下午11:08
 * @version 1.0
 */
public interface IOAuthGrantProcessor {

    IOAuthGrantProcessor UNSUPPORTED_GRANT_TYPE = new IOAuthGrantProcessor() {
        @Override
        public OAuthResponse process(HttpServletRequest request) throws Exception {
            return OAuthResponseUtils.badRequest(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE);
        }

        @Override
        public IOAuthGrantProcessor setParam(String key, Object value) {
            return this;
        }
    };

    OAuthResponse process(HttpServletRequest request) throws Exception;

    IOAuthGrantProcessor setParam(String key, Object value);
}
