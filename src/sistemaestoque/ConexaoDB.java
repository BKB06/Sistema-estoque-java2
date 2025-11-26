package sistemaestoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
public class ConexaoDB {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST = "localhost";
    private static final String PORTA = "3306";
    private static final String NOME_BANCO = "db_estoque";
    private static final String TIMEZONE = "serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORTA + "/" + NOME_BANCO + "?" + TIMEZONE;
    private static final String USUARIO = "root";
    private static final String SENHA = ""; 
    public static Connection getConnection() {    //conexao
        Connection conexao = null;
        try {
            Class.forName(DRIVER);
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            conexao.setAutoCommit(true); // Garante salvamento automático
            return conexao;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Falha ao carregar o driver do banco de dados!\nVerifique se o MySQL Connector/J foi adicionado às bibliotecas.",
                "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Falha ao conectar com o banco de dados!\n" +
                "Verifique se o servidor MySQL está ligado e se as credenciais estão corretas.\n\n" +
                "Erro: " + e.getMessage(),
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            System.exit(1); 
        }
        return null;
    }
}
