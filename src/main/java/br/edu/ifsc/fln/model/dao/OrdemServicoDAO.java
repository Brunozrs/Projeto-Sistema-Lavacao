package br.edu.ifsc.fln.model.dao;


import br.edu.ifsc.fln.model.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdemServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(OrdemServico ordemServico) {
        String sql = "INSERT INTO ordem_de_servico(total, agenda, desconto, situacao, id_veiculo) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            stmt.setDouble(1, ordemServico.calcularServico());
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto());
            if (ordemServico.getStatus().name() != null) {
                stmt.setString(4, ordemServico.getStatus().name());
            } else {
                stmt.setString(4, EStatus.ABERTA.name());
            }
            stmt.setInt(5, ordemServico.getVeiculo().getId());
            // execute ou executeQuery
            stmt.execute();
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(connection);
            for (ItemOS itemOS : ordemServico.getItemOS()) {
                itemOS.setOrdemServico(this.buscarUltimaOrdemServico());
                itemOSDAO.inserir(itemOS);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {

        }
    }

    // Analisar novamente
    public boolean alterar(OrdemServico ordemServico) {
        String sql = "UPDATE ordem_de_servico SET total=?, agenda=?, desconto=?, situacao=?, id_veiculo=? WHERE numero=?";
        try {
            connection.setAutoCommit(false);
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(connection);

            OrdemServico ordemServicoAnterior = buscar(ordemServico);
            List<ItemOS> itensOS = itemOSDAO.listarPorOrdem(ordemServicoAnterior);

            for (ItemOS ios : itensOS) {
                itemOSDAO.remover(ios);
            }
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, ordemServico.calcularServico());
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto());
            if (ordemServico.getStatus().name() != null) {
                stmt.setString(4, ordemServico.getStatus().name());
            } else {
                stmt.setString(4, EStatus.ABERTA.name());
            }
            stmt.setInt(5, ordemServico.getVeiculo().getId());
            stmt.setLong(6, ordemServico.getNumero());
            stmt.execute();
            // Alterar o getItemOS para getItensOS e o mesmo para o atributo
            for (ItemOS ios : ordemServico.getItemOS()) {
                itemOSDAO.inserir(ios);
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException exc1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(OrdemServico ordemServico) {
        String sql = "DELETE FROM ordem_de_servico WHERE numero=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemOSDAO itemOSDAO = new ItemOSDAO();
                itemOSDAO.setConnection(connection);
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                stmt.setLong(1, ordemServico.getNumero());
                stmt.execute();
                connection.commit();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
                }
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<OrdemServico> listar() {
        String sql = "SELECT * FROM ordem_de_servico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Veiculo veiculo = new Veiculo();
                List<ItemOS> itensOS;

                ordemServico.setNumero(resultado.getInt("numero"));
                ordemServico.setTotal(resultado.getDouble("total"));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                ordemServico.setStatus(Enum.valueOf(EStatus.class, resultado.getString("situacao")));
                veiculo.setId(resultado.getInt("id_veiculo"));

                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(connection);
                veiculo = veiculoDAO.buscar(veiculo);

                ItemOSDAO itemOSDAO = new ItemOSDAO();
                itemOSDAO.setConnection(connection);
                itensOS = itemOSDAO.listarPorOrdem(ordemServico);

                ordemServico.setVeiculo(veiculo);
                ordemServico.setItemOS(itensOS);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordem_de_servico WHERE numero=?";
        OrdemServico retorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                retorno.setNumero(resultado.getLong("numero"));
                retorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                retorno.setDesconto(resultado.getDouble("desconto"));
                retorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("situacao")));
                veiculo.setId(resultado.getInt("id_veiculo"));
                retorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(int id) {
        String sql = "SELECT * FROM ordem_de_servico WHERE numero=?";
        OrdemServico retorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                retorno.setNumero(resultado.getLong("numero"));
                retorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                retorno.setDesconto(resultado.getDouble("desconto"));
                retorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));
                retorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscarUltimaOrdemServico() {
        String sql = "SELECT max(numero) as max FROM ordem_de_servico";
        // Alterar o nome da databela "max"
        OrdemServico retorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setNumero(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}