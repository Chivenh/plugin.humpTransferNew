package com.fhtiger.plugins.pojo;

/**
 * HanderPojo
 *
 * @author LFH
 * @since 2021年01月10日 21:34
 */
@SuppressWarnings({ "unused" })
public interface HandlerPojo {

	 static String[] getSplitString(String selectedText){
		 String splitRegex = "(\\s+|,|;|/)";

		 String[] splitStr = selectedText.split(splitRegex);

		 return splitStr;
	}

	String transfer(String selectedText);
}
