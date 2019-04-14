package com.mp5a5.www.httprequest.net.api;

import android.util.ArrayMap;
import com.mp5a5.www.httprequest.net.entity.NBAEntity;
import com.mp5a5.www.library.use.RetrofitFactory;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @author ：mp5a5 on 2019/1/16 16：37
 * @describe
 * @email：wwb199055@126.com
 */
public class NbaService {

    private NBAApiT nbaApiT;

    private NbaService() {
        nbaApiT = RetrofitFactory.getInstance().create(NBAApiT.class);
    }

    public static NbaService getInstance() {
        return Nbaservice1Holder.S_INSTANCE;
    }

    private static class Nbaservice1Holder {
        private static final NbaService S_INSTANCE = new NbaService();
    }

    public Observable<NBAEntity> getNBAInfo(String key) {
        ArrayMap<String, Object> map = new ArrayMap<>();
        map.put("key", key);
        return nbaApiT.getNBAInfo(map);
    }

    public Flowable<NBAEntity> getNBAInfo1(String key) {
        ArrayMap<String, Object> map = new ArrayMap<>();
        map.put("key", key);
        return nbaApiT.getNBAInfo1(map);
    }

}
