//加载一级评论
$(function () {
    //日期转换
    function getLocalTime(date) {
        var oDate = new Date(parseInt(date)),
            oYear = oDate.getFullYear(),
            oMonth = oDate.getMonth()+1,
            oDay = oDate.getDate(),
            oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay);//最后拼接时间
        return oTime;
    }
    //补0操作
    function getzf(num){
        if(parseInt(num) < 10){
            num = '0'+num;
        }
        return num;
    }

    <!--获取url参数-->
    function getQueryString(name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    }
    
    $.get("/redis/LikeGut.do",{"likedArtId":getQueryString("artId")},function(res){
        $(".jieda-zan em").text(res)
    },"text")

    // 请求评论列表数据
    $.post("/comm/searchCommByArtId.do",{"artId":getQueryString("artId")},function (res) {
        // alert(JSON.stringify(res.user))

        //可以加上redis

        //给文章基本信息赋值 (res.article.artContent)
        $("#markdown-textarea").val(res.article.artContent);
        // markendown解析
        var testView = editormd.markdownToHTML("markdown-view", {
            htmlDecode      : "style,script,iframe",
            emoji           : true,
            taskList        : true,
            tex             : true
        });
        $(".page-header").attr({"date_id":res.user.userId})
        $(".page-header h1").text(res.article.artTitle);
        $(".detail-hits").attr({"id":res.article.artId});
        $(".fly-avatar").attr({"href":res.user.userBlog});
        $(".fly-avatar img").attr({"src": res.user.userImg ,"alt": res.user.userName});
        $(".fly-detail-user cite").text(res.user.userName)
        $(".fly-detail-user a").attr({"href":res.user.userBlog});
        $(".fly-detail-user span").text(getLocalTime(res.article.artCreTime));
        $("#commNum").text(res.commInfos.length);
        // 加载评论列表
        var str = '';
        for (let i = 0; i < res.commInfos.length; i++) {
            str += " <div class=\"media\">\n" +
                "                    <div class=\"media-left media-middle\">\n" +
                "                        <a href=\"#\">\n" +
                "                            <img class=\"media-object\" src="+res.commInfos[i].user.userImg+" alt="+res.commInfos[i].user.userName+" onerror=src='/static/imgs/defult_img.jpg'>\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"media-body\">\n" +
                "                        <li>\n" +
                "                            <a class=\"list-post-title\">"+res.commInfos[i].user.userName+"";
            if(res.user.userId == res.commInfos[i].comment.comUserId){
                str+="&nbsp;(楼主)"
            }
             str+="</a>\n" +
            "\n" +
            "                            <div class=\"fly-list-info\">\n" +
            "                                <span>"+getLocalTime(res.commInfos[i].comment.comTime)+"</span>\n" +
            "                            </div>\n" +
            "                        </li>\n" +
            "                    </div>\n" +
            "                    <div class=\"detail-body jieda-body photos\">"+res.commInfos[i].comment.comContent+"</div>\n" +
            "                    <div class=\"jieda-reply pull-right\">\n" +
            "                        <span data-id='"+res.commInfos[i].comment.comId+"' class=\"changColor collapsed \" onclick='toggerReply(this);' type=\"reply \">\n" +
            "                            <span class=\"glyphicon glyphicon-comment\" aria-hidden=\"true\"></span>\n" +
            "                              <span>回复</span>\n" +
            "                        </span>\n" +
            "    </div>\n" +
            "                </div>"+
                 "                        <div class=\"collapse seReply\" id=\"collapse_"+res.commInfos[i].comment.comId+"\" aria-expanded=\"true\">\n" +

                 "                    </div> "
        }

        $("#commList").append(str);
    },"json")

})

//点赞
function likeput() {
    var likedArtId = $(".detail-hits").attr("id")
    $.get("/redis/LikePut.do",{"likedArtId":likedArtId},function(res){
      console.log(res);
      if (res.code != 200) {
          layui.use('layer', function(){ //独立版的layer无需执行这一句
              var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

              //触发事件
              var active = {
                  setTop: function(){
                      var that = this;
                  }
                  ,confirmTrans: function(){
                      //配置一个透明的询问框
                      layer.msg(res.msg, {
                          time: 2000, //20s后自动关闭
                      });
                  }
              };
              active.confirmTrans();
          });
          return
      }
      $.get("/redis/LikeGut.do",{"likedArtId":getQueryString("artId")},function(res){
          $(".jieda-zan em").text(res)
        },"text")
    },"json")
}

