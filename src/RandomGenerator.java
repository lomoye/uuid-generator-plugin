/**
 * The MIT License (MIT)
 * 
 *  Copyright (c) 2015 Léo Millon
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import java.util.UUID;

/**
 * A random UUID generator action.
 *
 * @author Léo Millon
 */
public class RandomGenerator extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);

        if (project == null || editor == null) {
            return;
        }

        //New instance of Runnable to make a replacement
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (Caret caret : editor.getCaretModel().getAllCarets()) {
                    insertTextAtCaret(caret, UUID.randomUUID().toString());
                }
            }
        };
        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, runnable);
    }

    private static void insertTextAtCaret(Caret caret, CharSequence text) {
        int textLength = text.length();
        int start;
        Document document = caret.getEditor().getDocument();
        if (caret.hasSelection()) {
            start = caret.getSelectionStart();
            int end = caret.getSelectionEnd();

            document.replaceString(start, end, text);
            caret.setSelection(start, start + textLength);
        } else {
            start = caret.getOffset();

            document.insertString(start, text);
        }
        caret.moveToOffset(start + textLength);
    }
}
