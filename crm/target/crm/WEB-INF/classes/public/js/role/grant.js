var zTreeObj;
$(function () {
    loadModuleInfo();
});
function loadModuleInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllModules",
        data:{
            roleId:$("#roleId").val()
        },
        dataType:"json",
        success:function (data) {
            // zTree 的参数配置，深⼊使⽤请参考 API ⽂档（setting 配置详解）
            var setting = {
                data: {
                    simpleData: {
                        enable: true
            }
        },
            view:{
                showLine: false
                // showIcon: false
            },
            check: {
                enable: true,
                    chkboxType: { "Y": "ps", "N": "ps" }
            },
                callback: {
                    onCheck: zTreeOnCheck
                }
        };
            var zNodes =data;
            zTreeObj=$.fn.zTree.init($("#test1"), setting, zNodes);
        }
    })
}
function zTreeOnCheck(event, treeId, treeNode) {
    var nodes= zTreeObj.getCheckedNodes(true);
    var roleId=$("#roleId").val();
    if(nodes.length>0){
        var mIds="mIds=";
        for(var i=0;i<nodes.length;i++){
            if(i<nodes.length-1){
                mIds=mIds+nodes[i].id+"&mIds=";
            }else{
                mIds=mIds+nodes[i].id;
            }
        }
    }

    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mIds+"&roleId="+roleId,
        dataType:"json",
        success:function (data) {
            console.log(data);
        }
    })
}