/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mpisc
 */
public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente(nome, celular,email, dataCadastro) VALUES(?, ?, ?, ?)";
        String sqlPF = "INSERT INTO pessoaFisica(id_cliente,cpf,dataNascimento) VALUES(?, ?, ?)";
        String sqlPJ = "INSERT INTO pessoaJuridica(id_cliente,cnpj,inscricaoEstadual) VALUES(?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setDate(4, cliente.getDataCadastro());
            stmt.execute();

            if (cliente instanceof PessoaFisica) {
                stmt = connection.prepareStatement(sqlPF);
                stmt.setString(2, ((PessoaFisica) cliente).getCpf());
                stmt.setDate(3, ((PessoaFisica) cliente).getDataNascimento());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlPJ);
                stmt.setString(2, ((PessoaJuridica) cliente).getCnpj());
                stmt.setString(3, ((PessoaJuridica) cliente).getInscricaoEstadual());
                stmt.execute();
            }
            connection.commit();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
                System.out.println("rollback executado com sucesso!!!");
            } catch (SQLException e) {
                System.out.println("falha na operação roolback...");
                throw new RuntimeException(e);
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
        public boolean alterar (Cliente cliente) {
            String sql = "UPDATE cliente SET nome=?, celular=?, email=?, dataCadastro=? WHERE id=?";
            String sqlPF = "UPDATE pessoaFisica SET cpf=?, dataNacimento=?  WHERE id_cliente = ?";
            String sqlPJ = "UPDATE pessoaJuridica SET cnpj=?, inscricaoEstadual=? WHERE id_cliente = ?";
            try {
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getCelular());
                stmt.setString(3, cliente.getEmail());
                stmt.setInt(4, cliente.getId());
                stmt.execute();
                if (cliente instanceof PessoaFisica) {
                    stmt = connection.prepareStatement(sqlPF);
                    stmt.setString(1, ((PessoaFisica)cliente).getCpf());
                    stmt.setDate(2, ((PessoaFisica)cliente).getDataNascimento());
                    stmt.setInt(3, cliente.getId());
                    stmt.execute();
                } else {
                    stmt = connection.prepareStatement(sqlPJ);
                    stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
                    stmt.setString(2, ((PessoaJuridica)cliente).getInscricaoEstadual());
                    stmt.setInt(3, cliente.getId());
                    stmt.execute();
                }
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }




    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = populateVO(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        String sql = "SELECT * FROM cliente c "
                + "LEFT JOIN pessoaFisica pf on pf.id_cliente = c.id "
                + "LEFT JOIN pessoaJuridica pj on pj.id_cliente = c.id WHERE id=?";
        Cliente retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente = null;

        if (rs.getString("cpf") == null || rs.getString("cpf").length() <= 0) {
            //é um fornecedor nacional
            PessoaFisica Cliente = new PessoaFisica();
            ((PessoaFisica)Cliente).setCpf(rs.getString("cpf"));
            ((PessoaFisica)Cliente).setDataNascimento(rs.getDate("dataNascimento"));
            cliente = Cliente;
        } else if (rs.getString("cnpj") == null || rs.getString("cnpj").length() <= 0) {
       //é um fornecedor internacional
            PessoaJuridica Cliente = new PessoaJuridica();
            (Cliente).setCnpj(rs.getString("cnpj"));
            (Cliente).setInscricaoEstadual(rs.getString("inscricaoEstadual"));
            cliente = Cliente;
        }
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCelular(rs.getString("celular"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDataCadastro(rs.getDate("dataCadastro"));
        return cliente;
    }
}
