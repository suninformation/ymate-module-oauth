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

import net.ymate.platform.core.util.DateTimeUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/16 上午11:18
 * @version 1.0
 */
public class OAuthTokenBean extends BaseValueBean<OAuthTokenBean> {

    private String clientId;

    private String accessToken;

    private String lastAccessToken;

    private int expiresIn;

    public OAuthTokenBean() {
        super();
    }

    public OAuthTokenBean(String clientId, String accessToken, String lastAccessToken, int expiresIn) {
        super();
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.lastAccessToken = lastAccessToken;
        this.expiresIn = expiresIn;
    }

    public String getClientId() {
        return clientId;
    }

    public OAuthTokenBean setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public OAuthTokenBean setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getLastAccessToken() {
        return lastAccessToken;
    }

    public OAuthTokenBean setLastAccessToken(String lastAccessToken) {
        this.lastAccessToken = lastAccessToken;
        return this;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public OAuthTokenBean setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public boolean checkAccessToken() {
        return System.currentTimeMillis() - getLastModifyTime() <= DateTimeUtils.SECOND * expiresIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuthTokenBean that = (OAuthTokenBean) o;
        return new EqualsBuilder()
                .append(expiresIn, that.expiresIn)
                .append(clientId, that.clientId)
                .append(accessToken, that.accessToken)
                .append(lastAccessToken, that.lastAccessToken)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clientId)
                .append(accessToken)
                .append(lastAccessToken)
                .append(expiresIn)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientId", clientId)
                .append("accessToken", accessToken)
                .append("lastAccessToken", lastAccessToken)
                .append("expiresIn", expiresIn)
                .toString();
    }
}
