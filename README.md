# lintdemo
android lint  自定义文档整理


- [详细参考google官网地址](https://developer.android.google.cn/studio/write/lint?hl=zh_cn#commandline)

- 配置Lint（方式1）：在项目的根目录创建lint.xml文件。格式如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<lint>
        <!-- list of issues to configure -->
</lint>
```

- 配置Lint（方式2）：使用模块级 build.gradle 文件中的 lintOptions {} 块配置某些 Lint 选项，例如要运行或忽略哪些检查。如下：

```
android {
  ...
  lintOptions {
   ..
  }
}
    ...
```



- 那么有哪些Issues（规则）呢？

```
Security 安全性。在AndroidManifest.xml中没有配置相关权限等。
Usability 易用性。重复图标；上文开始黄色警告也属于该规则等。
Performance 性能。内存泄漏，xml结构冗余等。
Correctness 正确性。超版本调用API，设置不正确的属性值等。
Accessibility 无障碍。单词拼写错误等。
Internationalization国际化。字符串缺少翻译等。
```

- 自定义规则

```
1. java形式lintjar
    ——src
    ————main
    ——————java
    ————————lintjar
    ——————————NamingConventionDetector.java //定义ISSUE
    ——————————Register.java     //注册NamingConventionDetector
    ——build.gradle      //配置lint-api和lint-checks插件，添加Register.java注册

2. aar形式lintlib，以lintjar为基础
    ——src
    ————main
    ——————java
    ————————res
    ——build.gradle      //依赖lintjar
```

- 自带ISSUE请看doc/文档.md，结合maven私服做成基类sdk，请配合：doc/maven基本配置.md
