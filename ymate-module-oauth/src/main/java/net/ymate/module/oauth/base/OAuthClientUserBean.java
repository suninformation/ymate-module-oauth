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

import net.ymate.module.oauth.IOAuth;
import net.ymate.platform.core.util.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/7 上午12:40
 * @version 1.0
 */
public class OAuthClientUserBean extends OAuthTokenBean {

    private String openId;

    private String uid;

    private boolean authorized;

    private String scope;

    private String refreshToken;

    private int refreshCount;

    public OAuthClientUserBean() {
        super();
    }

    public OAuthClientUserBean(String clientId,
                               String uid,
                               boolean authorized,
                               String scope,
                               String accessToken,
                               String lastAccessToken,
                               String refreshToken,
                               int refreshCount,
                               int expiresIn) {
        super(clientId, accessToken, lastAccessToken, expiresIn);
        //
        this.uid = uid;
        this.authorized = authorized;
        this.scope = scope;
        this.refreshToken = refreshToken;
        this.refreshCount = refreshCount;
    }

    public String getOpenId() {
        return openId;
    }

    public OAuthTokenBean setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public OAuthClientUserBean setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public OAuthClientUserBean setAuthorized(boolean authorized) {
        this.authorized = authorized;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public OAuthClientUserBean setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public OAuthClientUserBean setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public int getRefreshCount() {
        return refreshCount;
    }

    public OAuthClientUserBean setRefreshCount(int refreshCount) {
        this.refreshCount = refreshCount;
        return this;
    }

    public boolean checkRefreshToken(int refreshCountMax, int expiresIn) {
        return (refreshCountMax == 0 || this.refreshCount <= refreshCountMax) && System.currentTimeMillis() - getLastModifyTime() <= DateTimeUtils.DAY * expiresIn;
    }

    public boolean checkScope(String scope) {
        return StringUtils.equalsIgnoreCase(StringUtils.trimToNull(this.scope), StringUtils.trimToNull(scope));
    }

    public boolean containsScope(Set<String> scopes) {
        boolean _flag = true;
        for (String _scope : scopes) {
            if (StringUtils.equalsIgnoreCase(IOAuth.Const.SCOPE_SNSAPI_BASE, _scope)) {
                continue;
            }
            if (StringUtils.containsIgnoreCase(this.scope, _scope)) {
                _flag = false;
                break;
            }
        }
        return _flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuthClientUserBean that = (OAuthClientUserBean) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(authorized, that.authorized)
                .append(refreshCount, that.refreshCount)
                .append(openId, that.openId)
                .append(uid, that.uid)
                .append(scope, that.scope)
                .append(refreshToken, that.refreshToken)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(openId)
                .append(uid)
                .append(authorized)
                .append(scope)
                .append(refreshToken)
                .append(refreshCount)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("openId", openId)
                .append("uid", uid)
                .append("authorized", authorized)
                .append("scope", scope)
                .append("refreshToken", refreshToken)
                .append("refreshCount", refreshCount)
                .toString();
    }
}
