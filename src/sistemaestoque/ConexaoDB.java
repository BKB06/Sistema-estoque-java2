package sistemaestoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
public class ConexaoDB {
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String HOST = "localhost";
    private static String PORTA = "3306";
    private static String NOME_BANCO = "db_estoque";
    private static String TIMEZONE = "serverTimezone=UTC";
    private static String URL = "jdbc:mysql://" + HOST + ":" + PORTA + "/" + NOME_BANCO + "?" + TIMEZONE;
    private static String USUARIO = "root";
    private static String SENHA = ""; 
    public static Connection getConnection() {    
        Connection conexao = null;
        try {
            Class.forName(DRIVER);
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            conexao.setAutoCommit(true); // Garante salvamento automático
            return conexao;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Falha ao carregar o driver do banco de dados!.",
                "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Falha ao conectar com o banco de dados!\n" +
                "Verifique o xampp.\n\n" +
                "Erro: " + e.getMessage(),
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            System.exit(1); 
        }
        return null;
    }
}
