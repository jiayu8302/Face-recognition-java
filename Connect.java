/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.sql.DriverManager;

/**
 * Class using to initialize embedded database system
 *
 * @author mac
 */
public class Connect {

    /**
     * make connection
     *
     * @return null
     */
    public java.sql.Connection Connect() {

        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            // use the driver
            Class.forName(driver);
            String url = "jdbc:derby:tests;create=true;user=student;password=student";
            java.sql.Connection con = DriverManager.getConnection(url);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
