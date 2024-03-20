├─.idea
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─vueblog
│  │  │          ├─common
│  │  │          │  ├─exception---各种异常处理（简单）
│  │  │          │  └─lang----|Result统一结果封装，规定给前端一定的统一数据形式404
│  │  │          ├─config----|MybatisPlusConfig开启接口扫描，添加分页插件；|shiroconfig，过滤验证jwt，和验证等一系列功能，既进行过滤作用、会话管理和安全管理以及调用shiro
│  │  │          ├─controller---控制器，接受参数，并调用相关服务，然后传给前端数据
│  │  │          ├─entity---用来存放对象属性的方法，如set，get以获得用户信息
│  │  │          ├─mapper
│  │  │          ├─service
│  │  │          │  └─impl
│  │  │          ├─shiro----|AccountRealm用户登录逻辑 实现用户登录（认证，授权，加密等），也是实现jwt获取的地方并验证功能
│  │  │          └─util----后端的实用工具类（工具集合），生成token的接口并定义过期市场，过期验证，和数据返回方法
│  │  └─resources
│  │      ├─mapper
│  │      ├─META-INF
│  │      ├─static
│  │      └─templates
|  |      |-application ---- 配置文件以及后端访问秘钥

pom.xml ------ 配置文件和依赖包

项目依赖简介：

项目以springboot作为基础框架，使用持久层框架Mybatis-plus作为与数据库交互的技术
权限认证使用shiro+jwt+redis，shiro较security更加轻量级，适合小型项目，jwt生成用户权限token，redis作为用户认证缓存，提高访问速度
lombok和hutool作为工具类，简化代码，提高开发效率
hibernate validator作为后端参数校验工具

项目大致流程：
1. 导入以上所说的各种依赖项
2. 配置application.yml文件，配置链接mysql数据库，本地redis，端口号等
3. 开启mapper接口扫描，用mybatis-plus添加分页插件
4. 创建数据库表，使用mybatis-plus自动生成代码：mapper接口，service接口，service实现类，entity实体类，controller类，省去大量不必要时间
5. jwtFilter类：实现jwtToken的生成，验证，是否超过过期时间等
6. 创建shiroconfig类，配置shiro的各种过滤器，验证器，加密器，会话管理器等，实现和redis的链接，使所有的路由都需要经过JwtFilter这个过滤器，然后判断请求头中是否含有jwt的信息，有就登录，没有就跳过。跳过之后也可以通过Controller中的shiro注解进行再次拦截，这样就能控制权限了
6. 创建AccountRealm类，使得reaml支持jwt的校验，并且实现权限校验，登录验证，
7. 全局异常处理类，实现对异常的捕获，然后返回给前端统一的数据格式
8. 实体校验类，使用hibernate validator，实现对后端参数的校验，返回给前端统一的数据格式
9. 登录接口和博客接口实现（两个controller类）
10.  控制层(controller-action)，业务层/服务层( bo-manager-service)，实体层(po-entity)，dao(dao)，视图对象(Vo-)，视图层(view-jsp/html)

