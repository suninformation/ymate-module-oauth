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
package net.ymate.module.oauth.impl;

import net.ymate.module.oauth.IOAuthTokenGenerator;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/4/30 下午10:56
 * @version 1.0
 */
public class DefaultTokenGenerator implements IOAuthTokenGenerator {

    private OAuthIssuer __issuer;

    public DefaultTokenGenerator() {
        __issuer = new OAuthIssuerImpl(new MD5Generator());
    }

    @Override
    public String accessToken() throws Exception {
        return __issuer.accessToken();
    }

    @Override
    public String authorizationCode() throws Exception {
        return __issuer.authorizationCode();
    }

    @Override
    public String refreshToken() throws Exception {
        return __issuer.refreshToken();
    }
}
