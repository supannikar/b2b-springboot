package no.api.dao;

import no.api.model.BasketOrderModel;
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
public class BasketProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketProductDao.class);

    public BasketProductModel save(BasketProductModel model) {
        BasketProductModel result = null;

        if (model.getId() == null) {
            result = add(model);
        } else {
            result = update(model);
        }
        return result;
    }

    public BasketProductModel findById(Long id){
        String sql = "SELECT id, basket_id, product_id, product_qnty " +
                "FROM basket_product WHERE id = ?";
        RowMapper<BasketProductModel> mapper = new BasketProductModelRowMapper();
        BasketProductModel basketProductModel = null;
        try {
            basketProductModel = jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Basket order not found by id: {}", id);
        }

        return basketProductModel;
    }

    public BasketProductModel findProductByProductId(Long productId){
        String sql = "SELECT id, basket_id, product_id, product_qnty " +
                "FROM basket_product WHERE product_id = ?";
        RowMapper<BasketProductModel> mapper = new BasketProductModelRowMapper();
        BasketProductModel basketProductModel = null;
        try {
            basketProductModel = jdbcTemplate.queryForObject(sql, mapper, productId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Basket order not found by product id: {}", productId);
        }

        return basketProductModel;
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM basket_product WHERE id = ?", id);
    }

    public List<BasketProductModel> listAll(){
        return jdbcTemplate.query("SELECT id, basket_id, product_id, product_qnty FROM basket_product",
                new BasketProductModelRowMapper());
    }

    public List<BasketProductModel> listProductByBasketId(Long basketId){
        return jdbcTemplate.query("SELECT id, basket_id, product_id, product_qnty " +
                "FROM basket_product WHERE basket_id = ?",
                new BasketProductModelRowMapper(), basketId);
    }

    private BasketProductModel add(BasketProductModel model) {
        Long result = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new BasketProductStatementCreator(model),keyHolder);
        result = keyHolder.getKey().longValue();
        model.setId(result);
        return model;

    }

    private BasketProductModel update(BasketProductModel model) {

        jdbcTemplate.update("UPDATE basket_product SET basket_id = ?, product_id = ?, product_qnty = ?" +
                " WHERE id = ?", model.getBasketId(), model.getProductId(), model.getProductQnty(),
                model.getId());
        return model;
    }


    private class BasketProductModelRowMapper implements RowMapper<BasketProductModel> {
        public BasketProductModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            BasketProductModel basketProductModel = new BasketProductModel();
            basketProductModel.setId(rs.getLong("id"));
            basketProductModel.setBasketId(rs.getLong("basket_id"));
            basketProductModel.setProductId(rs.getLong("product_id"));
            basketProductModel.setProductQnty(rs.getLong("product_qnty"));

            return basketProductModel;
        }
    }

    private static class BasketProductStatementCreator implements PreparedStatementCreator {
        private final BasketProductModel model;

        public BasketProductStatementCreator(BasketProductModel model) {
            this.model = model;
        }

        public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException {

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO basket_product (basket_id, product_id, product_qnty)" +
                    " VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            int parameterIndex = 1;
            ps.setLong(parameterIndex++, model.getBasketId());
            ps.setLong(parameterIndex++, model.getProductId());
            ps.setLong(parameterIndex, model.getProductQnty());
            return ps;
        }
    }
}
