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
package net.ymate.module.oauth;

import net.ymate.platform.core.lang.BlurObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/18 上午11:06
 * @version 1.0
 */
public abstract class AbstractScopeProcessor implements IOAuthScopeProcessor {

    private IOAuth owner;

    private boolean inited;

    private Map<String, Object> params = new HashMap<String, Object>();

    @Override
    public void init(IOAuth owner) throws Exception {
        this.owner = owner;
        this.inited = true;
    }

    public IOAuth getOwner() {
        return owner;
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public boolean isInited() {
        return inited;
    }

    @Override
    public IOAuthScopeProcessor setParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public BlurObject getParam(String key) {
        return BlurObject.bind(params.get(key));
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
