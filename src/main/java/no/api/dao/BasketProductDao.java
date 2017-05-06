package no.api.dao;

import no.api.model.BasketProductModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger logger = LoggerFactory.getLogger(BasketProductDao.class);

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
        return jdbcTemplate.queryForObject("SELECT id, basket_id, product_id, product_qnty " +
                "FROM basket_product WHERE id = ?", new BasketProductModelRowMapper(), id);
    }

    public BasketProductModel findProductByProductId(Long productId){
        return jdbcTemplate.queryForObject("SELECT id, basket_id, product_id, product_qnty " +
                "FROM basket_product WHERE product_id = ?", new BasketProductModelRowMapper(), productId);
    }

//    public List<CISDetailModel> groupByName(Long id){
//        String sql = "SELECT d.id, d.name, d.email, d.phone, d.group_id " +
//                "FROM cisgroup g INNER JOIN cisdetail d ON g.id = d.group_id " +
//                "WHERE g.id = ?";
//
//        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, id);
//        List<CISDetailModel> cisGroupModels = new ArrayList<>();
//        if(res == null || res.size() == 0){
//            return cisGroupModels;
//        }
//        res.stream().forEach(row -> {
//            CISDetailModel model = new CISDetailModel();
//            model.setId((Long) row.get("id"));
//            model.setName((String) row.get("name"));
//            model.setEmail((String) row.get("email"));
//            model.setPhone((String) row.get("phone"));
//            cisGroupModels.add(model);
//        });
//
//        return cisGroupModels;
//    }
//
//    public List<CISGroupModel> queryCISGroup(CISGroupQuery query){
//        List<Integer> paramTypes = new ArrayList<>();
//        List<Object> params = new ArrayList<>();
//
//        boolean isForCount = false;
//        String sql = composeSqlConditionForAdSearch(query, paramTypes, params, isForCount);
//
//        int[] paramTypeArray = new int[paramTypes.size()];
//        int index = 0;
//        for(Integer eachType : paramTypes) {
//            paramTypeArray[index] = eachType;
//            index++;
//        }
//        Object[] paramArray = new Object[params.size()];
//        paramArray = params.toArray(paramArray);
//
//        // Execute the query
//        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, paramArray, paramTypeArray);
//
//        List<CISGroupModel> cisGroupModels = new ArrayList<>();
//        if(res == null || res.size() == 0){
//            return cisGroupModels;
//        }
//        res.stream().forEach(row -> {
//            CISGroupModel model = new CISGroupModel();
//            model.setId((Long) row.get("id"));
//            model.setName((String) row.get("name"));
//            model.setClickCount((Long) row.get("click_count"));
////            model.setModifiedTime((DateTime) row.get("modifiedtime"));
//            cisGroupModels.add(model);
//        });
//
//        return cisGroupModels;
//    }
//
//    public int countCISGroup(CISGroupQuery query) {
//        List<Integer> paramTypes = new ArrayList<>();
//        List<Object> params = new ArrayList<>();
//
//        boolean isForCount = true;
//        String sql = composeSqlConditionForAdSearch(query, paramTypes, params, isForCount);
//
//        int[] paramTypeArray = new int[paramTypes.size()];
//        int index = 0;
//        for(Integer eachType : paramTypes) {
//            paramTypeArray[index] = eachType;
//            index++;
//        }
//        Object[] paramArray = new Object[params.size()];
//        paramArray = params.toArray(paramArray);
//
//        return jdbcTemplate.queryForObject(sql, paramArray, paramTypeArray, Integer.class);
//    }
//
//
//    private String composeSqlConditionForAdSearch(CISGroupQuery query, List<Integer> paramTypes,
//                                                  List<Object> params, boolean isForCount){
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        if(isForCount){
//            sb.append(" COUNT(g.id) ");
//        } else {
//            sb.append("g.id, g.name, g.click_count");
//        }
//
//        sb.append(" FROM cisgroup g");
//
//        boolean hasCondition = false;
//        StringBuilder conditionBuilder = new StringBuilder();
//
//        if(hasCondition) {
//            conditionBuilder.insert(0, " WHERE ");
//        }
//
//        if(!isForCount) {
//
//            if(StringUtils.isNotBlank(query.getSortBy())) {
//                conditionBuilder.append(" ORDER BY  ").append(query.getSortBy()).append(" asc ");
//            } else {
//                conditionBuilder.append(" ORDER BY g.name desc ");
//            }
//            conditionBuilder.append(" LIMIT ? OFFSET ? ");
//            paramTypes.add(Types.INTEGER);
//            params.add(query.getLimit());
//            paramTypes.add(Types.INTEGER);
//            params.add(query.getOffset());
//        }
//
//        String sqlCondition = conditionBuilder.toString();
//        if(StringUtils.isNotBlank(sqlCondition)) {
//            sb.append(sqlCondition);
//        }
//        logger.debug("SQL to search ads : " + sb.toString());
//
//        return sb.toString();
//    }

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
