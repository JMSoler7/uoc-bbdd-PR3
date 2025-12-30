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
      conn.setAutoCommit(false);
      ResultSet generatedKeys = null;
      ResultSet rsWineryId = null;
      ResultSet rsPdoId = null;
      PreparedStatement psUpdateWinery = conn.prepareStatement("UPDATE winery SET winery_phone = ? WHERE winery_name = ?");
      PreparedStatement psSelectWinery = conn.prepareStatement("SELECT winery_id FROM winery WHERE winery_name = ?");
      PreparedStatement psSelectPdo = conn.prepareStatement("SELECT pdo_id FROM pdo WHERE pdo_name = ?");
      PreparedStatement psInsertWine = conn.prepareStatement(
          "INSERT INTO wine (wine_name, winery_id, pdo_id, vintage, alcohol_content, category, color, price, stock) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)", PreparedStatement.RETURN_GENERATED_KEYS);
      PreparedStatement psInsertWineGrape = conn.prepareStatement(
          "INSERT INTO wine_grape (grape_id, wine_id) "
          + "VALUES (?, ?)");
      CallableStatement csFunctionGrapeCount = conn.prepareCall("{ ? = call get_grape_count_wine(?) }");

      // TODO Select, Update, insert or call from every row in file
      for (List<String> row : fileContents) {
        // Actualitzem el celler i llencem un error si no s'actualitza cap fila
        setPSUpdateWinery(psUpdateWinery, row);
        int rowsUpdated = psUpdateWinery.executeUpdate();
        if (rowsUpdated == 0) {
          throw new SQLException("No winery updated for row: " + row);
        }
        // Obtenim la winery_id i pdo_id a partir dels seus noms i llencem un error si no existeixen
        setPSSelectWinery(psSelectWinery, row);
        rsWineryId =  psSelectWinery.executeQuery();
        int winery_id = -1;
        if (rsWineryId.next()) {
          winery_id = rsWineryId.getInt("winery_id");
        } else {
          throw new SQLException("No winery found for row: " + row);
        }
        setPSSelectPdo(psSelectPdo, row);
        rsPdoId =  psSelectPdo.executeQuery();
        int pdo_id = -1;
        if (rsPdoId.next()) {
          pdo_id = rsPdoId.getInt("pdo_id");
        } else {
          throw new SQLException("No pdo found for row: " + row);
        }
        // Insertem el vi, controlant si ja existeix
        setPSInsertWine(psInsertWine, row, winery_id, pdo_id);
        try {
          psInsertWine.executeUpdate();
        } catch (SQLException e) {
          System.err.println("ERROR: Aquest vi ja existeix: " + row.get(0));
        }
        // Obtenim el wine_id del vi inserit per inserir les seves varietats de raïm
        generatedKeys = psInsertWine.getGeneratedKeys();
          if (generatedKeys.next()) {
          int wine_id = generatedKeys.getInt(1);
          for (int grape_index = 9; grape_index < 12; grape_index++) {
            Integer grape_id = null;
            if (grape_index < row.size() && row.get(grape_index) != null && !row.get(grape_index).isEmpty()) {
              grape_id = Integer.parseInt(row.get(grape_index));
            }
            if (grape_id != null){
              setPSInsertWineGrape(psInsertWineGrape, grape_id, wine_id);
              psInsertWineGrape.executeUpdate();
            }
          }
          // Cridem la funció per obtenir el nombre de varietats de raïm i informem a l'usuari
          setCSFunctionGrapeCount(csFunctionGrapeCount, wine_id);
          csFunctionGrapeCount.execute();
          int grape_count = csFunctionGrapeCount.getInt(1);
          System.out.println("INFO: El vi amb ID " + wine_id + " ha estat carregat amb " + grape_count + " varietats de raïm.");
        } else {
          throw new SQLException("No wine inserted for row: " + row);
        }
    }
    // TODO Validate transaction
    try {
      conn.commit();
    } catch (SQLException e) {
      String message = "ERROR: Could not commit the transaction";
      System.err.println(message);
      throw new RuntimeException(message, e);
    }
    } catch (SQLException e) {
      System.err.println("ERROR: SQL error");
      e.printStackTrace();
      // TODO Rollback transaction
      try {
        conn.rollback();
      } catch (SQLException ex) {
        System.err.println("ERROR: Could not rollback the transaction");
        ex.printStackTrace();
      }
    }
    // TODO Close resources and check exceptions
    finally {
      try {
        conn.close();
      } catch (SQLException e) {
        System.err.println("ERROR: Could not close the connection");
      }
    }
  }

  private void setPSSelectWinery(PreparedStatement selectStatement, List<String> row)
      throws SQLException {
    String[] rowArray = (String[]) row.toArray(new String[0]);
    setValueOrNull(selectStatement, 1, getValueIfNotNull(rowArray, 1)); // winery_name
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
