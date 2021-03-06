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

import net.ymate.platform.core.event.EventContext;
import net.ymate.platform.core.event.IEvent;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/2/15 上午2:47
 * @version 1.0
 */
public class OAuthEvent extends EventContext<IOAuth, OAuthEvent.EVENT> implements IEvent {

    public enum EVENT {
        CLIENT_CREDENTIALS, AUTHORIZATION_CODE, IMPLICIT, PASSWORD, REFRESH_TOKEN, SCOPE_PROCESSOR
    }

    public OAuthEvent(IOAuth owner, EVENT eventName) {
        super(owner, OAuthEvent.class, eventName);
    }
}
