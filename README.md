### OAuth

基于YMP框架实现的OAuth2授权服务模块；

#### Maven包依赖

    <dependency>
        <groupId>net.ymate.module</groupId>
        <artifactId>ymate-module-oauth</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

### 模块配置参数说明

    #-------------------------------------
    # module.oauth 模块初始化参数
    #-------------------------------------
    
    # AccessToken访问凭证超时时间, 单位(秒), 默认值: 7200(两小时)
    ymp.configs.module.oauth.access_token_expire_in=
    
    # 缓存名称前缀, 默认值: ""
    ymp.configs.module.oauth.cache_name_prefix=
    
    # Token生成器接口实现, 默认值: net.ymate.module.oauth.impl.DefaultTokenGenerator
    ymp.configs.module.oauth.token_generator_class=
    
    # 用户身份信息适配器接口实现, 默认值: 空
    ymp.configs.module.oauth.userinfo_adapter_class=
    
    # OAuth令牌存储适配器接口实现, 默认值: 空
    ymp.configs.module.oauth.oauth_storage_class=

#### One More Thing

YMP不仅提供便捷的Web及其它Java项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入 官方QQ群480374360，一起交流学习，帮助YMP成长！

了解更多有关YMP框架的内容，请访问官网：http://www.ymate.net/