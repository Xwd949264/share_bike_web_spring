/**
 * 定义新的layui模块：layui.define([mods], callback)
 * @param mods 表示示新增自定义layui模块名称
 * @param callback 用于输出该模块的接口
 */

layui.define(['layer',],function (exports) {
    //初始化layui模块
    const layer = layui.layer;

    layer.msg("单车分布")

    //导出自定义模块
    exports('layolmap',{
        //地图初始化模块
        // OlMap,
    })
})