//日期转换
function getLocalTime(date) {
    var oDate = new Date(parseInt(date)),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth()+1,
        oDay = oDate.getDate(),
        oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay);//最后拼接时间
    return oTime;
}
//补0操作
function getzf(num){
    if(parseInt(num) < 10){
        num = '0'+num;
    }
    return num;
}

<!--获取url参数-->
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}
//二级评论切换
function toggerReply(e) {
    var data_id = "#collapse_"+$(e).attr("data-id");
    // alert(data_id)
    if($(e).attr("flag") != "1"){
        $(e).removeClass("collapsed")
        $(data_id).addClass("in");
        $(e).attr({"flag":"1"});
        $.post("/comm/searchSeCommByArtId.do",{"commId":$(e).attr("data-id")},function (res){
            var str = "";
            for (let i = 0; i < res.length; i++) {
               str += "<div class=\"media\">\n" +
                   "                    <div class=\"media-left media-middle\">\n" +
                   "                        <a href=\"#\">\n" +
                   "<img class=\"media-object\" src="+res[i].user.userImg+" alt="+res[i].user.userName+" onerror=src='/static/imgs/defult_img.jpg'>\n"+
                   "                        </a>\n" +
                   "                    </div>\n" +
                   "                    <div class=\"media-body\">\n" +
                   "                        <li>\n" +
                   "                            <a class=\"list-post-title\">"+res[i].user.userName+"</a>\n" +
                   "\n" +
                   "                            <div class=\"fly-list-info\">\n" +
                   "                                <span>"+getLocalTime(res[i].commentMulti.comMultiTime)+"</span>\n" +
                   "                            </div>\n" +
                   "                        </li>\n" +
                   "                    </div>\n" +
                   "                    <div class=\"detail-body jieda-body photos\">"+res[i].commentMulti.comMultiContent+"</div>\n" +
                   "                    </div>\n" +
                   "                </div>"
            }
            $(data_id).children().remove();
            $(data_id).append(str);

            $(data_id).append(
                "<input type=\"text\" name='comMultiContent' class=\"form-control\" placeholder=\"回复一句\">"+
                "<input type='hidden' name='comId' value='"+$(e).attr("data-id")+"'>"+
                "<div class='myDiv'></div> " +
                "<button type=\"button\" class=\"btn btn-success Mypull-right\" onclick='addSeComm(this);'>提交</button>"
                );

        },"json")

    }
    else {
        $(e).addClass("collapsed")
        $(data_id).removeClass("in");
        $(e).attr({"flag":"0"})
    }
}
//添加二级评论
function addSeComm(e){
    if($("#Reply button").text() == ""){
        layui.use('layer', function(){ //独立版的layer无需执行这一句
            var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

            //触发事件
            var active = {
                setTop: function(){
                    var that = this;
                }
                ,confirmTrans: function(e){
                    //配置一个透明的询问框
                    layer.msg(e, {
                        time: 2000, //2s后自动关闭
                    });


                }
            };


            active.confirmTrans("请先登录");



        });
        return
    }
    var comMultiContent = $(e).parent().find("input")[0].value;
    if(comMultiContent == ''){
        layui.use('layer', function(){ //独立版的layer无需执行这一句
            var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

            //触发事件
            var active = {
                setTop: function(){
                    var that = this;
                }
                ,confirmTrans: function(e){
                    //配置一个透明的询问框
                    layer.msg(e, {
                        time: 2000, //20s后自动关闭
                    });


                }
            };


            active.confirmTrans("回复不能为空");



        });
        return;
    }
    var comId =$(e).parent().find("input")[1].value;

    if(!comId) return;
    //添加二级回复
    $.post("/comm/addSeComm.do",{"comMultiContent":comMultiContent,"comId":comId},function (res){
        if(res.valid == "success"){
            $(e).parent().find("input")[0].value="";
           var str = "<div class=\"media\">\n" +
               "                    <div class=\"media-left media-middle\">\n" +
               "                        <a href=\"#\">\n" +
               "<img class=\"media-object\" src="+res.userImg+" alt="+res.userName+" onerror=src='/static/imgs/defult_img.jpg'>\n"+
               "                        </a>\n" +
               "                    </div>\n" +
               "                    <div class=\"media-body\">\n" +
               "                        <li>\n" +
               "                            <a class=\"list-post-title\">"+res.userName+"</a>\n" +
               "\n" +
               "                            <div class=\"fly-list-info\">\n" +
               "                                <span>"+getLocalTime((new Date()).getTime())+"</span>\n" +
               "                            </div>\n" +
               "                        </li>\n" +
               "                    </div>\n" +
               "                    <div class=\"detail-body jieda-body photos\">"+comMultiContent+"</div>\n" +
               "                    </div>\n" +
               "                </div>"

            $(e).parent().prepend(str);
        }
    },"json");

}
//添加一级评论
function addComm(e){
    var comMultiContent = $(e).parent().find("textarea")[0].value
    if(comMultiContent == ''){
        layui.use('layer', function(){ //独立版的layer无需执行这一句
            var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

            //触发事件
            var active = {
                setTop: function(){
                    var that = this;
                }
                ,confirmTrans: function(e){
                    //配置一个透明的询问框
                    layer.msg(e, {
                        time: 2000, //20s后自动关闭
                    });


                }
            };


            active.confirmTrans("回复不能为空");



        });
        return;
    }

    var artId =getQueryString("artId");
    if(!artId) return;

    $.post("/comm/addComm.do",{"comMultiContent":comMultiContent,"artId":artId},function (res){
        if(res.valid == "success"){
            $(e).parent().find("textarea")[0].value="";
            var str = " <div class=\"media\">\n" +
                "                    <div class=\"media-left media-middle\">\n" +
                "                        <a href=\"#\">\n" +
                "                            <img class=\"media-object\" src="+res.userImg+" alt="+res.userName+" onerror=src='/static/imgs/defult_img.jpg'>\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"media-body\">\n" +
                "                        <li>\n" +
                "                            <a class=\"list-post-title\">"+res.userName+"";
            if($(".page-header").attr("date_id") == res.userId){
                str+="&nbsp;(楼主)"
            }
            str+="</a>\n" +
                "\n" +
                "                            <div class=\"fly-list-info\">\n" +
                "                                <span>"+getLocalTime((new Date()).getTime())+"</span>\n" +
                "                            </div>\n" +
                "                        </li>\n" +
                "                    </div>\n" +
                "                    <div class=\"detail-body jieda-body photos\">"+comMultiContent+"</div>\n" +
                "                    <div class=\"jieda-reply pull-right\">\n" +
                "                        <span class=\"jieda-zan changColor\" type=\"zan\">\n" +
                "                             <span class=\"glyphicon glyphicon-thumbs-up \" aria-hidden=\"true\"></span>\n" +
                "                             <em>5</em>\n" +
                "                        </span>\n" +
                "                        <span data-id='"+res.userFans+"' class=\"changColor collapsed \" onclick='toggerReply(this);' type=\"reply \">\n" +
                "                            <span class=\"glyphicon glyphicon-comment\" aria-hidden=\"true\"></span>\n" +
                "                              <span>回复</span>\n" +
                "                        </span>\n" +
                "    </div>\n" +
                "                </div>"+
                "                        <div class=\"collapse seReply\" id=\"collapse_"+res.userFans+"\" aria-expanded=\"true\">\n" +

                "                    </div> "

            $("#commList").prepend(str);
        }
    },"json");


}


//验证
$(function () {
    $('#formSeReply').bootstrapValidator({
        message: 'This value is not valid',
        excluded: [':disabled', ':hidden', ':not(:visible)'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            replyComm: {
                message: '回复验证失败!',
                validators: {
                    notEmpty: {
                        message: '回复不能为空'
                    },
                    stringLength: {  //长度限制
                        max: 25,
                        message: '标题长度不能超过25个字符'
                    },
                    regexp: { //正则表达式
                        regexp: /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
                        message: '标题只含有汉字、数字、字母、下划线,并且不能以下划线开头和结尾'
                    }
                }
            },
        }
    });

    $("#btn-test").click(function () {//非submit按钮点击后进行验证，如果是submit则无需此句直接验证
        $("#formSeReply").bootstrapValidator('validate');//提交验证
        if ($("#formSeReply").data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
            alert("yes")
        }
    });
})