## idea 打开cms\backend\cms
Java 17
这是后端 然后maven 打包
## vscode 打开cms\frontend\admin
nodejs 18 
这是后台 
打包用 pnpm i
pnpm  run dev 
blog 是前台 运行方式跟后台一样


技术介绍
前台前端（博客）： Vue3 + Pinia + Vue Router + TypeScript + Axios + Element Plus + Echarts……

后台启动（管理）： Vue3 + Pinia + Vue Router + TypeScript + Axios + Antdv Pro + Ant Design Vue  antv/g2plot

后端： JDK17 + SpringBoot3 + SpringSecurity + Mysql + Redis + Quartz + RabbitMQ + Minio + Mybatis-Plus

### 后端部分（Java）
1. **黑名单管理功能**
    - **添加黑名单**：支持添加用户或机器人到黑名单中。在添加用户时，会检查用户是否已在黑名单中，若存在则添加失败。同时会保存相关信息，包括用户ID（如果有）、原因、类型（用户或机器人）、过期时间等，并且对于机器人类型的黑名单，还会异步刷新IP详细信息。
    - **查询黑名单**：支持根据用户名、原因、类型、开始时间和结束时间等条件进行搜索查询，返回符合条件的黑名单列表，并包含用户名称信息。
    - **更新黑名单**：可以更新黑名单的原因和过期时间等信息，同时更新相关缓存。
    - **删除黑名单**：可以批量删除黑名单，并清除对应的缓存信息。
2. **系统信息管理**：定义了系统相关信息的实体类`Sys`，包含服务器名称、IP、项目路径、操作系统、系统架构等信息。
3. **文章模块** ：增删改查 存储MySQL数据库
4. **视频模块** ：增删改查 存储minio OSS服务
5. **可视化大屏幕** ： 使用antv/g2plot  后台展示 资源统计 在线人数统计用户访问数据
  - 页面访问量排名：列出访问量最高的页面，以排行榜形式展示，帮助确定哪些内容最受用户欢迎。
  - 用户地域分布：通过地图可视化用户的地域来源，了解不同地区用户对 CMS 内容的访问情况。
  - 用户互动数据
  - 评论和点赞数：展示文章、视频等内容的评论数和点赞数，以柱状图或折线图呈现，分析用户对内容的参与度和反馈情况。
  - 用户留存率：以折线图展示不同时间段内用户的留存率，帮助评估用户对 CMS 的粘性。
  3. 系统性能指标
  服务器性能
  服务器负载：以仪表盘形式展示服务器的 CPU 使用率、内存使用率、磁盘 I/O 等指标，实时监控服务器的运行状态。
  响应时间：以折线图展示页面的平均响应时间，帮助发现性能瓶颈。
  数据库指标
  数据库读写量：以柱状图展示数据库的读写操作次数，了解数据库的使用情况。
  数据库连接数：实时显示数据库的连接数，确保数据库的正常运行。
  4. 运营管理数据
  内容审核情况
  待审核内容数量：以数字卡片形式展示待审核的文章、图片、视频等内容的数量，提醒运营人员及时处理。
  审核通过率：以柱状图或折线图展示不同时间段内内容的审核通过率，评估审核标准的执行情况。
  用户管理数据
  用户注册量：以折线图展示用户注册数量的变化趋势，分析用户增长情况。
  用户活跃度：通过热力图或柱状图展示用户的登录频率和在线时长，了解用户的活跃程度。

6. **RBAC权限管理**：项目中的 RBAC（基于角色的访问控制，Role-Based Access Control）权限管理主要涵盖了角色、权限以及它们之间关系的管理

### 前端部分（Vue）
1. **组件和插件管理**
    - 引入并注册了全局组件，方便在项目中使用。
    - 引入了SVG插件、路由、Pinia状态管理等。
    - 引入了全局样式文件，包括自定义样式、Tailwind CSS、Element Plus的样式等，并且处理了样式冲突问题。
2. **指令管理**：注册了全局指令，如`vSlideIn`、`vLazy`、`vViewRequest`，可在项目中使用这些指令来实现特定的交互效果。

### 项目构建和运行
1. **后端**：使用Java 17和Maven进行项目构建和打包。
2. **前端**：使用Node.js 18，通过`pnpm i`安装依赖，使用`pnpm run dev`进行开发环境的运行。前端项目分为后台（admin）和前台（blog），运行方式相同。

**后端目录结构**

   **cms后台展示目录结构**
