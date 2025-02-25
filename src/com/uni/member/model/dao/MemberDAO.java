package com.uni.member.model.dao;

import static com.uni.common.JDBCTemplate.close;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.uni.member.model.dto.Member;
/* 1.Connection 객체연결하기 
 * 2.Statement 객체 생성하기 
 * 3.ResultSet 객체 생성하기 
 * 4.Sql작성하기 
 * 5.ResultSet  결과담기 
 * 6.list 에 객체 하나씩 담기 
 * 7.close 하기 (자원반납 - 생성의 역순)
 */
/* DAO(Database Access Object) : 데이터베이스 접근용 객체
 *  => CRUD 연산을 담당하는 메소드들의 집합으로 이루어진 클래스이다.
 *  Create: 삽입 (Insert)
 *  Read : 조회 (Select)
 *  Update: 수정 (Update)
 *  Delete: 삭제(Delete)
 *  */
public class MemberDAO {
	
	private Properties prop = null;
	
	public MemberDAO() {
		prop = new Properties();
		try {
			prop.load(new FileReader("config/connection-info.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Member> selectAll(Connection conn) {
		ArrayList<Member> list = null;
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM member";//자동으로 세미콜론 붙여서 실행
//		String sql = prop.getProperty("selectAll");
		
		try {
//			3. 쿼리문을 실행할 statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
//			4. 쿼리문을 전송, 실행한 결과를 resultset으로 받기.
			rset = pstmt.executeQuery();
			
//			5. 받은 결과값을 객체에 옮겨서 저장하기
			list = new ArrayList<Member>();
			
			while(rset.next()) {
				Member m = new Member();
				m.setMember_no(rset.getInt("member_no"));
				m.setMember_id(rset.getString("member_id"));
				m.setMember_pwd(rset.getString("member_pwd"));
				m.setMember_name(rset.getString("member_name"));
				
				list.add(m);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				close(rset);
				close(pstmt);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		
		return list;
	}

	public Member selectOne(Connection conn, String memberId) {
		Member m = null;
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM member WHERE member_id = ?";
//		String sql = prop.getProperty("selectOne");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
//			4. 쿼리문을 전송, 실행한 결과를 resultset으로 받기.
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				m = new Member();
				m.setMember_no(rset.getInt("member_no"));
				m.setMember_id(rset.getString("member_id"));
				m.setMember_pwd(rset.getString("member_pwd"));
				m.setMember_name(rset.getString("member_name"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return m;
	}

	public List<Member> selectByName(Connection conn, String memberName) {
		ArrayList<Member> list = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;// Select 한후 결과값 받아올 객체

		String sql = "SELECT * FROM member WHERE member_name LIKE ?";// 자동으로 세미콜론을 붙여 실행되므로 붙히지않는다
//		String sql = prop.getProperty("selectByName");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+memberName+"%");
			
			// 4.쿼리문 전송, 실행결과를 ResultSet 으로 받기
	//		rset = stmt.executeQuery(sql);
			rset = pstmt.executeQuery();
	
			// 5. 받은결과값을 객체에 옮겨서 저장하기
	
			list = new ArrayList<Member>();
	
			while (rset.next()) {
	
				Member m = new Member();
				m.setMember_no(rset.getInt("member_no"));
				m.setMember_id(rset.getString("member_id"));
				m.setMember_pwd(rset.getString("member_pwd"));
				m.setMember_name(rset.getString("member_name"));
	
				list.add(m);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}

		return list;
	}

	public int insertMember(Connection conn, Member m) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
//		String sql = "INSERT INTO member VALUES(?,?,?,?,?,?,?,?,?,sysdate)";
		String sql = "INSERT INTO member(member_id, member_pwd, member_name) VALUES(?,?,?)";
//		String sql = prop.getProperty("insertMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getMember_id());
			pstmt.setString(2, m.getMember_pwd());
			pstmt.setString(3, m.getMember_name());
			
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateMember(Connection conn, Member m) {
		int result = 0;
		PreparedStatement pstmt = null;// 실행할 쿼리
		
//		String sql = "UPDATE MEMBER SET PASSWORD = ? ,EMAIL = ? ,PHONE =? ,ADDRESS = ? WHERE USERID = ?";
		String sql = "UPDATE member SET member_pwd = ?, member_name = ? WHERE member_no = ?";
//		String sql = prop.getProperty("updateMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getMember_pwd());
			pstmt.setString(2, m.getMember_name());
			pstmt.setString(3, m.getMember_id());
			
			conn.setAutoCommit(false);
			result = pstmt.executeUpdate();//처리한 행의 갯수를 리턴(int형)
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int deleteMember(Connection conn, String memberId) {
		int result = 0;
		PreparedStatement pstmt = null;// 실행할 쿼리
		
		String sql = "DELETE FROM MEMBER WHERE member_id = ?";
//		String sql = prop.getProperty("deleteMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			result = pstmt.executeUpdate();//처리한 행의 갯수를 리턴(int형)

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}
}
