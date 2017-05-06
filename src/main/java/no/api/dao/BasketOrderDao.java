package no.api.dao;

import no.api.model.BasketOrderModel;
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
import java.util.List;

@Repository
public class BasketOrderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BasketOrderDao.class);

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
        return jdbcTemplate.queryForObject("SELECT id, basket_id, vat_no " +
                "FROM basket_order WHERE id = ?", new BasketOrderModelRowMapper(), id);
    }

//    public List<BasketOrderModel> groupByName(Long id){
//        String sql = "SELECT d.id, d.name, d.email, d.phone, d.group_id " +
//                "FROM cisgroup g INNER JOIN cisdetail d ON g.id = d.group_id " +
//                "WHERE g.id = ?";
//
//        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, id);
//        List<BasketOrderModel> cisGroupModels = new ArrayList<>();
//        if(res == null || res.size() == 0){
//            return cisGroupModels;
//        }
//        res.stream().forEach(row -> {
//            BasketOrderModel model = new BasketOrderModel();
//            model.setId((Long) row.get("id"));
//            model.setName((String) row.get("name"));
//            model.setEmail((String) row.get("email"));
//            model.setPhone((String) row.get("phone"));
//            cisGroupModels.add(model);
//        });
//
//        return cisGroupModels;
//    }

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
