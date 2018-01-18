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
package net.ymate.module.oauth.support;

import net.ymate.module.oauth.base.OAuthClientBean;

import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/17 下午3:55
 * @version 1.0
 */
public class NeedAuthorizationException extends Exception {

    private OAuthClientBean clientBean;

    private String uid;

    private Set<String> scopes;

    public NeedAuthorizationException(OAuthClientBean clientBean, String uid, Set<String> scopes) {
        this.clientBean = clientBean;
        this.uid = uid;
        this.scopes = scopes;
    }

    public OAuthClientBean getClientBean() {
        return clientBean;
    }

    public String getUid() {
        return uid;
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
