package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	private Connection conn; //자바와 db연결
	private ResultSet rs; //결과값 받아오기
	
	public BbsDAO() { //mysql접속하게 해주는 부분
		try {
			String dbURL ="jdbc:mysql://localhost:3306/BBS"; //mysql과 연결시켜주는 주소
			String dbID ="root"; //mysql 계정
			String dbPassword ="12345"; //mysql 계정 비밀번호
			Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 연결 클래스를 string타입으로 불러온다
			conn = DriverManager.getConnection(dbURL,dbID,dbPassword);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public String getDate() { 	//작성일자 메소드
		String SQL ="select now()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			rs=pstmt.executeQuery(); //rs로 실행하고 나서 결과 가져옴
			if(rs.next()) {//결과 있는경우
				return rs.getString(1);//현재날짜반환
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ""; //데이터베이스 오류
	}
	public int getNext() { //게시글 번호 부여 메소드
		String SQL ="select bbsID from BBS order by bbsID DESC"; //현재 게시글을 내림차순으로 조회한 후 가장 마지막 글의 번호를 구한다
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			rs=pstmt.executeQuery(); //rs로 실행하고 나서 결과 가져옴
			if(rs.next()) {//결과 있는경우
				return rs.getInt(1) + 1;//그 다음 게시글 반환
			}
			return 1; //현재가 첫번째 게시물일 경우
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	public int write(String bbsTitle, String userID, String bbsContent) { //글쓰기 메소드
		String SQL = "insert into bbs values (?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1); //글의 유효번호
			return pstmt.executeUpdate(); //성공 시 0이상인 결과를 반환
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //실패시 오류 반환
	}
	public ArrayList<Bbs> getList(int pageNumber){
		String SQL ="select * from BBS where bbsID < ? and bbsAvailable =1 order by bbsID DESC limit 10"; //
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			pstmt.setInt(1, getNext()-(pageNumber - 1)*10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Bbs bbs =  new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list; //
	}
	
	public boolean nextPage(int pageNumber) {//페이징 처리 위해 존재하는 함수
		String SQL ="select * from BBS where bbsID < ? and bbsAvailable =1"; //
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			pstmt.setInt(1, getNext()-(pageNumber - 1)*10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;//다음페이지로 넘어갈수 있음을 뜻함
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false; //
	}
	public Bbs getBbs(int bbsID) {
		String SQL ="select * from BBS where bbsID = ?"; //
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Bbs bbs =  new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null; //오류시 반환
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) {//글 수정 메소드
		String SQL = "update bbs set bbsTitle = ?, bbsContent = ? where bbsID = ? ";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate(); //성공 시 0이상인 결과를 반환
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //실패시 오류 반환
	}
	
	public int delete(int bbsID) {// 글 삭제 메소드
		String SQL = "update bbs set bbsAvailable = 0 where bbsID=? ";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //SQL문장 실행준비단계
			pstmt.setInt(1, bbsID); //해당bbsID번호의 bbsAvailable값을 0으로 바꿈으로써 삭제한다
			return pstmt.executeUpdate(); //성공 시 0이상인 결과를 반환
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //실패시 오류 반환
	}
}
