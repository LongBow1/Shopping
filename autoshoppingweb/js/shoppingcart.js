//购物数量加减
$(function () {
    //124.70.220.72
    var localhostUrlPrefix = "http://localhost:8080/orderApp/";
    var remotehostUrlPrefix = "http://124.70.220.72:80/orderApp/";
    var memberId = $("#memberidinput").val().trim();
    var requestUrlPrefix = localhostUrlPrefix;
    var sessionid = $("#authinput").val();
    checkSession();
    var timeClock;
    $('#testsession').click(function () {
        checkSession();
    });

    $("#deletelocaladdress").click(function () {
        var checkedRecepts = $(".addintendorder:checkbox:checked");
        if (checkedRecepts.length == 0) {
            alert("收件人必选!")
            return;
        }
        var receipts = new Array();
        checkedRecepts.each(function (i, e) {
            receipts.push($(e).data("dataaddressid"));
        });
        var intentInfo = {
            "auth": $("#authinput").val().trim(),
            "addressIds": receipts.join(","),
            "memberId": $("#memberidinput").val().trim()
        }
        $.post(requestUrlPrefix + "deleteLocalAddress", intentInfo, function (res) {
            if (res != null) {
                refreshAddress();
            }
        });
    })

    $("#createintendorder").click(function () {
        if ($("#authinput").val().trim().length > 0) {
            var goodShotName = $("#intendgoodshotname").val();
            if (goodShotName == null || goodShotName.trim() == "必填,模糊匹配") {
                alert("商品货号/名称必填!")
                return;
            }
            var checkedRecepts = $(".addintendorder:checkbox:checked");
            if (checkedRecepts.length == 0) {
                alert("收件人必选!")
                return;
            }
            var goodSizes = new Array();
            var receipts = new Array();
            var goodOtherAttrs = new Array();
            checkedRecepts.each(function (i, e) {
                receipts.push($(e).data("dataaddressid"));
            });
            $(".intendgoodsize").each(function (i, e) {
                if ($(e).val() != null && $(e).val().trim().length > 0) {
                    goodSizes.push($(e).val().trim());
                }
            })
            $(".intendotherattr").each(function (i, e) {
                if ($(e).val() != null && $(e).val().trim().length > 0) {
                    goodOtherAttrs.push($(e).val().trim());
                }
            })
            var toBuySellType = $('#settingdiv input[type=radio]:checked');
            var intendOrder = {
                "goodShotName": goodShotName.trim(),
                "goodShotNameExtra": $("#intendgoodshotnameextra").val().trim(),
                "goodSizeList": goodSizes.join(","),
                "goodNum": $("#intendgoodnum").val().trim(),
                "receptNameList": receipts.join(","),
                "needPresale": $('#intendgoodpresale').is(':checked'),
                "toBuySellType": toBuySellType != undefined && toBuySellType != null ? toBuySellType.val() : 0,
                "quantifierNum": $("#quantifierNum").val().trim(),
                "goodOtherAttrList":goodOtherAttrs.join(","),
                "mergeOrder": $('#intendgoodmergeorder').is(':checked')
            }
            var intentInfo = {
                "auth": $("#authinput").val().trim(),
                "intendOrderInfo": JSON.stringify(intendOrder),
                "memberId": $("#memberidinput").val().trim()
            }
            $.post(requestUrlPrefix + "createIntendOrder", intentInfo, function (res) {
                if (res != null) {
                    refreshIntendOrderList();
                }
            }, "json");
        } else {
            $("#connectpromptinput").val("连接失败，亲刷新重试");
        }
    })

    $('#commitorder').click(function () {
        var intendOrders = $(".intendorderlist:checkbox");
        // if(checkedRecepts == null || checkedRecepts.length == 0){
        // 	alert("请选择下单")
        // }
        var intendLocalNo = new Array();
        $.each(intendOrders, function (i, e) {
            intendLocalNo.push($(e).data("id"));
        })
        var commitOrderInfo = {
            auth: $("#authinput").val().trim(),
            localNos: intendLocalNo.join(","),
            memberId: $("#memberidinput").val().trim()
        }
        $.post(requestUrlPrefix + "autoShopping", commitOrderInfo, function (result) {
            //$(this).html(result);
            $("#commitorderlistlabel").html("下单结果:" + result)
            refreshAlreadBuyOrderList();
            //alert(result);
        })
        //window.clearInterval(timeClock);
        //timeClock = self.setInterval(changeOrderCommittingStatus,5000);
    });

    $('#topayorderlist').click(function () {
        refreshToPayOrderInfo();
    });

    $('#canceltppayorderbtn').click(function () {
        if (confirm("取消全部待支付订单？")) {
            var cancelOrderInfo = {
                "auth": $("#authinput").val().trim(),
                "orderNos": "",
                "memberId": $("#memberidinput").val().trim()
            }
            $.post(requestUrlPrefix + "cancelOrder", cancelOrderInfo, function (result) {
                $("#topayorderlistlabel").html(result);
                refreshToPayOrderInfo();
            })
        }

    })

    $("#clearallinfo").click(function () {
        if (confirm("清空记录将停止下单!")) {
            var postInfo = {
                "auth": $("#authinput").val().trim(),
                "memberId": $("#memberidinput").val().trim()
            }
            $.post(requestUrlPrefix + "clearAllOrderInfo", postInfo, function (result) {
                if (!result) {
                    alert("操作失败");
                }
            })
        }

    })

    $('#tosendorderlist').click(function () {
        var postInfo = {
            "auth": $("#authinput").val().trim(),
            "memberId": $("#memberidinput").val().trim()
        }
        $.post(requestUrlPrefix + "getToSendOrderList", postInfo, function (result) {

            if (result != null) {
                var intendOrders = JSON.parse(result);
                $("#tosendorderlistdiv").empty();
                if (intendOrders != null && intendOrders.length > 0) {
                    $.each(intendOrders, function (i, e) {

                        $("#tosendorderlistdiv").append("<div style='height:60px;'><img src='" + e.imgUrl + "'  style='height:50px;float:left' /><input class='tosendorderlist' type='checkbox' id='" + e.orderNo + "' data-id='" + e.orderNo + "'>" + e.desc + "</input></div>");
                        $("#tosendorderlistlabel").html("待发货订单:" + (i + 1) + "个");
                    })
                } else {
                    $("#tosendorderlistlabel").html("待发货订单");
                }
            } else {
                $("#tosendorderlistlabel").html("待发货订单");
            }
        })
    });

    $('#stopcommitorder').click(function () {
        //window.clearInterval(timeClock);
        var postInfo = {
            "auth": $("#authinput").val().trim(),
            "memberId": $("#memberidinput").val().trim()
        }
        $.post(requestUrlPrefix + "stopCommitOrder", postInfo, function (result) {
            if (result) {
                refreshAlreadBuyOrderList();
            }
        })
        changeOrderCommittingStatus();
    })

    $('#deleteintendorder').click(function () {
        var intendOrders = $(".intendorderlist:checkbox:checked");
        if (intendOrders.length == 0) {
            alert("请选择需要删除的意向单!")
            return;
        }
        var deleteIntendLocalNo = new Array();
        $.each(intendOrders, function (i, e) {
            deleteIntendLocalNo.push($(e).data("id"));
        })
        var delteIntendOrderInfo = {
            auth: $("#authinput").val().trim(),
            localNos: deleteIntendLocalNo.join(","),
            memberId: $("#memberidinput").val().trim()
        }
        $.post(requestUrlPrefix + "deleteIntendOrderList", delteIntendOrderInfo, function (result) {
            if (result) {
                refreshIntendOrderList();
            }
        })
    })

    $('#addgoodsize').click(function () {
        $("<input class='intendgoodsize' type='text' style='color:cornflowerblue;width: 40px'>").insertBefore('#addgoodsize');
    })
    $('#removegoodsize').click(function () {
        if ($('.intendgoodsize').length > 1) {
            $('.intendgoodsize')[$('.intendgoodsize').length - 1].remove();
        }
    })

    $('#addotherattr').click(function () {
        $("<input class='intendotherattr' type='text' style='color:cornflowerblue;width: 40px'>").insertBefore('#addotherattr');
    })
    $('#removedotherattr').click(function () {
        if ($('.intendotherattr').length > 1) {
            $('.intendotherattr')[$('.intendotherattr').length - 1].remove();
        }
    })

    $('#enablelocaladdress').change(function () {
        refreshAddress();
    })

    function checkSession() {
        var username = $("#username").val().trim();
        var password = $("#password").val().trim();
        if (username == null || password == null || username.length == 0 || password.length == 0) {
            alert("请输入用户名和密码!");
            return false;
        }
        var postInfo = {
            "username": username,
            "password": password
        }
        $.post(requestUrlPrefix + "getAuth", postInfo, function (result) {
            if (result != null) {
                var authInfo = JSON.parse(result);
                if (authInfo != null && authInfo.data != null) {
                    $("#authinput").val("bearer " + authInfo.data.jwt.access_token);
                    $("#memberidinput").val(authInfo.data.user.id);

                    sessionid = $("#authinput").val();
                    if (sessionid == null || sessionid.trim().length == 0) {
                        alert("凭证不能为空!");
                        return false;
                    }
                    sessionid = $("#authinput").val().trim();
                    var postInfo = {
                        "auth": $("#authinput").val().trim(),
                        "memberId": $("#memberidinput").val().trim(),//getShopInfo()
                        "useLocalAddress": $("#enablelocaladdress").is(':checked')
                    }
                    $.post(requestUrlPrefix + "testConnect", postInfo, function (result) {
                        if (result == true) {
                            $("#connectpromptinput").val("成功");
                            $.post(requestUrlPrefix + "getAddress", postInfo, function (address) {
                                var addressIdArray = new Array();
                                if (address != null && address.length > 0) {
                                    $("#receptdiv").empty();
                                    var addressInfo = JSON.parse(address);
                                    $.each(addressInfo, function (i, e) {
                                        $("#receptdiv").append("<p > <input class='addintendorder' type='checkbox' id='" + e.addressId + "' data-dataaddressid='" + e.addressId + "' data-username='" + e.receiveName + "' >" + e.receiveName + "  " + e.province + e.city + e.area + e.address + "</input> </p>");
                                        addressIdArray.push(e.id);
                                    });
                                    $("#connectpromptinput").val("成功,共" + addressIdArray.length + "个人");
                                }
                            })
                            refreshIntendOrderList();
                        } else {
                            $("#connectpromptinput").val("连接失败，凭证失效,请重新抓包获取(PC端Fiddler,APP端抓包精灵)");
                        }
                    })
                    return true;
                }
            }
        });
    }

    function refreshIntendOrderList() {
        var postInfo = {
            "auth": $("#authinput").val().trim(),
            "memberId": $("#memberidinput").val().trim()
            //"shopInfo":getShopInfo()
        }
        $.post(requestUrlPrefix + "getIntendOrderList", postInfo, function (result) {
            //$(this).html(result);
            if (result != null) {
                var intendOrders = JSON.parse(result);
                $("#intendorderlistdiv").empty();
                if (intendOrders != null && intendOrders.length > 0) {
                    $.each(intendOrders, function (i, e) {
                        $("#intendorderlistdiv").append("<input class='intendorderlist' type='checkbox' id='" + e.localNo + "' data-id='" + e.localNo + "'>" + e.desc + "</input><br/>");
                        $("#intendorderlistlabel").html("意向单:" + (i + 1) + "个");
                    })
                } else {
                    $("#intendorderlistlabel").html("意向单");
                }
            } else {
                $("#intendorderlistlabel").html("意向单");
            }
            autoCommitOrder();
        })
    }

    function refreshAlreadBuyOrderList() {
        var sessionid = $("#authinput").val().trim();
        var postInfo = {
            "auth": $("#authinput").val().trim(),
            "memberId": $("#memberidinput").val().trim()
        }
        var readyOrders = new Array();
        var commitOrders = new Array();
        $.ajaxSettings.async = false;
        $.post(requestUrlPrefix + "getReadyToBuyOrderList", postInfo, function (result) {
            if (result != null) {
                readyOrders = JSON.parse(result);
            }
        })

        $.post(requestUrlPrefix + "getAlreadyBuyOrderList", postInfo, function (result) {
            if (result != null) {
                commitOrders = JSON.parse(result);
            }
        })
        $.ajaxSettings.async = true;
        $("#commitorderlistdiv").empty();
        var labelName = "下单结果:";
        var labelName1 = "";
        var labelName2 = "";
        if (readyOrders != null || commitOrders != null) {
            if (readyOrders != null && readyOrders.length > 0) {
                labelName1 = "下单中：" + readyOrders.length + "个;";
                $.each(readyOrders, function (i, e) {
                    $("#commitorderlistdiv").append("<input class='readytobuyorderlist' type='checkbox' id='" + e.localNo + "' data-id='" + e.localNo + "'><a  style='color:red'>" + e.desc + "  下单中..." + "</a></input><br/>");
                })
            }
            var realOrderNum = 0;
            if (commitOrders != null && commitOrders.length > 0) {
                $.each(commitOrders, function (i, e) {
                    var orderNos = "";
                    if (e != null && e.commitOrderInfoList != null) {
                        $.each(e.commitOrderInfoList, function (i2, e2) {
                            orderNos = orderNos + e2.data.orderId + " ";
                            realOrderNum++;
                        })
                        $("#commitorderlistdiv").append("<input class='alreadybuyorderlist' type='checkbox' id='" + e.localNo + "' data-id='" + e.localNo + "'>" + e.desc + ",订单号:" + orderNos + "</input><br/>");
                        labelName2 = " 已完成下单:" + (i + 1) + "个,实际订单" + realOrderNum + "个";
                    }
                })
            }
        }
        $("#commitorderlistlabel").html(labelName + labelName1 + labelName2+" <input class='mergereadytobuyorderlist' type='checkbox' id='mergereadytobuyorderlistinput'>合并选择订单</inpur>");
    }

    function refreshToPayOrderInfo() {
        var postInfo = {
            "auth": $("#authinput").val().trim(),
            "memberId": $("#memberidinput").val().trim()
        }
        $.post(requestUrlPrefix + "getToPayOrderList", postInfo, function (result) {
            if (result != null) {
                var intendOrders = JSON.parse(result);
                $("#topayorderlistdiv").empty();
                if (intendOrders != null && intendOrders.length > 0) {
                    $.each(intendOrders, function (i, e) {
                        $("#topayorderlistdiv").append("<div style='height:60px;'><img src='" + e.imgUrl + "'  style='height:50px;float:left' /><input class='topayorderlist' type='checkbox' id='" + e.orderNo + "' data-id='" + e.orderNo + "'>" + e.desc + "</input></div>");
                        $("#topayorderlistlabel").html("待支付订单:" + (i + 1) + "个");
                    })
                } else {
                    $("#topayorderlistlabel").html("待支付订单");
                }
            } else {
                $("#topayorderlistlabel").html("待支付订单");
            }
        })
    }

    function changeOrderCommittingStatus() {
        var sessionid = $("#authinput").val().trim();
        var postInfo = {
            "auth": $("#authinput").val().trim(),
            "memberId": $("#memberidinput").val().trim()
        }
        var readyOrders = new Array();
        $.post(requestUrlPrefix + "getReadyToBuyOrderList", postInfo, function (result) {
            if (result != null) {
                readyOrders = JSON.parse(result);
            }
            if (readyOrders != null && readyOrders.length > 0) {
                $("#iscommittingorderlabel").show();
            } else {
                $("#iscommittingorderlabel").hide();
            }
        })
    }

    function autoCommitOrder() {
        var intendOrders = $(".intendorderlist:checkbox");
        var intendLocalNo = new Array();
        $.each(intendOrders, function (i, e) {
            intendLocalNo.push($(e).data("id"));
        })
        var commitOrderInfo = {
            auth: $("#authinput").val().trim(),
            localNos: intendLocalNo.join(","),
            memberId: $("#memberidinput").val().trim()
        }
        $.post(requestUrlPrefix + "autoShopping", commitOrderInfo, function (result) {
            //$(this).html(result);
            $("#commitorderlistlabel").html("下单结果:" + result)
            refreshAlreadBuyOrderList();
            //alert(result);
        })
    }

    function refreshAddress() {
        var postInfo = {
            "auth": $("#authinput").val().trim(),
            "memberId": $("#memberidinput").val().trim(),//getShopInfo()
            "useLocalAddress": $("#enablelocaladdress").is(':checked')
        }
        $.post(requestUrlPrefix + "testConnect", postInfo, function (result) {
            if (result == true) {
                $("#connectpromptinput").val("成功");
                $.post(requestUrlPrefix + "getAddress", postInfo, function (address) {
                    var addressIdArray = new Array();
                    if (address != null && address.length > 0) {
                        $("#receptdiv").empty();
                        var addressInfo = JSON.parse(address);
                        $.each(addressInfo, function (i, e) {
                            $("#receptdiv").append("<p > <input class='addintendorder' type='checkbox' id='" + e.addressId + "' data-dataaddressid='" + e.addressId + "' data-username='" + e.receiveName + "' >" + e.receiveName + "  " + e.province + e.city + e.area + e.address + "</input> </p>");
                            addressIdArray.push(e.id);
                        });
                        $("#connectpromptinput").val("成功,共" + addressIdArray.length + "个人");
                    }
                })
            } else {
                $("#connectpromptinput").val("连接失败，凭证失效,请重新抓包获取(PC端Fiddler,APP端抓包精灵)");
            }
        })
    }
})

$(function () {

    $("#intendreceiptname").on('input propertychange', function () {
        var inputVal = $(this).val();
        var checkedRecepts = $("#receptdiv .addintendorder:checkbox");
        if (checkedRecepts != null) {
            if (inputVal == null || inputVal.trim() == '' || inputVal.trim() == '搜索收件人') {
                checkedRecepts.each(function (i, e) {
                    $(e).parent().show();
                })
            } else {
                checkedRecepts.each(function (i, e) {
                    if ($(e).data('username').indexOf(inputVal.trim()) >= 0) {
                        $(e).parent().show();
                    } else {
                        $(e).parent().hide();
                    }
                })
            }
        }
    });
})

//tab切换
$(function () {
    window.onload = function () {
        var $li = $('#tab li');
        var $ul = $('#content ul');
        $li.click(function () {
            var $this = $(this);
            var $t = $this.index();
            $li.removeClass();
            $this.addClass('current');
            $ul.css('display', 'none');
            $ul.eq($t).css('display', 'block');
        })
    }
});
