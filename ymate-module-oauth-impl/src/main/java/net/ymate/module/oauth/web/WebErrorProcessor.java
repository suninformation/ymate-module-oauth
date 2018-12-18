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

import net.ymate.module.oauth.support.OAuthResponseUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.impl.DefaultWebErrorProcessor;
import net.ymate.platform.webmvc.util.ErrorCode;
import net.ymate.platform.webmvc.util.WebUtils;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/2/23 上午12:22
 * @version 1.0
 */
public class WebErrorProcessor extends DefaultWebErrorProcessor {

    private static final Log _LOG = LogFactory.getLog(WebErrorProcessor.class);

    @Override
    public IView showErrorMsg(int code, String msg, Map<String, Object> dataMap) {
        try {
            OAuthResponse _response = OAuthResponseUtils.appendParams(dataMap, OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(String.valueOf(code))
                    .setErrorDescription(msg)).buildJSONMessage();
            return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
        } catch (OAuthSystemException e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
        return WebUtils.buildErrorView(getOwner(), ErrorCode.create(code, msg).addAttribute(Type.Const.PARAM_DATA, dataMap));
    }

    @Override
    public IView onValidation(IWebMvc owner, Map<String, ValidateResult> results) {
        String _message = WebUtils.errorCodeI18n(getOwner(), ErrorCode.INVALID_PARAMS_VALIDATION, "Request parameter validation is invalid.");
        Map<String, Object> _dataMap = new HashMap<String, Object>();
        for (ValidateResult _vResult : results.values()) {
            _dataMap.put(_vResult.getName(), _vResult.getMsg());
        }
        return showErrorMsg(ErrorCode.INVALID_PARAMS_VALIDATION, _message, _dataMap);
    }
}
