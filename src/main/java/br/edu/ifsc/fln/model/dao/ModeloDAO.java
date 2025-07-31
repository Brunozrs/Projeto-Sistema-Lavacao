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

public class ModeloDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Modelo modelo) {
        final String sql = "INSERT INTO modelo(descricao, id_marca,eCategoria) VALUES(?,?,?);";
        final String sqlMotor = "INSERT INTO motor(id_modelo,potencia,TipoCombustivel) VALUES((SELECT max(id) FROM modelo),?,?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmtMotor = connection.prepareStatement(sqlMotor);

            //registra o modelo
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3,modelo.getCategoria().getDescricao());
            stmt.execute();
            //registra o motor do modelo imediatament
            stmtMotor.setInt(1, modelo.getMotor().getPotencia());
            stmtMotor.setString(2,modelo.getMotor().getTipoCombustivel().getDescricao());
            stmtMotor.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Modelo modelo) {
        String sql = "UPDATE modelo SET descricao=?, id_marca=?,eCategoria=? WHERE id=?";
        String sqlMotor = "UPDATE motor SET potencia=?,tipoCombustivel=? WHERE id_modelo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmtMotor = connection.prepareStatement(sqlMotor);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getCategoria().name());
            stmt.setInt(4, modelo.getId());
            stmtMotor.setInt(1,modelo.getMotor().getPotencia());
            stmtMotor.setString(2,modelo.getMotor().getTipoCombustivel().name());
            stmtMotor.setInt(3,modelo.getId());
            stmt.execute();
            stmtMotor.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Modelo> listar() {
        String sql = "SELECT * FROM modelo m JOIN motor mo ON m.id = mo.id_modelo";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();

        modelo.setId(rs.getInt("id"));
        modelo.setDescricao(rs.getString("descricao"));
        modelo.setCategoria(ECategoria.valueOf(rs.getString("eCategoria")));
        modelo.getMotor().setPotencia(rs.getInt("potencia"));
        modelo.getMotor().setTipoCombustivel(ETipoCombustivel.valueOf(rs.getString("tipoCombustivel")));

        Marca marca = new Marca();
        marca.setId(rs.getInt("id_marca"));
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        marca = marcaDAO.buscar(marca);
        modelo.setMarca(marca);

        return modelo;
    }

        public Modelo buscar(Modelo modelo) {
        String sql = "SELECT * FROM modelo WHERE id=?";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                modelo.setDescricao(resultado.getString("descricao"));
                modelo.setCategoria(ECategoria.valueOf(resultado.getString("eCategoria")));
                Marca marca = new Marca();
                marca.setId(resultado.getInt("id_marca"));
                MarcaDAO marcaDAO = new MarcaDAO();
                marcaDAO.setConnection(connection);
                marca = marcaDAO.buscar(marca);
                modelo.setMarca(marca);


                retorno = modelo;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}