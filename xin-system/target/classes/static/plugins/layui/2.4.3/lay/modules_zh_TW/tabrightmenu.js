layui.define(['element'], function (exports) {
    var element = layui.element;
    var $ = layui.jquery;
    var currentActiveTabID = undefined;
    var currentActiveTabTitle = undefined;
    var MOD_NAME = 'tabrightmenu';
    var RIGHTMENUMOD = function () {
        this.v = '1.0.1';
        this.author = 'TangHanF';
        this.email = 'guofu_gh@163.com';
        this.filter = '';//Tab選項卡的事件過濾

    };
    String.prototype.format = function () {
        if (arguments.length == 0) return this;
        var param = arguments[0];
        var s = this;
        if (typeof (param) == 'object') {
            for (var key in param) s = s.replace(new RegExp("\\{" + key + "\\}", "g"), param[key]);
            return s;
        } else {
            for (var i = 0; i < arguments.length; i++) s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
            return s;
        }
    }

    function createStyle(ulClassName, width) {
        var style = '.{name} {position: absolute;width: {width_}px;z-index: 9999;display: none;background-color: #fff;padding: 2px;color: #333;border: 1px solid #eee;border-radius: 2px;cursor: pointer;}.{name} li {text-align: left;display: block;height: 26px;line-height: 26px;padding: 4px;}.{name} li:hover {background-color: #666;color: #fff;}'
            .format({name: ulClassName, width_: width});
        return style;
    }

    /**
     * 初始化
     */
    RIGHTMENUMOD.prototype.render = function (opt) {
        if (!opt.container) {
            console.error("[ERROR]使用rightmenu組件需要制定'container'屬性！");
            return;
        }
        if (!opt.filter) {
            console.error("[ERROR]使用rightmenu組件需要制定'filter'屬性！");
            return;
        }
        this.filter = opt.filter;
        this.isClickMidCloseTab = opt.isClickMidCloseTab || false;
        this.width = opt.width ? opt.width : 110;// 右鍵彈出框的寬度，一般100~110即可

        // pinTabTitles和pintabIDs作用一樣，只是便於開發使用考慮加入標題和ID形式進行兩種方式的過濾
        this.pinTabTitles = opt.pinTabTitles;//固定標籤的標題集合
        this.pintabIDs = opt.pintabIDs;//固定標籤的ID集合

        var defaultNavArr = [
            {eventName: 'refresh', title: '刷新當前頁'},
            {eventName: 'closeallbutpin', title: '關閉所有但固定'},
            {eventName: 'closethis', title: '關閉標籤'},
            {eventName: 'closeothersbutpin', title: '關閉其他但固定'},
            {eventName: 'closeleftbutpin', title: '關閉左側標籤但固定'},
            {eventName: 'closerightbutpin', title: '關閉右側標籤但固定'}
        ];
        opt = opt || {};
        opt.navArr = opt.navArr || defaultNavArr;
        CreateRightMenu(opt);
        registClickMidCloseTab(this.isClickMidCloseTab,opt);
    };


    /**
     * 註冊點擊滑鼠中間關閉選項卡事件
     * @param isClose
     * @param rightMenuConfig
     */
    function registClickMidCloseTab(isClose,rightMenuConfig) {
        if (!isClose) {
            return;
        }

        $("#" + rightmenuObj.filter).on('mousedown', 'li', function (e) {
            currentActiveTabID = $(e.target).attr('lay-id'); // 獲取當前啟動的選項卡ID,當前ID改為右鍵菜單彈出時就獲取
            currentActiveTabTitle = $.trim($(e.target).text()); //獲取當前啟動選項卡的標題
            if (rightMenuConfig.pinTabTitles && rightMenuConfig.pinTabTitles.indexOf(currentActiveTabTitle) != -1 || currentActiveTabTitle == undefined) { //特殊ID，彈出默認的右鍵菜單
                return true;
            }
            if (rightMenuConfig.pintabIDs && rightMenuConfig.pintabIDs.indexOf(currentActiveTabID) != -1 || currentActiveTabID == undefined) { //特殊ID，彈出默認的右鍵菜單
                return true;
            }
            if (e.which != 2) {
                return true;
            }
            //滑鼠中鍵關閉指定標籤頁
            element.tabDelete(rightMenuConfig.filter, currentActiveTabID);
            //隱藏菜單(如果有)
            $("." + rightMenuConfig.filter).hide();
            return false;
        });
    }


    /**
     * 創建右鍵菜單專案
     * @param rightMenuConfig
     * @constructor
     */
    function CreateRightMenu(rightMenuConfig) {
        // 使用"filter"屬性作為css樣式名稱
        $("<style></style>").text(createStyle(rightMenuConfig.filter, rightMenuConfig.width)).appendTo($("head"));
        var li = '';
        $.each(rightMenuConfig.navArr, function (index, conf) {
            if (conf.eventName === 'line')
                li += '<hr/>';
            else
                li += '<li data-type="{eventName}"><i class="layui-icon {icon}"></i>{title}</li>'
                    .format({eventName: conf.eventName, icon: conf.icon ? conf.icon : "", title: conf.title});
        })
        var tmpHtml = '<ul class="{className}">{liStr} </ul>'.format({liStr: li, className: rightMenuConfig.filter})
        $(rightMenuConfig.container).after(tmpHtml);
        _CustomRightClick(rightMenuConfig);
    }


    /**
     * 綁定右鍵菜單
     * @constructor
     */
    function _CustomRightClick(rightMenuConfig) {
        //遮罩Tab右鍵
        //$("#" + rightmenuObj.filter + ' li').on('contextmenu', function (e) {
        //    return false;
        //})
        $('#' + rightmenuObj.filter + ',' + rightmenuObj.filter + ' li').click(function () {
            $('.' + rightMenuConfig.filter).hide();
        });
        //事件委託，處理動態添加的li
        $("#" + rightmenuObj.filter).on('contextmenu', 'li', function (e) {
            currentActiveTabID = $(e.target).attr('lay-id');// 獲取當前啟動的選項卡ID,當前ID改為右鍵菜單彈出時就獲取
            currentActiveTabTitle = $.trim($(e.target).text());//獲取當前啟動選項卡的標題

            if (rightMenuConfig.pinTabTitles && rightMenuConfig.pinTabTitles.indexOf(currentActiveTabTitle) != -1 || currentActiveTabTitle == undefined) {   //特殊ID，彈出默認的右鍵菜單
                return true;
            }
            if (rightMenuConfig.pintabIDs && rightMenuConfig.pintabIDs.indexOf(currentActiveTabID) != -1 || currentActiveTabID == undefined) {   //特殊ID，彈出默認的右鍵菜單
                return true;
            }
            var popupmenu = $("." + rightMenuConfig.filter);
            var leftValue = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
            var topValue = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
            popupmenu.css({left: leftValue, top: topValue}).show();
            return false;
        });

        // 點擊空白處隱藏彈出菜單
        $(document).click(function (event) {
            event.stopPropagation();
            $("." + rightMenuConfig.filter).hide();
        });

        /**
         * 註冊tab右鍵菜單點擊事件
         */
        $('.' + rightMenuConfig.filter + ' li').click(function (e) {
            var tabs = $("#" + rightMenuConfig.filter + " li");
            var allTabIDArrButPin = [];//所有非固定選項卡集合
            var allTabIDArr = [];// 所有選項卡集合
            var idIndexButPin = 0;
            var idIndex = 0;
            $.each(tabs, function (i) {
                var id = $(this).attr("lay-id");
                var title = $(this).text();
                if (rightMenuConfig.pinTabTitles == undefined || rightMenuConfig.pintabIDs == undefined) {
                    allTabIDArrButPin[idIndexButPin] = id;
                    idIndexButPin++;
                } else if ((rightMenuConfig.pinTabTitles && rightMenuConfig.pinTabTitles.indexOf(title) == -1) &&
                    ((rightMenuConfig.pintabIDs && rightMenuConfig.pintabIDs.indexOf(id) == -1) || id == undefined))  //去除特殊ID
                {
                    allTabIDArrButPin[idIndexButPin] = id;
                    idIndexButPin++;
                }
                allTabIDArr[idIndex] = id;
                idIndex++;
            })

            // 事件處理
            switch ($(this).attr("data-type")) {
                case "closethis"://關閉當前，如果開始了tab可關閉，實際意義不大
                    tabDelete(currentActiveTabID, rightMenuConfig.filter);
                    break;
                case "closeallbutpin"://關閉所有但固定
                    tabDeleteAll(allTabIDArrButPin, rightMenuConfig.filter);
                    break;
                case "closeothersbutpin"://關閉非當前但固定
                    $.each(allTabIDArrButPin, function (i) {
                        var tmpTabID = allTabIDArrButPin[i];
                        if (currentActiveTabID != tmpTabID)
                            tabDelete(tmpTabID, rightMenuConfig.filter);
                    })
                    break;
                case "closeleftbutpin"://關閉左側全部但固定
                    var indexCloseleft = allTabIDArrButPin.indexOf(currentActiveTabID);
                    console.log(allTabIDArrButPin.slice(0, indexCloseleft));
                    tabDeleteAll(allTabIDArrButPin.slice(0, indexCloseleft), rightMenuConfig.filter);
                    break;
                case "closerightbutpin"://關閉右側全部但固定
                    var indexCloseright = allTabIDArrButPin.indexOf(currentActiveTabID);
                    tabDeleteAll(allTabIDArrButPin.slice(indexCloseright + 1), rightMenuConfig.filter);
                    break;

                case "closeall"://關閉所有
                    tabDeleteAll(allTabIDArr, rightMenuConfig.filter);
                    break;
                case "closeothers"://關閉非當前
                    $.each(allTabIDArr, function (i) {
                        var tmpTabID = allTabIDArr[i];
                        if (currentActiveTabID != tmpTabID)
                            tabDelete(tmpTabID, rightMenuConfig.filter);
                    })
                    break;
                case "closeleft"://關閉左側全部
                    var indexCloseleft = allTabIDArr.indexOf(currentActiveTabID);
                    tabDeleteAll(allTabIDArr.slice(0, indexCloseleft), rightMenuConfig.filter);
                    break;
                case "closeright"://關閉右側全部
                    var indexCloseright = allTabIDArr.indexOf(currentActiveTabID);
                    tabDeleteAll(allTabIDArr.slice(indexCloseright + 1), rightMenuConfig.filter);
                    break;
                case "refresh":
                    refreshiFrame();
                    break;
                case "openNewWindow":
                    openNewWindow();
                    break;

                default:
                    var currentTitle = $("#" + rightMenuConfig.filter + ">li[class='layui-this']").text();
                    rightMenuConfig.registMethod[$(this).attr("data-type")](currentActiveTabID, currentTitle, rightMenuConfig.container, $(this)[0]);
            }
            $('.rightmenu').hide();
        })
    }

    var tabDelete = function (id, currentNav) {
        element.tabDelete(currentNav, id);//刪除
    }
    var tabDeleteAll = function (ids, currentNav) {
        $.each(ids, function (i, item) {
            element.tabDelete(currentNav, item);
        })
    }
    /**
     * 重新加載iFrame，實現更新
     * @returns {boolean}
     */
    var refreshiFrame = function () {
        var $curFrame = $('iframe:visible');
        $curFrame.attr("src", $curFrame.attr("src"));
        return false;
    }

    var openNewWindow = function () {
        var $curFrame = $('iframe:visible');
        window.open($curFrame.attr("src"));

    }

    var rightmenuObj = new RIGHTMENUMOD();
    exports(MOD_NAME, rightmenuObj);
})