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

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/6 下午11:56
 * @version 1.0
 */
public class OAuthAccount {

    private String id;

    private String serviceUrl;

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private Map<String, String> attributes = new HashMap<String, String>();

    public OAuthAccount(String id, String serviceUrl, String clientId, String clientSecret) {
        if (StringUtils.isBlank(id)) {
            throw new NullArgumentException("id");
        }
        if (StringUtils.isBlank(serviceUrl)) {
            throw new NullArgumentException("service_url");
        } else if (!StringUtils.startsWithAny(StringUtils.lowerCase(serviceUrl), new String[]{"https://", "http://"})) {
            throw new IllegalArgumentException("service_url must be start with 'https://' or 'http://'.");
        }
        if (StringUtils.isBlank(clientId)) {
            throw new NullArgumentException("client_id");
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new NullArgumentException("client_secret");
        }
        this.id = id;
        this.serviceUrl = serviceUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public OAuthAccount(String id, String serviceUrl, String clientId, String clientSecret, String redirectUri) {
        this(id, serviceUrl, clientId, clientSecret);
        if (StringUtils.isNotBlank(redirectUri) && !StringUtils.startsWithAny(StringUtils.lowerCase(serviceUrl), new String[]{"https://", "http://"})) {
            throw new IllegalArgumentException("redirect_uri must be start with 'https://' or 'http://'.");
        }
        this.redirectUri = redirectUri;
    }

    public String getId() {
        return id;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public OAuthAccount addAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
