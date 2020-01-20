package com.pyro.entities;


import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSqlExample {

    private Connection connection;
    private Statement statement;

    private static PostgreSqlExample instance = null;

    public static PostgreSqlExample getInstance(){
        if(instance == null){
            instance= new PostgreSqlExample();
        }
        return instance;
    }

    private PostgreSqlExample() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
            statement = connection.createStatement();
        }  catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }


    public List<Col> describeTabe(String param) throws SQLException {
        List<Col> Cols = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT isc.column_name as column_name, isc.data_type as data_type, isc.is_nullable as is_nullable" +
                "                FROM information_schema.columns AS isc " +
                "                WHERE isc.table_schema = 'public' AND isc.table_name = '"+ param+"'");

        while (resultSet.next()) {
            Cols.add(new Col(resultSet.getString("column_name"),
                    resultSet.getString("data_type"),
                    resultSet.getString("is_nullable")));
        }
        return Cols;
    }
    public List<String> getTables() throws SQLException {
        List<String> tbls = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("select tablename from pg_tables where hastriggers = true");
        while (resultSet.next()) {
            tbls.add(resultSet.getString("tablename"));
        }
        return tbls;
    }

    public List<String> getDB() throws SQLException {
        List<String> tbls = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("select datname from pg_database");
        while (resultSet.next()) {
            tbls.add(resultSet.getString("datname"));
        }
        return tbls;
    }

    public List<String> getUsers() throws SQLException {
        List<String> tbls = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("select usename from pg_user");
        while (resultSet.next()) {
            tbls.add(resultSet.getString("usename"));
        }
        return tbls;
    }

    public void reconnect(String database) throws SQLException {
        statement.close();
        connection.close();
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+database, "postgres", "root");
        statement = connection.createStatement();
    }

    public void deleteUser(String username) throws SQLException {
        statement.execute("DROP USER "+username);
    }
    public void createUser(String username,
                           String password,
                           boolean insert,
                           boolean update,
                           boolean delete) throws SQLException {
        try {
            statement.execute("CREATE USER "+username+ "  WITH    PASSWORD '"+password+"'");
        }
       catch (PSQLException e) {

           statement.execute("drop USER "+username);
           statement.execute("CREATE USER "+username+ "  WITH    PASSWORD '"+password+"'");
       }
        String grants="";
        grants+="select";
        if(insert){
            grants+=", insert";
        }
        if(update) {
            grants+=", update";
        }
        if(delete) {
            grants+= ", delete ";
        }
        if(grants.length()>1)
        statement.execute("GRANT "+ grants+" ON ALL TABLES IN SCHEMA public TO "+username);
    }



}