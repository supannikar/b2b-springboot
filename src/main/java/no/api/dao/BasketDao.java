package no.api.dao;

import no.api.model.BasketModel;
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
public class BasketDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BasketDao.class);

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
        return jdbcTemplate.queryForObject("SELECT id, basket_name, basket_desc, member_id " +
                "FROM basket WHERE id = ?", new BasketModelRowMapper(), id);
    }

//    public List<BasketModel> queryCISDetail(CISDetailQuery query){
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
//        List<CISDetailModel> cisDetailModels = new ArrayList<>();
//        if(res == null || res.size() == 0){
//            return cisDetailModels;
//        }
//        res.stream().forEach(row -> {
//            CISDetailModel model = new CISDetailModel();
//            model.setId((Long) row.get("id"));
//            model.setName((String) row.get("name"));
//            model.setPhone((String) row.get("phone"));
//            model.setEmail((String) row.get("email"));
//            model.setGroupId((Long) row.get("group_id"));
////            model.setModifiedTime((DateTime) row.get("modifiedtime"));
//            cisDetailModels.add(model);
//        });
//
//        return cisDetailModels;
//    }

//    public int countAdObject(CISDetailQuery query) {
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
//    private String composeSqlConditionForAdSearch(CISDetailQuery query, List<Integer> paramTypes,
//                                                  List<Object> params, boolean isForCount){
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        if(isForCount){
//            sb.append(" COUNT(d.id) ");
//        } else {
//            sb.append("d.id, d.name, d.email, d.phone, " +
//                    "d.group_id");
//        }
//
//        sb.append(" FROM cisdetail d");
//
//        boolean hasCondition = false;
//        StringBuilder conditionBuilder = new StringBuilder();
//        if(StringUtils.isNoneBlank(query.getEmail())){
//            if(hasCondition){
//                conditionBuilder.append(" AND ");
//            } else hasCondition = true;
//            conditionBuilder.append(" d.email = ?");
//            paramTypes.add(Types.VARCHAR);
//            params.add(query.getEmail());
//        }
//
//        if(StringUtils.isNoneBlank(query.getName())){
//            if(hasCondition){
//                conditionBuilder.append(" AND ");
//            } else hasCondition = true;
//            conditionBuilder.append(" d.name = ?");
//            paramTypes.add(Types.VARCHAR);
//            params.add(query.getName());
//        }
//
//        if(StringUtils.isNoneBlank(query.getPhone())){
//            if(hasCondition){
//                conditionBuilder.append(" AND ");
//            } else hasCondition = true;
//            conditionBuilder.append(" d.phone = ?");
//            paramTypes.add(Types.VARCHAR);
//            params.add(query.getPhone());
//        }
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
//                conditionBuilder.append(" ORDER BY d.name desc ");
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
