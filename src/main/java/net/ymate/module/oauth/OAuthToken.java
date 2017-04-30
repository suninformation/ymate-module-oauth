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

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/16 上午11:18
 * @version 1.0
 */
public class OAuthToken implements Serializable {

    private String clientId;

    private String accessToken;

    private String lastAccessToken;

    private int expiresIn;

    private long createTime;

    private long lastModifyTime;

    public OAuthToken() {
    }

    public OAuthToken(String clientId, String accessToken, String lastAccessToken, int expiresIn, long createTime, long lastModifyTime) {
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.lastAccessToken = lastAccessToken;
        this.expiresIn = expiresIn;
        this.createTime = createTime;
        this.lastModifyTime = lastModifyTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLastAccessToken() {
        return lastAccessToken;
    }

    public void setLastAccessToken(String lastAccessToken) {
        this.lastAccessToken = lastAccessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
