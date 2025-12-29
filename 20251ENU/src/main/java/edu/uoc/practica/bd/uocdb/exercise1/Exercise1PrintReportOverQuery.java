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
        
        // TODO Loop over results and get the main values
        
        // TODO End loop
       
      } 
      // TODO Close All resources
      finally {
      }
    }
  }
}
