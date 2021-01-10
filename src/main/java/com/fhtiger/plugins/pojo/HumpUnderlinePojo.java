package com.fhtiger.plugins.pojo;

import com.fhtiger.plugins.humptransfern.HumpTransferUtil;

/**
 * HumpUnderlinePojo
 * @param toHump 是否转驼峰,非则转下划线
 * 	 @param smallCaml    转驼峰时smallCaml参数
 * 	  @param uppercase   转下划线时upperCase参数
 * @author LFH
 * @since 2021年01月10日 21:33
 */
@SuppressWarnings({ "unused" })
public class HumpUnderlinePojo implements HandlerPojo{

	/**
	 * 是否转驼峰,非则转下划线
	 */
	private final boolean toHump;
	/**
	 * 转驼峰时smallCaml参数
	 */
	private final boolean smallCaml;
	/**
	 * 转下划线时upperCase参数
	 */
	private final boolean uppercase;

	public HumpUnderlinePojo(boolean toHump, boolean smallCaml, boolean uppercase) {
		this.toHump = toHump;
		this.smallCaml = smallCaml;
		this.uppercase = uppercase;
	}

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
				resultBuilder.append(result, 0, strIndex).append(this.toHump?  HumpTransferUtil.transfer2hump(str,
						this.smallCaml): HumpTransferUtil.transfer2underline(str,this.uppercase));
				//将result中已经存入resultBuilder中的部分移除.
				result = result.substring(strIndex+str.length());
			}
			result = resultBuilder.toString();
		}else{
			result = this.toHump?  HumpTransferUtil.transfer2hump(selectedText,this.smallCaml):
					HumpTransferUtil.transfer2underline(selectedText,this.uppercase);
		}
		return result;
	}
}
