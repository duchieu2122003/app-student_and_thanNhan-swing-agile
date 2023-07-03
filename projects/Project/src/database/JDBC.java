/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.microsoft.sqlserver.jdbc.SQLServerXADataSource;
import java.sql.Connection;

/**
 *
 * @author dinhv
 */
public class JDBC {

    public Connection connect() {
        Connection conn = null;
        String severName = "DESKTOP-70NOM3V/SQLEXPRESS";
        String user = "sa";
        String password = "123";
        String dateBase = "Agile";
        int port = 1433;
        SQLServerXADataSource ds = new SQLServerXADataSource();
        ds.setUser(user);
        ds.setPassword(password);
        ds.setServerName(severName);
        ds.setDatabaseName(dateBase);
        ds.setPortNumber(port);
        try {
            conn = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
