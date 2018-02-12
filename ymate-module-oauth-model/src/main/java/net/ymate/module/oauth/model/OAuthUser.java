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
@Entity("oauth_user")
public class OAuthUser extends BaseEntity<OAuthUser, java.lang.String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Property(name = "id", nullable = false, length = 32)
    @PropertyState(propertyName = "id")
    private java.lang.String id;

    @Property(name = "uid", nullable = false, length = 32)
    @PropertyState(propertyName = "uid")
    private java.lang.String uid;

    @Property(name = "client_id", nullable = false, length = 32)
    @PropertyState(propertyName = "client_id")
    private java.lang.String clientId;

    @Property(name = "is_authorized", unsigned = true, length = 1)
    @Default("0")
    @PropertyState(propertyName = "is_authorized")
    private java.lang.Integer isAuthorized;

    @Property(name = "access_token", length = 128)
    @PropertyState(propertyName = "access_token")
    private java.lang.String accessToken;

    @Property(name = "last_access_token", length = 128)
    @PropertyState(propertyName = "last_access_token")
    private java.lang.String lastAccessToken;

    @Property(name = "refresh_token", length = 128)
    @PropertyState(propertyName = "refresh_token")
    private java.lang.String refreshToken;

    @Property(name = "refresh_count", unsigned = true, length = 1)
    @Default("0")
    @PropertyState(propertyName = "refresh_count")
    private java.lang.Integer refreshCount;

    @Property(name = "expires_in", length = 11)
    @Default("0")
    @PropertyState(propertyName = "expires_in")
    private java.lang.Integer expiresIn;

    @Property(name = "scope", length = 100)
    @PropertyState(propertyName = "scope")
    private java.lang.String scope;

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
    public OAuthUser() {
    }

    /**
     * 构造器
     *
     * @param id
     * @param uid
     * @param clientId
     * @param createTime
     */
    public OAuthUser(java.lang.String id, java.lang.String uid, java.lang.String clientId, java.lang.Long createTime) {
        this.id = id;
        this.uid = uid;
        this.clientId = clientId;
        this.createTime = createTime;
    }

    /**
     * 构造器
     *
     * @param id
     * @param uid
     * @param clientId
     * @param isAuthorized
     * @param accessToken
     * @param lastAccessToken
     * @param refreshToken
     * @param refreshCount
     * @param expiresIn
     * @param scope
     * @param createTime
     * @param lastModifyTime
     */
    public OAuthUser(java.lang.String id, java.lang.String uid, java.lang.String clientId, java.lang.Integer isAuthorized, java.lang.String accessToken, java.lang.String lastAccessToken, java.lang.String refreshToken, java.lang.Integer refreshCount, java.lang.Integer expiresIn, java.lang.String scope, java.lang.Long createTime, java.lang.Long lastModifyTime) {
        this.id = id;
        this.uid = uid;
        this.clientId = clientId;
        this.isAuthorized = isAuthorized;
        this.accessToken = accessToken;
        this.lastAccessToken = lastAccessToken;
        this.refreshToken = refreshToken;
        this.refreshCount = refreshCount;
        this.expiresIn = expiresIn;
        this.scope = scope;
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
     * @return the uid
     */
    public java.lang.String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(java.lang.String uid) {
        this.uid = uid;
    }

    /**
     * @return the clientId
     */
    public java.lang.String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(java.lang.String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the isAuthorized
     */
    public java.lang.Integer getIsAuthorized() {
        return isAuthorized;
    }

    /**
     * @param isAuthorized the isAuthorized to set
     */
    public void setIsAuthorized(java.lang.Integer isAuthorized) {
        this.isAuthorized = isAuthorized;
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
     * @return the refreshToken
     */
    public java.lang.String getRefreshToken() {
        return refreshToken;
    }

    /**
     * @param refreshToken the refreshToken to set
     */
    public void setRefreshToken(java.lang.String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * @return the refreshCount
     */
    public java.lang.Integer getRefreshCount() {
        return refreshCount;
    }

    /**
     * @param refreshCount the refreshCount to set
     */
    public void setRefreshCount(java.lang.Integer refreshCount) {
        this.refreshCount = refreshCount;
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
     * @return the scope
     */
    public java.lang.String getScope() {
        return scope;
    }

    /**
     * @param scope the scope to set
     */
    public void setScope(java.lang.String scope) {
        this.scope = scope;
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

    public static OAuthUserBuilder builder() {
        return new OAuthUserBuilder();
    }

    public OAuthUserBuilder bind() {
        return new OAuthUserBuilder(this);
    }

    public static class OAuthUserBuilder {

        private OAuthUser _model;

        public OAuthUserBuilder() {
            _model = new OAuthUser();
        }

        public OAuthUserBuilder(OAuthUser model) {
            _model = model;
        }

        public OAuthUser build() {
            return _model;
        }


        public IConnectionHolder connectionHolder() {
            return _model.getConnectionHolder();
        }

        public OAuthUserBuilder connectionHolder(IConnectionHolder connectionHolder) {
            _model.setConnectionHolder(connectionHolder);
            return this;
        }

        public String dataSourceName() {
            return _model.getDataSourceName();
        }

        public OAuthUserBuilder dataSourceName(String dsName) {
            _model.setDataSourceName(dsName);
            return this;
        }

        public IShardingable shardingable() {
            return _model.getShardingable();
        }

        public OAuthUserBuilder shardingable(IShardingable shardingable) {
            _model.setShardingable(shardingable);
            return this;
        }

        public java.lang.String id() {
            return _model.getId();
        }

        public OAuthUserBuilder id(java.lang.String id) {
            _model.setId(id);
            return this;
        }

        public java.lang.String uid() {
            return _model.getUid();
        }

        public OAuthUserBuilder uid(java.lang.String uid) {
            _model.setUid(uid);
            return this;
        }

        public java.lang.String clientId() {
            return _model.getClientId();
        }

        public OAuthUserBuilder clientId(java.lang.String clientId) {
            _model.setClientId(clientId);
            return this;
        }

        public java.lang.Integer isAuthorized() {
            return _model.getIsAuthorized();
        }

        public OAuthUserBuilder isAuthorized(java.lang.Integer isAuthorized) {
            _model.setIsAuthorized(isAuthorized);
            return this;
        }

        public java.lang.String accessToken() {
            return _model.getAccessToken();
        }

        public OAuthUserBuilder accessToken(java.lang.String accessToken) {
            _model.setAccessToken(accessToken);
            return this;
        }

        public java.lang.String lastAccessToken() {
            return _model.getLastAccessToken();
        }

        public OAuthUserBuilder lastAccessToken(java.lang.String lastAccessToken) {
            _model.setLastAccessToken(lastAccessToken);
            return this;
        }

        public java.lang.String refreshToken() {
            return _model.getRefreshToken();
        }

        public OAuthUserBuilder refreshToken(java.lang.String refreshToken) {
            _model.setRefreshToken(refreshToken);
            return this;
        }

        public java.lang.Integer refreshCount() {
            return _model.getRefreshCount();
        }

        public OAuthUserBuilder refreshCount(java.lang.Integer refreshCount) {
            _model.setRefreshCount(refreshCount);
            return this;
        }

        public java.lang.Integer expiresIn() {
            return _model.getExpiresIn();
        }

        public OAuthUserBuilder expiresIn(java.lang.Integer expiresIn) {
            _model.setExpiresIn(expiresIn);
            return this;
        }

        public java.lang.String scope() {
            return _model.getScope();
        }

        public OAuthUserBuilder scope(java.lang.String scope) {
            _model.setScope(scope);
            return this;
        }

        public java.lang.Long createTime() {
            return _model.getCreateTime();
        }

        public OAuthUserBuilder createTime(java.lang.Long createTime) {
            _model.setCreateTime(createTime);
            return this;
        }

        public java.lang.Long lastModifyTime() {
            return _model.getLastModifyTime();
        }

        public OAuthUserBuilder lastModifyTime(java.lang.Long lastModifyTime) {
            _model.setLastModifyTime(lastModifyTime);
            return this;
        }

    }

    /**
     * OAuthUser 字段常量表
     */
    public class FIELDS {
        public static final String ID = "id";
        public static final String UID = "uid";
        public static final String CLIENT_ID = "client_id";
        public static final String IS_AUTHORIZED = "is_authorized";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String LAST_ACCESS_TOKEN = "last_access_token";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String REFRESH_COUNT = "refresh_count";
        public static final String EXPIRES_IN = "expires_in";
        public static final String SCOPE = "scope";
        public static final String CREATE_TIME = "create_time";
        public static final String LAST_MODIFY_TIME = "last_modify_time";
    }

    public static final String TABLE_NAME = "oauth_user";

}
