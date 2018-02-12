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
package net.ymate.module.oauth.model;

import net.ymate.platform.core.beans.annotation.PropertyState;
import net.ymate.platform.persistence.IShardingable;
import net.ymate.platform.persistence.annotation.*;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/02/11 上午 01:52:32
 * @version 1.0
 */
@Entity("oauth_client")
public class OAuthClient extends BaseEntity<OAuthClient, java.lang.String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Property(name = "id", nullable = false, length = 32)
    @PropertyState(propertyName = "id")
    private java.lang.String id;

    @Property(name = "name", nullable = false, length = 32)
    @PropertyState(propertyName = "name")
    private java.lang.String name;

    @Property(name = "domain", length = 100)
    @PropertyState(propertyName = "domain")
    private java.lang.String domain;

    @Property(name = "icon_url", length = 255)
    @PropertyState(propertyName = "icon_url")
    private java.lang.String iconUrl;

    @Property(name = "secret_key", nullable = false, length = 32)
    @PropertyState(propertyName = "secret_key")
    private java.lang.String secretKey;

    @Property(name = "description", length = 1000)
    @PropertyState(propertyName = "description")
    private java.lang.String description;

    @Property(name = "access_token", length = 128)
    @PropertyState(propertyName = "access_token")
    private java.lang.String accessToken;

    @Property(name = "last_access_token", length = 128)
    @PropertyState(propertyName = "last_access_token")
    private java.lang.String lastAccessToken;

    @Property(name = "expires_in", length = 11)
    @Default("0")
    @PropertyState(propertyName = "expires_in")
    private java.lang.Integer expiresIn;

    @Property(name = "ip_white_list", length = 200)
    @PropertyState(propertyName = "ip_white_list")
    private java.lang.String ipWhiteList;

    @Property(name = "type", unsigned = true, length = 2)
    @Default("0")
    @PropertyState(propertyName = "type")
    private java.lang.Integer type;

    @Property(name = "status", unsigned = true, length = 2)
    @Default("0")
    @PropertyState(propertyName = "status")
    private java.lang.Integer status;

    @Property(name = "create_time", nullable = false, length = 13)
    @PropertyState(propertyName = "create_time")
    @Readonly
    private java.lang.Long createTime;

    @Property(name = "last_modify_time", length = 13)
    @Default("0")
    @PropertyState(propertyName = "last_modify_time")
    private java.lang.Long lastModifyTime;

    /**
     * 构造器
     */
    public OAuthClient() {
    }

    /**
     * 构造器
     *
     * @param id
     * @param name
     * @param secretKey
     * @param createTime
     */
    public OAuthClient(java.lang.String id, java.lang.String name, java.lang.String secretKey, java.lang.Long createTime) {
        this.id = id;
        this.name = name;
        this.secretKey = secretKey;
        this.createTime = createTime;
    }

    /**
     * 构造器
     *
     * @param id
     * @param name
     * @param domain
     * @param iconUrl
     * @param secretKey
     * @param description
     * @param accessToken
     * @param lastAccessToken
     * @param expiresIn
     * @param ipWhiteList
     * @param type
     * @param status
     * @param createTime
     * @param lastModifyTime
     */
    public OAuthClient(java.lang.String id, java.lang.String name, java.lang.String domain, java.lang.String iconUrl, java.lang.String secretKey, java.lang.String description, java.lang.String accessToken, java.lang.String lastAccessToken, java.lang.Integer expiresIn, java.lang.String ipWhiteList, java.lang.Integer type, java.lang.Integer status, java.lang.Long createTime, java.lang.Long lastModifyTime) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.iconUrl = iconUrl;
        this.secretKey = secretKey;
        this.description = description;
        this.accessToken = accessToken;
        this.lastAccessToken = lastAccessToken;
        this.expiresIn = expiresIn;
        this.ipWhiteList = ipWhiteList;
        this.type = type;
        this.status = status;
        this.createTime = createTime;
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public java.lang.String getId() {
        return id;
    }

    @Override
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * @return the name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * @return the domain
     */
    public java.lang.String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(java.lang.String domain) {
        this.domain = domain;
    }

    /**
     * @return the iconUrl
     */
    public java.lang.String getIconUrl() {
        return iconUrl;
    }

    /**
     * @param iconUrl the iconUrl to set
     */
    public void setIconUrl(java.lang.String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * @return the secretKey
     */
    public java.lang.String getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(java.lang.String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * @return the description
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * @return the accessToken
     */
    public java.lang.String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(java.lang.String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the lastAccessToken
     */
    public java.lang.String getLastAccessToken() {
        return lastAccessToken;
    }

    /**
     * @param lastAccessToken the lastAccessToken to set
     */
    public void setLastAccessToken(java.lang.String lastAccessToken) {
        this.lastAccessToken = lastAccessToken;
    }

    /**
     * @return the expiresIn
     */
    public java.lang.Integer getExpiresIn() {
        return expiresIn;
    }

    /**
     * @param expiresIn the expiresIn to set
     */
    public void setExpiresIn(java.lang.Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * @return the ipWhiteList
     */
    public java.lang.String getIpWhiteList() {
        return ipWhiteList;
    }

    /**
     * @param ipWhiteList the ipWhiteList to set
     */
    public void setIpWhiteList(java.lang.String ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    /**
     * @return the type
     */
    public java.lang.Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    /**
     * @return the status
     */
    public java.lang.Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    /**
     * @return the createTime
     */
    public java.lang.Long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(java.lang.Long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the lastModifyTime
     */
    public java.lang.Long getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * @param lastModifyTime the lastModifyTime to set
     */
    public void setLastModifyTime(java.lang.Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }


    //
    // Chain
    //

    public static OAuthClientBuilder builder() {
        return new OAuthClientBuilder();
    }

    public OAuthClientBuilder bind() {
        return new OAuthClientBuilder(this);
    }

    public static class OAuthClientBuilder {

        private OAuthClient _model;

        public OAuthClientBuilder() {
            _model = new OAuthClient();
        }

        public OAuthClientBuilder(OAuthClient model) {
            _model = model;
        }

        public OAuthClient build() {
            return _model;
        }


        public IConnectionHolder connectionHolder() {
            return _model.getConnectionHolder();
        }

        public OAuthClientBuilder connectionHolder(IConnectionHolder connectionHolder) {
            _model.setConnectionHolder(connectionHolder);
            return this;
        }

        public String dataSourceName() {
            return _model.getDataSourceName();
        }

        public OAuthClientBuilder dataSourceName(String dsName) {
            _model.setDataSourceName(dsName);
            return this;
        }

        public IShardingable shardingable() {
            return _model.getShardingable();
        }

        public OAuthClientBuilder shardingable(IShardingable shardingable) {
            _model.setShardingable(shardingable);
            return this;
        }

        public java.lang.String id() {
            return _model.getId();
        }

        public OAuthClientBuilder id(java.lang.String id) {
            _model.setId(id);
            return this;
        }

        public java.lang.String name() {
            return _model.getName();
        }

        public OAuthClientBuilder name(java.lang.String name) {
            _model.setName(name);
            return this;
        }

        public java.lang.String domain() {
            return _model.getDomain();
        }

        public OAuthClientBuilder domain(java.lang.String domain) {
            _model.setDomain(domain);
            return this;
        }

        public java.lang.String iconUrl() {
            return _model.getIconUrl();
        }

        public OAuthClientBuilder iconUrl(java.lang.String iconUrl) {
            _model.setIconUrl(iconUrl);
            return this;
        }

        public java.lang.String secretKey() {
            return _model.getSecretKey();
        }

        public OAuthClientBuilder secretKey(java.lang.String secretKey) {
            _model.setSecretKey(secretKey);
            return this;
        }

        public java.lang.String description() {
            return _model.getDescription();
        }

        public OAuthClientBuilder description(java.lang.String description) {
            _model.setDescription(description);
            return this;
        }

        public java.lang.String accessToken() {
            return _model.getAccessToken();
        }

        public OAuthClientBuilder accessToken(java.lang.String accessToken) {
            _model.setAccessToken(accessToken);
            return this;
        }

        public java.lang.String lastAccessToken() {
            return _model.getLastAccessToken();
        }

        public OAuthClientBuilder lastAccessToken(java.lang.String lastAccessToken) {
            _model.setLastAccessToken(lastAccessToken);
            return this;
        }

        public java.lang.Integer expiresIn() {
            return _model.getExpiresIn();
        }

        public OAuthClientBuilder expiresIn(java.lang.Integer expiresIn) {
            _model.setExpiresIn(expiresIn);
            return this;
        }

        public java.lang.String ipWhiteList() {
            return _model.getIpWhiteList();
        }

        public OAuthClientBuilder ipWhiteList(java.lang.String ipWhiteList) {
            _model.setIpWhiteList(ipWhiteList);
            return this;
        }

        public java.lang.Integer type() {
            return _model.getType();
        }

        public OAuthClientBuilder type(java.lang.Integer type) {
            _model.setType(type);
            return this;
        }

        public java.lang.Integer status() {
            return _model.getStatus();
        }

        public OAuthClientBuilder status(java.lang.Integer status) {
            _model.setStatus(status);
            return this;
        }

        public java.lang.Long createTime() {
            return _model.getCreateTime();
        }

        public OAuthClientBuilder createTime(java.lang.Long createTime) {
            _model.setCreateTime(createTime);
            return this;
        }

        public java.lang.Long lastModifyTime() {
            return _model.getLastModifyTime();
        }

        public OAuthClientBuilder lastModifyTime(java.lang.Long lastModifyTime) {
            _model.setLastModifyTime(lastModifyTime);
            return this;
        }

    }

    /**
     * OauthClient 字段常量表
     */
    public class FIELDS {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DOMAIN = "domain";
        public static final String ICON_URL = "icon_url";
        public static final String SECRET_KEY = "secret_key";
        public static final String DESCRIPTION = "description";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String LAST_ACCESS_TOKEN = "last_access_token";
        public static final String EXPIRES_IN = "expires_in";
        public static final String IP_WHITE_LIST = "ip_white_list";
        public static final String TYPE = "type";
        public static final String STATUS = "status";
        public static final String CREATE_TIME = "create_time";
        public static final String LAST_MODIFY_TIME = "last_modify_time";
    }

    public static final String TABLE_NAME = "oauth_client";

}
