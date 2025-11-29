package sistemaestoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat; // Importação necessária para a data
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MovimentacaoDAO {

    // Método para REGISTRAR uma entrada ou saída (usado quando salvamos um produto)
    public void registrarMovimentacao(int idProduto, String tipo, int quantidade) {
        String sql = "INSERT INTO movimentacoes (id_produto, tipo, quantidade) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProduto);
            stmt.setString(2, tipo);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar movimentação: " + e.getMessage());
        }
    }

    // Método para BUSCAR o histórico completo para o relatório PDF
    public List<String[]> listarParaRelatorio() {
        List<String[]> historico = new ArrayList<>();
        
        // SQL que une a tabela de movimentações com a de produtos para pegar o nome
        String sql = "SELECT p.nome, m.tipo, m.quantidade, m.data_hora " +
                     "FROM movimentacoes m " +
                     "JOIN produtos p ON m.id_produto = p.id " +
                     "ORDER BY m.data_hora DESC";
        
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // Formatador para deixar a data bonita (ex: 29/11/2025 14:30)
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            while (rs.next()) {
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo");
                String qtd = String.valueOf(rs.getInt("quantidade"));
                
                // Pega a data do banco e formata
                String data = "";
                if (rs.getTimestamp("data_hora") != null) {
                    data = sdf.format(rs.getTimestamp("data_hora"));
                }
                
                // Adiciona uma linha na lista com as 4 informações
                historico.add(new String[]{nome, tipo, qtd, data});
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar histórico: " + e.getMessage());
        }
        return historico;
    }
}