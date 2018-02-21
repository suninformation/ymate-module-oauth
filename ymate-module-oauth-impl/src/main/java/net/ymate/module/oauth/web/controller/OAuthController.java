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
package net.ymate.module.oauth.web.controller;

import net.ymate.framework.core.Optional;
import net.ymate.framework.webmvc.intercept.UserSessionCheckInterceptor;
import net.ymate.module.oauth.IOAuth;
import net.ymate.module.oauth.IOAuthGrantProcessor;
import net.ymate.module.oauth.OAuth;
import net.ymate.module.oauth.annotation.OAuthScope;
import net.ymate.module.oauth.impl.ImplicitGrantProcessor;
import net.ymate.module.oauth.support.NeedAuthorizationException;
import net.ymate.module.oauth.web.INeedAuthorizationProcessor;
import net.ymate.module.oauth.web.impl.DefaultNeedAuthorizationProcessor;
import net.ymate.module.oauth.web.intercept.UserAccessTokenCheckInterceptor;
import net.ymate.platform.core.beans.annotation.Before;
import net.ymate.platform.core.beans.annotation.ContextParam;
import net.ymate.platform.core.beans.annotation.ParamItem;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 17/3/3 上午1:43
 * @version 1.0
 */
@Controller
@RequestMapping("/oauth2")
public class OAuthController {

    /**
     * @return 返回访问凭证 (grant_type=client_credentials)
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/token", method = Type.HttpMethod.POST)
    public IView token() throws Exception {
        OAuthResponse _response = OAuth.get().getGrantProcessor(GrantType.CLIENT_CREDENTIALS).process(WebContext.getRequest());
        return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
    }

    /**
     * 获取授权码或使用简单模式直接获取访问凭证 (response_type=[code|token], scope=[snsapi_base|snsapi_userinfo])
     *
     * @return 重定向至redirect_url指定的URL地址
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/sns/authorize", method = {Type.HttpMethod.POST, Type.HttpMethod.GET})
    @Before(UserSessionCheckInterceptor.class)
    @ContextParam(@ParamItem(Optional.OBSERVE_SILENCE))
    public IView authorize() throws Exception {
        IView _view;
        try {
            OAuthResponse _response;
            if (OAuth.get().getModuleCfg().getAllowGrantTypes().contains(GrantType.AUTHORIZATION_CODE)) {
                _response = new ImplicitGrantProcessor(OAuth.get(), ResponseType.CODE).process(WebContext.getRequest());
            } else {
                _response = IOAuthGrantProcessor.UNSUPPORTED_GRANT_TYPE.process(WebContext.getRequest());
            }
            if (StringUtils.isNotBlank(_response.getLocationUri())) {
                _view = View.httpStatusView(_response.getResponseStatus()).addHeader("Location", _response.getLocationUri());
            } else {
                _view = new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
            }
        } catch (NeedAuthorizationException e) {
            INeedAuthorizationProcessor _processorClass = ClassUtils.impl(OAuth.get().getOwner().getConfig().getParam(IOAuth.MODULE_NAME + ".need_authorization_processor_class"), INeedAuthorizationProcessor.class, this.getClass());
            if (_processorClass == null) {
                _processorClass = new DefaultNeedAuthorizationProcessor();
            }
            _view = _processorClass.process(e);
        }
        return _view;
    }

    /**
     * @return 返回访问凭证 (grant_type=[authorization_code|password])
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/sns/access_token", method = Type.HttpMethod.POST)
    public IView accessToken() throws Exception {
        OAuthResponse _response;
        try {
            HttpServletRequest _request = WebContext.getRequest();
            GrantType _grantType = GrantType.valueOf(StringUtils.upperCase(StringUtils.trimToEmpty(_request.getParameter(org.apache.oltu.oauth2.common.OAuth.OAUTH_GRANT_TYPE))));
            _response = OAuth.get().getGrantProcessor(_grantType).process(_request);
        } catch (OAuthProblemException e) {
            _response = OAuth.get().getModuleCfg().getErrorAdapter().onError(e);
        } catch (IllegalArgumentException e) {
            _response = OAuth.get().getModuleCfg().getErrorAdapter().onError(IOAuth.ErrorType.INVALID_REQUEST);
        }
        return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
    }

    /**
     * @return 刷新访问凭证 (grant_type=refresh_token)
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/sns/refresh_token", method = Type.HttpMethod.POST)
    public IView refreshToken() throws Exception {
        OAuthResponse _response = OAuth.get().getGrantProcessor(GrantType.REFRESH_TOKEN).process(WebContext.getRequest());
        return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
    }

    /**
     * @return 验证访问凭证是否有效
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping("/sns/auth")
    @Before(UserAccessTokenCheckInterceptor.class)
    @OAuthScope(IOAuth.Const.SCOPE_SNSAPI_BASE)
    public IView auth() throws Exception {
        OAuthResponse _response = OAuthASResponse.errorResponse(HttpServletResponse.SC_OK).setError("ok").buildJSONMessage();
        return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
    }

    /**
     * @return 返回用户信息
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping("/sns/userinfo")
    @Before(UserAccessTokenCheckInterceptor.class)
    @OAuthScope(IOAuth.Const.SCOPE_SNSAPI_USERINFO)
    public IView userinfo() throws Exception {
        return View.nullView();
    }
}
