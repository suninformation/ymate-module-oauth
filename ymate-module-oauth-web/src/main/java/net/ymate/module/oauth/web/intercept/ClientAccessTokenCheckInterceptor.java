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
package net.ymate.module.oauth.web.intercept;

import net.ymate.module.oauth.OAuth;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/13 上午2:22
 * @version 1.0
 */
public class ClientAccessTokenCheckInterceptor extends AbstractInterceptor {

    @Override
    protected Object __before(InterceptContext context) throws Exception {
        OAuthResponse _response = OAuth.get().checkClientAccessToken(WebContext.getRequest());
        if (_response != null) {
            return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
        }
        return null;
    }

    @Override
    protected Object __after(InterceptContext context) throws Exception {
        return null;
    }
}
