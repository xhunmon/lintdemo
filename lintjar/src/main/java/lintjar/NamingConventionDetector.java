package lintjar;

import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.sun.istack.NotNull;

import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Description:
 * Detector:
 *      查找指定的Issue，一个Issue对应一个Detector。自定义Lint 规则的过程也就是重写Detector类相关方法的过程。具体看下小结实践。
 * Scanner:
 *      扫描并发现代码中的Issue,Detector需要实现Scaner,可以继承一个到多个。
 *  Scanner类型：
 *      UastScanner 扫描Java文件和Kotlin文件
 *      ClassScanner 扫描Class文件
 *      XmlScanner 扫描Xml文件
 *      ResourceFolderScanner 扫描资源文件夹
 *      BinaryResourceScanner 扫描二进制资源文件
 *      OtherFileScanner 扫描其他文件
 *      GradleScanner 扫描Gradle脚本
 * <br>
 * Author: cxh <br>
 * Date: 2020-01-09
 */
public class NamingConventionDetector extends Detector implements Detector.UastScanner {

    /**
     * 定义命名规范规则
     *
     * 第一个参数id 唯一的id,简要表面当前提示的问题。
     * 第二个参数briefDescription 简单描述当前问题
     * 第三个参数explanation 详细解释当前问题和修复建议
     * 第四个参数category 问题类别，例如上文讲到的Security、Usability等等。
     * 第五个参数priority 优先级，从1到10，10最重要
     * 第六个参数Severity 严重程度：FATAL（奔溃）, ERROR（错误）, WARNING（警告）,INFORMATIONAL（信息性）,IGNORE（可忽略）
     * 第七个参数Implementation Issue和哪个Detector绑定，以及声明检查的范围。Scope有如下选择范围：
     * RESOURCE_FILE（资源文件),BINARY_RESOURCE_FILE（二进制资源文件）,RESOURCE_FOLDER（资源文件夹）,ALL_RESOURCE_FILES（所有资源文件）,JAVA_FILE（Java文件）, ALL_JAVA_FILES（所有Java文件）,CLASS_FILE（class文件）, ALL_CLASS_FILES（所有class文件）,MANIFEST（配置清单文件）, PROGUARD_FILE（混淆文件）,JAVA_LIBRARIES（Java库）, GRADLE_FILE（Gradle文件）,PROPERTY_FILE(属性文件),TEST_SOURCES（测试资源）,OTHER(其他);
     */
    public static final Issue ISSUE = Issue.create("NamingConventionWarning",
            "你的命名规范错误",
            "你的要使用驼峰命名法，方法命名开头小写",
            Category.USABILITY,
            5,
            Severity.WARNING,
            new Implementation(NamingConventionDetector.class,
                    EnumSet.of(Scope.JAVA_FILE)));

    //返回我们所有感兴趣的类，即返回的类都被会检查
    @Nullable
    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    //重写该方法，创建自己的处理器
    @Nullable
    @Override
    public UElementHandler createUastHandler(@NotNull final JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitClass(@NotNull UClass node) {
                node.accept(new NamingConventionVisitor(context, node));
            }
        };
    }

    //定义一个继承自AbstractUastVisitor的访问器，用来处理感兴趣的问题
    public static class NamingConventionVisitor extends AbstractUastVisitor {

        JavaContext context;

        UClass uClass;

        public NamingConventionVisitor(JavaContext context, UClass uClass) {
            this.context = context;
            this.uClass = uClass;
        }

        @Override
        public boolean visitClass(@org.jetbrains.annotations.NotNull UClass node) {
            //获取当前类名
            char beginChar = node.getName().charAt(0);
            int code = beginChar;
            //如果类名不是大写字母，则触碰Issue，lint工具提示问题
            if (97 < code && code < 122) {
                context.report(ISSUE,context.getNameLocation(node),
                        "for the  name of class must start with uppercase:" + node.getName());
                //返回true表示触碰规则，lint提示该问题；false则不触碰
                return true;
            }

            return super.visitClass(node);
        }

        @Override
        public boolean visitMethod(@NotNull UMethod node) {
            //当前方法不是构造方法
            if (!node.isConstructor()) {
                char beginChar = node.getName().charAt(0);
                int code = beginChar;
                //当前方法首字母是大写字母，则报Issue
                if (65 < code && code < 90) {
                    context.report(ISSUE, context.getLocation(node),
                            "for the method must start with lowercase:" + node.getName());
                    //返回true表示触碰规则，lint提示该问题；false则不触碰
                    return true;
                }
            }
            return super.visitMethod(node);

        }

    }
}