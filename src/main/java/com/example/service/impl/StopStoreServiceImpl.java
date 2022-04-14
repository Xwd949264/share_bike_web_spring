package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.mapper.StopStoreMapper;
import com.example.pojo.Bike;
import com.example.pojo.Result;
import com.example.pojo.Stop;
import com.example.pojo.StopStore;
import com.example.service.BikeService;
import com.example.service.StopService;
import com.example.service.StopStoreService;
import com.example.utils.ResultUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @ClassName StopStoreServiceImpl
 * @Description: com.example.service.impl
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 16:10
 * @version: 1.0
 */
@Service
public class StopStoreServiceImpl implements StopStoreService {
    //properties
    @Autowired
    private StopStoreMapper stopStoreMapper;
    @Autowired
    private StopService stopService;
    @Autowired
    private BikeService bikeService;

    private static Random random = new Random();

    @ExceptionHandler
    public Result handlerException(){
        return ResultUtil.error(-1,"系统异常");
    }

    //methods
    public static double getRandomDouble(){
        double nextDouble = random.nextDouble()/1000.0;
        if (nextDouble<0.0001)
            return nextDouble;
        return getRandomDouble();
    }


    public static Point getRandomPoint(Polygon polygon){
        Point centroid = polygon.getCentroid();
        Point point = new Point(centroid.getCoordinate(), new PrecisionModel(PrecisionModel.FLOATING),centroid.getSRID());
        if (random.nextInt()%2==0){
            point.getCoordinate().setX(point.getX()+getRandomDouble());
            point.getCoordinate().setY(point.getY()+getRandomDouble());
        }else {
            point.getCoordinate().setX(point.getX()-getRandomDouble());
            point.getCoordinate().setY(point.getY()-getRandomDouble());
        }
        if (point.intersects(polygon)){
            return point;
        }
        return getRandomPoint(polygon);
    }



    @Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public Result addStopStore(String username, StopStore stopStore) {
        if (StrUtil.isEmpty(username)||stopStore==null)
            return ResultUtil.error(0,"操作用户未登录");
        //判断可用容量是否满足条件--前端进行判断
        //计算目标停车区域几何中心点
        Integer stopid = stopStore.getStopid();
        Integer addtion = stopStore.getAddtion();
        Integer typeid = stopStore.getTypeid();
        //判断必要参数是否为空
        if (stopid==null||addtion==null||typeid==null){
            return ResultUtil.error(0,"上报信息不完整,不允许单车投放");
        }
        //执行中心点计算操作
        Stop stop = stopService.selectById(stopid);
        if (stop==null)
            return ResultUtil.error(0,"目标停车点不存在,不允许单车投放");
        if (stop.getCapacity()<addtion) {
            return ResultUtil.error(0,"单车投放量大于最大容量,不允许单车投放");
        }
        //计算所在停车区域的中心点Point坐标
        Polygon polygon = stop.getGeom();
        Point centroid = polygon.getCentroid();
        if (centroid==null){
            return ResultUtil.error(0,"上报信息不完整,不允许单车投放");
        }
        //如果满足条件-自动执行添加操作[添加n条bike记录到数据库中]
        if (addtion>0){
            //先修改当前停车点容量信息
            stop.setSize(stop.getSize()+addtion);
            Result result_updateStop = stopService.updateCapacityAndSizeById(stop);
            if (result_updateStop.getCode()==1){
                //执行添加日志操作
                //添加完成-执行操作日志记录-增加bike-store记录——放入参数值
                Integer rows = stopStoreMapper.addStopStore(username, stopStore);
                if (rows>0){
                    //执行批处理操作-投放单车
                    Bike bike = null;
                    //执行批处理添加操作
                    List<Bike> bikes = new ArrayList<>();
                    for (int i = 0; i < addtion; i++) {
                        bike = new Bike();
                        bike.setLocation(getRandomPoint(polygon).toString());
                        bike.setStopid(stopid);
                        bike.setTypeid(typeid);
                        bike.setStatus(0);//0-默认为空闲状态
                        //更新UUID
                        bike.setBno(UUID.randomUUID().toString());
                        bikes.add(bike);
                    }
                    Result result = bikeService.batchInsertBike(bikes);
                    if (result.getCode()==1){
                        //批处理完成
                        //修改当前停车点容量信息
                        return ResultUtil.error(1,"单车投放成功");
                    }
                }else
                {
                    return ResultUtil.error(0,"单车投放失败");
                }
            }else {
                //失败
                return ResultUtil.error(0,"单车投放失败");
            }
        }
        return ResultUtil.error(0,"单车投放失败");
    }

    @Override
    public Result selectAllDeatil() {
        List<StopStore> stopStores = stopStoreMapper.selectAllDeatil();
        if (stopStores!=null)
            return ResultUtil.success(1,"查询成功",stopStores);
        else
            return ResultUtil.error(0,"查询失败");
    }
}