admin
├─ public ## 静态资源文件夹
├─ scripts ## 工程脚本文件
├─ themes ## 主题文件夹
├─ servers ## nitro mock服务文件夹
├─ types ## 类型声明文件夹
├─ src ## 主项目文件夹
│  ├─ App.vue ## 组件入口
│  ├─ assets  ## 静态资源文件夹
│  ├─ components ## 组件文件夹会这里的组件会自动导入
│  ├─ composables ## 组合式api文件夹，默认会自动导入
│  ├─ config ## 配置文件夹
│  │  └─ default-setting.ts ## 主题配置文件
│  ├─ layouts ## 布局文件夹
│  ├─ main.ts ## 项目整体入口
│  ├─ pages ## 页面文件夹
│  ├─ router ## 路由配置文件
│  │  ├─ dynamic-routes.ts ## 动态路由文件夹，这里面配置的会同步生成菜单
│  │  ├─ generate-route.ts ## 生成动态路由结构
│  │  ├─ router-guard.ts ## 路由拦截
│  │  └─ static-routes.ts ## 静态路由
│  ├─ stores ## pinia配置文件夹，默认支持自动导入
│  └─ utils ## 工具函数
├─ .env ## 默认环境配置文件
├─ .env.development ## 开发环境配置文件
├─ .eslintignore ## eslint忽略文件
├─ .eslintrc ## eslint配置文件
├─ index.html
├─ tsconfig.json ## ts配置文件
├─ tsconfig.node.json ## vite.config.ts的ts配置
├─ package.json ## 依赖描述文件
├─ pnpm-lock.yaml ## pnpm包管理版本锁定文件
├─ unocss.config.ts ## unocss配置文件
├─ vercel.json ## 发布到vercel配置文件
└─ vite.config.ts ## vite配置文件
**前台展示目录结构**
blog
├─ public ## 静态资源文件夹，用于存放无需经过打包处理，可直接被访问的静态文件，如 HTML 模板文件、favicon 等
└─ src ## 主项目文件夹，包含项目的主要源代码
    ├─ apis ## 存放与后端接口交互的 API 请求相关代码
    │  ├─ article ## 文章相关的 API 请求代码
    │  ├─ category ## 分类相关的 API 请求代码
    │  ├─ email ## 邮件相关的 API 请求代码
    │  ├─ favorite ## 收藏相关的 API 请求代码
    │  ├─ home ## 首页相关的 API 请求代码
    │  ├─ leaveWord ## 留言相关的 API 请求代码
    │  ├─ like ## 点赞相关的 API 请求代码
    │  ├─ link ## 链接相关的 API 请求代码
    │  ├─ music ## 音乐相关的 API 请求代码
    │  ├─ photo ## 照片相关的 API 请求代码
    │  ├─ tag ## 标签相关的 API 请求代码
    │  ├─ thirdParty ## 第三方相关的 API 请求代码
    │  ├─ treeHole ## 树洞相关的 API 请求代码
    │  ├─ user ## 用户相关的 API 请求代码
    │  ├─ video ## 视频相关的 API 请求代码
    │  └─ website ## 网站相关的 API 请求代码
    ├─ assets ## 静态资源文件夹，存放项目中使用的各类静态资源
    │  ├─ cursor ## 光标样式相关的资源
    │  ├─ icons ## 图标相关的资源
    │  ├─ images ## 图片相关的资源
    │  │  └─ emoji ## 表情符号相关的图片资源
    │  │      └─ heo ## 特定的表情符号图片资源
    │  └─ woff ## 字体文件相关的资源
    ├─ components ## 组件文件夹，存放项目中可复用的组件
    │  ├─ Banner ## 横幅组件
    │  ├─ BottomRightLayout ## 右下角布局组件
    │  ├─ BottomRightMore ## 右下角更多操作组件
    │  ├─ Card ## 卡片组件相关文件夹
    │  │  └─ RandomArticle ## 随机文章卡片组件
    │  ├─ CardEssay ## 文章卡片组件
    │  ├─ CardInfo ## 信息卡片组件
    │  ├─ CardVideo ## 视频卡片组件
    │  ├─ Comment ## 评论组件
    │  ├─ DayNightToggle ## 日夜切换组件
    │  ├─ ElectronicClocks ## 电子时钟组件
    │  ├─ Fullscreen ## 全屏组件
    │  ├─ GoBottom ## 前往底部组件
    │  ├─ Layout ## 布局组件相关文件夹
    │  │  ├─ Footer ## 页脚组件
    │  │  ├─ Header ## 页眉组件
    │  │  │  ├─ Menu ## 菜单组件
    │  │  │  └─ MoveMenu ## 移动菜单组件
    │  │  ├─ Main ## 主体内容组件
    │  │  └─ SideBar ## 侧边栏组件
    │  │      └─ ChargingList ## 充电列表组件
    │  ├─ Loading ## 加载中组件
    │  ├─ Music ## 音乐组件相关文件夹
    │  │  ├─ controls ## 音乐控制相关组件文件夹
    │  │  │  └─ components ## 音乐控制子组件
    │  │  └─ list ## 音乐列表相关组件文件夹
    │  │      └─ components ## 音乐列表子组件
    │  ├─ Pagination ## 分页组件
    │  ├─ ReadingMode ## 阅读模式组件
    │  ├─ Search ## 搜索组件
    │  ├─ SseCount ## SSE 计数组件
    │  ├─ SvgIcon ## SVG 图标组件
    │  ├─ ToTop ## 返回顶部组件
    │  └─ Wave ## 波浪效果组件
    ├─ config ## 配置文件夹，存放项目的配置文件
    ├─ const ## 常量文件夹，存放项目中使用的常量
    │  └─ Jwt ## JWT 相关的常量
    ├─ directives ## 自定义指令文件夹，存放项目中使用的自定义指令
    ├─ router ## 路由配置文件夹，存放项目的路由配置代码
    ├─ store ## 状态管理文件夹，存放项目的状态管理代码
    │  └─ modules ## 状态管理模块文件夹
    ├─ styles ## 样式文件夹，存放项目的样式文件
    ├─ types ## 类型声明文件夹，存放项目中使用的类型声明文件
    ├─ utils ## 工具函数文件夹，存放项目中使用的工具函数
    │  ├─ base64-img ## 处理 base64 图片的工具函数
    │  └─ O.o ## 可能是特定的工具函数或模块
    └─ views ## 视图文件夹，存放项目的页面组件
        ├─ About ## 关于页面
        ├─ Amusement ## 娱乐相关页面文件夹
        │  ├─ Message ## 消息相关页面文件夹
        │  │  ├─ MessageDetail ## 消息详情页面
        │  │  └─ MessageList ## 消息列表页面
        │  └─ TreeHole ## 树洞页面
        ├─ Article ## 文章相关页面文件夹
        │  ├─ DirectoryCard ## 文章目录卡片页面
        │  └─ MobileDirectoryCard ## 移动端文章目录卡片页面
        ├─ Home ## 首页相关页面文件夹
        │  ├─ Brand ## 品牌相关页面
        │  ├─ Images ## 图片相关页面
        │  └─ Main ## 首页主体内容页面
        │      └─ RecommendArticle ## 推荐文章页面
        ├─ Layout ## 布局页面
        ├─ Link ## 链接页面
        ├─ Music ## 音乐页面
        ├─ Photo ## 照片页面
        │  └─ components ## 照片页面的子组件
        ├─ Pigeonhole ## 归档相关页面文件夹
        │  ├─ ArticleList ## 文章列表页面
        │  ├─ Category ## 分类页面
        │  ├─ Tags ## 标签页面
        │  └─ TimeLine ## 时间线页面
        ├─ Setting ## 设置页面
        ├─ Video ## 视频页面
        │  └─ watch ## 视频观看页面
        └─ Welcome ## 欢迎页面文件夹
            ├─ Login ## 登录页面
            ├─ Register ## 注册页面
            └─ Reset ## 重置页面
