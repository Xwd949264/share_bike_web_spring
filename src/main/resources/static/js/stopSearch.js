layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        $ = layui.jquery;
    //表格数据渲染
    /**
     * 由于指定了url时,page分页字段失效
     * 因此,可以先由Ajax异步请求数据,再调用layUI.table的render方法进行渲染。
     */
        //定义表格渲染参数模板
    var table_reanderOptions={
            elem: '#test'
            , toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            , defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                , layEvent: 'LAYTABLE_TIPS'
                , icon: 'layui-icon-tips'
            }]
            //标题栏
            , cols: [[
                //定义表头
                {field:"id",title:"ID",width:50,fixed:"left",align:"center"},
                {field:"stopno",title:"停车点编号",width:150,align:"center"},
                {field:'stopname',title:"停车点名称",width:150,align:"center"},
                {field:"builddate",title:"启用日期",width:100,align:"center"},
                {field:"capacity",title:"总容量/位",width:100,align:"center"},
                {field:"size",title:"投放量/辆",width:100,align:"center"},
                {field:"city",title:"政区名称",width:100,align:"center",templet:function (res){return res.city.cityname;}},
                {field:"belong",title:"政区编号",width:100,align:"center"},
                {fixed: 'right', title:'操作', toolbar: '#barOperations', width:120}
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
    $.ajax({
        url:"http://localhost:8011/stop/select/stop/by",
        method:"GET",
        success:function (res){
            //指定表格数据
            table_reanderOptions.data=res.data;
            //调用render方法渲染数据
            table.render(table_reanderOptions);
        }
    });

    //监听行工具事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        // console.log(obj)
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                obj.del();
                layer.msg("删除")
                layer.close(index);
            });
        } else if (obj.event === 'locate') {
            //创建一个geoFeature
            var geoJsonFeature = {
                "type": "Feature",
                "properties": {"party": "Democrat"},
                "geometry": obj.data.geom};
            //创建Geometry对象
            var centeroid = L.geoJSON(geoJsonFeature).getBounds().getCenter();
            window.localStorage.removeItem("centeroid");
            localStorage.setItem("centeroid",JSON.stringify(centeroid));
            layer.msg("正在定位到:" + obj.data.stopname)
            //关闭当前窗口
            setTimeout(function (){
                //当你在iframe页面关闭自身时
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
            },1000);
        }
    });

    //监听停车区域检索操作
    form.on('submit(searchClick)', function (data) {
        // console.log(data.field)
        $.ajax({
            url:"http://localhost:8011/stop/select/stop/by",
            method:"GET",
            data:{
                stopname:data.field.stopname,//停车点名称
                belong:data.field.belong,//所属政区
            },
            success:function (res){
                //设置表单数据
                console.log(res)
                table_reanderOptions.data=res.data;
                //重载表格数据
                table.reload("test",table_reanderOptions,true);
            }
        })
        return false;
    });
});