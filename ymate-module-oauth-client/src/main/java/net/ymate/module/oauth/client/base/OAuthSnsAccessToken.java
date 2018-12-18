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
package net.ymate.module.oauth.client.base;

import net.ymate.framework.commons.IHttpResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/7 上午12:36
 * @version 1.0
 */
public class OAuthSnsAccessToken extends OAuthAccessToken.Result {

    private String refreshToken;

    private String openId;

    private String scope;

    private String unionId;

    public OAuthSnsAccessToken(IHttpResponse result) {
        super(result);
        if (isOK()) {
            this.refreshToken = getOriginalResult().getString("refresh_token");
            this.openId = getOriginalResult().getString("open_id");
            this.scope = getOriginalResult().getString("scope");
            this.unionId = getOriginalResult().getString("union_id");
        }
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public String getScope() {
        return scope;
    }

    public String getUnionId() {
        return unionId;
    }
}
