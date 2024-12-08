import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CustomerDashboardServlet")
public class CustomerDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/agriculture_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Soh@il786";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String location = request.getParameter("location");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM farmers WHERE address = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, location);

            rs = pstmt.executeQuery();

            out.println("<!DOCTYPE html><html><head><title>Customer Dashboard</title>");
            out.println("<style>");
            out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
            out.println("th { background-color: #4CAF50; color: white; }");
            out.println("</style></head><body>");
            out.println("<h1>Farmers in Your Location</h1>");
            out.println("<table>");
            out.println("<tr><th>Name</th><th>Farm Size</th><th>Crops</th><th>Experience</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("farm_size") + "</td>");
                out.println("<td>" + rs.getString("crops") + "</td>");
                out.println("<td>" + rs.getString("experience") + " years</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error fetching data: " + e.getMessage() + "</h1>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
