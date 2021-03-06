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

/**
 * @author 刘镇 (suninformation@163.com) on 2017/4/30 下午10:50
 * @version 1.0
 */
public interface IOAuthTokenGenerator {

    /**
     * @return 生成访问令牌
     * @throws Exception 可能产生的任何异常
     */
    String accessToken() throws Exception;

    /**
     * @return 生成授权码
     * @throws Exception 可能产生的任何异常
     */
    String authorizationCode() throws Exception;

    /**
     * @return 生成刷新令牌
     * @throws Exception 可能产生的任何异常
     */
    String refreshToken() throws Exception;
}
