package com.digitzones.util;
import org.apache.commons.lang.StringUtils;
/**
 * 字符串工具类
 */
public class StringUtil {
	/**
	 * 去掉原字符串中指定字符串
	 * @param resource 原字符串
	 * @param removedString 去掉的字符串
	 * @return 去掉removedString后的字符串
	 */
	public static String remove(String resource,String removedString) {
		if(resource==null) {
			return null;
		}
		return resource.replace(removedString,"");
	}
	/**
	 * 字符串自增
	 * @param src
	 * @return
	 */
	public static String increment(String src){
		//字符串只有一个字符
		if(StringUtils.isEmpty(src) || "".equals(src)){
			return "";
		}
		char lastCharacter = src.charAt(src.length()-1);
		String subString = src.substring(0,src.length()-1);
		String result = "";
		if(lastCharacter=='9'){
			result = increment(subString) + '0';
			if(result.equals("0")){
				return "10";
			}
			return result;
		}
		if(lastCharacter=='z'){
			result = increment(subString) + 'a';
			if(result.equals("a")){
				return "aa";
			}
			return result;
		}
		if(lastCharacter=='Z'){
			result = increment(subString) + 'A';
			if(result.equals("A")){
				return "AA";
			}
			return result;
		}
		return subString + (++lastCharacter);
	}

	public static void main(String[] args) {
		System.out.println(increment("RKSQ-20190701001"));
	}
}
