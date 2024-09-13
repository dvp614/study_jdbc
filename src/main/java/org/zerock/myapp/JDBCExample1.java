package org.zerock.myapp;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class JDBCExample1 {
	// JDBC 연결정보 (필수)
	static final String user="HR";
	static final String pass="oracle";
	static final String tnsname="XEPDB1";		// (1) TNS
	static final String ipAddress="localhost";	// (2) EZ
	static final int 	port =1521;				// (2) EZ
	static final String dbName="XEPDB1";		// (2) EZ
	// 10.12.232.120
	// 각 DB 벤더가 제공한 JDBC Driver 를 사용하려면,
	// 아래의 정보를 알아야 합니다. 이는 각 DB벤더마다 다르게 문서로 제공됩니다.
	// (1) 실제 오라클 JDBC Driver(구동기) 클래스의 FQCN을 알아야 합니다.
	
	static final String driverClass = "oracle.jdbc.OracleDriver";
	
	// (2) JDBC표준에 따라, 웹브라우저의 URL주소와 비슷하게, 연결할 DB에 대한
	//     주소형식을 표준으로 지정해 놓았습니다. 오라클은 아래와 같이 JDBC URL을 제공합니다.
	static final String jdbcURL1="jdbc:oracle:thin:@localhost/XEPDB1";  				// (1) TNSNAMES 방식인 경우
	static final String jdbcURL2="jdbc:oracle:thin:@DESKTOP-J8C96E9:1521/XEPDB1"; // (2) EZCONNECT
	
	
	
	public static void main(String...args) throws SQLException {
		log.trace("main({}) invoked.", Arrays.toString(args));
		
		// 1. 연결생성
		Connection conn = null;
		conn = DriverManager.getConnection(jdbcURL1, user, pass);
		log.info("\t+ conn: {}", conn);
		
		// 2. Statement (SQL문장을 객체로 만든거) 객체 생성
		// 동적SQL (Dynamic SQL) <-> 준비된SQL (Prepared SQL)
//		Statement stmt = conn.createStatement();
//		log.info("\t+ stmt : {}", stmt);
		
		boolean doYouWantCommissionPct = false;
		
		// ? : Bind Variable, 바인드 변수의 개수는 0개 이상입니다. (count(x) > 0)
		String sql = "SELECT * FROM employees "
					+ "WHERE salary > ? AND last_name LIKE '%'||?||'%'";
		
		if(doYouWantCommissionPct) { // 영업수수료가 있는 직원을 원할 때만s
			// 아래 3번째 체크조건을 추가로 붙임.
			sql  = sql + "AND commission_pct IS NOT NULL";
		}// if
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		log.info("\t+ pstmt : {}", pstmt);
		
		// 위의 바인드변수(?)마다, 값을 지정(binding) 해줌
		pstmt.setDouble(1, 2000);
		pstmt.setString(2, "K");
		
		// 3. 실제 수행시킬 SQL문을 준비하고, 준비된 SQL 문장을
		//    위 2에서 얻어낸, Statement 객체의 메소드 를 통해, DB서버로 전송
//		ResultSet rs = pstmt.executeQuery(sql); // Dynamic SQL 실행방식 (DQL only)
		ResultSet rs = pstmt.executeQuery(); // Prepared SQL 실행방식 (DQL only)
		log.info("\t+ rs : {}", rs);
		
		// 4. 결과셋객체(ResultSet)로 부터, 각 사원정보를 얻어내자!
		List<String> employees = new ArrayList<>();
		
		while(rs.next()) {
			int 	employeeId 		= rs.getInt("EMPLOYEE_ID");
			String 	firstName 		= rs.getString("FIRST_NAME");
			String 	lastName 		= rs.getString("LAST_NAME");
			String 	email 			= rs.getString("EMAIL");
			String 	phoneNumber 	= rs.getString("PHONE_NUMBER");
			Date 	hireDate 		= rs.getDate("HIRE_DATE");
			String 	jobId 			= rs.getString("JOB_ID");
			Double 	salary		  	= rs.getDouble("SALARY");
			Double 	commissionPct 	= rs.getDouble("COMMISSION_PCT");
			int 	managerId 		= rs.getInt("MANAGER_ID");
			int 	departmentId 	= rs.getInt("DEPARTMENT_ID");
			
			String employee =
					String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
							employeeId,
							firstName,
							lastName,
							email,
							phoneNumber,
							hireDate,
							jobId,
							salary,
							commissionPct,
							managerId,
							employeeId,
							departmentId
						);
			
//			String employee = "%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s"
//					.formatted(
//							employeeId, firstName, lastName, email, 
//							phoneNumber, hireDate, jobId, salary, 
//							commissionPct, managerId, departmentId
//					);
			
			log.info("*********************************************");
//			log.info("employee : {}", employee);
			
//			log.info("EMPLOYEE_ID : {}", employeeId);
//			log.info("FIRST_NAME : {}", firstName);
//			log.info("LAST_NAME : {}", lastName);
//			log.info("EMAIL : {}", email);
//			log.info("PHONE_NUMBER : {}", phoneNumber);
//			log.info("HIRE_DATE : {}", hireDate);
//			log.info("JOB_ID : {}", jobId);
//			log.info("SALARY : {}", salary);
//			log.info("COMMISSION_PCT : {}", commissionPct);
//			log.info("MANAGER_ID : {}", managerId);
//			log.info("DEPARTMENT_ID : {}", departmentId);
			
			employees.add(employee);
		} // while
		
		// N. 연결해제
		assert (rs != null && !( rs.isClosed() ));
		rs.close();
		
//		stmt.close();
		if(pstmt != null && !( rs.isClosed() )) {
			pstmt.close();
		} // if
		
		assert (conn != null && !( rs.isClosed() ));
		conn.close();
		
//		log.info("\t+ employees : {}", employees);
//		employees.forEach(s -> log.info(s));
//		employees.forEach(log::info);
		
		// void accept;
		employees.forEach(t -> log.info(t));
		
		employees.clear();
		employees = null;
	} // main 
	
} // end class
