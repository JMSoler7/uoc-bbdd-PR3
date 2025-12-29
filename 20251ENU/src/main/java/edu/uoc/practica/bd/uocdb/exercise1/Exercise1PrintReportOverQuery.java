package edu.uoc.practica.bd.uocdb.exercise1;

import edu.uoc.practica.bd.util.Column;
import edu.uoc.practica.bd.util.DBAccessor;
import edu.uoc.practica.bd.util.Report;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.uoc.practica.bd.uocdb.exercise1.Exercise1Row;



public class Exercise1PrintReportOverQuery {

  public static void main(String[] args) {
    Exercise1PrintReportOverQuery app = new Exercise1PrintReportOverQuery();
    app.run();
  }

  private void run() {
    DBAccessor dbaccessor = new DBAccessor();
    dbaccessor.init();
    Connection conn = dbaccessor.getConnection();

    if (conn != null) {
      Statement cstmt = null;
      ResultSet resultSet = null;

      try {

        List<Column> columns = Arrays.asList(new Column("Wine", 22, "wine_name"),
            new Column("Winery", 14, "winery_name"),
            new Column("Pdo", 10, "pdo_name"),
            new Column("Vintage", 8, "vintage"),
            new Column("Prizes", 6, "prizes"),
            new Column("Price", 6, "price"),
            new Column("BestBeforeDate", 14, "BestBeforeDate"));

        Report report = new Report();
        report.setColumns(columns);
        List<Object> list = new ArrayList<Object>();


          // TODO Execute SQL sentence
          cstmt = conn.createStatement();
          resultSet = cstmt.executeQuery("SELECT * FROM premium_wines WHERE prizes > 5 ORDER BY price DESC;");

          // TODO Loop over results and get the main values
          while (resultSet.next()) {
            Exercise1Row row = new Exercise1Row(
                resultSet.getString("wine_name"),
                resultSet.getString("winery_name"),
                resultSet.getString("pdo_name"),
                resultSet.getLong("vintage"),
                resultSet.getLong("prizes"),
                resultSet.getDouble("price"),
                resultSet.getDate("bestbeforedate")
            );
            list.add(row);
          }
          // TODO End loop

          if (list.isEmpty()) {
            System.err.println("ERROR: List without data");
            return;
          }
          report.printReport(list);
      } catch (SQLException e) {
        System.err.println("ERROR: List not available");
      } finally {
        // TODO Close All resources
        try {
          if (resultSet != null) {
            resultSet.close();
          }
          if (cstmt != null) {
            cstmt.close();
          }
          if (conn != null) {
            conn.close();
          }
        } catch (SQLException e) {
          System.err.println("ERROR: Could not close the resources");
        }
      }
    }
  }
}
