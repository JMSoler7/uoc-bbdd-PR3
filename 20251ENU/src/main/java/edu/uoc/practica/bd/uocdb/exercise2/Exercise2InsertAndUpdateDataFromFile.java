package edu.uoc.practica.bd.uocdb.exercise2;

import edu.uoc.practica.bd.util.DBAccessor;
import edu.uoc.practica.bd.util.FileUtilities;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class Exercise2InsertAndUpdateDataFromFile {

  private FileUtilities fileUtilities;

  public Exercise2InsertAndUpdateDataFromFile() {
    super();
    fileUtilities = new FileUtilities();
  }

  public static void main(String[] args) {
    Exercise2InsertAndUpdateDataFromFile app = new Exercise2InsertAndUpdateDataFromFile();
    app.run();
  }

  private void run() {
    List<List<String>> fileContents = null;

    try {
      fileContents = fileUtilities.readFileFromClasspath("exercise2.data");
    } catch (FileNotFoundException e) {
      System.err.println("ERROR: File not found");
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("ERROR: I/O error");
      e.printStackTrace();
    }

		if (fileContents == null) {
			return;
		}

    DBAccessor dbaccessor = new DBAccessor();
    dbaccessor.init();
    Connection conn = dbaccessor.getConnection();

	if (conn == null) {
		return;
	}

	// TODO Prepare everything before updating or inserting

    try {    	
      // TODO Select, Update, insert or call from every row in file
    	
      // TODO Validate transaction
    }
    // TODO Close resources and check exceptions
    finally {
      }
  }
  
  
  private void setPSUpdateWinery(PreparedStatement updateStatement, List<String> row)
      throws SQLException {
    String[] rowArray = (String[]) row.toArray(new String[0]);
    
    setValueOrNull(updateStatement, 1, getValueIfNotNull(rowArray, 2)); // winery_phone
    setValueOrNull(updateStatement, 2, getValueIfNotNull(rowArray, 1)); // winery_name
  }

  private void setPSSelectPdo(PreparedStatement selectStatement, List<String> row)
      throws SQLException {
    String[] rowArray = (String[]) row.toArray(new String[0]);
    setValueOrNull(selectStatement, 1, getValueIfNotNull(rowArray, 3)); // pdo_name
  }
  
  private void setPSInsertWine(PreparedStatement insertStatement, List<String> row, int winery_id, int pdo_id)
	      throws SQLException {
    String[] rowArray = (String[]) row.toArray(new String[0]);
    setValueOrNull(insertStatement, 1, 
    		getValueIfNotNull(rowArray, 0)); // wine_name   
    setValueOrNull(insertStatement, 2, winery_id); // winery_id
    setValueOrNull(insertStatement, 3, pdo_id); // pdo_id
    setValueOrNull(insertStatement, 4, 
    		getIntegerFromStringOrNull(getValueIfNotNull(rowArray, 4))); // vintage   
    setValueOrNull(insertStatement, 5, 
    		getDoubleFromStringOrNull(getValueIfNotNull(rowArray, 5))); // alcohol_content   
    setValueOrNull(insertStatement, 6, getValueIfNotNull(rowArray, 6)); // category   
    setValueOrNull(insertStatement, 7, getValueIfNotNull(rowArray, 7)); // color   
    setValueOrNull(insertStatement, 8, 
    		getDoubleFromStringOrNull(getValueIfNotNull(rowArray, 8))); // price   
  }

  private void setPSInsertWineGrape(PreparedStatement insertStatement, int grape_id, int wine_id)
      throws SQLException {
    
    setValueOrNull(insertStatement, 1, grape_id);  // grape_id
    setValueOrNull(insertStatement, 2, wine_id);  // wine_id
  }

  private void setCSFunctionGrapeCount(CallableStatement callStatement, int wine_id)
	      throws SQLException {
		callStatement.registerOutParameter(1, java.sql.Types.INTEGER);
	    setValueOrNull(callStatement, 2, wine_id);  // wine_id
  }
  
  private Integer getIntegerFromStringOrNull(String integer) {
    return (integer != null) ? Integer.valueOf(integer) : null;
  }

  private Double getDoubleFromStringOrNull(String doubl) {
	    return (doubl != null) ? Double.valueOf(doubl) : null;
  }
  
  private String getValueIfNotNull(String[] rowArray, int index) {
    return (index < rowArray.length && rowArray[index].length() > 0) ? rowArray[index] : null;
  }
  
  private void setValueOrNull(PreparedStatement preparedStatement, int parameterIndex,
	      Integer value) throws SQLException {
			if (value == null) {
				preparedStatement.setNull(parameterIndex, Types.INTEGER);
			} else {
				preparedStatement.setInt(parameterIndex, value);
			}
  }
  private void setValueOrNull(CallableStatement callStatement, int parameterIndex,
	      Integer value) throws SQLException {
			if (value == null) {
				callStatement.setNull(parameterIndex, Types.INTEGER);
			} else {
				callStatement.setInt(parameterIndex, value);
			}
  }
  
  private void setValueOrNull(PreparedStatement preparedStatement, int parameterIndex, String value)
      throws SQLException {
			if (value == null || value.length() == 0) {
				preparedStatement.setNull(parameterIndex, Types.VARCHAR);
			} else {
				preparedStatement.setString(parameterIndex, value);
			}
  	}
  private void setValueOrNull(PreparedStatement preparedStatement, int parameterIndex, Double value)
	      throws SQLException {
			if (value == null) {
				preparedStatement.setNull(parameterIndex, Types.DOUBLE);
			} else {
				preparedStatement.setDouble(parameterIndex, value);
			}
	  }
  
  
}
