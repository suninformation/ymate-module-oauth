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
package net.ymate.module.oauth.handle;

import net.ymate.module.oauth.IOAuth;
import net.ymate.module.oauth.IOAuthScopeProcessor;
import net.ymate.platform.core.beans.IBeanHandler;
import net.ymate.platform.core.util.ClassUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/14 下午6:13
 * @version 1.0
 */
public class OAuthScopeHandler implements IBeanHandler {

    private final IOAuth __owner;

    public OAuthScopeHandler(IOAuth owner) {
        __owner = owner;
        __owner.getOwner().registerExcludedClass(IOAuthScopeProcessor.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object handle(Class<?> targetClass) throws Exception {
        if (ClassUtils.isInterfaceOf(targetClass, IOAuthScopeProcessor.class)) {
            __owner.registerScopeProcessor((Class<? extends IOAuthScopeProcessor>) targetClass);
        }
        return null;
    }
}
