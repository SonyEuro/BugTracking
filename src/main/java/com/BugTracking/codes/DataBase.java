package com.BugTracking.codes;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.BugTracking.models.cProject;

public class DataBase {

	// Подключение
	public static Connection cnct = null;
	// Адрес
	// jdbc:postgresql://<database_host>:<port>/<database_name>
	public static String url = "jdbc:postgresql://localhost:5432/BugTracking";
	// Логин
	public static String login = "postgres";
	// Пароль
	public static String password = "admin123";

	public static ResultSet Select(String query) {
		try {
        return Connect().executeQuery(query);
		 } catch (Exception ex) {
	            //выводим наиболее значимые сообщения
	            Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            if (cnct != null) {
	                try {
	                    cnct.close();
	                } catch (SQLException ex) {
	                    Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	                }
	            }
	        }
        return null;
	}

	private static Statement Connect() throws ClassNotFoundException, SQLException {
		System.out.println("Подготовка к работе");
        //Загружаем драйвер
        Class.forName("org.postgresql.Driver");
        System.out.println("Драйвер работает");
        //Создаём соединение
        DriverManager.setLoginTimeout(30);
        cnct = DriverManager.getConnection(url, login, password);
        System.out.println("Подключено к БД");
        Statement statement = null;
        statement = cnct.createStatement();
		return statement;
	}
	
	public static boolean Insert(String query) {
		try {
        if (Connect().executeUpdate(query)!=0)
        		return true;
        else
        	return false;
		 } catch (Exception ex) {
	            //выводим наиболее значимые сообщения
	            Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            if (cnct != null) {
	                try {
	                    cnct.close();
	                } catch (SQLException ex) {
	                    Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	                }
	            }
	        }
        return false;
	}
	
	public static boolean Update(String query) {
		try {
        if (Connect().executeUpdate(query)!=0)
        		return true;
        else
        	return false;
		 } catch (Exception ex) {
	            //выводим наиболее значимые сообщения
	            Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            if (cnct != null) {
	                try {
	                    cnct.close();
	                } catch (SQLException ex) {
	                    Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	                }
	            }
	        }
        return false;
	}
	
	public static boolean Delete(String query) {
		try {
        if (Connect().executeUpdate(query)!=0)
        		return true;
        else
        	return false;
		 } catch (Exception ex) {
	            //выводим наиболее значимые сообщения
	            Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            if (cnct != null) {
	                try {
	                    cnct.close();
	                } catch (SQLException ex) {
	                    Logger.getLogger(cProject.class.getName()).log(Level.SEVERE, null, ex);
	                }
	            }
	        }
        return false;
	}
	
	
}
