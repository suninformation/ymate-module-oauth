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
package net.ymate.module.oauth.client.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ymate.framework.commons.IHttpResponse;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/7 上午12:15
 * @version 1.0
 */
public class OAuthResult implements Serializable {

    @JSONField(serialize = false)
    private JSONObject originalResult;

    private String error;

    private String description;

    public OAuthResult(IHttpResponse result) {
        originalResult = JSONObject.parseObject(result.getContent());
        if (originalResult.containsKey("error")) {
            error = StringUtils.trimToNull(originalResult.getString("error"));
            if (originalResult.containsKey("error_description")) {
                description = originalResult.getString("error_description");
            }
        }
    }

    @JSONField(serialize = false)
    public boolean isOK() {
        return error == null;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }

    public JSONObject getOriginalResult() {
        return originalResult;
    }
}
