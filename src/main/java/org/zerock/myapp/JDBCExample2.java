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
public class JDBCExample2 {
	
	// Step1. JDBC 4개 필수정보 선언
	static String driver = "oracle.jdbc.OracleDriver";			// JDBC Driver 안에 포함된 구동기 클래스명
	static String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/xepdb1";			// 연결할 DB서버의 주소
	static String dbUser = "hr";			// 접속할 계정명
	static String dbPass = "oracle";			// 계정의 암호

	
	public static void main(String[] args) {
		log.trace("main({}) invoked.", Arrays.toString(args));
		
		// Step2. 반드시 필요한 JDBC 인터페이스 타입의 지역변수 선언
		Connection conn = null;
		Statement stmt = null;
//		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// Step3. JDBC Connection 획득 with DriverManger class.
			conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
			
			// Step4. Step3 에서 획득한 Connection 으로부터, Statement 객체 생성
			stmt = conn.createStatement();
			
			// Step5. Step4에서 얻은 Statement 객체의 executeQuery() 메소드로 
			//        SQL전송하고, 결과셋(ResultSet) 획득
			//		  JDBC로 수행시킬 SQL문장의 끝에는 세미콜론(;)을 붙이지 않음(***)
			rs = stmt.executeQuery("SELECT current_date AS now FROM dual;");
	
			// Step6. Step5에서 얻은 ResultSet 에서 현재날짜 획득
			Date now;
	//		if(rs.next()) {
				rs.next();	// 결과셋 테이블에서, 기본 포인터(위치)는 첫번째 행이 아니라
							// 첫번째 행 직전에 위치하기 때문에, 이 메소드로 다음행으로 이동
				now = rs.getDate("now");
				log.info("now: {}", now);
	//		} // if
		} catch(SQLException e) {
			e.printStackTrace();
			
			// 발생하는 오류코드에 따라, 예외처리를 다르게 하는 경우
			int errCode = e.getErrorCode();
			log.info("errCode: {}", errCode);
			
			switch(errCode) {
				case 933: // To do ....
					break;
				case 100: // To do ....
					break;
				default:  // To do ...
					break;
			} // switch
		} finally {					// 필수적으로 여기서 자원해제하자!
			// Step7. 자원객체 해제
			try { rs.close(); 	} catch(SQLException ignored) {;;}
			try { stmt.close(); } catch(SQLException ignored) {;;}
			try { conn.close(); } catch(SQLException ignored) {;;}
		} // try-catch-finally
	} // main

} // end class

