/**
 * 定义新的layui模块：layui.define([mods], callback)
 *  index表示新增自定义layui模块名称
 *  exports 参数，用于输出该模块的接
 */
layui.define(['layer','form','element'],function (exports){
    var layer = layui.layer,
        form = layui.form,
        element=layui.element;
    //弹出窗口信息
    layer.msg("欢迎使用共享单车智能管理系统!");

    var bike_type_chart=undefined;

    /**
     * 注册地图操作事件
     * @param map 自定义地图对象
     */
    function registerEvents(map,_lngid,_latid,_zoomid){
        /**
         * 菜单点击事件
         */
        $("#btnLogonOut").on("click",function (){
            layer.confirm('确认退出登录吗?',
                {icon: 3, title:'提示'},
                function(index){
                    //确认
                    //do something
                    //销毁本地存储中的个人信息
                    localStorage.removeItem("user");
                    //页面跳转到登陆页面
                    window.location.href="login.html";
                    //关闭窗口
                    layer.close(index);
                });
        })

        /**图标点击事件*/
        /**
         * 用户定位
         */
        $("#locate_control").on("click",function (){
            map.locatePosition();
        })

        /**
         * 隐藏geojson图层
         */
        $("#hideLayer_control").on("click",function (){
            map.hideGeoJsonLayer();
        })

        /**
         * 切换图层
         */
        $("#switchLayer_control").on("click",function (){
            map.switchLayer();
        })

        /**
         * 还原视图
         */
        $("#resetView_control").on("click",function (){
            layer.msg("即将还原视图",{
                anim:0
            });
            map.resetMapView();
        })
        /**
         * 鼠标坐标监控
         */
        map.registOnMouseMove(_lngid,_latid,_zoomid);

        /**
         * localstorage监听
         */
        window.addEventListener('storage', function (event) {
            //监听centeroid的值变化
            if (event.key==="centeroid"){
                // console.log(window.localStorage.getItem("centeroid"))
                map.panToPoint(JSON.parse(window.localStorage.getItem("centeroid")));
            }else if(event.key=="centeroid_bike"){
                // console.log(window.localStorage.getItem("centeroid_bike"))
                map.panToPoint(JSON.parse(window.localStorage.getItem("centeroid_bike")),20);
            }
        })
    }

    /**
     * 注册菜单点击事件
     */
    function registMenuEvents(){
        //监听nav导航栏菜单的点击事件
        element.on("nav(menu)",function (elem){
            // console.log(elem[0].getAttribute("lay-filter"))
            if (elem[0]&&elem[0].getAttribute("lay-filter")){
                //根据lay-filter属性值，判断将要执行的操作
                var lay_filterVal = elem[0].getAttribute("lay-filter");
                switch (lay_filterVal){
                    case "addStops":{
                        //增设停车区域
                        layer.msg("请先绘制新的停车区域,再执行增设操作:")
                        break;
                    }
                    case "queryStops":{
                        //查询停车点
                        layer.open({
                            type:2
                            ,title: '停车区域检索'
                            ,content: 'stopSearch.html'
                            ,area: ['1000px', '800px']
                            ,maxmin:true//最大最小化
                            ,offset: 'auto' //垂直水平居中
                            ,anim: 5
                            ,shadeClose: true
                            ,scrollbar: false
                            ,success:function (layer0, index){
                                layer.msg("即将执行默认检索...")
                            }
                        })
                        break;
                    }
                    case "locateBike":{
                        //单车定位查询
                        layer.open({
                            type:2
                            ,title: '单车定位查询(默认检索全部共享单车)'
                            ,content: 'bikeSearch.html'
                            ,area: ['1000px', '800px']
                            ,maxmin:true//最大最小化
                            ,offset: 'auto' //垂直水平居中
                            ,anim: 5
                            ,shadeClose: true
                            ,scrollbar: false
                            ,success:function (layer0, index){
                                console.log("回调")
                            }
                        })
                        break;
                    }
                    case "queryBroken":{
                        //损坏车辆查询
                        layer.open({
                            type:2
                            ,title: '单车定位查询(默认检索全部损坏单车)'
                            ,content: 'brokenBikeSearch.html'
                            ,area: ['1000px', '800px']
                            ,maxmin:true//最大最小化
                            ,offset: 'auto' //垂直水平居中
                            ,anim: 5
                            ,shadeClose: true
                            ,scrollbar: false
                            ,success:function (layer0, index){
                                console.log("回调")

                            }
                        })

                        break;
                    }
                    case "userManager":{
                        //系统用户管理
                        window.open("admin");//打开新的标签页
                        //window.location.href="admin.html";//链接跳转
                        break;
                    }
                }
            }
        })
    }

    /**
     * 初始化图表
     */
    function init_Bike_type_chart(_id){
        if (bike_type_chart != null && bike_type_chart != "" && bike_type_chart != undefined){
            bike_type_chart.dispose();//销毁
        }
        bike_type_chart =
            echarts.init(document.getElementById(_id));
        var options =  {
            title: {
                text: undefined,
                subtext: undefined,//'停车区个数/单车容纳量',
                left: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                left: 'center',
                top: 'bottom',
            },
            series:
                {
                    name: undefined,
                    type: 'pie',
                    roseType: 'area',
                    radius: [20, 100],
                    itemStyle: {
                        borderRadius: 5
                    },
                    data: undefined,
                }
        };
        //发送数据请求
        $.ajax({
            url:"http://localhost:8011/biketype/get/group",
            method:"GET",
            success:function (result){
                if (result&&result.data){
                    var data = result.data;
                    // console.log(data)
                    //初始化图表
                    options.title.text="单车类型统计";
                    options.title.subtext="单车投放数量";
                    options.series.name="单车投放数量";
                    options.series.data=data;
                    //设置options
                    bike_type_chart.setOption(options);
                    //添加事件
                    bike_type_chart.on(
                        'mouseover',
                        "series.pie",
                        function (params) {
                            // console.log(params);
                            //获取数据
                            var data_cur = params.data;
                            //弹出层
                            layer.open({
                                title:data_cur.name?("单车类型:-"+data_cur.name):"单车"
                                ,type: 1
                                ,area:["350px","250px"]
                                ,offset: ["300px","215px"] //具体配置参考：http://www.layuion.com/doc/modules/layer.html#offset
                                ,id: 'layerDemo'+new Date().getTime() //防止重复弹出
                                ,anim: 5
                                ,content: '<img src="data:image/jpeg;base64,'+data_cur.photo+'" style="width: 100%;height: 100%">'
                                ,shade: 0 //不显示遮罩
                                ,time:1000 //1秒后自动关闭
                            });
                        });
                }
            },
            error:function (){
                layer.msg("单车类型统计图表初始化失败!")
            }
        })


    }


    //模块名：index[模块输出的核心,必须和use时的模块名一致]
    exports('index',{
        registerEvents,
        registMenuEvents,
        init_Bike_type_chart
    });
})
