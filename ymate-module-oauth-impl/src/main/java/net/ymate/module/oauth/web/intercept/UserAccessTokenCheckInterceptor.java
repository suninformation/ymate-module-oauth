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

import net.ymate.module.oauth.IOAuth;
import net.ymate.module.oauth.IOAuthScopeProcessor;
import net.ymate.module.oauth.OAuth;
import net.ymate.module.oauth.OAuthEvent;
import net.ymate.module.oauth.annotation.OAuthScope;
import net.ymate.module.oauth.base.OAuthClientUserBean;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/13 上午2:10
 * @version 1.0
 */
public class UserAccessTokenCheckInterceptor extends AbstractInterceptor {

    @Override
    protected Object __before(InterceptContext context) throws Exception {
        OAuthResponse _response;
        HttpServletRequest _request = WebContext.getRequest();
        OAuthScope _scope = context.getTargetMethod().getAnnotation(OAuthScope.class);
        _response = OAuth.get().checkUserAccessToken(_request, _scope != null ? _scope.value() : null);
        if (_response == null) {
            if (_scope != null) {
                if (StringUtils.isBlank(_scope.value())) {
                    _response = OAuth.get().getModuleCfg().getErrorAdapter().onError(IOAuth.ErrorType.INVALID_SCOPE);
                } else if (_scope.automatic()) {
                    OAuthClientUserBean _tokenBean = (OAuthClientUserBean) _request.getAttribute(OAuthClientUserBean.class.getName());
                    if (_tokenBean != null) {
                        IOAuthScopeProcessor _processor = OAuth.get().getScopeProcessor(_scope.value());
                        if (_processor != null) {
                            _response = _processor.process(_request, _tokenBean);
                            //
                            context.getOwner().getEvents().fireEvent(new OAuthEvent(OAuth.get(), OAuthEvent.EVENT.SCOPE_PROCESSOR).setEventSource(_scope.value()).addParamExtend(OAuthClientUserBean.class.getName(), _tokenBean));
                        }
                    }
                    if (_response == null) {
                        _response = OAuth.get().getModuleCfg().getErrorAdapter().onError(IOAuth.ErrorType.INVALID_REQUEST);
                    }
                }
            }
        }
        if (_response != null) {
            return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
        }
        return null;
    }

    @Override
    protected Object __after(InterceptContext context) {
        return null;
    }
}
