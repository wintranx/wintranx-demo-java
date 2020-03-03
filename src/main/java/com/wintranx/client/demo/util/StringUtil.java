package com.wintranx.client.demo.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

	public static String decodeString(String str) {
		BASE64Decoder dec = new BASE64Decoder();
		try {
			return new String(dec.decodeBuffer(str));
		} catch (IOException io) {
			throw new RuntimeException(io.getMessage(), io.getCause());
		}
	}

	public static String encodePassword(String password, String algorithm) {
		if (algorithm == null)
			return password;
		byte unencodedPassword[] = password.getBytes();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			log.error((new StringBuilder()).append("Exception: ").append(e)
					.toString());
			return password;
		}
		md.reset();
		md.update(unencodedPassword);
		byte encodedPassword[] = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 16)
				buf.append("0");
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}

	public static String encodeString(String str) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encodeBuffer(str.getBytes()).trim();
	}

	public static StringBuilder format(String msgWithFormat, Object args[]) {
		return formatStr(new StringBuilder(msgWithFormat), true, args);
	}

	public static String format2(String format, Object args[]) {
		return String.format(format, args);
	}

	public static StringBuilder formatNoBracket(String msgWithFormat,
			Object args[]) {
		return formatStr(new StringBuilder(msgWithFormat), false, args);
	}

	private static StringBuilder formatStr(CharSequence msgWithFormat,
			boolean autoQuote, Object args[]) {
		StringBuilder sb = new StringBuilder(msgWithFormat);
		int argsLen = args.length;
		if (argsLen > 0) {
			boolean markFound = false;
			for (int i = 0; i < argsLen; i++) {
				String flag = (new StringBuilder()).append("%").append(i + 1)
						.toString();
				String tmp;
				for (int idx = sb.indexOf(flag); idx >= 0; idx = sb.indexOf(
						flag, idx + tmp.length())) {
					markFound = true;
					tmp = toString(args[i], autoQuote);
					sb.replace(idx, idx + 2, tmp);
				}

			}

			if (args[argsLen - 1] instanceof Throwable) {
				StringWriter sw = new StringWriter();
				((Throwable) args[argsLen - 1])
						.printStackTrace(new PrintWriter(sw));
				sb.append("\n").append(sw.toString());
			} else if (argsLen == 1 && !markFound)
				sb.append(args[argsLen - 1].toString());
		}
		return sb;
	}

	public static String getEncryptPassword(String password) {
		return encodePassword(password, "MD5");
	}
//	@SuppressWarnings("unused")
	public static boolean isNumber(String value) {
		Pattern pattern= Pattern.compile("[0-9]*");
        Matcher match=pattern.matcher(value);
        return match.matches();
	}

	public static boolean isMatchRegex(String value,String regex) {
		Pattern pattern= Pattern.compile(regex);
        Matcher match=pattern.matcher(value);
        return match.matches();
	}

	public static final String[] toArray(String str) {
		return toArrayByDel(str, ";");
	}

	public static final String[] toArrayByDel(String str, String del) {
		String array[] = null;
		if (str != null && str.length() > 0 && del != null && del.length() > 0) {
			StringTokenizer st = new StringTokenizer(str, del);
			if (st != null && st.countTokens() > 0) {
				array = new String[st.countTokens()];
				int i = 0;
				while (st.hasMoreTokens())
					array[i++] = st.nextToken();
			}
		}
		return array;
	}

	public static String toString(Object obj, boolean autoQuote) {
		StringBuilder sb = new StringBuilder();
		if (obj == null)
			sb.append("NULL");
		else if (obj instanceof Object[]) {
			for (int i = 0; i < ((Object[]) (Object[]) obj).length; i++)
				sb.append(((Object[]) (Object[]) obj)[i]).append(", ");

			if (sb.length() > 0)
				sb.delete(sb.length() - 2, sb.length());
		} else {
			sb.append(obj.toString());
		}
		if (autoQuote && sb.length() > 0
				&& (sb.charAt(0) != '[' || sb.charAt(sb.length() - 1) != ']')
				&& (sb.charAt(0) != '{' || sb.charAt(sb.length() - 1) != '}'))
			sb.insert(0, "[").append("]");
		return sb.toString();
	}
	
	/*
	 * HL add
	 */
	public static boolean isNotEmpty(String str) {
		if(str != null && !str.equals("")) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isNotEmpty(Integer num) {
		if(num != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isEmpty(String str) {
		if(str == null || str.equals("")) {
			return true;
		}else {
			return false;
		}
	}

	private StringUtil() {
	}

	private static final Log log = LogFactory.getLog(StringUtil.class);
	
	/**
	 * 判断一个字符串是否在list中
	 * 
	 * @param str
	 *            String
	 * @param list
	 *            List
	 * @return boolean
	 */
	public static boolean isInList(String str, List<String> list) {
		boolean ret = false;
		for (int i = 0; i < list.size(); i++) {
			String tmp_str = list.get(i);
			if (str.equals(tmp_str)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public static String[] split(String toSplit, String delimiter) {
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}
	
	public static String[] splitAll(String org_str, String find_str) {
		ArrayList<String> list = new ArrayList<String>();
		int index = 0;
		int nextIndex = org_str.indexOf(find_str);
		while (nextIndex != -1) {
			list.add(org_str.substring(index, nextIndex));
			index = nextIndex + find_str.length();
			nextIndex = org_str.indexOf(find_str, index);
		}
		String[] ret = new String[list.size() + 1];
		for (int i = 0; i < list.size(); i++) {
			ret[i] = (String) list.get(i);
		}
		// 最后一个
		ret[list.size()] = org_str.substring(index);
		return ret;
	}
	
	public static String getCurrencyMark(String currencyCode){
		String result="";
		/**
		if (currencyCode!= null){
			if (currencyCode.equals("USD")){
				result = "$";
			}
		}
		**/
		return result;
	}

    public static String getURLParameter(String url, String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        name = name + "=";
        int start = url.indexOf(name);
        if (start < 0) {
            return null;
        }
        start += name.length();
        int end = url.indexOf("&", start);
        if (end == -1) {
            end = url.length();
        }
        return url.substring(start, end);
    }
    
    /**
     * string 转换
     * @param str
     */
    public static String nullToString(Object str) {
    	if (null == str) {
			return "";
		}
    	return str.toString().trim();
    }
    

    
    public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}

