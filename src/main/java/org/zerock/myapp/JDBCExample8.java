package org.zerock.myapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class JDBCExample8 {
	
	// Step1. JDBC 4개 필수정보 선언
	static String driver = "oracle.jdbc.OracleDriver";			// JDBC Driver 안에 포함된 구동기 클래스명
	static String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/xepdb1";			// 연결할 DB서버의 주소
	static String dbUser = "hr";			// 접속할 계정명
	static String dbPass = "oracle";			// 계정의 암호

	
	// 하나의 Connection에서, 부서테이블을 CRUD하자!!!!
	public static void main(String[] args) {
		log.trace("main({}) invoked.", Arrays.toString(args));

		try {			
			Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
			
			// ---------------------
			// 1st. SQL : CREATE
			// ---------------------
//			String createSQL = 
//				"INSERT INTO departments (DEPARTMENT_ID, DEPARTMENT_NAME) "+
//			    "VALUES(?, ?)"; // 새로운 부서 생성
//			
//			PreparedStatement createPstmt = conn.prepareStatement(createSQL);
//			
//			try (createPstmt) {
//				createPstmt.setInt(1, 280);
//				createPstmt.setString(2, "MyDepartment");
//				
//				// 생성, 수정, 삭제 쿼리 수행 메소드
//				int affectedRows = createPstmt.executeUpdate();
//				log.info("\t+ affectedRows: {}", affectedRows);
//			} // try-with-resources
			
			// ---------------------
			// 2nd. SQL : UPDATE
			// ---------------------
			String updateSQL = 
					"UPDATE departments " 		+
					"SET " 						+
					"	manager_id = ?, " 		+
					"   location_id = ? " 		+
					"WHERE department_id = ?";
				
			PreparedStatement updatePstmt = conn.prepareStatement(updateSQL);
			
			try (updatePstmt) {
				updatePstmt.setInt(1, 100);
				updatePstmt.setInt(2, 1700);
				updatePstmt.setInt(3, 280);
				
				// 생성, 수정, 삭제 쿼리 수행 메소드
				int affectedRows = updatePstmt.executeUpdate();
				log.info("\t+ affectedRows: {}", affectedRows);
			} // try-with-resources
			
			// ---------------------
			// 2nd. SQL : REMOVE
			// ---------------------
			String deleteSQL = 
					"DELETE FROM departments " +
					"WHERE department_id = ?";
				
			PreparedStatement deletePstmt = conn.prepareStatement(deleteSQL);
			
			try (deletePstmt) {
				deletePstmt.setInt(1, 280);
				
				// DML: 생성, 수정, 삭제 쿼리 수행 메소드
				int affectedRows = deletePstmt.executeUpdate();
				log.info("\t+ affectedRows: {}", affectedRows);
			} // try-with-resources
		} catch(SQLException e) {
			e.printStackTrace();	
		} // try-catch
	} // main
		
} // end class

