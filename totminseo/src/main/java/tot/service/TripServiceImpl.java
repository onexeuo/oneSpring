package tot.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tot.dao.TripDao;
import tot.domain.TripVO;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripDao tripDao;

    @Override
    public List<TripVO> getTripsByMemId(String memId) {
        return tripDao.getTripsByMemId(memId);
    }

    @Override
    public TripVO getTripById(int tripId) {
        return tripDao.getTripById(tripId);
    }

}
