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
package net.ymate.module.oauth.web;

import net.ymate.module.oauth.support.NeedAuthorizationException;
import net.ymate.platform.webmvc.view.IView;

/**
 * OAuth用户授权视图处理器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2018/2/8 上午1:42
 * @version 1.0
 */
public interface INeedAuthorizationProcessor {

    String CLIENT_TITLE = "client_title";

    String CLIENT_ICON = "client_icon";

    String CLIENT_DOMAIN = "client_domain";

    String CLIENT_SCOPES = "scopes";

    IView process(NeedAuthorizationException e) throws Exception;
}
