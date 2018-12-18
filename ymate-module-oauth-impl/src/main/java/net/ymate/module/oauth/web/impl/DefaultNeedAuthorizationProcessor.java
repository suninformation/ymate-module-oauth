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
package net.ymate.module.oauth.web.impl;

import net.ymate.module.oauth.support.NeedAuthorizationException;
import net.ymate.module.oauth.web.INeedAuthorizationProcessor;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/2/8 上午1:43
 * @version 1.0
 */
public class DefaultNeedAuthorizationProcessor implements INeedAuthorizationProcessor {

    @Override
    public IView process(NeedAuthorizationException e) throws Exception {
        return View.jspView()
                .addAttribute(CLIENT_TITLE, e.getClientBean().getName())
                .addAttribute(CLIENT_ICON, e.getClientBean().getIconUrl())
                .addAttribute(CLIENT_DOMAIN, e.getClientBean().getDomain())
                .addAttribute(CLIENT_SCOPES, e.getScopes());
    }
}
