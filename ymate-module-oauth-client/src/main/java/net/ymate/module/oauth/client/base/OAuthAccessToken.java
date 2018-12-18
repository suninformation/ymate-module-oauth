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
 * @author 刘镇 (suninformation@163.com) on 2018/10/7 上午12:10
 * @version 1.0
 */
public class OAuthAccessToken {

    private String token;

    private long expiredTime;

    public OAuthAccessToken(String token, long expiredTime) {
        this.token = token;
        this.expiredTime = expiredTime;
    }

    public String getToken() {
        return token;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiredTime;
    }

    /**
     * 请求访问令牌响应结果对象
     */
    public static class Result extends OAuthResult {

        private OAuthAccessToken accessToken;

        public Result(IHttpResponse result) {
            super(result);
            if (isOK()) {
                accessToken = new OAuthAccessToken(getOriginalResult().getString("access_token"), System.currentTimeMillis() + (getOriginalResult().getIntValue("expires_in") - 30) * 1000);
            }
        }

        public OAuthAccessToken getAccessToken() {
            return accessToken;
        }
    }
}
