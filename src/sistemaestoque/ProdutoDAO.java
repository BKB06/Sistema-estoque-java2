
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
        // Adicionado 'fornecedor'
        String sql = "INSERT INTO produtos (nome, descricao, quantidade, preco, fornecedor) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) { // Importante para pegar o ID novo
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setString(5, produto.getFornecedor()); // <--- Novo
            stmt.executeUpdate();
            
            // Lógica para registrar a movimentação inicial (Entrada)
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
                
                // --- AQUI ESTÁ A NOVIDADE ---
                produto.setFornecedor(rs.getString("fornecedor")); 
                
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
        // Adicionado 'fornecedor'
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, quantidade = ?, preco = ?, fornecedor = ? WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setString(5, produto.getFornecedor()); // <--- Novo
            stmt.setInt(6, produto.getId()); // O ID agora é o 6º parâmetro
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage());
        }
    }

    /**
     * Exclui um produto do banco de dados com base no seu ID.
     * @param id O ID do produto a ser excluído.
     */
    public void excluir(int id) {
       // Passo 1: Apagar o histórico de movimentações deste produto
        String sqlHistorico = "DELETE FROM movimentacoes WHERE id_produto = ?";
        // Passo 2: Apagar o produto em si
        String sqlProduto = "DELETE FROM produtos WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConnection()) {
            
            // Executa a exclusão do histórico primeiro
            try (PreparedStatement stmtHist = conn.prepareStatement(sqlHistorico)) {
                stmtHist.setInt(1, id);
                stmtHist.executeUpdate();
            }
            
            // Agora que o produto não tem mais "filhos", podemos excluí-lo
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
                    
                    // --- AQUI TAMBÉM ---
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
