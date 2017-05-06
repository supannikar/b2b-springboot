package no.api.service;

import no.api.dao.BasketDao;
import no.api.model.BasketModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("basketService")
public class BasketService {

    @Autowired
    private BasketDao basketDao;

    public BasketModel save(BasketModel model){
        return basketDao.save(model);
    }
    public BasketModel findById(Long id){
        return basketDao.findById(id);
    }

    public List<BasketModel> listAll(){
        return basketDao.listAll();
    }

//    public List<BasketModel> queryCISDetail(CISDetailQuery cisDetailQuery){
//        return basketDao.queryCISDetail(cisDetailQuery);
//    }

//    public int countCISDetail(CISDetailQuery cisDetailQuery){
//        return basketDao.countAdObject(cisDetailQuery);
//    }

    public void delete(Integer id){
        basketDao.delete(id);
    }
}
