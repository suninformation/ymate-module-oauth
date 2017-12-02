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


import net.ymate.framework.core.support.ValueObject;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/13 下午9:12
 * @version 1.0
 */
public class OAuthClient extends ValueObject<OAuthClient> {

    private String title;

    private String iconUrl;

    private String domain;

    private String secretKey;

    private String accessToken;

    private String lastAccessToken;

    private Integer expiresIn;

    public OAuthClient() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public OAuthClient setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public OAuthClient setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public OAuthClient setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public OAuthClient setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getLastAccessToken() {
        return lastAccessToken;
    }

    public OAuthClient setLastAccessToken(String lastAccessToken) {
        this.lastAccessToken = lastAccessToken;
        return this;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public OAuthClient setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
