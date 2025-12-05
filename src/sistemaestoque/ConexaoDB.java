package sistemaestoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
public class ConexaoDB {

    public static Connection getConnection() {
      
        String url = "jdbc:mysql://localhost:3306/db_estoque";
        String user = "root";
        String password = "";

        Connection conn = null;
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            
            conn = DriverManager.getConnection(url, user, password);

            conn.setAutoCommit(true); 
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro: Driver JDBC não encontrado.", "Erro de Driver", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO DE CONEXÃO. Verifique o XAMPP.\nDetalhe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
