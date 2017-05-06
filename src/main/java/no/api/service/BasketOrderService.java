package no.api.service;

import no.api.dao.BasketOrderDao;
import no.api.model.BasketOrderModel;
import no.api.model.BasketProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("basketOrderService")
public class BasketOrderService {

    @Autowired
    private BasketOrderDao basketOrderDao;

    public BasketOrderModel save(BasketOrderModel model){
        return basketOrderDao.save(model);
    }
    public BasketOrderModel findById(Long id){
        return basketOrderDao.findById(id);
    }

    public List<BasketOrderModel> listAll(){
        return basketOrderDao.listAll();
    }

//    public List<BasketProductModel> groupByName(Long id){
//        return basketOrderDao.groupByName(id);
//    }
//
//    public List<CISGroupModel> queryCISGroup(CISGroupQuery cisGroupQuery){
//        return basketOrderDao.queryCISGroup(cisGroupQuery);
//    }
//
//    public int countCISGroup(CISGroupQuery cisGroupQuery){
//        return basketOrderDao.countCISGroup(cisGroupQuery);
//    }

    public void delete(Integer id){
        basketOrderDao.delete(id);
    }
}
