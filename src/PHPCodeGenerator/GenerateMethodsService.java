package PHPCodeGenerator;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.util.SmartList;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.impl.ClassReferenceImpl;
import com.jetbrains.php.lang.psi.elements.impl.PhpClassImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

class GenerateMethodsService {

    static void generateMethods(AnActionEvent event, String typeOfMethod) {
        final Project project = event.getData(PlatformDataKeys.PROJECT);
        final Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {
            final CaretModel caret = editor.getCaretModel();
            PsiFile file = event.getData(LangDataKeys.PSI_FILE);
            if (file != null) {
                PsiElement selectedElement = file.findElementAt(caret.getOffset());
                if (selectedElement != null && selectedElement.getParent().getReference() != null) {
                    PsiElement variableDeclaration = selectedElement.getParent().getReference().resolve();
                    if (variableDeclaration != null) {
                        PsiElement assignmentExpression = variableDeclaration.getContext();
                        if (assignmentExpression != null) {
                            final Collection<PsiElement> selectedElementClass = new SmartList<>();
                            assignmentExpression.accept(new PsiRecursiveElementVisitor() {
                                @Override
                                public void visitElement(@NotNull PsiElement element) {
                                    if (element instanceof ClassReferenceImpl) {
                                        if (element.getReference() != null) {
                                            selectedElementClass.add(element.getReference().resolve());
                                        }
                                    }
                                    super.visitElement(element);
                                }
                            });

                            Document document = editor.getDocument();
                            boolean firstMethodGeneration = true;
                            for (PsiElement initClass : selectedElementClass) {
                                PhpClassImpl phpClass = (PhpClassImpl) initClass;
                                for (Method method : phpClass.getMethods()) {
                                    String methodName = method.getName();

                                    // If method name has "set" on position 0 add it to editor
                                    if (methodName.indexOf(typeOfMethod) == 0) {
                                        boolean finalFirstSetter = firstMethodGeneration;
                                        WriteCommandAction.runWriteCommandAction(project, () -> {
                                                    if (finalFirstSetter) {
                                                        caret.moveToOffset(caret.getVisualLineEnd());
                                                        document.insertString(caret.getOffset(), "\n");
                                                    }
                                                    caret.moveToOffset(caret.getVisualLineEnd());
                                                    document.insertString(caret.getOffset(), selectedElement.getText()+"->" + methodName + "();\n");
                                                }
                                        );
                                    }
                                    firstMethodGeneration = false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
