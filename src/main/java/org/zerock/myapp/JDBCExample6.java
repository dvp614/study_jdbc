package org.zerock.myapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class JDBCExample6 {
	
	// Step1. JDBC 4개 필수정보 선언
	static String driver = "oracle.jdbc.OracleDriver";			// JDBC Driver 안에 포함된 구동기 클래스명
	static String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/xepdb1";			// 연결할 DB서버의 주소
	static String dbUser = "hr";			// 접속할 계정명
	static String dbPass = "oracle";			// 계정의 암호

	
	// 수행시킬 SQL문장이 여러개 일 때에는, 어떻게 코딩해야 좋을까!?
	// (1) 각 SQL문장마다, 아래의 try-catch + try-with-resources 를 반복해서 사용
	// (2) 아래의 try-catch + try-with-resources 를 그대로 재사용하고,
	//     대신, 각 실행할 SQL문장만 바꿔서 실행
	public static void main(String[] args) {
		log.trace("main({}) invoked.", Arrays.toString(args));

		// -------------------------
		// 첫번째 SQL문장 실행
		// -------------------------
		try {			
			Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT current_date AS now FROM dual");
		
			try (conn; stmt; rs;) {			 
				rs.next();
				
				Date now = rs.getDate("now");
				log.info("1. now: {}", now);
			} // try-with-resources			
		} catch(SQLException e) {
			e.printStackTrace();
		} // try-catch

		// -------------------------
		// 두번째 SQL문장 실행
		// -------------------------
		// Prepared SQL로, 모든 부서목록 출력
		try {			
			Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);

			String sql = "SELECT * FROM departments WHERE department_id > ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 50);
			
			ResultSet rs = pstmt.executeQuery();
		
			try (conn; pstmt; rs;) {			 
				while(rs.next()) { 				
					int departmentId = rs.getInt("DEPARTMENT_ID");
					String departmentName = rs.getString("DEPARTMENT_NAME");
					int managerId = rs.getInt("MANAGER_ID");
					int locationId = rs.getInt("LOCATION_ID");
					
					String department = 
						"%s, %s, %s, %s"
						.formatted(departmentId, departmentName, managerId, locationId);
					
					log.info("2. department({})", department);
				} // while
			} // try-with-resources			
		} catch(SQLException e) {
			e.printStackTrace();
		} // try-catch

		// -------------------------
		// 세번째 SQL문장 실행
		// -------------------------
		// Prepared SQL로, 모든 부서목록 출력
		try {			
			Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);

			String sql = "SELECT * FROM departments WHERE manager_id IN (?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 200);
			pstmt.setInt(2, 201);
			pstmt.setInt(3, 203);
			
			ResultSet rs = pstmt.executeQuery();
		
			try (conn; pstmt; rs;) {			 
				while(rs.next()) { 				
					int departmentId = rs.getInt("DEPARTMENT_ID");
					String departmentName = rs.getString("DEPARTMENT_NAME");
					int managerId = rs.getInt("MANAGER_ID");
					int locationId = rs.getInt("LOCATION_ID");
					
					String department = 
						"%s, %s, %s, %s"
						.formatted(departmentId, departmentName, managerId, locationId);
					
					log.info("3. department({})", department);
				} // while
			} // try-with-resources			
		} catch(SQLException e) {
			e.printStackTrace();
		} // try-catch
		
	} // main

} // end class

