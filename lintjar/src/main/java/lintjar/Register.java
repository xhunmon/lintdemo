package lintjar;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.sun.istack.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Description: 注册自定义的Issue  <br>
 * Author: cxh <br>
 * Date: 2020-01-09
 */
public class Register extends IssueRegistry {
    /**
     * 用于注册要检查的Issue(规则)，只有注册了Issue,该Issue才能被使用。例如注册上文的命名规范规则。
     */
    @NotNull
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                NamingConventionDetector.ISSUE,
                NewThreadDetector.ISSUE
        );
    }
}
