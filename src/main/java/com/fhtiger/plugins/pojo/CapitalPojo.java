package com.fhtiger.plugins.pojo;

import org.jetbrains.annotations.NotNull;

/**
 * CapitalPojo
 *
 * @author LFH
 * @since 2021年01月10日 21:33
 */
@SuppressWarnings({ "unused" })
public class CapitalPojo implements HandlerPojo{

	@Override
	public String transfer(String selectedText) {
		String[] splitStr = HandlerPojo.getSplitString(selectedText);
		String result;
		StringBuilder resultBuilder = new StringBuilder();
		int strIndex;
		if(splitStr.length>0){
			result = selectedText;
			for (String str : splitStr) {
				int strLength = str.length();
				if(strLength<1){
					continue;
				}
				strIndex = result.indexOf(str);
				//将result中匹配字符串前面部分和当前部分处理后的结果存入结果字符串构造中
				resultBuilder.append(result, 0, strIndex).append(capitalFirstChar(selectedText));
				//将result中已经存入resultBuilder中的部分移除.
				result = result.substring(strIndex+str.length());
			}
			result = resultBuilder.toString();
		}else{
			result = capitalFirstChar(selectedText);
		}
		return result;
	}

	@NotNull
	private static String capitalFirstChar(String selectedText) {
		return Character.toUpperCase(selectedText.charAt(0)) + selectedText.substring(1);
	}
}
