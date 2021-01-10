package com.fhtiger.plugins.humptransfern;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * CodeCapitalFirstCharAction
 *
 * @author LFH
 * @since 2021年01月10日 21:54
 */
@SuppressWarnings({ "unused" })
public class CodeCapitalFirstCharAction  extends CodeTransferHandler{

	@Override
	public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
		capitalFirstChar(anActionEvent);
	}
}
