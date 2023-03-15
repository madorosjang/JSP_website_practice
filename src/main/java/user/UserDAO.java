package user;
//ctrl+shift+o -> 라이브러리 자동 추가
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	private Connection conn; //자바와 db연결
	private PreparedStatement pstmt; //쿼리문 대기 및 설정, SQL 인젝션 해킹 방어 기법
	private ResultSet rs; //결과값 받아오기
	
	public UserDAO() { //mysql접속하게 해주는 부분
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
	public int login(String userID, String userPassword) {//하나의 계정에 대한 로그인 시도를 하는 함수
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL); //sql쿼리문 대기
			pstmt.setString(1, userID); //?에 매개변수로 받아온 userID 대입
			rs = pstmt.executeQuery(); //쿼리 실행 결과 rs에 저장
			if(rs.next()) {//아이디가 있는 경우
				if(rs.getString(1).equals(userPassword)) 
					return 1; //로그인 성공
				else 
					return 0; //비밀번호 틀림
			}
			return -1; //아이디가 없다
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -2; //데이터 베이스 오류
	}
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
}
