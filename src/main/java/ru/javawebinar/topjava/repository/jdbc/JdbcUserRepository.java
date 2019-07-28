package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private DataSourceTransactionManager transactionManager;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              DataSourceTransactionManager transactionManager) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public User save(User user) {
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus ts = transactionManager.getTransaction(td);
        try {
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

            if (user.isNew()) {
                Number newKey = insertUser.executeAndReturnKey(parameterSource);
                user.setId(newKey.intValue());
            } else if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                    parameterSource) == 0) {
                transactionManager.commit(ts);
                return null;
            }
            transactionManager.commit(ts);
            return user;
        }catch (Exception e) {
            transactionManager.rollback(ts);
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus ts = transactionManager.getTransaction(td);
        try {
            boolean toReturn = jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
            transactionManager.commit(ts);
            return toReturn;
        }catch (Exception e) {
            transactionManager.rollback(ts);
            return false;
        }
    }

    @Override
    public User get(int id) {
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus ts = transactionManager.getTransaction(td);
        try {
            List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
            User user = DataAccessUtils.singleResult(users);
            transactionManager.commit(ts);
            return user;
        }catch (Exception e) {
            transactionManager.rollback(ts);
            return null;
        }

    }

    @Override
    public User getByEmail(String email) {
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus ts = transactionManager.getTransaction(td);
        try {
            //        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
            List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
            transactionManager.commit(ts);
            return DataAccessUtils.singleResult(users);
        }catch (Exception e) {
            transactionManager.rollback(ts);
            return null;
        }

    }

    @Override
    public List<User> getAll() {
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus ts = transactionManager.getTransaction(td);
        try {
            List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
            transactionManager.commit(ts);
            return users;
        }catch (Exception e) {
            transactionManager.rollback(ts);
            return Collections.emptyList();
        }
    }
}
