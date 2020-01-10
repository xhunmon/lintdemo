package lintjar;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;
import com.sun.istack.NotNull;

import org.jetbrains.uast.UCallExpression;

import java.util.Collections;
import java.util.List;

/**
 * Description: <br>
 * Author: cxh <br>
 * Date: 2020-01-09
 */
public class NewThreadDetector extends Detector implements Detector.UastScanner {

    public static final Issue ISSUE = Issue.create(
            "NewThread",
            "不要自己创建Thread",
            "请勿直接调用new Thread()，建议使用统一的线程管理工具类--",
            Category.PERFORMANCE, 5, Severity.ERROR,
            new Implementation(NewThreadDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableConstructorTypes() {
        return Collections.singletonList("java.lang.Thread");
    }

    @Override
    public void visitConstructor(@NotNull JavaContext context,
                                 @NotNull UCallExpression node,
                                 @NotNull PsiMethod constructor) {
        context.report(ISSUE, node, context.getLocation(node),
                "避免自己创建Thread");
    }
}