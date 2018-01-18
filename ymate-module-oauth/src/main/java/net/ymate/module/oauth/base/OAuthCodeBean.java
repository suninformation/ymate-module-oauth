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
package net.ymate.module.oauth.base;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/16 下午5:45
 * @version 1.0
 */
public class OAuthCodeBean extends BaseValueBean<OAuthCodeBean> {

    private String code;

    private String redirectUri;

    private String clientId;

    private String uid;

    private String scope;

    public OAuthCodeBean() {
        super();
    }

    public OAuthCodeBean(String code, String redirectUri, String clientId, String uid, String scope) {
        super();
        this.code = code;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.uid = uid;
        this.scope = scope;
    }

    public String getCode() {
        return code;
    }

    public OAuthCodeBean setCode(String code) {
        this.code = code;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public OAuthCodeBean setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public OAuthCodeBean setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public OAuthCodeBean setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public OAuthCodeBean setScope(String scope) {
        this.scope = scope;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuthCodeBean that = (OAuthCodeBean) o;
        return new EqualsBuilder()
                .append(code, that.code)
                .append(redirectUri, that.redirectUri)
                .append(clientId, that.clientId)
                .append(uid, that.uid)
                .append(scope, that.scope)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(code)
                .append(redirectUri)
                .append(clientId)
                .append(uid)
                .append(scope)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("redirectUri", redirectUri)
                .append("clientId", clientId)
                .append("uid", uid)
                .append("scope", scope)
                .toString();
    }
}