**springboot后端目录结构**
├─ src ## 项目源代码主目录
│  ├─ main ## 存放应用主要业务逻辑代码
│  │  ├─ java ## Java 代码目录
│  │  │  └─ com
│  │  │      └─ overthinker
│  │  │          └─ cloud
│  │  │              └─ web
│  │  │                  ├─ annotation ## 自定义注解目录
│  │  │                  ├─ aop ## 面向切面编程代码目录
│  │  │                  ├─ component ## 自定义组件目录
│  │  │                  ├─ config ## 配置类目录，含 MinIO 和 RabbitMQ 配置子目录
│  │  │                  ├─ controller ## 处理客户端请求的控制器目录，有基础控制器子目录
│  │  │                  ├─ entity ## 实体类及数据传输对象目录，含多种子类型目录
│  │  │                  ├─ exception ## 自定义异常类目录
│  │  │                  ├─ filter ## 过滤器目录
│  │  │                  ├─ handler ## 处理器目录
│  │  │                  ├─ interceptor ## 拦截器目录
│  │  │                  ├─ mapper ## 数据访问层映射器接口目录
│  │  │                  ├─ quartz ## Quartz 定时任务代码目录
│  │  │                  ├─ service ## 服务层接口目录，有实现类子目录
│  │  │                  ├─ utils ## 工具类目录
│  │  │                  └─ validators ## 数据验证器目录
│  │  └─ resources ## 资源文件目录
│  │      ├─ mapper ## MyBatis 映射文件目录
│  │      └─ templates ## 模板文件目录


