package cc.unitmesh.devti.gui.chat

import cc.unitmesh.devti.prompting.VcsPrompting
import com.intellij.openapi.components.service
import com.intellij.openapi.project.ProjectManager

enum class ChatActionType {
    CHAT,
    REFACTOR,
    EXPLAIN,
    CODE_COMPLETE,
    GENERATE_TEST,
    GENERATE_TEST_DATA,
    GEN_COMMIT_MESSAGE,
    FIX_ISSUE,
    CREATE_CHANGELOG,
    CREATE_GENIUS,
    CUSTOM_COMPLETE,
    CUSTOM_ACTION,
    COUNIT,
    CODE_REVIEW
    ;

    override fun toString(): String {
        return instruction()
    }

    private fun prepareVcsContext(): String {
        val project = ProjectManager.getInstance().openProjects.firstOrNull() ?: return ""
        val prompting = project.service<VcsPrompting>()

        return prompting.prepareContext()
    }

    private fun generateCommitMessage(diff: String): String {
        return """Write a cohesive yet descriptive commit message for a given diff. 
Make sure to include both information What was changed and Why.
Start with a short sentence in imperative form, no more than 50 characters long.
Then leave an empty line and continue with a more detailed explanation, if necessary.
Explanation should have less than 200 characters.

examples:
- fix(authentication): add password regex pattern
- feat(storage): add new test cases

Diff:

```diff
$diff
```

"""
    }

    fun instruction(lang: String = ""): String {
        return when (this) {
            EXPLAIN -> "请给出说用，解释选中的$lang"+"代码"
//            REFACTOR -> "重构选中的$lang"+"代码"
            REFACTOR -> "作为一名资深$lang" + "代码开发工程师，发现代码中的错误并给出修复建议，如果没有明显错误，则回复无错误"
            CODE_COMPLETE -> "Complete $lang code, return rest code, no explaining"
            GENERATE_TEST -> "作为一名资深$lang"+"代码开发工程师，使用Spring和Junit框架，为每个public方法生成单元测试，" +
                    "测试用例需要对输入参数的有效性进行验证，确保系统能正确处理各种异常情况，同时确保代码的完整性、规范性、安全健壮性，能够正常运行。"
            FIX_ISSUE -> "Help me fix this issue"
            GEN_COMMIT_MESSAGE -> generateCommitMessage(prepareVcsContext())
            CREATE_CHANGELOG -> "generate release note"
            CHAT -> ""
            CUSTOM_COMPLETE -> ""
            CUSTOM_ACTION -> ""
            COUNIT -> ""
            CODE_REVIEW -> ""
            CREATE_GENIUS -> ""
            GENERATE_TEST_DATA -> "Generate JSON for given $lang code. So that we can use it to test for APIs. \n" +
                    "Make sure JSON contains real business logic, not just data structure. \n" +
                    "For example, if the code is a function that returns a list of users, " +
                    "the JSON should contain a list of users, not just a list of user objects."
        }
    }
}
