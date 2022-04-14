layui.use(['form','table','layer'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        $=layui.jquery;
    //表格数据渲染
    var tableRender_Options={
        elem: '#bike_table'
        // ,url:'http://localhost:8011/bike/get/all/aslist'
        ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
        ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
            title: '提示'
            ,layEvent: 'LAYTABLE_TIPS'
            ,icon: 'layui-icon-tips'
        }]
        ,title: '用户数据表'
        //标题栏
        ,cols: [[
            {field:'id', title:'ID', width:80, fixed: 'left', unresize: true,align:"center"}
            ,{field:'bno', title:'编号', width:250, edit: 'text',align:"center"}
            ,{field:'type', title:'单车类型', width:120, edit: 'text',align:"center"}
            ,{field:'description', title:'单车状态', width:120, edit: 'text',align:"center"}
            ,{field:'startdate', title:'投放日期', width:120, edit: 'text',align:"center", templet:function (res){
                    var use_date = new Date(res.startdate);
                    return use_date.getFullYear()+"-"+(use_date.getMonth()+1)+"-"+use_date.getDate()+" "+
                        use_date.getHours()+":"+use_date.getMinutes()+":"+use_date.getSeconds();
                }}
            ,{field:'stopname', title:'所属停车点', width:150, edit: 'text',align:"center"}
            ,{field:'cityname', title:'所属市区', width:120,align:"center"}
            ,{fixed: 'right', title:'操作', toolbar: '#barOperations', width:60}
        ]]
        ,page: true
        ,parseData: function (res) {
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
        }
        ,data:[]
    };
    //数据请求
    $.ajax({
        url:"http://localhost:8011/bike/get/all/aslist",
        method:"GET",
        success:function (res){
            // console.log(res)
            //指定表格数据
            tableRender_Options.data=res.data;
            //调用render方法渲染数据
            table.render(tableRender_Options);
        }
    })

    //监听行工具事件
    table.on('tool(bike_table)', function(obj){
        if(obj.event === 'locate'){
            layer.msg("正在定位到目标单车,编号:"+obj.data.bno)
            var centeroid=JSON.parse(obj.data.location).coordinates;
            var lnglat={"lat":centeroid[1],"lng":centeroid[0]};
            window.localStorage.removeItem("centeroid_bike");
            window.localStorage.setItem("centeroid_bike",JSON.stringify(lnglat));
            //关闭当前窗口
            setTimeout(function (){
                //当你在iframe页面关闭自身时
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
            },1000);
        }
    });

    //监听提交
    form.on('submit(formDemo)', function (data) {
        layer.msg(JSON.stringify(data.field));
        //发起数据请求,重载图表
        $.ajax({
            url:"http://localhost:8011/bike/get/all/aslist",
            method:"GET",
            data:{
                belong:data.field.city,
                bno:data.field.stopno,
            },
            success:function (result){
                // console.log(result)
                if (result&&result.code==1){
                    //修改表格渲染模板的data值
                    tableRender_Options.data=result.data;
                    //重载表格数据
                    table.reload("bike_table",tableRender_Options,true);
                }else{
                    layer.msg("未找到目标单车,请修改查询条件!")
                }
            },
            error:function (){
                layer.msg("未找到目标单车,请修改查询条件!")
            }
        })
        return false;
    });
});