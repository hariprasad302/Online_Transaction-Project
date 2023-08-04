
package com.j1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Day5 {
	static final Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		String path ="D:\\New folder\\JDBC\\src\\com\\tap\\utilities\\mysqlinfo.properties";
		FileInputStream fis = null;
		Properties p = null;
		
		
		Connection Mycon = null; 
		Statement Myst = null;
		ResultSet res = null;
		
		String sql = "Update `emp` SET `salary`= `salary`+5000 WHERE `dept`= 'It'";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			
			fis = new FileInputStream(path);
			
			p = new Properties();
			
			p.load(fis);
			
		
			String url =p.getProperty("url");
			String un = p.getProperty("un");
			String pwd = p.getProperty("pwd");
			
			Mycon =DriverManager.getConnection(url, un, pwd);
			
			//System.out.println("Connected"); 
			
		    Myst = transaction(Mycon);
		    
		 	
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	finally {
		 try {
			Day2.close(res ,Myst ,Mycon,fis);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		 
		
		
		
		
	
		

	}

	public static Statement transaction(Connection Mycon) throws SQLException {
		Statement Myst;
		Mycon.setAutoCommit(false);
		
		System.out.println("Enter the Sender Name :");
	    String sender = scan.next();
	    
	    System.out.println("Enter Reciever Name :");
	    String reciever =scan.next();
	    
	    System.out.println("Enter the Amount");
         int amount = scan.nextInt();
         
         String balanceQuery = "SELECT `salary` FROM `emp` WHERE `Name`= ?";
         
     PreparedStatement pstmt    = Mycon.prepareStatement(balanceQuery);
		
		  pstmt.setString(1,sender);
		 ResultSet res = pstmt.executeQuery();
		 int balance = 0;
		 if(res.next()) {
			// int balance = 0;
			balance = res.getInt("salary");
			 
	 }
	int i  =	 updateBalance(Mycon,sender,-amount);
		  
	int j  =	 updateBalance(Mycon,reciever,amount);

		boolean flag =checkTransaction(amount, i, j ,balance);
		
		if(flag) {
			Mycon.commit();
			System.out.println("Transaction Sucessful");
		}
		else {
			Mycon.rollback();
			System.out.println("Transaction Failed");

		}

		
		
		Myst = Mycon.createStatement();
		
		return Myst;
		
		
		
			
		
	}

	  public static boolean checkTransaction(int amount, int i, int j ,int balance) {
		
		System.out.println("Do You Want to Confirm the Transaction (Yes/No)");
		
		 String choice =  scan.next();
		 
	return choice.equalsIgnoreCase("Yes")&& balance >= amount && i == 1 && j ==1;
		 
	
		
		
		
		
	}

	public static int updateBalance(Connection mycon, String sender, int amount) throws SQLException {
		
		// TODO Auto-generated method stub
		String sql = "UPDATE `emp` "
		           + " SET `salary`=`salary`+ ? "
		           + "WHERE `Name`=?";
		 PreparedStatement pstmt    = mycon.prepareStatement(sql);
		   pstmt.setInt(1, amount);
		   pstmt.setString(2, sender);
		  
		 int i =  pstmt.executeUpdate();
		
		return i;
		
		
		
	}

}
