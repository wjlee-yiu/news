package com.example.news;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet(urlPatterns = "/news.nhn")
public class NewsControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private NewsDAO dao;
    private ServletContext ctx;

    private final String START_PAGE = "ch10/newsList.jsp";
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = new NewsDAO();
        ctx = getServletContext();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action = request.getParameter("action");

        dao = new NewsDAO();

        Method m;
        String view = null;

        if(action == null) {
            action = "listNews";
        }
        try {
            m = this.getClass().getMethod(action, HttpServletRequest.class);
            view = (String)m.invoke(this, request);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            ctx.log("요청 action이 없음!!");
            request.setAttribute("error", "action 파라미터 오류");
            view = START_PAGE;
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(view.startsWith("redirect:/")) {
            String rview = view.substring("redirect:/".length());
            response.sendRedirect(rview);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher(view);
            dispatcher.forward(request, response);
        }
    }
    public String listNews(HttpServletRequest request) {
        List<News> list;

        try {
            list = dao.getAll();
            request.setAttribute("newslist", list);
        } catch(Exception e) {
            e.printStackTrace();
            ctx.log("목록 가져오기 오류");
            request.setAttribute("error", "목록 가져오기 오류!!");
        }
        return "ch10/newslist.jsp";
    }

}
