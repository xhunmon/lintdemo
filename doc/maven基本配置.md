### 一、不包含第三方依赖的打包

*1. Android Studio 打开右侧的 Gradle 面板，选择需要打包的module —> Tasks —> build, 双击 assemble*

*2. Build Successed 之后，将会在module的build/outputs/aar目录下生成 debug 和 release 两个版本的 aar包*

*3. 先将刚才生成的aar拷贝到 app module 的 libs目录下*

*4. 在打开app module 的 build.gradle，添加一下代码：*


```
repositories  {
    flatDir{ dirs 'libs'  }
}

implementation(name: 'xxxx-debug', ext: 'aar')
```

### 二、包含第三方依赖的打包

*如果library中引用了第三方的依赖包，再采用上面的方式，编译时将会提示说一些第三方的类文件找不到了。所以此时需要将library打成本地或远程的aar仓库，然后在项目中引用；*

*1. 打开 library 的 build.gradle 文件，在最外层加入如下内容*


```
apply plugin: 'maven'

// 省略其他配置

uploadArchives{
    repositories {
        mavenDeployer {
             // Ubuntu本地仓库路径， Windows 为(url:"file://D://***/***/***/")
            repository(url:"file:/Users/***/Desktop/")

            pom.project {
                // 版本号
                version = "0.0.1-SNAPSHOT"
                // 项目名称
                artifactId = "wspeer"
                // 唯一标识
                groupId = "io.dcsgo.wspeer"
                // 生成类型
                packaging = "aar"
                // 描述
                description = "wspeer"
            }
        }
    }
}
```

*2. 同样的，打开Android Studio右侧的Gradle面板，双击 module下面的 Tasks/upload/uploadArchives*

*3. 在app module中引用aar，首先在根目录的 build.gradle 的仓库配置中加入本地仓库地址，然后在app module 的bulid.gradle 中添加 项目依赖*


```
// 添加到 root 的 build.gradle
maven{
    url 'file:/Users/***/Desktop'
}

// 添加到 app 的 build.gradle，注意名字规则和上面配置本地仓库之间的关联
implementation 'lintlib:lintlib:0.0.1-SNAPSHOT'
```

### as私服maven配置

```
apply plugin: 'com.android.library'
apply plugin: 'maven'
def NEXUS_REPOSITORY_SNAPSHOTS = "http://***/repository/maven-snapshots/"
def NEXUS_REPOSITORY_RELEASES = "http://***/repository/maven-releases/"

def POM_GROUP_ID = "lintlib"
def NEXUS_USERNAME = "admin"
def NEXUS_PASSWORD = "***"
def POM_VERSION = "0.0.1-SNAPSHOT"
def POM_ARTIFACT_ID = "lintlib"
def POM_PACKAGING = "aar"
def POM_DESCRIPTION = "lintlib"

android {
   //省略
}

dependencies {
    //省略
}

//上传到maven私服
uploadArchives {
    repositories {
        mavenDeployer {
            snapshotRepository(url: NEXUS_REPOSITORY_SNAPSHOTS) {
                authentication(userName: NEXUS_USERNAME, password: NEXUS_PASSWORD)
            }

            repository(url: NEXUS_REPOSITORY_RELEASES) {
                authentication(userName: NEXUS_USERNAME, password: NEXUS_PASSWORD)
            }

            pom.project {
                version POM_VERSION
                artifactId POM_ARTIFACT_ID
                groupId POM_GROUP_ID
                packaging POM_PACKAGING
                description POM_DESCRIPTION
            }
        }
    }
}
```
