/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzzbuzz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author wwwsh
 */
@WebServlet(name = "RemoveWord", urlPatterns = {"/buzzbuzz/RemoveWord"})
public class RemoveWord extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Connection con = null;
            Statement st = null;
            Statement st2 = null;
            List<PostedWordInfo> words = (ArrayList<PostedWordInfo>)request.getAttribute("words");
            try{
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                con = DriverManager.getConnection("jdbc:derby://localhost:1527/buzzword", "db", "db");
                st = con.createStatement();
                st2 = con.createStatement();
                ResultSet rs = st.executeQuery("select * from BUZZWORDS");
                while(rs.next()){
                    String w = rs.getString("WORD");
                    if(request.getParameter(w) != null){
                        st2.executeUpdate("delete from BUZZWORDS where WORD='" + w + "'");
                        st2.executeUpdate("delete from INVEST_INFO where INVEST_WORD='" + w + "'");
                    }
                }
                rs.close();
                RequestDispatcher dis = request.getRequestDispatcher("PostRemoveWord");
                dis.forward(request, response);
            }catch(Exception e){
                throw new ServletException(e);
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                        throw new ServletException(e);
                    }
                }
                if (st2 != null) {
                    try {
                        st2.close();
                    } catch (SQLException e) {
                        throw new ServletException(e);
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        throw new ServletException(e);
                    }
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
