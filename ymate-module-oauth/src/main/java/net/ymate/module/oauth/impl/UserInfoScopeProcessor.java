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
package net.ymate.module.oauth.impl;

import net.ymate.module.oauth.AbstractScopeProcessor;
import net.ymate.module.oauth.IOAuth;
import net.ymate.module.oauth.OAuth;
import net.ymate.module.oauth.annotation.OAuthScope;
import net.ymate.module.oauth.base.OAuthClientUserBean;
import net.ymate.module.oauth.base.OAuthUserInfoBean;
import net.ymate.module.oauth.support.OAuthResponseUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/18 上午11:05
 * @version 1.0
 */
@OAuthScope(IOAuth.Const.SCOPE_SNSAPI_USERINFO)
public class UserInfoScopeProcessor extends AbstractScopeProcessor {

    @Override
    public OAuthResponse process(HttpServletRequest request, OAuthClientUserBean tokenBean) throws Exception {
        OAuthResponse _response = null;
        String _openId = request.getParameter(IOAuth.Const.OPEN_ID);
        if (StringUtils.isNotBlank(_openId) && StringUtils.equalsIgnoreCase(tokenBean.getOpenId(), _openId)) {
            OAuthUserInfoBean _infoBean = getOwner().getModuleCfg().getStorageAdapter().findUserInfo(tokenBean.getClientId(), _openId);
            if (_infoBean != null) {
                OAuthResponse.OAuthResponseBuilder _builder = new OAuthResponse.OAuthResponseBuilder(HttpServletResponse.SC_OK)
                        .setParam(IOAuth.Const.OPEN_ID, _openId);
                _response = OAuthResponseUtils.appendParams(_infoBean.getAttributes(), OAuthResponseUtils.appendParams(getParams(), _builder)).buildJSONMessage();
            }
        }
        if (_response == null) {
            _response = OAuth.get().getModuleCfg().getErrorAdapter().onError(IOAuth.ErrorType.INVALID_OPEN_ID);
        }
        return _response;
    }
}
