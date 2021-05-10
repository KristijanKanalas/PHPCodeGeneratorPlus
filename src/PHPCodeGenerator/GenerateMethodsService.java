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
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.FieldImpl;
import com.jetbrains.php.lang.psi.elements.impl.VariableImpl;

import java.util.Collection;

class GenerateMethodsService {

    static void generateMethods(AnActionEvent event, String typeOfMethod) {
        final Project project = event.getData(PlatformDataKeys.PROJECT);
        final Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (editor != null && project != null) {
            final CaretModel caret = editor.getCaretModel();
            PsiFile file = event.getData(LangDataKeys.PSI_FILE);
            if (file != null) {
                PsiElement selectedElement = file.findElementAt(caret.getOffset());
                if (selectedElement != null && selectedElement.getParent().getReference() != null) {

                    PhpNamedElement variable = null;
                    if (selectedElement.getParent().getReference().resolve() instanceof FieldImpl) {
                        variable = (FieldImpl) selectedElement.getParent().getReference().resolve();
                    }

                    if (selectedElement.getParent().getReference().resolve() instanceof VariableImpl) {
                        variable = (VariableImpl) selectedElement.getParent().getReference().resolve();
                    }

                    if(variable != null) {
                        Collection<PhpClass> qualifiedClasses = PhpIndex.getInstance(project).getClassesByFQN(variable.getType().toString());
                        PhpClass phpClass = qualifiedClasses.iterator().next();
//                    Method myMethod = myClass.findMethodByName("setMake");
//                    System.out.println(myMethod.getDocType()); this one good

                        writeMethods(editor.getDocument(), phpClass.getMethods(), typeOfMethod, project, caret, selectedElement);
                    }



                }
            }
        }
    }

    private static void writeMethods(Document document, Collection<Method> methods, String typeOfMethod,
                                     Project project, CaretModel caret, PsiElement selectedElement) {

        boolean firstMethodGeneration = true;

        for (Method method : methods) {
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
