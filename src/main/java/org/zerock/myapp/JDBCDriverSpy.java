package org.zerock.myapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class JDBCDriverSpy {
	
	// Step1. JDBC 4개 필수정보 선언
	static String driver = "oracle.jdbc.OracleDriver";			// JDBC Driver 안에 포함된 구동기 클래스명
	
	// 1st. condition : Driver Spy를 적용하기 위해, 아래의 url을 변경
//	static String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/xepdb1";			// 연결할 DB서버의 주소
	static String jdbcUrl = "jdbc:log4jdbc:oracle:thin:@localhost:1521/xepdb1";			// 연결할 DB서버의 주소
	
	// 2nd. condition : log4j2.xml 파일에, log4jdbc를 위한 logger 추가.
	
	static String dbUser = "hr";			// 접속할 계정명
	static String dbPass = "oracle";			// 계정의 암호

	
	public static void main(String[] args) {
		log.trace("main({}) invoked.", Arrays.toString(args));

		try {			
			Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT current_date AS now FROM dual");
		
			try (conn; stmt; rs;) {			 
				rs.next();
				
				Date now = rs.getDate("now");
				log.info("now: {}", now);
			} // try-with-resources			
		} catch(SQLException e) {
			e.printStackTrace();
		} // try-catch
	} // main

} // end class

