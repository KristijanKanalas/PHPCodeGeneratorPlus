package PHPCodeGenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;


public class GetSettersActionClass extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        GenerateMethodsService.generateMethods(event, "set");
    }
}
