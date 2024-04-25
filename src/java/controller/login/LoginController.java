/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import controller.Constants;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

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
        AcountDAO accDAO = new AcountDAO();
        String code = request.getParameter("code");
        String user = request.getParameter("Username");
        String pass = request.getParameter("Password");

        if (code != null) {
            // Xử lý đăng nhập qua Google OAuth
            String accessToken = getToken(code);
            Account acc = getUserInfo(accessToken);
            Account existingAccount = accDAO.checkAccountExist(acc.getEmail());

            if (existingAccount == null) {
                // Tạo một tài khoản mới và gắn email vào user
                acc.setUser(acc.getEmail());
                accDAO.insertAccount(acc.getUser(), "default_password");
                HttpSession session = request.getSession();
                session.setAttribute("acc", acc);
                response.sendRedirect("index.jsp");
            } else {
                // Tài khoản đã tồn tại, tiến hành đăng nhập
                HttpSession session = request.getSession();
                session.setAttribute("acc", existingAccount);
                response.sendRedirect("index.jsp");
            }
        } else if (user != null && pass != null) {
            // Xử lý đăng nhập thông thường

            Account account = accDAO.login(user, pass);

            if (account == null) {
                request.setAttribute("mess", "Wrong user or pass");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else if (!account.isActive()) {
                request.setAttribute("mess", "Account has been banned");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("acc", account);
                response.sendRedirect("index.jsp");
            }
        } else {
            // Xử lý các trường hợp khác
            response.sendRedirect("login.jsp");
        }
    }

    public static String getToken(String code) throws ClientProtocolException, IOException {
        // call api to get token
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    public static Account getUserInfo(final String accessToken) throws IOException {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(Constants.GOOGLE_LINK_GET_USER_INFO + accessToken))
                .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Response from API: " + response.body()); // In ra phản hồi từ API
                return new Gson().fromJson(response.body(), Account.class);
            } else {
                System.err.println("Error retrieving user info. Status code: " + response.statusCode());
                return null;
            }
        } catch (InterruptedException e) {
            // Xử lý ngoại lệ nếu có
            e.printStackTrace();
            return null;
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
