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
package net.ymate.module.oauth.base;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/1/14 下午6:39
 * @version 1.0
 */
public class BaseValueBean<T> implements Serializable {

    private Map<String, Object> attributes;

    private long createTime;

    private long lastModifyTime;

    public BaseValueBean() {
        this.attributes = new HashMap<String, Object>();
    }

    @SuppressWarnings("unchecked")
    public T setAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return (T) this;
    }

    public Object getAttribute(String attrKey) {
        return this.attributes.get(attrKey);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }


    public long getCreateTime() {
        return createTime;
    }

    @SuppressWarnings("unchecked")
    public T setCreateTime(long createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    @SuppressWarnings("unchecked")
    public T setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseValueBean<?> that = (BaseValueBean<?>) o;
        return new EqualsBuilder()
                .append(attributes, that.attributes)
                .append(createTime, that.createTime)
                .append(lastModifyTime, that.lastModifyTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(attributes)
                .append(createTime)
                .append(lastModifyTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("attributes", attributes)
                .append("createTime", createTime)
                .append("lastModifyTime", lastModifyTime)
                .toString();
    }
}
