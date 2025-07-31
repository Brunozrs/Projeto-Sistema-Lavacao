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

public class ItemOSDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ItemOS itemOS) {
        String sql = "INSERT INTO itemOS(valor_do_servico, observacao, id_servico, id_os) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, itemOS.getValorServico());
            stmt.setString(2, itemOS.getObservacoes());
            stmt.setInt(3, itemOS.getServico().getId());
            stmt.setLong(4, itemOS.getOrdemServico().getNumero());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(ItemOS itemOS) {
        return true;
    }

    //Precisa desse remove? Ja possui o delete on cascade
    public boolean remover(ItemOS itemOS) {
        String sql = "DELETE FROM itemOS WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, itemOS.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<ItemOS> listar() {
        String sql = "SELECT * FROM itemOS";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valor_do_servico"));
                itemOS.setObservacoes(resultado.getString("observacao"));

                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setNumero(resultado.getInt("id_os"));

                //Obtendo os dados completos do Produto associado ao Item de Venda
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);

                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico);

                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);

                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<ItemOS> listarPorOrdem(OrdemServico ordemServico) {
        String sql = "SELECT * FROM itemOS WHERE id_os=?";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                // Entender o que acontece com essa OS
                OrdemServico os = new OrdemServico();
                //
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valor_do_servico"));
                itemOS.setObservacoes(resultado.getString("observacao"));

                servico.setId(resultado.getInt("id_servico"));
                os.setNumero(resultado.getInt("id_os"));

                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);

                itemOS.setServico(servico);
                itemOS.setOrdemServico(os);

                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public ItemOS buscar(ItemOS itemOS) {
        String sql = "SELECT * FROM itemOS WHERE id=?";
        ItemOS retorno = new ItemOS();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, itemOS.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valor_do_servico"));
                itemOS.setObservacoes(resultado.getString("observacao"));

                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setNumero(resultado.getInt("id_os"));

                //Obtendo os dados completos do Produto associado ao Item de Venda
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);

                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico);

                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);

                retorno = itemOS;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}