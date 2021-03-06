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
package org.telosys.tools.generator.context.tools;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.EntityInContext;

/**
 * Each instance contains the JDBC SQL requests for a given entity with its mapping
 * 
 * @author Laurent GUERIN
 *
 */
public class JdbcRequests {
	
    private final EntityInContext          entity;
    private final List<AttributeInContext> attributesForPrimaryKey ;
    private final List<AttributeInContext> attributesForSelect ;
    private final List<AttributeInContext> attributesForInsert ;
    private final List<AttributeInContext> attributesForUpdate ;
    		
    //--- SQL
    private final String table  ;
    private final String sqlSelect ;
    private final String sqlExists ;
    private final String sqlInsert ;
    private final String sqlUpdate ;
    private final String sqlDelete ;

	/**
	 * Constructor
	 * @param entity
	 * @param useSchema
	 */
	public JdbcRequests(EntityInContext entity, boolean useSchema) {
		super();
		this.entity = entity ;
		
		this.table = entity.getDatabaseTable();
		this.attributesForPrimaryKey = buildAttributesForPrimaryKey();
		this.attributesForSelect = buildAttributesForSelect();
		this.attributesForInsert = buildAttributesForInsert();
		this.attributesForUpdate = buildAttributesForUpdate();
				
        //--- Build the 4 SQL Request
		this.sqlSelect = buildSqlSelect();
		this.sqlExists = buildSqlSelectCount();
		this.sqlInsert = buildSqlInsert();
		this.sqlUpdate = buildSqlUpdate();
		this.sqlDelete = buildSqlDelete();
	}
	
	
    public List<AttributeInContext> getAttributesForPrimaryKey() {
		return attributesForPrimaryKey;
	}


	public List<AttributeInContext> getAttributesForSelect() {
		return attributesForSelect;
	}


	public List<AttributeInContext> getAttributesForInsert() {
		return attributesForInsert;
	}


	public List<AttributeInContext> getAttributesForUpdate() {
		return attributesForUpdate;
	}


	public String getTable() {
		return table;
	}


	public String getSqlSelect() {
		return sqlSelect;
	}


	public String getSqlExists() {
		return sqlExists;
	}


	public String getSqlInsert() {
		return sqlInsert;
	}


	public String getSqlUpdate() {
		return sqlUpdate;
	}


	public String getSqlDelete() {
		return sqlDelete;
	}


	private List<AttributeInContext> buildAttributesForPrimaryKey() {
        List<AttributeInContext> list = new LinkedList<AttributeInContext>();
        for ( AttributeInContext attribute : entity.getAttributes() ) {
        	if ( attribute.isKeyElement() ) {
            	list.add(attribute);
        	}
        }
        return list ;
    }
    
    private List<AttributeInContext> buildAttributesForSelect() {
        List<AttributeInContext> list = new LinkedList<AttributeInContext>();
        for ( AttributeInContext attribute : entity.getAttributes() ) {
        	list.add(attribute);
        }
        return list ;
    }

    private List<AttributeInContext> buildAttributesForInsert() {
        List<AttributeInContext> list = new LinkedList<AttributeInContext>();
        for ( AttributeInContext attribute : entity.getAttributes() ) {
        	// Do not use "auto-incremented" attributes
        	if ( attribute.isAutoIncremented() == false ) {
            	list.add(attribute);
        	}
        }
        return list ;
    }
    
    private List<AttributeInContext> buildAttributesForUpdate() {
        List<AttributeInContext> list = new LinkedList<AttributeInContext>();
        for ( AttributeInContext attribute : entity.getAttributes() ) {
        	// Do not use "primary key" and "auto-incremented" attributes
        	if ( attribute.isKeyElement() == false && attribute.isAutoIncremented() == false ) {
            	list.add(attribute);
        	}
        }
        return list ;
    }
    
    
    private String buildColumnsList(List<AttributeInContext> attributes, boolean bPrefix) 
    {
        StringBuffer sb = new StringBuffer(200);
        int n = 0 ;
        for ( AttributeInContext attribute : attributes ) {
            if (n > 0) {
                sb.append(", ");
            }
            if (bPrefix) {
                sb.append(this.table + ".");
            }
            sb.append( attribute.getDatabaseName() );
            n++;
        }
        return sb.toString();
    }
    
    /**
     * Build the where criteria ( for KEY columns only) <br>
     * ie : "tab.col1 = ? and tab.col2 = ?"
     * 
     * @return
     */
    private String whereCriteria(List<AttributeInContext> attributes, boolean bPrefix)
    {
        StringBuffer sb = new StringBuffer(200);
        int n = 0 ;
        for ( AttributeInContext attribute : attributes ) {
            if (n > 0) {
                sb.append(" and ");
            }
            if (bPrefix) {
                sb.append(this.table + ".");
            }
            sb.append( attribute.getDatabaseName() + " = ?" );
            n++;
        }
        return sb.toString();
    }

    private String buildQuestionMarsks(List<AttributeInContext> attributes) 
    {
    	if ( attributes.size() == 0 ) {
    		return "" ;
    	}
    	else {
            StringBuffer sb = new StringBuffer();
            for ( int c = 0 ; c < attributes.size() ; c++ )  {
                if (c > 0) {
                    sb.append(", ");
                }
                sb.append("?");
            }
            return sb.toString();
    	}
    }
    
    /**
     * Build the set column value clause (for DATA columns only) ie : "tab.col1 = ?, tab.col2 = ?"
     * 
     * @return
     */
    private String buildSetValuesForUpdate(List<AttributeInContext> attributes, boolean bPrefix)
    {
        StringBuffer sb = new StringBuffer(200);
        //--- Data Columns
//        for ( int c = 0 ; c < _dataColumns.length ; c++ )
//        {
//            if (c > 0)
//            {
//                sb.append(", ");
//            }
//            if (bPrefix)
//            {
//                sb.append(_table + ".");
//            }
//            sb.append(_dataColumns[c] + " = ?");
//        }
        int n = 0 ;
        for ( AttributeInContext attribute : attributes ) {
            if (n > 0) {
                sb.append(", ");
            }
            if (bPrefix) {
                sb.append(this.table + ".");
            }
            sb.append( attribute.getDatabaseName() + " = ?" );
            n++;
        }
        return sb.toString();
    }

    
    /**
     * Build the SQL SELECT request
     * @return
     */
    private String buildSqlSelect() {
        return "select " + buildColumnsList(this.attributesForSelect, false) 
        		+ " from " + this.table 
        		+ " where " + whereCriteria(this.attributesForPrimaryKey, false);
    }

    /**
     * Build the SQL COUNT request
     * @return
     */
    private String buildSqlSelectCount() {
        return "select count(*) from " + this.table 
        		+ " where " + whereCriteria(this.attributesForPrimaryKey, false);
    }

    /**
     * Build the SQL INSERT request
     * @return
     */
    private String buildSqlInsert() {
        return "insert into " + this.table 
        		+ " ( " + buildColumnsList(this.attributesForInsert, false)  + " )"
        		+ " values ( " + buildQuestionMarsks(this.attributesForInsert) + " )";
    }

    /**
     * Build the SQL UPDATE request
     * @return
     */
    private String buildSqlUpdate()
    {
        return "update " + this.table 
        		+ " set " + buildSetValuesForUpdate(this.attributesForUpdate, false) 
        		+ " where " + whereCriteria(this.attributesForPrimaryKey, false);
    }

    /**
     * Build the SQL DELETE request
     * @return
     */
    private String buildSqlDelete()
    {
        return "delete from " + this.table 
        		+ " where " + whereCriteria(this.attributesForPrimaryKey, false);
    }

}
