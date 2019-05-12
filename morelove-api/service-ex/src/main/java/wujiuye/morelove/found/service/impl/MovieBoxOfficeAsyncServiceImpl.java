package wujiuye.morelove.found.service.impl;

import wujiuye.morelove.found.service.MovieBoxOfficeAsyncService;
import wujiuye.morelove.found.service.MovieBoxOfficeService;
import wujiuye.morelove.pojo.MovieBoxOffice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 异步
 */
@Service
public class MovieBoxOfficeAsyncServiceImpl implements MovieBoxOfficeAsyncService {

    @Resource
    private MovieBoxOfficeService movieBoxOfficeService;

    @Async
    public Future<List<MovieBoxOffice>> hourBoxOfficeList(int year, int month, int day, int hour) {
        List<MovieBoxOffice> movieBoxOffices = movieBoxOfficeService.queryHourBoxOffice(year, month, day, hour);
        return new AsyncResult<>(movieBoxOffices);
    }

    @Async
    public Future<List<MovieBoxOffice>> dayBoxOfficeList(int year, int month, int day) {
        List<MovieBoxOffice> movieBoxOffices = movieBoxOfficeService.queryDayBoxOffice(year, month, day);
        return new AsyncResult<>(movieBoxOffices);
    }

    /**
     * AsyncResult是Future的子类，使用spring@Async注解有返回值的方法必须将返回声明为Future类型
     *
     * @return
     */
    @Async
    public Future<List<MovieBoxOffice>> monthBoxOfficeList(int year, int month) {
        List<MovieBoxOffice> movieBoxOffices = movieBoxOfficeService.queryMonthBoxOffice(year, month);
        return new AsyncResult<>(movieBoxOffices);
    }

}
