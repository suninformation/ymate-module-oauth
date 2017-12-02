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
package net.ymate.module.oauth;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户身份信息适配器接口
 *
 * @author 刘镇 (suninformation@163.com) on 17/4/30 上午11:25
 * @version 1.0
 */
public interface IOAuthUserInfoAdapter {

    void init(IOAuth owner);

    void destroy();

    /**
     * @param username 用户名称或电子邮件地址或手机号码
     * @param passwd   登录密码md5值
     * @return 验证用户身份合法性, 验证通过则返回用户Id, 否则返回null
     * @throws Exception 可能产生的任何异常
     */
    String verify(String username, String passwd) throws Exception;

    /**
     * @param uid 用户的唯一标识
     * @return 返回用户信息
     * @throws Exception 可能产生的任何异常
     */
    JSONObject getUserInfo(String uid) throws Exception;
}
