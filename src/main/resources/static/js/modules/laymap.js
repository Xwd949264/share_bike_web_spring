/**
 * 定义新的layui模块：layui.define([mods], callback)
 * @param mods 表示示新增自定义layui模块名称
 * @param callback 用于输出该模块的接口
 */
layui.define(['layer','form'],function (exports){
    const form = layui.form,
            $=layui.$;
    /**
     * 封装LeafletMap类-定义地图构造器
     * @constructor 构造函数
     */
    function LeafletMap(){
        //成员属性
        this.map=undefined;//Leaflet的Map对象
        this.baseMap_url='http://webrd0{s}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scale=1&style=8";';
        this.baseLayers=[];//基础图层数组
        this.geoJson=undefined;//geoJson图层
        this.isshowGeoJson=true;//是否显示geojson图层
        this.zoom=1;//地图缩放大小
        this.editableLayers=undefined;//可编辑图层Layer对象
        this.selectionLayer=undefined;//当前已选中的可编辑Layer对象
        this.stops_layerGroup=undefined;//停车点斑块区域图层组
        this.bikes_layerGroup=undefined;//单车点图层组
        //成员方法
        /*
         *   @param _mapid 地图容器id
         *   @param _lon 视图中心经度
         *   @param _lat 视图中心纬度
         *   @param _zoom 缩放大小
         * */
        this.init=function (_mapid,_lon=119.00,_lat=33.30,_zoom=7){
            this._zoom=_zoom;
            this.map=L.map(_mapid,{
                // drawControl: true
                contextmenu: true,
                contextmenuWidth: 140,
                contextmenuItems: [{
                    text: '显示当前坐标',
                    callback: this.showCoordinates,
                }, {
                    text: '添加至停车区域',
                    callback: this.addAsStopZone
                }, '-', {
                    text: '取消已选图层',
                    callback: this.cancelSelection
                }, {
                    text: '移除选中图层',
                    callback: this.removeSelection
                }]
            });
            //添加工具栏
            // this.addToolbarToMap();
            //创建Layer对象
            //卫星图
            this.baseLayers.push(L.tileLayer.chinaProvider('GaoDe.Satellite.Map', {
                maxZoom: 18,
                minZoom: 5
            }));
            //普通地图
            this.baseLayers.push(L.tileLayer(this.baseMap_url,{
                subdomains:"1234",
                crs: L.CRS.EPSG3857,
                minZoom: 1,
                maxZoom: 20,
            }))
            this.addToolbarToMap();
            //设置视图
            this.map.setView([_lat,_lon],_zoom);
            //添加
            this.addLayer(this.baseLayers[1]);
            this.registOnClick();
            this.registOnZoom();
            //添加停车点区域斑块
            this.init_StopPlaque();
            //添加停车点
            this.init_BikePoint();
            //每隔5分钟刷新一下图层
            let _that = this;
            setInterval(function (){
                // console.log("刷新")
                if (_that.bikes_layerGroup){
                    _that.bikes_layerGroup.remove();
                    _that.init_BikePoint();
                }
                if (_that.stops_layerGroup){
                    _that.stops_layerGroup.remove();
                    _that.init_StopPlaque();
                }
            },1000*60*30);

        }


        this.init_BikePoint=function (_bno,_belong){
            let _that = this
            //?=320800&bno=732bc0b0-9cb9-4ab5-bcdf-dd899bd668f9
            $.ajax({
                url: "http://localhost:8011/bike/get/all/asfeaturecollection",
                method: "GET",
                data: {
                    bno: _bno,
                    belong: _belong,
                },
                success: function (result) {
                    if (result&&result.code==1){
                        //添加数据
                        let geojson = result.data;
                        // console.log(geojson)
                        _that.bikes_layerGroup=L.geoJSON(geojson.feature, {
                            style: function (feature) {
                                return {color: feature.properties.color};
                            }
                        }).bindPopup(function (layer){
                            let startDate = new Date(layer.feature.properties.startdate);
                            let dataStr = startDate.getFullYear()+"-"+(startDate.getMonth()+1)+"-"+startDate.getDate();
                            let template =
                                '<p style="text-align: center;font-weight: 700;color: orange">共享单车</p>'+
                                '<table class="layui-table">\n' +
                                '  <colgroup>\n' +
                                '    <col width="150">\n' +
                                '    <col width="200">\n' +
                                '    <col>\n' +
                                '  </colgroup>\n' +
                                '  <thead>\n' +
                                '    <tr>\n' +
                                '      <th>ID</th>\n' +
                                '      <th id="stopname">'+layer.feature.properties.id + '</th>\n' +
                                '    </tr> \n' +
                                '  </thead>\n' +
                                '  <tbody>\n' +
                                '    <tr>\n' +
                                '      <td>单车编号</td>\n' +
                                '      <td id="bno" style="word-break: break-all;">'+layer.feature.properties.bno+'</td>\n' +
                                '    </tr>\n' +
                                '    <tr>\n' +
                                '      <td>单车类型</td>\n' +
                                '      <td id="capacity">'+layer.feature.properties.type+'</td>\n' +
                                '    </tr>\n' +
                                '    <tr>\n' +
                                '      <td>所属停车点</td>\n' +
                                '      <td id="size">'+layer.feature.properties.stopname+'</td>\n' +
                                '    </tr>\n' +
                                '    <tr>\n' +
                                '      <td>单车状态</td>\n' +
                                '      <td id="status">'+layer.feature.properties.description+'('+layer.feature.properties.status+')'+'</td>\n' +
                                '    </tr>\n' +
                                '    <tr>\n' +
                                '      <td>投放日期</td>\n' +
                                '      <td id="startdate">'+dataStr+'</td>\n' +
                                '    </tr>\n' +
                                '  </tbody>\n' +
                                '</table>' ;
                           return   template;
                        }).bindTooltip(function (layer){
                            let startDate = new Date(layer.feature.properties.startdate);
                            let dataStr = startDate.getFullYear()+"-"+(startDate.getMonth()+1)+"-"+startDate.getDate();
                            let template =
                                '<p style="text-align: center;font-weight: 700;color: orange">共享单车</p>'+
                                '<table class="layui-table">\n' +
                                '  <colgroup>\n' +
                                '    <col width="150">\n' +
                                '    <col width="200">\n' +
                                '    <col>\n' +
                                '  </colgroup>\n' +
                                '  <thead>\n' +
                                '    <tr>\n' +
                                '      <th>ID</th>\n' +
                                '      <th id="stopname">'+layer.feature.properties.id + '</th>\n' +
                                '    </tr> \n' +
                                '  </thead>\n' +
                                '  <tbody>\n' +
                                '    <tr>\n' +
                                '      <td>单车编号</td>\n' +
                                '      <td id="bno" style="word-break: break-all;">'+layer.feature.properties.bno+'</td>\n' +
                                '    </tr>\n' +
                                '    <tr>\n' +
                                '      <td>单车类型</td>\n' +
                                '      <td id="capacity">'+layer.feature.properties.type+'</td>\n' +
                                '    </tr>\n' +
                                '    <tr>\n' +
                                '      <td>单车状态</td>\n' +
                                '      <td id="status">'+layer.feature.properties.description+'('+layer.feature.properties.status+')'+'</td>\n' +
                                '    </tr>\n' +
                                '    <tr>\n' +
                                '      <td>投放日期</td>\n' +
                                '      <td id="startdate">'+dataStr+'</td>\n' +
                                '    </tr>\n' +
                                '  </tbody>\n' +
                                '</table>' ;
                            return template;
                        }).addTo(_that.map);
                    }else {
                        console.log("单车点加载失败")
                    }
                }
            })
        }

        /**
         * 初始化停车点斑块区域
         */
        this.init_StopPlaque=function (_stopname,_belong){
            let _that = this
            //请求停车区区域列表
            $.ajax({
                url:"http://localhost:8011/stop/select/stop/by",
                method:"GET",
                data:{
                    stopname:_stopname,
                    belong:_belong,
                },
                success:function (result){
                    // console.log(result)
                    //添加数据到地图中
                    //1-定义FeatureCollection
                    let stops_FeatureCollection={
                        type:"FeatureCollection",
                        feature:[],//geojson格式的Feature
                    };
                    //2-遍历结果集
                    for (let i = 0; i < result.data.length; i++) {
                        //3-Feature转Layer
                        let temp_geoFeature={
                            //定义geojson格式的Feature
                            type:"Feature",
                            //基本属性-properties参数
                            properties:{
                                id:result.data[i].id,
                                stopno:result.data[i].stopno,
                                stopname:result.data[i].stopname,
                                capacity: result.data[i].capacity,
                                size:result.data[i].size,
                                belong:result.data[i].belong,
                                builddate: result.data[i].builddate,
                                cityname:result.data[i].city.cityname,
                                color:"#ff7033",//填充颜色
                            },
                            //几何属性-geometry实例
                            geometry:result.data[i].geom,
                        };
                        stops_FeatureCollection.feature.push(temp_geoFeature);
                    }
                    // console.log(stops_FeatureCollection)
                    //4-将stops_FeatureCollection添加到map中
                    _that.stops_layerGroup=L.geoJSON(stops_FeatureCollection.feature, {
                        style: function (feature) {
                            return {color: feature.properties.color};
                        }
                    }).bindPopup(function (layer){
                        //定制弹窗属性信息
                        //return layer.feature.properties.stopname;
                        let template =
                            '<p style="text-align: center;font-weight: 700;color: orange">停车点斑块</p>'+
                            '<table class="layui-table">\n' +
                            '  <colgroup>\n' +
                            '    <col width="150">\n' +
                            '    <col width="200">\n' +
                            '    <col>\n' +
                            '  </colgroup>\n' +
                            '  <thead>\n' +
                            '    <tr>\n' +
                            '      <th>停车点</th>\n' +
                            '      <th id="stopname">'+layer.feature.properties.stopname+'-'+layer.feature.properties.id+'</th>\n' +
                            '    </tr> \n' +
                            '  </thead>\n' +
                            '  <tbody>\n' +
                            '    <tr>\n' +
                            '      <td>编号</td>\n' +
                            '      <td id="stopno" style="word-break: break-all;">'+layer.feature.properties.stopno+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>容量</td>\n' +
                            '      <td id="capacity">'+layer.feature.properties.capacity+'/位'+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>单车投入</td>\n' +
                            '      <td id="size">'+layer.feature.properties.size+'/辆'+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>可用容量</td>\n' +
                            '      <td id="restcapacity">'+(layer.feature.properties.capacity-layer.feature.properties.size)+'/位'+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>启用日期</td>\n' +
                            '      <td id="builddate">'+layer.feature.properties.builddate+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>所在政区</td>\n' +
                            '      <td id="cityname">'+layer.feature.properties.cityname+'('+layer.feature.properties.belong+')'+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>操作</td>\n' +
                            '      <td><button type="button" class="layui-btn  layui-btn-xs layui-btn-radius layui-btn-normal" id="btn_popup_addBike">投放单车</button></td>\n' +
                            '    </tr>\n' +
                            '  </tbody>\n' +
                            '</table>' ;
                        return template;
                    }).bindTooltip(function (layer){
                        let template =
                            '<table class="layui-table">\n' +
                            '  <colgroup>\n' +
                            '    <col width="150">\n' +
                            '    <col width="200">\n' +
                            '    <col>\n' +
                            '  </colgroup>\n' +
                            '  <thead>\n' +
                            '    <tr>\n' +
                            '      <th>停车点</th>\n' +
                            '      <th id="stopname">'+layer.feature.properties.stopname+'-'+layer.feature.properties.id+'</th>\n' +
                            '    </tr> \n' +
                            '  </thead>\n' +
                            '  <tbody>\n' +
                            '    <tr>\n' +
                            '      <td>编号</td>\n' +
                            '      <td id="stopno" style="word-break: break-all;">'+layer.feature.properties.stopno+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>容量</td>\n' +
                            '      <td id="capacity">'+layer.feature.properties.capacity+'/位'+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>单车投入</td>\n' +
                            '      <td id="size">'+layer.feature.properties.size+'/辆'+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>启用日期</td>\n' +
                            '      <td id="builddate">'+layer.feature.properties.builddate+'</td>\n' +
                            '    </tr>\n' +
                            '    <tr>\n' +
                            '      <td>所在政区</td>\n' +
                            '      <td id="cityname">'+layer.feature.properties.cityname+'('+layer.feature.properties.belong+')'+'</td>\n' +
                            '    </tr>\n' +
                            '  </tbody>\n' +
                            '</table>' ;
                        return template;
                    }).addTo(_that.map);
                    //绑定事件
                    // $("#btn_popup_addBike").on("click",function (e){
                    //     console.log(e)
                    // })
                }
            })
            /**
             * 监听弹出窗事件
             */
            this.map.on("popupopen",(ev1)=>{
                let popupEle = ev1.popup.getElement();
                // console.log(popupEle)
                // let cityname=popupEle.querySelector("#cityname").textContent;
                // let ss = popupEle.querySelector('#btn_popup_addBike');
                // console.log(ev1.popup._container)
                // let querySelector = document.querySelector(".leaflet-popup-content");
                // console.log(querySelector.innerHTML)
                // let id=ss.getAttribute("id");
                // ss.addEventListener("click",(ev)=>{
                //     console.log(this,`选中的是${id }`);
                // })
                //获取按钮
                let addBike_btn = popupEle.querySelector('#btn_popup_addBike');
                if (!addBike_btn) return false;
                addBike_btn.onclick=function (){
                    console.log(popupEle.querySelector("#cityname").textContent)
                    let cityname_text =popupEle.querySelector("#cityname").textContent;
                    let stopname = popupEle.querySelector("#stopname").textContent;
                    let capacity = popupEle.querySelector("#restcapacity").textContent;

                    //投放单车-弹出层
                    layer.open({
                        type: 2
                        ,title:"投放单车"
                        ,area: ['450px', '470px']
                        ,offset: "r" //右侧
                        ,id: 'layerDemo'+new Date().getTime() //防止重复弹出
                        ,content: "addBike.html"
                        ,btnAlign: 'c' //按钮居中
                        ,shade: 0.3 //显示遮罩
                        ,success:function (layer0, index){
                            //回调方法
                            //设置基本信息
                            let body = layer.getChildFrame('body', index);
                            // var iframeWin = window[layer0.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                            // console.log(body) //得到iframe页的body内容
                            // body.find('input').val('Hi，我是从父页来的')
                            //给子页面传参
                            body.find("#stopid").val(stopname.substring(stopname.lastIndexOf("-")+1));//停车点ID
                            body.find("#stopname").val(stopname.substring(0,stopname.lastIndexOf("-")));//停车点名
                            body.find("#cityname").val(cityname_text);//城市名称
                            body.find("#capacity").val(capacity);//可用容量
                        }
                    })
                }
            });
        }


        /**
         * 添加工具栏到地图
         */
        this.addToolbarToMap=function (){
            if (this.map){
                let _that = this;
                this.editableLayers=new L.FeatureGroup();
                this.map.addLayer(this.editableLayers);

                var MyCustomMarker = L.Icon.extend({
                    options: {
                        shadowUrl: null,
                        iconAnchor: new L.Point(12, 12),
                        iconSize: new L.Point(24, 24),
                        iconUrl: './images/marker-icon.png'
                    }
                });
                var options = {
                    position: 'topright',
                    draw: {
                        polyline: {
                            shapeOptions: {
                                color: '#CCCCCC',
                                weight: 10
                            }
                        },
                        polygon: {
                            allowIntersection: false, // Restricts shapes to simple polygons
                            drawError: {
                                color: '#CCCCCC', // Color the shape will turn when intersects
                                message: '<strong>Oh snap!<strong> you can\'t draw that!' // Message that will show when intersect
                            },
                            shapeOptions: {
                                color: '#CCCCCC'
                            }
                        },
                        circle: false, // Turns off this drawing tool
                        rectangle: {
                            shapeOptions: {
                                clickable: false
                            }
                        },
                        marker: {
                            icon: new MyCustomMarker()
                        }
                    },
                    edit: {
                        featureGroup: this.editableLayers, //REQUIRED!!
                        remove: false
                    }
                };
                var drawControl = new L.Control.Draw(options);
                this.map.addControl(drawControl);

                this.map.on(L.Draw.Event.CREATED, function (e) {
                    var type = e.layerType,
                        layer = e.layer;

                    if (type === 'marker') {
                        layer.bindPopup('A popup!');
                    }
                    _that.editableLayers.addLayer(layer);
                });
            }
        }

        /**
         * 右键菜单回调方法
         */
        //显示当前坐标值
        this.showCoordinates = function(e) {
            let latlng = e.latlng;
            let msg = "当前坐标:("+
                ((latlng&&latlng.lng)?latlng.lng.toFixed(4):"00.0000") +
                ","+ ((latlng&&latlng.lat)?latlng.lat.toFixed(4):"00.0000")+ ")";
            layer.msg(msg);
        }

        /**
         * 添加选中要素为停车区域
         */
        this.addAsStopZone = function (e){
            // console.log(window.selectionLayer)
            if (window.selectionLayer){
                // console.log(JSON.stringify(window.selectionLayer.toGeoJSON()))
                window.selectionLayer.getBounds();
                layer.open({
                    type:2
                    ,title: '添加停车区'
                    ,content: 'addStopForm.html'
                    ,area: ['600px', '560px']
                    ,offset: 'auto' //垂直水平居中
                    ,anim: 5
                    ,shadeClose: true
                    ,scrollbar: false
                    ,success:function (layer0, index){
                        // console.log(layer,index)
                        let frameBody = layer.getChildFrame("body",index);
                        console.log(frameBody)
                        //参数值回传
                        let northeast_val=window.selectionLayer.getBounds().getNorthEast();
                        let southwest_val=window.selectionLayer.getBounds().getSouthWest();
                        frameBody.find("#northeast").val("("+northeast_val.lat.toFixed(3)+","+northeast_val.lng.toFixed(3)+")")
                        frameBody.find("#southwest").val("("+southwest_val.lat.toFixed(3)+","+southwest_val.lng.toFixed(3)+")")
                        frameBody.find("#stopdefine").val(JSON.stringify(window.selectionLayer.toGeoJSON().geometry))
                        // console.log(JSON.stringify(window.selectionLayer.toGeoJSON()));
                        //获取所在区域
                        // $.ajax({
                        //     url:"http://localhost:8011/city/query/insect",
                        //     method:"POST",
                        //     data:{
                        //         area:JSON.stringify(window.selectionLayer.toGeoJSON().geometry),
                        //     },
                        //     success:function (result){
                        //         // console.log(result)
                        //         if (result.data&&result.data[0]){
                        //             frameBody.find(".layui-select-title input").val(result.data[0].cityname);
                        //         }
                        //     }
                        // })
                    }
                })
            }else{
                layer.msg("未选中任何图层,添加失败!",{
                    anim:3
                })
            }
        }

        //取消已选中图层
        this.cancelSelection= function (e){
            console.log(e)
            if (window.selectionLayer){
                window.selectionLayer.setStyle({
                    color:"#CCCCCC"
                });
                window.selectionLayer=undefined;
            }else {
                layer.msg("未选中任何图层!",{
                    anim:3
                })
            }
        }

        //移除已选中图层
        this.removeSelection=function (e){
            if (window.selectionLayer){
                window.selectionLayer.remove();
                window.selectionLayer=undefined;
            }else {
                layer.msg("未选中任何图层,移除失败!",{
                    anim:3
                })
            }
        }

        /**
         * 添加图层
         * @param _tileLayer Layer对象
         */
        this.addLayer=function (_tileLayer){
            if (this.map){
                this.map.addLayer(_tileLayer);
            }
        }

        /**
         * 监听鼠标点击事件
         */
        this.registOnClick=function (){
            let _that = this;
            if (this.map){
                this.editableLayers.on("click",function (evt){
                    console.log(evt)
                    window.selectionLayer=undefined;
                    //引用被选中对象
                    window.selectionLayer=evt.sourceTarget;
                    evt.target.setStyle({
                        color:"#CCCCCC"
                    })
                    evt.sourceTarget.setStyle({
                        color:"#d3d305"
                    });
                })
            }
        }

        /**
         * 监听鼠标移动事件
         */
        this.registOnMouseMove=function (_lngid,_latid,_zoomid){
            let _that=this;
            if(this.map){
                this.map.on("mousemove",function (e){
                    //初始化鼠标位置
                    _that.mousePosition = e.latlng;
                    $("#"+_lngid).text((_that.mousePosition&&_that.mousePosition.lng)? _that.mousePosition.lng.toFixed(4):"00.0000");
                    $("#"+_latid).text((_that.mousePosition&&_that.mousePosition.lat)? _that.mousePosition.lat.toFixed(4):"00.0000");
                    $("#"+_zoomid).text(_that.map.getZoom());
                })
            }
        }
        /**
         * 监听地图缩放事件
         */
        this.registOnZoom=function (){
            let _that=this;
            if (this.map){
                this.map.on("zoom",function (e) {
                    _that.zoom=_that.map.getZoom();
                })
            }
        }
        /**
         * 添加geojson对象-支持添加一个geojson
         */
        this.addGeoJson=function (_geojson){
            if (this.map){
                this.geoJson=L.geoJSON(_geojson,
                    {
                        style:{
                            "color": "#33ffd5",
                            "weight": 1,
                            "opacity": 0.65,
                            "fill":this.isshowGeoJson,}
                    }).bindPopup(function (layer) {return layer.feature.properties.name})
                    .addTo(this.map);
            }
        }

        /**
         * 用户定位
         */
        this.locatePosition=function (){
            let _that = this;
            //用户定位
            if (navigator.geolocation)
            {
                layer.msg("正在获取您的地理位置",{
                    anim:3
                })
                navigator.geolocation.getCurrentPosition(function (position) {
                    // console.log(position)
                    let latitude=position.coords.latitude;
                    let longitude=position.coords.longitude;
                    //移动视图中心位置
                    let marker_layer_temp = L.marker([latitude,longitude]).addTo(_that.map);
                    //3秒后自定移除marker
                    setTimeout(function(){
                        marker_layer_temp.remove();
                    }, 3000);
                    //移动视图到当前位置
                    _that.map.setView([ latitude,longitude], 14);
                });
            }
            else
            {
                layer.msg("当前浏览器不支持获取地理位置!",{
                    anim:3
                });
            }
        }

        /**
         * 隐藏geojson
         */
        this.hideGeoJsonLayer=function (){
            if (this.map&&this.geoJson){
                this.isshowGeoJson=!this.isshowGeoJson;
                this.geoJson.setStyle({
                    "color": "#33ffd5",
                    "weight": 1,
                    "opacity": 0.65,
                    "fill":this.isshowGeoJson,
                });
            }
        }

        /**
         * 切换视图
         */
        this.switchLayer=function (){
            if (this.map.hasLayer(this.baseLayers[1])){
                this.map.removeLayer(this.baseLayers[1]);
                this.map.addLayer(this.baseLayers[0]);
            }else if (this.map.hasLayer(this.baseLayers[0])){
                this.map.removeLayer(this.baseLayers[0]);
                this.map.addLayer(this.baseLayers[1]);
            }
        }

        /**
         * 重置视图
         */
        this.resetMapView=function (){
            if (this.map){
                this.map.setView([33.30,119.00],7);
            }
        }

        /**
         * 移动视图中心-设置缩放比
         */
        this.panToPoint=function (_centeroid,_zoom=14){
            if (this.map){
                this.map.panTo(_centeroid)
                this.map.setZoom(_zoom);
            }
        }

        /**
         * 绘图事件
         */
        this.drawPolygon=function (){
            alert("开始绘图")
        }
    }


    //导出模块
    exports("laymap",{
        //导出函数列表
        LeafletMap,
    })
})
