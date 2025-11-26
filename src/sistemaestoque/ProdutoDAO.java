
package sistemaestoque;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * DAO (Data Access Object)
 * Classe responsável por todas as interações com a tabela 'produtos' no banco de dados.
 */
public class ProdutoDAO {

    /**
     * Insere um novo produto no banco de dados.
     * @param produto O objeto Produto contendo todos os dados a serem inseridos.
     */
    public void inserir(Produto produto) {
        String sql = "INSERT INTO produtos (nome, descricao, quantidade, preco) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir produto: " + e.getMessage(), "Erro no DAO", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca todos os produtos cadastrados no banco de dados.
     * @return uma Lista de objetos Produto.
     */
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
                produtos.add(produto);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage(), "Erro no DAO", JOptionPane.ERROR_MESSAGE);
        }
        return produtos;
    }

    /**
     * Atualiza um produto existente no banco de dados, baseado no ID.
     * @param produto O objeto Produto com os dados atualizados (deve conter o ID).
     */
    public void atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, quantidade = ?, preco = ? WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar produto: " + e.getMessage(), "Erro no DAO", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exclui um produto do banco de dados com base no seu ID.
     * @param id O ID do produto a ser excluído.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
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
                    produtosEncontrados.add(produto);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar produtos: " + e.getMessage(), "Erro no DAO", JOptionPane.ERROR_MESSAGE);
        }
        return produtosEncontrados;
    }
}
