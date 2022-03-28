package com.springboot.rafael.domain.repositories;

import com.springboot.rafael.domain.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class Clients {
    private static final String INSERT = "insert into client (name) values (?)";
    private static final String UPDATE = "update client set name = ? where id = ?";
    private static final String DELETE = "delete client where id = ?";
    private static final String SELECT_ALL = "select * from client";
    private static final String SELECT_BY_NAME = "select * from client where name like ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Client save(Client client) {
        entityManager.persist(client);

        return client;
    }

    @Transactional
    public Client update(Client client) {
        entityManager.merge(client);

        return client;
    }
//Using JDCBTemplate samples
//    public Client update(Client client) {
//        jdbcTemplate.update(UPDATE, new Object[]{client.getName(), client.getId()});
//
//        return client;
//    }
//
//    public void delete(String clientId) {
//        jdbcTemplate.update(DELETE, new Object[]{clientId});
//    }
//
//    public List<Client> getByName(String name) {
//        return jdbcTemplate.query(SELECT_BY_NAME,
//                new Object[]{"%" + name + "%"},
//                createClientRowMapper());
//    }
//
//    public Client save(Client client) {
//        jdbcTemplate.update(INSERT, new Object[]{client.getName()});
//
//        return client;
//    }
//
//    public List<Client> getAll() {
//        return jdbcTemplate.query(SELECT_ALL, createClientRowMapper());
//    }
//
//    private RowMapper createClientRowMapper() {
//        return new RowMapper<Client>() {
//            @Override
//            public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return new Client(rs.getInt("id"), rs.getString("name"));
//            }
//        };
//    }
}
