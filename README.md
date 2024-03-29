# JSP_website_practice
유튜브 '동빈나'님 JSP 게시판 강좌 클론코딩(개인 프로젝트)

### 진행 기간
- 23.02.27 ~ 23.03.15

### 역할
- JSP와 bootstrap을 활용한 반응형 웹페이지 제작
- Java와 MySQL을 활용한 Back-end 구현

### 개발 환경
- IDE : Eclipse
- MySQL 8.0

### Front-end
- JSP
- bootstrap

### Back-end
- Java
- MySQL

### 프로젝트 설명
- main 페이지와 게시판으로 구성 된 반응형 웹페이지 구현
- 새로운 회원의 아이디, 이메일, 비밀번호 등 정보를 MySQL을 통해 서버에 저장

### 프로젝트 성과
- 로그인, 회원가입, 회원탈퇴 기능 구현
  ```
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
  ```
- 게시판 글 작성, 수정, 삭제 기능 구현
```
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
```
- 제목, 작성자, 제목 & 작성자를 이용한 게시글 검색 구현
![1](https://github.com/madorosjang/JSP_website_practice/assets/122807795/8f3b7055-73cd-4c56-ba30-32166db98c03)
![2](https://github.com/madorosjang/JSP_website_practice/assets/122807795/3f0820df-8c58-40ec-b95d-ea767a672b1e)
![3](https://github.com/madorosjang/JSP_website_practice/assets/122807795/728e2e08-41b9-4fbd-9c63-955ab0855613)
![4](https://github.com/madorosjang/JSP_website_practice/assets/122807795/20448439-f117-4494-9abd-4f54ec73656e)
![5](https://github.com/madorosjang/JSP_website_practice/assets/122807795/344566a0-a239-44e8-b731-a7344b5161f7)

### 코드 출처
- 유튜브 '동빈나'

### 프로젝트 후 배운 점
- Java와 MySQL과 연동한 Back-end 구현 방법 습득
- 데이터 베이스 연동에 필요한 UserDAO 등 여러 클래스와 함수를 구현 방법 습득
- JSP와 부트스트랩을 활용한 반응형 웹페이지 Front-end 구현 지식 습득

___
#### 참고자료 
- 인생최적화, 티스토리블로그,<https://happy-inside.tistory.com/entry/JSP-JSP-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EB%A7%8C%EB%93%A4%EA%B8%B0-1%EA%B0%95-%EC%A4%80%EB%B9%84>, 2020.07.04
- 동빈나, 유튜브, <https://www.youtube.com/watch?v=wEIBDHfoMBg&list=PLRx0vPvlEmdAZv_okJzox5wj2gG_fNh_6>, 2017.05.04


