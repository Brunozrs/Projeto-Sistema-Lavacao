package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VeiculoDAO {

    private Connection connection;

    public Connection getConnection() {return connection;}

    public void setConnection(Connection connection) {this.connection = connection;}

    public boolean inserir(Veiculo veiculo) {
        final String sql = "INSERT INTO veiculo(placa,observacoes,id_cor,id_modelo,id_cliente) VALUES(?,?,?,?,?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            //registra o modelo
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3,veiculo.getCor().getId());
            stmt.setInt(4,veiculo.getModelo().getId());
            stmt.setInt(5,veiculo.getCliente().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa=?, observacoes=?,id_cor=?,id_modelo=?,id_cliente=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3,veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Veiculo> listar() {
        String sql = "SELECT * FROM veiculo";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Veiculo populateVO(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();

        veiculo.setId(rs.getInt("id"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setObservacoes(rs.getString("observacoes"));


        Cliente pessoaFisica = new PessoaFisica();
        Cliente pessoaJuridica = new PessoaJuridica();
        pessoaFisica.setId(rs.getInt("id_cliente"));
        pessoaJuridica.setId(rs.getInt("id_cliente"));
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.setConnection(connection);
        pessoaFisica = clienteDAO.buscar(pessoaFisica);
        pessoaJuridica = clienteDAO.buscar(pessoaJuridica);
        if(pessoaFisica != null){
            veiculo.setCliente(pessoaFisica);
        }else{
            veiculo.setCliente(pessoaJuridica);
        }

        Cor cor = new Cor();
        cor.setId(rs.getInt("id_cor"));
        CorDAO corDAO = new CorDAO();
        corDAO.setConnection(connection);
        cor = corDAO.buscar(cor);
        veiculo.setCor(cor);


        Modelo modelo = new Modelo();
        modelo.setId(rs.getInt("id_modelo"));
        ModeloDAO modeloDAO = new ModeloDAO();
        modeloDAO.setConnection(connection);
        modelo = modeloDAO.buscar(modelo);
        veiculo.setModelo(modelo);

        return veiculo;
    }

}
