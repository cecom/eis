package com.geewhiz.eis.utils;

import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

public class QueryBuilder {

	private static final String BEGIN_TOKEN = "@";
	private static final String END_TOKEN = "@";

	public static final String replaceEntriesInQuery(String query, Map<String, String> replaceEntries) {

		StrSubstitutor substitutor = new StrSubstitutor(replaceEntries);
		substitutor.setVariablePrefix(BEGIN_TOKEN);
		substitutor.setVariableSuffix(END_TOKEN);

		return substitutor.replace(query);
	}
}
