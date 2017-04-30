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
 * @author 刘镇 (suninformation@163.com) on 17/3/7 上午12:40
 * @version 1.0
 */
public class OAuthSnsToken extends OAuthToken {

    private String openId;

    private String uid;

    private boolean authorized;

    private String scope;

    private String refreshToken;

    private int refreshCount;

    public OAuthSnsToken() {
    }

    public OAuthSnsToken(String clientId,
                         String openId,
                         String uid,
                         boolean authorized,
                         String scope,
                         String accessToken,
                         String lastAccessToken,
                         String refreshToken,
                         int refreshCount,
                         int expiresIn,
                         long createTime,
                         long lastModifyTime) {
        super(clientId, accessToken, lastAccessToken, expiresIn, createTime, lastModifyTime);
        //
        this.openId = openId;
        this.uid = uid;
        this.authorized = authorized;
        this.scope = scope;
        this.refreshToken = refreshToken;
        this.refreshCount = refreshCount;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(int refreshCount) {
        this.refreshCount = refreshCount;
    }
}
