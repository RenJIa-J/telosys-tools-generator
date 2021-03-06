/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.generator.context ;

import java.util.LinkedList;

import org.telosys.tools.commons.JavaClassUtil;

public class Util {

	public static String numberToString(Number value, String defaultValue) {
		if ( value != null ) {
			return value.toString();
		}
		else {
			return defaultValue ;
		}
	}
	
	public static int intValue(Integer value, int defaultValue) {
		if ( value != null ) {
			return value.intValue();
		}
		else {
			return defaultValue ;
		}
	}
	
    /**
     * Returns 'getFoo' for 'foo'(or 'isFoo' if foo is boolean )
     * @param attributeName
     * @param sLongType
     * @return
     */
    public static String buildGetter(String attributeName, String sLongType) {
		//String s = attName.substring(0, 1).toUpperCase()+attName.substring(1, attName.length());
		if ( "boolean".equals(sLongType) )
		{
			//return "is" + s;
			return "is" + transformAttributeName(attributeName) ;
		}
		else
		{
			//return "get" + s;
			return "get" + transformAttributeName(attributeName) ;
		}
	}
    
    /**
     * Returns 'getFoo' for 'foo'
     * @param attributeName
     * @return
     */
    public static String buildGetter( String attributeName ) {
		return "get" + transformAttributeName(attributeName) ;
    }
    
    /**
     * Returns 'setFoo' for 'foo'
     * @param attributeName
     * @return
     */
    public static String buildSetter(String attributeName) {
		// return "set" + attName.substring(0, 1).toUpperCase()+attName.substring(1, attName.length());
		return "set" + transformAttributeName(attributeName) ;
	}

    /**
     * Transform the given attribute name, eg : "foo" to "Foo", "firstName" to "FirstName"
     * @param attributeName
     * @return
     */
    private static String transformAttributeName(String attributeName) {
		return attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1, attributeName.length());
	}

    private static boolean inList (String sLongType, LinkedList<String> fullNames) 
    {
    	for ( String s : fullNames ) {
    		if ( s != null )
    		{
    			if ( s.equals(sLongType) )
    			{
    				return true ; // Found 
    			}
    		}
    	}
    	return false ; // Not found
    }    
    /**
     * Determines the shortest type to use according with the given "not imported types" list
     * @param sLongType
     * @param notImportedTypes
     * @return
     */
    public static String shortestType(String sLongType, LinkedList<String> notImportedTypes) 
    {
    	if ( notImportedTypes != null )
    	{
    		if ( inList(sLongType, notImportedTypes) )
    		{
    			return sLongType ; // Keep the "long type" because not imported
    		}    		
    	}
		// No full names to keep => reduce it to short name
		return JavaClassUtil.shortName(sLongType);
	}

    /**
     * Trim the given string, returns the default value is the string is null
     * @param s
     * @param defaultValue
     * @return
     */
    public static String trim(String s, String defaultValue) {
    	if ( s != null ) {
    		return s.trim();
    	}
    	return defaultValue ;
    }
}
