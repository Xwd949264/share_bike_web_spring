layui.use(['form','table','layer'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        $=layui.jquery;
    //表格数据渲染
    table.render({
        elem: '#test'
        // ,url:'https://www.layuiweb.com/test/table/demo1.json'/*tpa=https://www.layuiweb.com/test/table/demo1.json*/
        ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
        ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
            title: '提示'
            ,layEvent: 'LAYTABLE_TIPS'
            ,icon: 'layui-icon-tips'
        }]
        ,title: '用户数据表'
        //标题栏
        ,cols: [[
            {field:'id', title:'ID', width:80, fixed: 'left', unresize: true, sort: true}
            ,{field:'stopid', title:'编号', width:120, edit: 'text'}
            ,{field:'stopname', title:'停车点名称', width:150, edit: 'text', templet: function(res){
                    return res.stopname
                }}
            ,{field:'startdate', title:'投放日期', width:120, edit: 'text', sort: true}
            ,{field:'totalCapacity', title:'总体容量', width:120}
            ,{field:'restCapacity', title:'剩余容量',width: 120}
            ,{field:'belongCity', title:'所在政区', width:120}
            ,{fixed: 'right', title:'操作', toolbar: '#barOperations', width:150}
        ]]
        ,page: true
        ,data:[
            {
                id:1,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:2,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:3,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:4,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:5,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:6,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:7,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:8,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:9,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:10,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:1,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:2,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:3,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:4,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            },
            {
                id:5,
                stopid:"00001",
                stopname:"天河市1号停车点",
                startdate:"2021-01-26",
                totalCapacity:50,
                restCapacity:25,
                belongCity:"南京市"
            }
        ]
    });

    //监听行工具事件
    table.on('tool(test)', function(obj){
        var data = obj.data;
        console.log(obj)
        if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){
                obj.del();
                layer.msg("删除")
                layer.close(index);
            });
        } else if(obj.event === 'locate'){
            layer.msg("正在定位到:"+obj.data.stopname)
        }
    });

    //监听提交
    form.on('submit(formDemo)', function (data) {
        layer.msg(JSON.stringify(data.field));
        return false;
    });
});