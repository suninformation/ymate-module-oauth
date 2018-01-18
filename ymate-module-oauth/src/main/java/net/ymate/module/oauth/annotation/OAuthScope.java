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
package net.ymate.module.oauth.annotation;

import java.lang.annotation.*;

/**
 * 声明一个类为OAuth授权作用域处理器或设置访问一个类方法所需要用户的授权作用域名称
 *
 * @author 刘镇 (suninformation@163.com) on 2018/1/14 下午6:06
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OAuthScope {

    /**
     * @return 需要用户授权的作用域名称
     */
    String value() default "";

    /**
     * @return 是否自动调用处理器
     */
    boolean automatic() default true;
}
