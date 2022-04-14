/**
 * 定义新的layui模块：layui.define([mods], callback)
 *  index表示新增自定义layui模块名称
 *  exports 参数，用于输出该模块的接
 */
layui.define(['layer','form','element','laydate','tree', 'util','table',"laytpl"],function (exports){
    //定义模块
    const  layer = layui.layer,
            form = layui.form,
            element = layui.element,
            laydate = layui.laydate,
            tree = layui.tree,
            util = layui.util,
            table = layui.table,
            laytpl = layui.laytpl,
            $=layui.$;
    //提示信息
    layer.msg("欢迎进入系统用户管理中心!")

    var mapInstance = undefined;

    /**
     * 此图初始化模块
     * @param _targetId
     * @constructor
     */
    class OlMap {
        /**
         * 构造器
         */
        constructor(_targetId,_geojsonObject) {
            //成员属性map
            this.map=undefined;
            this.LastModifiedFeature=undefined;//记录上次被修改的feature
            this.popupOverLay=undefined;//记录popup图层
            //图表资源
            this.stop_group_pie=undefined;//分组统计-stops信息
            this.bike_group_pie=undefined;//分组统计-bikes信息
            //地图初始化
            this.initMap(_targetId,_geojsonObject);
            //事件注册
            this.addClickEvent();
            this.addMouseMoveInEvent();
            this.addPopUpListener();

            //初始化echarts图表
            this.initStop_group_pie();
        }

        /**
         * 初始化地图
         * @param _targetId 地图容器ID值
         * @param _geojsonObject geojson数据源
         * @return ol.map对象
         */
        initMap(_targetId,_geojsonObject){
            const vectorLayer = new ol.layer.Vector({
                // background: '#1a2b39',
                source: new ol.source.Vector({
                    features:new ol.format.GeoJSON().readFeatures(_geojsonObject),
                }),
                style:  new ol.style.Style({
                    stroke:new ol.style.Stroke({
                        color:"#5fb878",
                        width:1,
                        lineDash: [2, 2, 2, 2]
                    }),
                    fill: new ol.style.Fill({
                        color: '#ffffff'
                    })
                }),
            });
            //初始化地图
            this.map =new ol.Map({
                layers: [vectorLayer],
                target: _targetId,
                view: new ol.View({
                    center: [119.00,33.30],
                    zoom: 24,
                }),
                controls: ol.control.defaults({
                    attribution:false,//取消显示默认控件
                    zoom: false,
                })
            });
        }

        /**
         * 添加点击事件
         */
        addClickEvent(){
            // //添加点击事件
            // this.map.on("click",(e)=>{
            //     let feature = this.map.forEachFeatureAtPixel(e.pixel, function (feature, layer) {
            //         return feature;
            //     });
            //     if (feature) {
            //         console.log(feature)
            //         //修改style填充颜色#3b7fff
            //     }
            // })
            let selectClick = new ol.interaction.Select({
                // 事件类型
                condition: ol.events.condition.click,
                // 点击后的样式
                style: new ol.style.Style({
                    stroke:new ol.style.Stroke({
                        color:"#ccc",
                        width:1,
                        lineDash: [2, 2, 2, 2]
                    }),
                    fill: new ol.style.Fill({
                        color: '#3b7fff'
                    })
                })
            })
            this.map.addInteraction(selectClick);
        }


        /**
         * 添加鼠标移入事件
         */
        addMouseMoveInEvent(){
            let _that = this;
            this.map.on('pointermove', function (e) {
                //先还原上次修改过的Feature的样式
                if (_that.LastModifiedFeature){
                    _that.LastModifiedFeature.setStyle(
                        new ol.style.Style({
                            stroke:new ol.style.Stroke({
                                color:"#5fb878",
                                width:1,
                                lineDash: [2, 2, 2, 2]
                            }),
                            fill: new ol.style.Fill({
                                color: '#fff'
                            })
                        }))
                }
                //再修改被选中的当前要素的样式
                let features = _that.map.getFeaturesAtPixel(e.pixel);
                if (features&&features[0]){
                    //记录当前被修改的Feature要素
                    _that.LastModifiedFeature=features[0];
                    //修改样式
                    features[0].setStyle(new ol.style.Style({
                        stroke:new ol.style.Stroke({
                            color:"#ccc",
                            width:1,
                            lineDash: [2, 2, 2, 2]
                        }),
                        fill: new ol.style.Fill({
                            color: '#3b7fff'
                        })
                    }))
                    // console.log(e.coordinate)
                    //获取要素属性信息
                    let properties = features[0].getProperties();
                    //获取坐标位置-唤醒popup
                    let curCoordinate = e.coordinate;
                    // let hdms = ol.coordinate.toStringHDMS(ol.proj.toLonLat(curCoordinate));
                    //获取content
                    let popup_content = document.getElementById("popup-content");
                    // console.log(properties)
                    let template = _that.getPopupTemplate();
                    if (properties&&properties.adcode){
                        $.ajax({
                            url:"http://localhost:8011/stop/select/stop/group",
                            method:"GET",
                            data:{code:properties.adcode},
                            success:function (result){
                                if (result&&result.data&&result.data[0]){
                                    let ajaxData = result.data[0];
                                    popup_content.innerHTML = laytpl(template).render({
                                        name:properties.name,
                                        level:properties.level,
                                        adcode:properties.adcode,
                                        stopsnum:ajaxData.stopsnum,
                                        bikecapacity:ajaxData.bikecapacity+"/辆",
                                        bikecount:ajaxData.bikecount+"/辆"
                                    })
                                    //显示弹窗
                                    //显示弹窗
                                    _that.popupOverLay.setPosition(curCoordinate);
                                    //2秒后自动隐藏
                                    setTimeout(function (){
                                        _that.popupOverLay.setPosition(undefined);
                                    },5000);
                                }else{
                                    popup_content.innerHTML = laytpl(template).render({
                                        name:properties.name,
                                        level:properties.level,
                                        adcode:properties.adcode,
                                        stopsnum:"?/个",
                                        bikecapacity:"?/辆",
                                        bikecount:"?/辆"
                                    })
                                    //显示弹窗
                                    _that.popupOverLay.setPosition(curCoordinate);
                                    //2秒后自动隐藏
                                    setTimeout(function (){
                                        _that.popupOverLay.setPosition(undefined);
                                    },5000);
                                }
                            }
                        })
                    }
                }
            })
        }

        /**
         * addPopUp弹出窗事件
         */
        addPopUpListener(){
            //获取DOM
            let container = document.getElementById("popup");
            let content = document.getElementById("popup-content");
            let closer = document.getElementById("popup-closer");
            //支持popup
            let overLay = new ol.Overlay({
                element:container,
                autoPan:{
                    animation:{
                        duration:250,
                    }
                }
            })
            //绑定closer事件
            closer.onclick=function (){
                overLay.setPosition(undefined);
                closer.blur();
                return false;
            }
            //添加overLay
            this.popupOverLay=overLay;
            this.map.addOverlay(overLay);
        }

        /**
         * 获取Popup渲染模板
         * @param properties
         * @return {string}
         */
        getPopupTemplate(){
            //发起数据请求
            return '<table class="layui-table">\n' +
                '  <colgroup>\n' +
                '    <col width="150">\n' +
                '    <col width="200">\n' +
                '    <col>\n' +
                '  </colgroup>\n' +
                '  <thead>\n' +
                '    <tr>\n' +
                '      <th>政区</th>\n' +
                '      <th>{{d.name}}</th>\n' +
                '    </tr> \n' +
                '  </thead>\n' +
                '  <tbody>\n' +
                '    <tr>\n' +
                '      <td>级别</td>\n' +
                '      <td>{{d.level}}</td>\n' +
                '    </tr>\n' +
                '    <tr>\n' +
                '      <td>编码</td>\n' +
                '      <td>{{d.adcode}}</td>\n' +
                '    </tr>\n' +
                '    <tr>\n' +
                '      <td>停车点量:</td>\n' +
                '      <td>{{d.stopsnum}}</td>\n' +
                '    </tr>\n' +
                '    <tr>\n' +
                '      <td>单车容量:</td>\n' +
                '      <td>{{d.bikecapacity}}</td>\n' +
                '    </tr>\n' +
                '    <tr>\n' +
                '      <td>总投放量:</td>\n' +
                '      <td>{{d.bikecount}}</td>\n' +
                '    </tr>\n' +
                '  </tbody>\n' +
                '</table>';;
        }

        /**
         * 初始化图表资源
         */
        initStop_group_pie(){
            let _that = this;
            //判断对象是否为空
            if (this.stop_group_pie != null && this.stop_group_pie != "" && this.stop_group_pie != undefined)
                this.stop_group_pie.dispose();//销毁
            if(this.bike_group_pie != null && this.bike_group_pie != "" && this.bike_group_pie != undefined)
                this.bike_group_pie.dispose();//销毁

            //创建图表对象
            this.stop_group_pie=echarts.init(document.getElementById("stop_group_pie"));
            this.bike_group_pie=echarts.init(document.getElementById("bike_group_pie"));
            //配置options
            let option=  {
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
                url:"http://localhost:8011/stop/select/stops/group",
                method:"GET",
                success:function (result){
                    if (result&&result.data){
                        let data = result.data;
                        // console.log(data)
                        //初始化图表
                        option.title.text="停车区统计";
                        option.title.subtext="停车区个数";
                        option.series.name="停车区数量";
                        option.series.data=data;
                        //设置options
                        _that.stop_group_pie.setOption(option);
                    }
                }
            })
            $.ajax({
                url:"http://localhost:8011/stop/select/bikes/group",
                method:"GET",
                success:function (result){
                    if (result&&result.data){
                        let data = result.data;
                        // console.log(data)
                        //初始化图表
                        option.title.text="单车量统计";
                        option.title.subtext="单车个数";
                        option.series.name="单车数量";
                        option.series.data=data;
                        //设置options
                        _that.bike_group_pie.setOption(option);
                    }
                }
            })


        }


        /**
         * 随机生成十六进制颜色
         * @returns {string}
         */
        randomHexColor(){
            return '#' + ('00000' + (Math.random() * 0x1000000 << 0).toString(16)).substr(-6);
        }

    }

    //注册导航菜单点击事件
    function registNavMenuEvents(){
        /**
         * 菜单点击事件
         */
        $("#btnLogonOut").on("click",function (){
            layer.confirm('确认退出登录吗?', {icon: 3, title:'提示'}, function(index){
                //do something
                //销毁本地存储中的个人信息
                localStorage.removeItem("user");
                //页面跳转到登陆页面
                window.location.href="login.html";
                //关闭窗口
                layer.close(index);
            });
        })

        /**
         * 导航菜单切换
         */
        element.on("nav(menu)",function (elem){
            // console.log(elem[0].getAttribute("lay-filter"))
            //获取内容区域容器
            if (elem[0]&&elem[0].getAttribute("lay-filter")){
                //根据lay-filter属性值，判断将要执行的操作
                let lay_filterVal = elem[0].getAttribute("lay-filter");
                switch (lay_filterVal){
                    case "settings":{
                        //个人信息设置
                        tabChange("#settings");
                        $(".sub-title").text("个人信息设置")
                        break;
                    }
                    case "pswdModify":{
                        //修改用户密码
                        tabChange("#pswdModify");
                        //修改子标题
                        $(".sub-title").text("修改用户密码")
                        //显示用户名
                        $("#modi-username").val(localStorage.getItem("user"));
                        break;
                    }
                    case "addBike":{
                        //单车投放管理
                        tabChange("#addBike");
                        $(".sub-title").text("单车投放管理")
                        //为子节点添加layui-show属性(因为左边导航菜单在执行切换操作时,会将相同class属性的子节点的layui-show属性移除掉)
                        $("#view_type").addClass("layui-show");
                        //初始化单车类型信息
                        init_all_bike_panel();
                        break;
                    }
                }
            }
        })

        /**
         * 通过Jquery实现标签页内容切换
         * @param _id
         */
        function tabChange(_id){
            const layuiTabCons = $(".layui-tab-content");
            let showTabCon = layuiTabCons.find(".layui-show")
            //通过id获取新的子content内容
            let willShowCon = layuiTabCons.find(_id);
            //如果查询结果不为空
            if (showTabCon&&showTabCon[0]&&willShowCon&&willShowCon[0]){
                //移除layui-show属性属性
                showTabCon.removeClass("layui-show");
                //切换到内容1
                willShowCon.addClass("layui-show")
            }
        }
    }

    /**
     * 获取用户信息
     */
    function getUserInfoByUsername(username){
        // $("#user-photo").attr("src","http://localhost:8011/user/get/photo?uername="+localStorage.getItem("user"))
        // console.log($("#user-photo").attr("src"));
        //获取用户信息
        $.ajax({
            url:"http://localhost:8011/user/get/user",
            method:"GET",
            data:{username:username},
            success:function (result){
                if (result){
                    switch (result.code){
                        case 0:{
                            //获取失败
                            layer.msg(result.msg);
                            break;
                        }
                        case 1:{
                            //获取成功
                            layer.msg(result.msg);
                            //解析数据
                            let user  = result.data;
                            if (user){
                                //显示图片
                                user.photo?$("#user-photo").attr("src","data:image/jpeg;base64,"+user.photo):'';
                                //表单值的修改
                                form.val("user-form",{
                                    name:user.name,
                                    age:user.age,
                                    sex:user.gender?user.gender:'男',
                                    email:user.email,
                                    tel:user.tel,
                                    address:user.address?user.address:"",
                                    'like[write]':user.hobby?(user.hobby.indexOf('写作')!=-1?true:false):'',
                                    'like[read]':user.hobby?(user.hobby.indexOf('阅读')!=-1?true:false):'',
                                    'like[dai]':user.hobby?(user.hobby.indexOf('发呆')!=-1?true:false):'',
                                    signature:user.signature,
                                })
                            }
                            // console.log(result)
                            break;
                        }
                        default:{
                            layer.msg("系统异常");
                        }
                    }

                }
            }
        })
    }

    /**
     * 修改单车照片
     * @constructor
     */
    function BikePhotoCHangeListener(){
        $("#change-bike-photo").on("change",function (e){
            if (e.currentTarget&&e.currentTarget.files[0]){
                // window.URL 主要作用是读取文件的字符串
                let imgUrl = window.URL||window.webkitURL;
                let file = e.currentTarget.files[0];
                //判断文件格式是否支持
                let dotIndex = file.name.lastIndexOf(".");
                if (file.name.substring(dotIndex+1)!="jpg"){
                    layer.msg("仅支持jpg格式图片");
                    return false;
                }
                // console.log(file)
                let img = new Image();
                //注册事件
                img.onload=function (){
                    $("#bike-photo").attr("src",this.src);
                }
                //修改图片
                img.src = imgUrl.createObjectURL(file);
            }
        })
    }


    /**
     * 修改用户头像
     * @constructor
     */
    function UserPhotoChangeListener(){
        $("#change-photo").on("change",function (e){
            if (e.currentTarget&&e.currentTarget.files[0]){
                // window.URL 主要作用是读取文件的字符串
                let imgUrl = window.URL||window.webkitURL;
                let file = e.currentTarget.files[0];
                //判断文件格式是否支持
                let dotIndex = file.name.lastIndexOf(".");
                if (file.name.substring(dotIndex+1)!="jpg"){
                    layer.msg("仅支持jpg格式图片");
                    return false;
                }
                // console.log(file)
                let img = new Image();
                //注册事件
                img.onload=function (){
                    $("#user-photo").attr("src",this.src);
                }
                //修改图片
                img.src = imgUrl.createObjectURL(file);
                //更新后台照片
                let formdata = new FormData();
                formdata.append("username",localStorage.getItem("user"));
                formdata.append("photo",file);
                $.ajax({
                    url:"http://localhost:8011/user/update/photo",
                    method:"POST",
                    processData:false,
                    contentType:false,
                    data:formdata,
                    success:function (result){
                        if (result){
                            switch (result.code){
                                case 0:{
                                    //登陆失败
                                    layer.msg(result.msg);
                                    break;
                                }
                                case 1:{
                                    //登录成功
                                    layer.msg(result.msg);
                                    break;
                                }
                                default:{
                                    layer.msg("系统异常");
                                }
                            }
                        }
                    },
                    error:function (){
                        layer.msg("图片大小应小于10MB");
                    }
                })

            }
        })
    }

    /**
     * 监听-表单信息刷新
     */
    function formRefreshListener(){
        form.on('submit(refresh)',function (){
            layer.msg("刷新用户信息视图")
            getUserInfoByUsername(localStorage.getItem("user"));
            return false;
        })
    }


    /**
     * 监听-表单信息提交
     */
    function formSubmitListener(){
        //监听个人信息设置提交
        form.on('submit(settings)', function(data){
            // layer.msg(JSON.stringify(data.field));
            let user = data.field;
            // console.log(user['like[dai]']=='on')
            user.hobby=(user['like[dai]']=='on'?"[发呆]":"")+
                        (user['like[write]']=='on'?"[写作]":"")+
                        (user['like[read]']=='on'?"[阅读]":"");
            // console.log(user)
            $.ajax({
                url:"http://localhost:8011/user/update/user",
                method:"POST",
                data:{
                    account:localStorage.getItem("user"),
                    name:user.name,
                    age:user.age,
                    gender:user.sex,
                    tel:user.tel,
                    email: user.email,
                    address: user.address,
                    hobby:user.hobby,
                    signature:user.signature,
                },
                success:function (result){
                    // console.log(result)
                    switch (result.code){
                        case 0:{
                            //更新失败
                            layer.msg("信息更新失败");
                            break;
                        }
                        case 1:{
                            //更新成功
                            layer.msg("信息更新成功");
                            break;
                        }
                        default:{
                            layer.msg("系统异常");
                        }
                    }
                },
                error:function (){
                    layer.msg("网络异常,修改失败!")
                }
            })

            return false;
        });
        //监听密码修改设置提交
        form.on("submit(modify)",function (data){
            // console.log(data.field)
            //获取填写的用户信息
            let user = data.field;
            // console.log(user)
            if (user.modipassword!=user.modicheckPswd){
                layer.msg("两次输入的密码不一致");
                return  false;
            }
            //继续请求
            $.ajax({
                url:"http://localhost:8011/user/update/user",
                method:"POST",
                data:{
                    account:localStorage.getItem("user"),
                    password:calcMD5(user.modipassword),
                },
                success:function (result){
                    // console.log(result)
                    switch (result.code){
                        case 0:{
                            //更新失败
                            layer.msg("密码修改失败");
                            break;
                        }
                        case 1:{
                            //更新成功
                            layer.msg("密码修改成功");
                            break;
                        }
                        default:{
                            layer.msg("系统异常");
                        }
                    }
                },
                error:function (){
                    layer.msg("网络异常,修改失败!")
                }
            })
            return false;
        })
    }


    /**
     * 初始化树形菜单+菜单点击事件监听
     */
    function treeMenuInitialize(){
        let data= [{
            title: '单车类型管理'
            ,id: 1
            ,children: [{
                title: '查看类型'
                ,id: 1000
            },{
                title: '增加类型'
                ,id: 1001
            }]
        },{
            title: '单车投放管理'
            ,id: 2
            ,children: [{
                title: '投放分布'
                ,id: 2000
            },{
                title: '操作日志'
                ,id: 2001
            }]
        }]
        tree.render({
            elem:"#treemenu",
            data:data,
            isJump:true,
            showLine:false,
            accordion:true,
            spread:true,
            click:function (node){
                //点击回调
                //根据菜单名称判断要执行的动作
                switch (node.data.title){
                    case "查看类型":{
                        //查看单车类型sub-tab-content
                        // console.log("查看类型")
                        tabChange("#sub-tab-content","#view_type")
                        //修改标题
                        $(".title-left").text("单车类型管理");
                        $(".sub-title-left").text("查看类型");
                        //初始化单车类型信息
                        init_all_bike_panel();
                        break;
                    }
                    case "增加类型":{
                        //增加单车类型
                        console.log("增加类型")
                        tabChange("#sub-tab-content","#add_type")
                        //修改标题
                        $(".title-left").text("单车类型管理");
                        $(".sub-title-left").text("增加类型");
                        break;
                    }
                    case "投放分布":{
                        //加载Json地图-查看单车在各个政区的分布状况
                        console.log("投放分布")
                        tabChange("#sub-tab-content","#send_distribute")
                        //修改标题
                        $(".title-left").text("单车投放管理");
                        $(".sub-title-left").text("投放分布");
                        //初始化地图
                        if (!mapInstance){
                            //mapInstance为全局变量——用于监听地图是否已经实例化
                            mapInstance = new OlMap("map_container",geojson_jiangsu);
                        }
                        break;
                    }
                    case "操作日志":{
                        //查看单车投放的操作日志
                        console.log("操作日志")
                        tabChange("#sub-tab-content","#operation_log")
                        //修改标题
                        $(".title-left").text("单车投放管理");
                        $(".sub-title-left").text("操作日志");
                        //初始化操作日志信息
                        init_all_operationLogs();
                        break;
                    }
                }

            }
        });
        /**
         * 通过Jquery实现标签页内容切换
         * @param _id
         */
        function tabChange(_containerSelector,_id){
            const layuiTabCons = $(_containerSelector);
            let showTabCon = layuiTabCons.find(".layui-show")
            //通过id获取新的子content内容
            let willShowCon = layuiTabCons.find(_id);
            console.log(showTabCon)
            console.log(willShowCon)
            //如果查询结果不为空
            if (showTabCon&&showTabCon[0]&&willShowCon&&willShowCon[0]){
                //移除layui-show属性属性
                showTabCon.removeClass("layui-show");
                //切换到内容1
                willShowCon.addClass("layui-show")
            }
        }
    }

    /**
     * 初始化单车信息panel面板
     */
    function init_all_bike_panel(){
        //先清除上一次的渲染结果
        $("#view_type_content").empty();
        $.ajax({
            url:"http://localhost:8011/biketype/get/all/count",
            method:"GET",
            success:function (result){
                // console.log(result)
                //解析结果集-填充数据到HTML模板中
                if (result&&result.data){
                    layer.msg("正在加载单车类型数据...")
                    for (let i = 0; i < result.data.length; i++) {
                        let template = '<div class="layui-col-md4" style="min-height: 650px">\n' +
                            '           <div class="layui-card">\n' +
                            '               <div class="layui-card-header">单车类型：'+(result.data[i].type)+'</div>\n' +
                            '               <div class="layui-card-body">\n' +
                            '                   <div class="layui-row">\n' +
                            '                       <div class="layui-col-md12">\n' +
                            '                           <img src="'+("data:image/jpeg;base64,"+result.data[i].photo) +'" style="width: 90%;height: 90%;object-fit:fill"/>\n' +
                            '                       </div>\n' +
                            '                   </div>\n' +
                            '                   <div class="layui-row">\n' +
                            '                       <div class="layui-col-md12" style="padding: 5px">\n' +
                            '                           <table class="layui-table">\n' +
                            '                               <colgroup>\n' +
                            '                                   <col width="150">\n' +
                            '                                   <col width="200">\n' +
                            '                                   <col>\n' +
                            '                               </colgroup>\n' +
                            '                               <thead>\n' +
                            '                               <tr>\n' +
                            '                                   <th>ID编号</th>\n' +
                            '                                   <th>'+(result.data[i].id)+'</th>\n' +
                            '                               </tr>\n' +
                            '                               </thead>\n' +
                            '                               <tbody>\n' +
                            '                               <tr>\n' +
                            '                                   <td>单车类型</td>\n' +
                            '                                   <td>'+(result.data[i].type)+'</td>\n' +
                            '                               </tr>\n' +
                            '                               <tr>\n' +
                            '                                   <td>成本价格</td>\n' +
                            '                                   <td>'+(result.data[i].price.toFixed(2))+'￥</td>\n' +
                            '                               </tr>\n' +
                            '                               <tr>\n' +
                            '                                   <td>使用年限</td>\n' +
                            '                                   <td>'+(result.data[i].limits.toFixed(1))+' 年</td>\n' +
                            '                               </tr>\n' +
                            '                               <tr>\n' +
                            '                                   <td>总投放量</td>\n' +
                            '                                   <td>'+result.data[i].count+'/辆</td>\n' +
                            '                               </tr>\n' +
                            '                               <tr>\n' +
                            '                                   <td>累计成本</td>\n' +
                            '                                   <td>'+(result.data[i].price*result.data[i].count).toFixed(2)+'￥</td>\n' +
                            '                               </tr>\n' +
                            '                               <tr>\n' +
                            '                                   <td>描述信息</td>\n' +
                            '                                   <td>'+(result.data[i].description)+'</td>\n' +
                            '                               </tr>\n' +
                            '                               </tbody>\n' +
                            '                           </table>\n' +
                            '                       </div>\n' +
                            '                   </div>\n' +
                            '               </div>\n' +
                            '           </div>\n' +
                            '       </div>';
                        //增加HTML子节点到父容器中
                        $("#view_type_content").append(template);
                    }
                }else{
                    layer.msg("查询结果为空!");
                }
            },
            error:function (){
                layer.msg("数据请求失败!");
            }
        })
    }

    /**
     * 增加单车类型信息表单提交监听
     */
    function add_single_bike_submit(){
        //表单验证
        form.on("submit(add_single_bike_type)",function (data_form){
            // console.log(data_form)
            let filedVal = data_form.field;
            //判断数据类型是否有效
            if (!(/^[1-9][0-9]*([\.][0-9]{1,2})?$/.test(filedVal.price))){
                layer.msg("成本价格仅支持正整数或者小数,请修改!");
                return false;
            }
            if (!(/^[1-9][0-9]*([\.][0-9]{1,2})?$/.test(filedVal.limits))){
                layer.msg("使用年限仅支持正整数或者小数,请修改!");
                return false;
            }
            //创建FormData对象
            let formData = new FormData();
            // console.log( $("#change-bike-photo")[0].files[0])
            formData.append("type",filedVal.type);
            formData.append("description",filedVal.description);
            formData.append("price",filedVal.price);
            formData.append("limits",filedVal.limits);
            formData.append("photo",$("#change-bike-photo")[0].files[0]);
            //提交数据
            // console.log(formData)
            $.ajax({
                url:"http://localhost:8011/biketype/insert",
                method:"POST",
                data:formData,
                processData:false,
                contentType:false,
                success:function (result){
                    if(result&&result.code==1){
                        layer.msg("已成功增加新的单车类型");
                    }else{
                        layer.msg("添加新车型失败,请稍后再试!")
                    }
                },
                error:function (){
                    layer.msg("系统异常!")
                }
            })
                //获取图片文件资源
                // $("#change-bike-photo")[0].files[0]
            return false;
        })
    }

    /**
     * 增加单车类型信息表单重置监听
     */
    function  add_single_bike_reset(){
        $("#reset_bikeForm_btn").on("click",function(){
            form.val("add_bike_type_form",{
                bikephoto:undefined,
                type:"",
                price:"",
                limits:"",
                description:"",
            })
            //释放src指向的图片资源
            $("#bike-photo-item").empty();
            $("#bike-photo-item").append(' <label class="layui-form-label">单车图片</label>\n' +
                '                          <div class="layui-input-block">\n' +
                '                              <img src="../static/imgs/unknown.png" id="bike-photo">\n' +
                '                          </div>\n' +
                '                          <div class="layui-form-mid layui-word-aux bike-photo-tips">Tips:<a class="input-change-bike-photo">点击此处<input type="file" name="bikephoto" accept="image/jpeg" multiple="false" id="change-bike-photo"></a>修改照片</div>')
            //重新注册bike图片修改事件
            BikePhotoCHangeListener();
        })
    }

    /**
     * 初始化操作日志信息
     */
    function init_all_operationLogs(){
        //定义表格渲染参数模板
        let table_reanderOptions={
            elem: '#operationLog'
            //标题栏
            , cols: [[
                //定义表头
                {field:"id",title:"ID",width:50,fixed:"left",align:"center"},
                {field:'stopname',title:"停车点名称",width:150,align:"center",templet:function (res){return res.stop.stopname;}},
                {field:"biketype",title:"投放类型",width:100,align:"center",templet:function (res){return res.bikeType.type;}},
                {field:"addtion",title:"投放量(辆)",width:150,align:"center"},
                {field:"operationdate",title:"投放时间",width:180,align:"center"},
                {field:"price",title:"成本单价(￥)",width:150,align:"center",templet:function (res){return res.bikeType.price;}},
                {field:"totalPrice",title:"总成本(￥)",width:150,align:"center",templet:function (res){return ((res.bikeType.price)*(res.addtion)).toFixed(2);}},
                {field:"username",title:"操作者",width:150,align:"center"},
            ]]
            , page: true
            , parseData: function (res) {
                /**
                 * 接口返回的数据格式并不一定都符合 table 默认规定的格式,
                 * layUI默认格式:
                 {
                              "code": 0,
                              "msg": "",
                              "count": 1000,
                              "data": [{}, {}]
                            }
                 借助 parseData 回调函数将其解析成 table 组件所规定的数据格式
                 */
                return {
                    code: res.code==1?0:1,//状态码--必须为0-否则也不会加载
                    msg: res.msg,//解析提示文本
                    count: res.data.length,//解析数据长度
                    data: res.data,//解析数据列表
                }
            },
            data:undefined,//表格数据
        };
        //请求数据
        $.ajax({
            url:"http://localhost:8011/stopstore/select/detail",
            method:"GET",
            success:function (result){
                if (result&&result.data){
                    table_reanderOptions.data=result.data
                    table.render(table_reanderOptions)
                }
            }
        })
    }

    /**
     * 事件注册中心函数
     */
    function registEvents(){
        //注册左侧导航菜单点击事件
        registNavMenuEvents();
        //头像修改事件监听
        UserPhotoChangeListener();
        //单车照片修改事件监听
        BikePhotoCHangeListener();
        //表单提交事件监听
        formSubmitListener();
        //表单信息刷新监听
        formRefreshListener();
        //树形菜单注册
        treeMenuInitialize();
        //注册子面板——增加单车类型:表单提交信息监听
        add_single_bike_submit();
        //注册子面板——增加单车类型:表单重置信息监听
        add_single_bike_reset();
    }

    //导出当前模块
    exports('admin',{
        registEvents,
        //功能接口
        getUserInfoByUsername,
    })
})
