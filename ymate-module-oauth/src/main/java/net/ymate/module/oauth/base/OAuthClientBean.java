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


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/13 下午9:12
 * @version 1.0
 */
public class OAuthClientBean extends OAuthTokenBean {

    private String name;

    private String iconUrl;

    private String domain;

    private String secretKey;

    public OAuthClientBean() {
        super();
    }

    public String getName() {
        return name;
    }

    public OAuthClientBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public OAuthClientBean setIconUrl(String iconUrl) {
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

    public OAuthClientBean setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public boolean checkSecret(String secretKey) {
        return StringUtils.equals(this.secretKey, secretKey);
    }

    public boolean checkDomain(String redirectURI) {
        if (StringUtils.isBlank(domain)) {
            return true;
        }
        boolean _resultValue;
        try {
            _resultValue = StringUtils.equalsIgnoreCase(new URL(domain).getHost(), new URL(redirectURI).getHost());
        } catch (MalformedURLException e) {
            _resultValue = StringUtils.startsWithIgnoreCase(redirectURI, domain);
        }
        return _resultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuthClientBean that = (OAuthClientBean) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(name, that.name)
                .append(iconUrl, that.iconUrl)
                .append(domain, that.domain)
                .append(secretKey, that.secretKey)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(name)
                .append(iconUrl)
                .append(domain)
                .append(secretKey)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("iconUrl", iconUrl)
                .append("domain", domain)
                .append("secretKey", secretKey)
                .toString();
    }
}
