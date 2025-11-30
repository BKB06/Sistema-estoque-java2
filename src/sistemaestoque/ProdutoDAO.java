
package sistemaestoque;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
public class ProdutoDAO {

   public void inserir(Produto produto) {
       
        String sql = "INSERT INTO produtos (nome, descricao, quantidade, preco, fornecedor) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setString(5, produto.getFornecedor()); 
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idNovo = generatedKeys.getInt(1);
                    new MovimentacaoDAO().registrarMovimentacao(idNovo, "ENTRADA", produto.getQuantidade());
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir: " + e.getMessage());
        }
    }
    public List<Produto> listar() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setFornecedor(rs.getString("fornecedor")); 
                produtos.add(produto);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage(), "Erro no DAO", JOptionPane.ERROR_MESSAGE);
        }
        return produtos;
    }
    public void atualizar(Produto produto) {
   
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, quantidade = ?, preco = ?, fornecedor = ? WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setString(5, produto.getFornecedor()); 
            stmt.setInt(6, produto.getId()); 
            stmt.executeUpdate();  
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage());
        }
    }
    public void excluir(int id) {
       
        String sqlHistorico = "DELETE FROM movimentacoes WHERE id_produto = ?";
       
        String sqlProduto = "DELETE FROM produtos WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConnection()) {
            try (PreparedStatement stmtHist = conn.prepareStatement(sqlHistorico)) {
                stmtHist.setInt(1, id);
                stmtHist.executeUpdate();
            }
            try (PreparedStatement stmtProd = conn.prepareStatement(sqlProduto)) {
                stmtProd.setInt(1, id);
                stmtProd.executeUpdate();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir produto: " + e.getMessage(), "Erro no DAO", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ?";
        
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setPreco(rs.getBigDecimal("preco"));
                    
                   
                    produto.setFornecedor(rs.getString("fornecedor"));
                    
                    produtosEncontrados.add(produto);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar produtos: " + e.getMessage(), "Erro no DAO", JOptionPane.ERROR_MESSAGE);
        }
        return produtosEncontrados;
    }
}
