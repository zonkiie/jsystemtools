package utils;

import java.lang.*;
import java.util.*;
import java.util.regex.*;

public class RegexHelper
{
	public static List<String> getRegexNamedGroups(String regex)
	{
		List<String> groupList = new ArrayList<String>();
		Matcher groupNamesMatcher = Pattern.compile("\\(\\?<(.+?)>").matcher(regex);
		while(groupNamesMatcher.find())
		{
			for(int i = 1; i <= groupNamesMatcher.groupCount(); i++)
			{
				groupList.add(groupNamesMatcher.group(i));
			}
		}
		return groupList;
	}
}
