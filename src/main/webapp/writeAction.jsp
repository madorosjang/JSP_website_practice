<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %><!-- 자바스크립트 문장 사용 위한 것 -->
<%@ page import="bbs.BbsDAO" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"/>
<jsp:setProperty name="bbs" property="bbsTitle"/>
<jsp:setProperty name="bbs" property="bbsContent"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 화면 최적화, 반응형웰을 위한 메타태크 -->
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%	
		//현재 세션 상태를 체크한다
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		if(userID == null){//로그인 안돼있을 경우
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인 후 게시판 이용 가능합니다.')");
			script.println("location.href='login.jsp'");
			script.println("</script>");
		}else{//로그인 돼있는 경우
			if(bbs.getBbsTitle()==null || bbs.getBbsContent() == null){
				// 입력 안 된 부분이 있는지 체크
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력이 안 된 사항이 있습니다.')");
				script.println("history.back()");
				script.println("</script>");
			}else{// 모든 사항이 입력 된 경우
				BbsDAO bbsDAO = new BbsDAO();
				int result = bbsDAO.write(bbs.getBbsTitle(),userID,bbs.getBbsContent()); //db에 저장됨
				if( result == -1){//글쓰기에 실패한 경우
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기에 실패했습니다.')");
					script.println("history.back()");
					script.println("</script>");
				}else{// 글쓰기 이상 없는 경우
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('정상적으로 작성되었습니다')");
					script.println("location.href='bbs.jsp'");
					script.println("</script>");
				}
			}
		}
	%>
</body>
</html>