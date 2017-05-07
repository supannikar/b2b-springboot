package no.api.dao;

import no.api.model.BasketModel;
import no.api.model.BasketProductModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BasketDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketDao.class);

    public BasketModel save(BasketModel model) {
        BasketModel result = null;

        if (model.getId() == null) {
            result = add(model);
        } else {
            result = update(model);
        }

        return result;
    }

    public BasketModel findById(Long id){
        String sql = "SELECT id, basket_name, basket_desc, member_id " +
                "FROM basket WHERE id = ?";
        RowMapper<BasketModel> mapper = new BasketModelRowMapper();
        BasketModel basketModel = null;
        try {
            basketModel = jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Basket not found by id: {}", id);
        }
        return basketModel;
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM basket WHERE id = ?", id);
    }

    public List<BasketModel> listAll(){
        return jdbcTemplate.query("SELECT id, basket_name, basket_desc, member_id FROM basket",
                new BasketModelRowMapper());
    }

    private BasketModel add(BasketModel model) {
        Long result = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new BasketStatementCreator(model),keyHolder);
        result = keyHolder.getKey().longValue();
        model.setId(result);
        return model;

    }

    private BasketModel update(BasketModel model) {

        jdbcTemplate.update("UPDATE basket SET basket_name = ?, basket_desc = ?, member_id = ?" +
                        " WHERE id = ?", model.getBasketName(), model.getBasketDesc(), model.getMemberId(),
                model.getId());
        return model;
    }

    private class BasketModelRowMapper implements RowMapper<BasketModel> {
        public BasketModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            BasketModel basketModel = new BasketModel();
            basketModel.setId(rs.getLong("id"));
            basketModel.setBasketName(rs.getString("basket_name"));
            basketModel.setBasketDesc(rs.getString("basket_desc"));
            basketModel.setMemberId(rs.getLong("member_id"));

            return basketModel;
        }
    }

    private static class BasketStatementCreator implements PreparedStatementCreator {
        private final BasketModel model;

        public BasketStatementCreator(BasketModel model) {
            this.model = model;
        }

        public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException {

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO basket (basket_name, basket_desc, member_id)" +
                            " VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            int parameterIndex = 1;
            ps.setString(parameterIndex++, model.getBasketName());
            ps.setString(parameterIndex++, model.getBasketDesc());
            ps.setLong(parameterIndex, model.getMemberId());
//            ps.setLong(parameterIndex, model.getModifiedTime().getMillis());
            return ps;
        }
    }
}
