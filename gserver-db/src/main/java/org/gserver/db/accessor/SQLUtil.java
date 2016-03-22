package org.gserver.db.accessor;

import java.util.Map;
import java.util.Set;

/**
 * 生成sql助手类
 * 
 * @author zhaohui
 * 
 */
public class SQLUtil {

	public final static String insert_into = "insert ignore into  ";
	public final static String values = " VALUES ";
	public final static String set = " set ";
	public final static String empty_string = "";
	public final static String blank_string = " ";
	public final static String nil_string = "null";
	public final static String equals_string = " = ";
	public final static String quota_string = "'";
	public final static String comma_string = ",";
	public final static String left_Brackets = " (";
	public final static String right_Brackets = " )";
	public final static String end_string = ";";
	public final static String in = "in";
	public final static String caseS = " CASE ";
	public final static String end = " end ";

	public final static String update_table = "update ";
	public final static String delete_from = "delete from ";
	public final static String where_id = " where id ";
	public static final String id = "id";
	public static final String where = " where ";

	public static String insert(Map<String, String> beanMap, String tableName) {
		if (beanMap == null || beanMap.size() <= 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append(insert_into).append(tableName);

		StringBuffer keyStr = new StringBuffer(left_Brackets);
		StringBuffer valueStr = new StringBuffer(left_Brackets);

		Set<String> keySet = beanMap.keySet();
		int index = 0;
		for (String key : keySet) {
			String value = beanMap.get(key);
			if (index != keySet.size() - 1) {
				keyStr.append(key).append(comma_string);
				valueStr.append(value).append(comma_string);
			} else {
				keyStr.append(key);
				valueStr.append(value);
			}
			index++;
		}
		keyStr.append(right_Brackets);
		valueStr.append(right_Brackets);

		buffer.append(keyStr).append(values).append(valueStr)
				.append(end_string);
		return buffer.toString();
	}

	public String update(Map<String, String> updateBean, String tableName) {
		if (updateBean == null || updateBean.size() <= 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append(update_table).append(tableName).append(set);

		Set<String> keySet = updateBean.keySet();
		int index = 0;
		for (String key : keySet) {
			String value = updateBean.get(key);
			if (index != keySet.size() - 1) {
				buffer.append(key).append(equals_string).append(value)
						.append(comma_string);
			} else {
				buffer.append(key).append(equals_string).append(value);
			}
			index++;
		}
		return buffer.toString();
	}

	public String delete(Map<String, String> params, String tableName) {
		StringBuilder buffer = new StringBuilder();
		return buffer.toString();
	}
}
