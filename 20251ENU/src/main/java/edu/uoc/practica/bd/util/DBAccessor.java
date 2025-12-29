package edu.uoc.practica.bd.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBAccessor {

  private String dbname;
  private String host;
  private String port;
  private String user;
  private String passwd;
  private String schema;

  /**
   * Initializes the class loading the database properties file and assigns values to the instance
   * variables.
   *
   * @throws RuntimeException Properties file could not be found.
   */
  public void init() {
    Properties prop = new Properties();
    InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");

    try {
      prop.load(propStream);
      this.host = prop.getProperty("host");
      this.port = prop.getProperty("port");
      this.dbname = prop.getProperty("dbname");
      this.user = prop.getProperty("user");
      this.passwd = prop.getProperty("passwd");
      this.schema = prop.getProperty("schema");
    } catch (IOException e) {
      String message = "ERROR: db.properties file could not be found";
      System.err.println(message);
      throw new RuntimeException(message, e);
    }
  }

  /**
   * Obtains a {@link Connection} to the database, based on the values of the
   * <code>db.properties</code> file.
   *
   * @return DB connection or null if a problem occurred when trying to connect.
   */
  public Connection getConnection() {
    Connection conn = null;

    // TODO Implement the DB connection
    String url = "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.dbname;
    try {
      conn = DriverManager.getConnection(url, this.user, this.passwd);
      System.out.println("Connected to the PostgreSQL server successfully.");
    } catch (SQLException e) {
      String message = "ERROR: Could not connect to the database";
      System.err.println(message);
      throw new RuntimeException(message, e);
    }

    // TODO Sets the search_path
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("SET search_path TO " + this.schema);
    } catch (SQLException e) {
      String message = "ERROR: Could not set search_path";
      System.err.println(message);
      throw new RuntimeException(message, e);
    }

    return conn;
  }
}
