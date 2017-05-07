package no.api.dao;

import no.api.model.BasketModel;
import no.api.model.BasketOrderModel;
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
import java.util.List;

@Repository
public class BasketOrderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketOrderDao.class);

    public BasketOrderModel save(BasketOrderModel model) {
        BasketOrderModel result = null;

        if (model.getId() == null) {
            result = add(model);
        } else {
            result = update(model);
        }
        return result;
    }

    public BasketOrderModel findById(Long id){
        String sql = "SELECT id, basket_id, vat_no " +
                "FROM basket_order WHERE id = ?";
        RowMapper<BasketOrderModel> mapper = new BasketOrderModelRowMapper();
        BasketOrderModel basketOrderModel = null;
        try {
            basketOrderModel = jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Basket order not found by id: {}", id);
        }

        return basketOrderModel;
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM basket_order WHERE id = ?", id);
    }

    public List<BasketOrderModel> listAll(){
        return jdbcTemplate.query("SELECT id, basket_id, vat_no FROM basket_order",
                new BasketOrderModelRowMapper());
    }

    private BasketOrderModel add(BasketOrderModel model) {
        Long result = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new BasketOrderStatementCreator(model),keyHolder);
        result = keyHolder.getKey().longValue();
        model.setId(result);
        return model;

    }

    private BasketOrderModel update(BasketOrderModel model) {

        jdbcTemplate.update("UPDATE basket_order SET basket_id = ?, vat_no = ?" +
                " WHERE id = ?", model.getBasketId(), model.getVatNo(),
                model.getId());
        return model;
    }


    private class BasketOrderModelRowMapper implements RowMapper<BasketOrderModel> {
        public BasketOrderModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            BasketOrderModel basketOrderModel = new BasketOrderModel();
            basketOrderModel.setId(rs.getLong("id"));
            basketOrderModel.setBasketId(rs.getLong("basket_id"));
            basketOrderModel.setVatNo(rs.getString("vat_no"));
            return basketOrderModel;
        }
    }

    private static class BasketOrderStatementCreator implements PreparedStatementCreator {
        private final BasketOrderModel model;

        public BasketOrderStatementCreator(BasketOrderModel model) {
            this.model = model;
        }

        public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException {

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO basket_order (basket_id, vat_no)" +
                    " VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            int parameterIndex = 1;
            ps.setLong(parameterIndex++, model.getBasketId());
            ps.setString(parameterIndex, model.getVatNo());
            return ps;
        }
    }
}
