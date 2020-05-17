package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

class DBController {

    private final String tableName = "station";
    private Connection conn = null;

    private Connection getConnection() throws SQLException {
        Connection conn;
        Properties connectionProps = new Properties();
        String userName = "u0613437_odz";
        String password = "Lock2612";
        connectionProps.put("user", userName);
        connectionProps.put("password", password);

        String serverName = "server222.hosting.reg.ru";
        int portNumber = 3306;
        String dbName = "u0613437_odz";
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + serverName + ":" +
                        portNumber + "/" + dbName +"?useUnicode=true&serverTimezone=UTC", connectionProps);
        return conn;
    }

    ArrayList<Station> getEmployees() throws SQLException {
        ArrayList<Station> stations = new ArrayList<>();
        String command = "SELECT * FROM " + this.tableName;
        ResultSet result = executeSelectQuery(conn, command);
        return formatEmployees(stations, result);
    }

    void deleteEmployee(int id) {
        String command = "DELETE FROM " + this.tableName + " WHERE ID = " + id;
        System.out.println(command);
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addEmployee(Station station) {
        String command =
                "INSERT INTO `station`(`ID`, `NUMBER`, `DESTINY`, `DEPARTURE_TIME`, `ARRIVAL_TIME`, `AMOUNT`) VALUES (" +
                        "\'" + station.getID() + "\'," +
                        "\'" + station.getNumber() + "\'," +
                        "\'" + station.getDestiny() + "\'," +
                        "\'" + station.getDepartureTime() + "\'," +
                        "\'" + station.getArrivalTime() + "\'," +
                        "\'" + station.getAmount() + "\'" + ")";

        try {
            Statement stmt = conn.createStatement();
            System.out.println(command);
            stmt.execute(command);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateEmployee(Station station) throws SQLException{
        System.out.println("Updating");
        String command = "UPDATE `employees` SET " +
                "`NUMBER`= \'" + station.getNumber() + "\'," +
                "`DESTINY`= \'" + station.getDestiny() + "\'," +
                "`DEPARTURE_TIME`= \'" + station.getDepartureTime() + "\'," +
                "`ARRIVAL_TIME`= \'" + station.getArrivalTime() + "\'," +
                "`AMOUNT`= \'" + station.getAmount() + "\'," +
                " WHERE `ID` =" + station.getID();

            Statement stmt = conn.createStatement();
            System.out.println(command);
            stmt.executeUpdate(command);
    }

    ArrayList<Station> findEmployee(String destiny) throws SQLException {
        ArrayList<Station> stations = new ArrayList<>();
        String command = "SELECT * FROM `station` WHERE `DESTINY` = \'" + destiny + "\'";
        ResultSet result = executeSelectQuery(conn, command);
        return formatEmployees(stations, result);
    }

    private ArrayList<Station> formatEmployees(ArrayList<Station> stations, ResultSet result) throws SQLException {
        while (result.next()) {
            Station station = new Station(
                    result.getInt("ID"),
                    result.getInt("NUMBER"),
                    result.getString("DESTINY"),
                    result.getString("DEPARTURE_TIME"),
                    result.getString("ARRIVAL_TIME"),
                    result.getInt("AMOUNT"));
            stations.add(station);
        }
        return stations;
    }

    private ResultSet executeSelectQuery(Connection conn, String command) {
        Statement stmt;
        ResultSet result = null;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    void run() {
        try {
            conn = this.getConnection();
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the database");
            e.printStackTrace();
        }
    }
}