/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.login;

import dal.AcountDAO;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SignupController", urlPatterns = {"/signup"})
public class SignupController extends HttpServlet {

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

        // lay ra user va pass va repass tu trang sign up de truy van
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String repass = request.getParameter("repass");

        // neu pass va repass khac nhau thi tra ve message va giu nguyen nguoi dung o lai
        if (!pass.equals(repass)) {
            request.setAttribute("mess", "Pass not match!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } 
        // kiem tra xem tai khoan da ton tai tren co so du lieu chua, neu chua thi tien hanh set du lieu de insert 
        else {
            AcountDAO accDAO = new AcountDAO();
            Account account = accDAO.checkAccountExist(user);
            if (account == null) {
                Account accountNew = new Account();
                accountNew.setUser(user);
                accountNew.setPass(pass);
                
                //tra ve trang home khi vua dang ki xong thanh cong
                HttpSession session = request.getSession();
                session.setAttribute("acc", accountNew);
                
                accDAO.insertAccount(user, pass);
                response.sendRedirect("index.jsp");
            }
            // neu tai khoan da trung thi thong bao message va giu nguoi dung o lai trang login 
            else {
                request.setAttribute("mess", "Account Exist! Please fill different user");
                request.getRequestDispatcher("login.jsp").forward(request, response);
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
