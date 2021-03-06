package com.fhtiger.plugins.humptransfern;

import com.fhtiger.plugins.pojo.CapitalPojo;
import com.fhtiger.plugins.pojo.HandlerPojo;
import com.fhtiger.plugins.pojo.HumpUnderlinePojo;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Code2HumpOrUnderLine
 * @version 0.1.3
 * @author LFH
 * @since 2019年08月15日 16:42
 */
public abstract class CodeTransferHandler extends AnAction {

	private final static JBColor JBColorDefine = new JBColor(new Color(186, 238, 186), new Color(73, 117, 73));

	/**
	 * 事件触发
	 * @param anActionEvent -
	 */
	@Override abstract public void actionPerformed(@NotNull AnActionEvent anActionEvent);

	/**
	 * 选中文本后才可以使用
	 * @param anActionEvent 状态变化控制
	 */
	@Override
	public void update(@NotNull AnActionEvent anActionEvent) {
		boolean b=false;
		final Editor mEditor = anActionEvent.getData(PlatformDataKeys.EDITOR);
		if (null != mEditor) {
			SelectionModel model = mEditor.getSelectionModel();
			final String selectedText = model.getSelectedText();
			b=!TextUtils.isEmpty(selectedText);
		}
		anActionEvent.getPresentation().setEnabledAndVisible(b);
	}

	/**
	 * 修改为支持批量转换的操作 2019/8/17 14:11
	 * @param anActionEvent 事件
	 */
	private void transfer(@NotNull AnActionEvent anActionEvent, HandlerPojo handlerPojo){
		final Editor mEditor = anActionEvent.getData(PlatformDataKeys.EDITOR);
		if (null == mEditor) {
			return;
		}
		SelectionModel model = mEditor.getSelectionModel();
		final String selectedText = model.getSelectedText();
		if (TextUtils.isEmpty(selectedText)) {
			return;
		}

		String result=handlerPojo.transfer(selectedText);

		// Work off of the primary caret to get the selection info
		Caret primaryCaret = mEditor.getCaretModel().getPrimaryCaret();
		int start = primaryCaret.getSelectionStart();
		int end = primaryCaret.getSelectionEnd();
		final Document document = mEditor.getDocument();
		Project theProject = anActionEvent.getProject();
		// Replace the selection with a fixed string.
		// Must do this document change in a write action context.

		/*基本的修改调用,但是调用后在Undo和Redo中没有注册明确的指令,所以会出现Undo Undefined这种问题*/
		/*WriteCommandAction.runWriteCommandAction(theProject, () ->
				document.replaceString(start, end, resultText)
		);*/

		/*使用CommandProcessor.getInstance().executeCommand(),在修改操作后注册Undo及Redo,其中HumpTransfer就是undo和redo后显示的操作名*/
		final String resultText = result;
		ApplicationManager.getApplication().runWriteAction(()->{
			CommandProcessor.getInstance().executeCommand(theProject, ()->{
				document.replaceString(start, end, resultText);
			}, anActionEvent.getPresentation().getText(), ActionGroup.EMPTY_GROUP);
			//anActionEvent.getPresentation() 就是当前action对象
		});

		// De-select the text range that was just replaced
		//primaryCaret.removeSelection();

		showPopupBalloon(mEditor,"原:"+selectedText);
	}

	protected void transfer2hump(@NotNull AnActionEvent anActionEvent,boolean smallCaml){
		transfer(anActionEvent,new HumpUnderlinePojo(true,smallCaml,false));
	}

	protected void transfer2hump(@NotNull AnActionEvent anActionEvent){
		transfer2hump(anActionEvent,true);
	}

	protected void transfer2underline(@NotNull AnActionEvent anActionEvent,boolean uppercase){
		transfer(anActionEvent, new HumpUnderlinePojo(false,true,uppercase));
	}

	protected void transfer2underline(@NotNull AnActionEvent anActionEvent){
		transfer2underline(anActionEvent,false);
	}

	protected void capitalFirstChar(@NotNull AnActionEvent anActionEven){
		transfer(anActionEven,new CapitalPojo());
	}

	/**
	 * 在小窗口显示内容出来
	 * @param editor 编辑器
	 * @param result 结果
	 */
	private void showPopupBalloon(final Editor editor, final String result) {
		ApplicationManager.getApplication().invokeLater(() -> {
			JBPopupFactory factory = JBPopupFactory.getInstance();
			factory.createHtmlTextBalloonBuilder(result, null, JBColorDefine, null)
					.setFadeoutTime(5000)
					.createBalloon()
					.show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
		});
	}
}
