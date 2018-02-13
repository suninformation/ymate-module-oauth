### YMP-OAuth:

> OAuth2授权服务模块，特性如下：
>
> - 支持客户端模式 `grant_type=client_credentials`；
> - 支持授权码模式 `grant_type=authorization_code`；
> - 支持密码模式 `grant_type=password`；
> - 支持简单模式 `response_type=token`；
> - 支持`scope`权限范围：`snsapi_base`和`snsapi_userinfo`；
> - 支持令牌更新：`grant_type=refresh_token`；
> - 支持令牌有效性验证；
> - 支持拦截器限制接口请求的`scope`权限：
>
>   - 客户端授权拦截器：ClientAccessTokenCheckInterceptor.class
>   - 用户网页授权拦截器：SnsAccessTokenCheckInterceptor.class

#### Maven包依赖

    <dependency>
        <groupId>net.ymate.module</groupId>
        <artifactId>ymate-module-oauth</artifactId>
        <version>1.0.0</version>
    </dependency>

#### 模块配置参数说明

    #-------------------------------------
    # module.oauth 模块初始化参数
    #-------------------------------------
    
    # AccessToken访问凭证超时时间, 单位(秒), 默认值: 7200(两小时)
    ymp.configs.module.oauth.access_token_expire_in=
    
    # AccessToken访问凭证最大刷新次数，若为0表示不限制
    ymp.configs.module.oauth.refresh_count_max=
    
    # RefreshToken刷新凭证超时时间, 单位(天), 默认值: 30
    ymp.configs.module.oauth.refresh_token_expire_in=
    
    # 授权码过期时间, 单位(分钟), 默认值: 5
    ymp.configs.module.oauth.authorization_code_expire_in=
    
    # 缓存名称前缀, 默认值: ""
    ymp.configs.module.oauth.cache_name_prefix=
    
    # 设置开启的授权模式, 多个模式之间用'|'分隔, 默认值: none，可选值范围: [authorization_code|implicit|password|refresh_token|client_credentials|none]
    ymp.configs.module.oauth.allow_grant_types=
    
    # Token生成器接口实现, 默认值: net.ymate.module.oauth.impl.DefaultTokenGenerator
    ymp.configs.module.oauth.token_generator_class=
    
    # OAuth令牌存储适配器接口实现, 默认值: 空
    ymp.configs.module.oauth.storage_adapter_class=
    
    # OAuth用户授权处理器接口实现, 默认值: net.ymate.module.oauth.web.impl.DefaultNeedAuthorizationProcessor
    ymp.params.module.oauth.need_authorization_processor_class=

#### 示例代码

- 通过拦截器限制接口请求`scope`的权限必须是`snsapi_userinfo`：

        @RequestMapping("/sns/userinfo")
        @Before(UserAccessTokenCheckInterceptor.class)
        @OAuthScope(IOAuth.Const.SCOPE_SNSAPI_USERINFO)
        public IView userinfo(@RequestParam(IOAuth.Const.ACCESS_TOKEN) String accountToken, @RequestParam(IOAuth.Const.OPEN_ID) String openId) throws Exception {
            OAuthResponse _response = null;
            IOAuthScopeProcessor _processor = OAuth.get().getScopeProcessor(_scope.value());
            if (_processor != null) {
                _response = _processor.process(WebContext.getRequest(), (OAuthTokenBean) WebContext.getRequest().getAttribute(OAuthTokenBean.class.getName()));
            }
            if (_response == null) {
                _response = OAuthResponseUtils.badRequest(OAuthError.ResourceResponse.INVALID_REQUEST);
            }
            return new HttpStatusView(_response.getResponseStatus(), false).writeBody(_response.getBody());
        }

- 客户端模式：

    - 以POST方式请求URL地址：

            http://localhost:8080/oauth2/token
    
    - POST请求报文：

            POST /oauth2/token HTTP/1.1
            Host: localhost:8080
            Content-Type: application/x-www-form-urlencoded
            
            client_id=default&client_secret=7890123&grant_type=client_credentials

- 授权码模式：

    - 获取授权码，以GET方式请求URL地址，成功则重定向并携带`code`授权码：

            http://localhost:8080/oauth2/sns/authorize?client_id=default&response_type=code&redirect_uri=http://localhost:8080/oauth2/sns/redirect&scope=snsapi_base&state=Helloworld

    - 接收到`code`授权码后，获取`access_token`令牌，以POST方式请求URL地址：

            http://localhost:8080/oauth2/sns/access_token
    
    - POST请求报文：

            POST /oauth2/sns/access_token HTTP/1.1
            Host: localhost:8080
            Content-Type: application/x-www-form-urlencoded

            code=f32ab01222936356e5a8352b9beeacc3&client_id=default&client_secret=7890123&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2%2Fsns%2Fredirect

- 简间单模式：

    - 以GET方式请求URL地址，成功则重定向并携带`access_token`令牌：

            http://localhost:8080/oauth2/sns/authorize?client_id=default&response_type=token&redirect_uri=http://localhost:8080/oauth2/sns/redirect&scope=snsapi_base&state=Helloworld

- 密码模式：

    - 以POST方式请求URL地址：

            http://localhost:8080/oauth2/sns/access_token

    - POST请求报文：

            POST /oauth2/sns/access_token HTTP/1.1
            Host: localhost:8080
            Content-Type: application/x-www-form-urlencoded

            client_id=default&client_secret=7890123&grant_type=password&username=suninformation&password=8fa6adcdaa9e50635c5bf54eacfca83a&scope=snsapi_userinfo


- 令牌更新：

    - 以POST方式请求URL地址：

            http://localhost:8080/oauth2/sns/refresh_token

    - POST请求报文：

            POST /oauth2/sns/refresh_token HTTP/1.1
            Host: localhost:8080
            Content-Type: application/x-www-form-urlencoded

            grant_type=refresh_token&refresh_token=207f2a5de46198dc8b42ba175e75cac1&client_id=default&client_secret=7890123

- 令牌有效性验证：

    - 以GET方式请求URL地址：
    
            http://localhost:8080/oauth2/sns/auth?access_token=39ec0af0b9f5f7ebe2877389bb2919b4&open_id=6bf18fa2f9a136273fb90e58dff4a964


- 获取授权用户基本信息：

    - 以GET方式请求URL地址：

            http://localhost:8080/oauth2/sns/userinfo?access_token=8fec1e7eca5d6e18b48d33f1725f6082&open_id=6bf18fa2f9a136273fb90e58dff4a964

#### One More Thing

YMP不仅提供便捷的Web及其它Java项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入 官方QQ群480374360，一起交流学习，帮助YMP成长！

了解更多有关YMP框架的内容，请访问官网：http://www.ymate.net/