var statesArr = new Array("Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","District of Columbia","Florida","Georgia","Hawaii","Idaho",
                        "Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri",
                        "Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island",
                        "South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming");

var congressApp = angular.module('congressApp', ['angularUtils.directives.dirPagination', 'ngAnimate', 'ngSanitize', 'ui.bootstrap']);

congressApp.controller('congressController', function($scope, $http, $filter) {

    var curActiveCont = "#leg-cont";

    $scope.changeToLegCont = function() {
        if(curActiveCont != "#leg-cont") {
            $(curActiveCont).css("display", "none");
            $("#leg-cont").css("display", "inline");
            curActiveCont = "#leg-cont";
        }
    }

    $scope.changeToBillsCont = function() {
        if(curActiveCont != "#bill-cont") {
            if(billCurActiveNav == "")
                initBillCont();
            $(curActiveCont).css("display", "none");
            $("#bills-cont").css("display", "inline");
            curActiveCont = "#bills-cont";
        }
    }

    $scope.changeToComCont = function() {
        if(curActiveCont != "#com-cont") {
            if(comCurActiveNav == "")
                initComCont();
            $(curActiveCont).css("display", "none");
            $("#com-cont").css("display", "inline");
            curActiveCont = "#com-cont";
        }
    }

    $scope.changeToFavCont = function() {
        if(curActiveCont != "#fav-cont") {
            if(favCurActiveNav == "")
                initFavCont();
            refeshFavCont();
            $(curActiveCont).css("display", "none");
            $("#fav-cont").css("display", "inline");
            curActiveCont = "#fav-cont";
        }
    }

    //legislators variable
    var allLegResults;
    var legCurActiveNav = "#leg-state";
    var stateFilter = "";
    var legSenateText = "";
    var legHouseText = "";
    var legFavID = [];
    var legFavCont = [];
    
    //legislators functions
    initLegCont();
    function initLegCont() {
        $http({
            method: "GET",
            url: "hw8.php?type=legislators",
        }).success(function(data, status){
            allLegResults = data["results"];
            $scope.legResults = allLegResults;
            $scope.stateSelects = statesArr;
            $scope.legStateFilter = stateFilter;
            $scope.legCurChamber = "";
            $scope.legTitle = "State"
            $scope.legTextFilter = "";
            $scope.legOrder = "state";
            $scope.legCurPage = 1;
        });
        if(localStorage.getItem("favLegID") == null) {
            localStorage.setItem("favLegID", JSON.stringify(legFavID));
            localStorage.setItem("favLegCont", JSON.stringify(legFavCont));
        } else {
            legFavID = JSON.parse(localStorage.getItem("favLegID"));
            legFavCont = JSON.parse(localStorage.getItem("favLegCont"));
        }
    }

    //init legislators state table
    $scope.legTableOfState = function() {
        if(legCurActiveNav != "#leg-state") {
            $scope.legStateFilter = stateFilter;
            $scope.legTextFilter = "";
            $scope.legOrder = "state";
            $scope.legCurChamber = "";
            $scope.legTitle = "State"
            $(legCurActiveNav).removeClass("active");
            legCurActiveNav = "#leg-state";
            $(legCurActiveNav).addClass("active");
            $("#leg-select").css("display", "inline");
            $("#leg-search").css("display", "none");
        }  
    }

    //init legislators senate table
    $scope.legTableOfSenate = function() {
        if(legCurActiveNav != "#leg-senate") {
            $scope.legCurChamber = "senate";
            $scope.legOrder = "last_name";
            $scope.legStateFilter = "";
            $scope.legTextFilter = legSenateText;
            $scope.legTitle = "Senate";
            $(legCurActiveNav).removeClass("active");
            legCurActiveNav = "#leg-senate";
            $(legCurActiveNav).addClass("active");
            $("#leg-select").css("display", "none");
            $("#leg-search").css("display", "inline");
            $("#leg-search").value(legSenateText);
        }
    }

    //init legislators house table
    $scope.legTableOfHouse = function() {
        if(legCurActiveNav != "#leg-house") {
            $scope.legCurChamber = "house";
            $scope.legOrder = "last_name";
            $scope.legStateFilter = "";
            $scope.legTextFilter = legHouseText;
            $scope.legTitle = "House";
            $(legCurActiveNav).removeClass("active");
            legCurActiveNav = "#leg-house";
            $(legCurActiveNav).addClass("active");
            $("#leg-select").css("display", "none");
            $("#leg-search").css("display", "inline");
            $("#leg-search").val(legHouseText);
        }
    }

    //handle the change of state
    $scope.stateChange = function() {
        stateFilter = $scope.selectedState;
        if(stateFilter == null)
            stateFilter = "";
        $scope.legStateFilter = stateFilter;
    }
    
    //handle the change of search text
    $scope.legTextChange = function() {
        if(legCurActiveNav == "#leg-house") {
            legHouseText = $scope.legSearch;
            $scope.legTextFilter = legHouseText;
        } else if(legCurActiveNav == "#leg-senate") {
            legSenateText = $scope.legSearch;
            $scope.legTextFilter = legSenateText;
        }
    }

    //view detail button click
    $scope.viewLegDetails = function(curLeg) {
        var legID = curLeg["bioguide_id"];
        $scope.leg_detail = curLeg;
        $scope.term = getProgressVal(new Date($filter("date")(curLeg["term_start"])), new Date($filter("date")(curLeg["term_end"]))).toFixed(2) * 100;
        $http({
            method: "GET",
            url: "hw8.php?type=leg_detail&leg_id=" + legID,
        }).success(function(data, status){
            $scope.leg_bills = data["bills"];
            $scope.leg_coms = data["committees"];
            if(legFavID.indexOf(legID) == -1)
                $scope.favLegFlag = false;
            else
                $scope.favLegFlag = true;
        });
    }

    //favorite button click
    $scope.favLeg = function(curLeg) {
        var legID = curLeg["bioguide_id"];
        var index = legFavID.indexOf(legID);
        
        if(index != -1) {
            legFavID.splice(index, 1);
            legFavCont.splice(index, 1);
            $scope.favLegFlag = false;
        } else {
            legFavID.push(legID);
            legFavCont.push(curLeg);
            $scope.favLegFlag = true;
        }
        localStorage.setItem("favLegID", JSON.stringify(legFavID));
        localStorage.setItem("favLegCont", JSON.stringify(legFavCont));
    }
    function getProgressVal(startTime, endTime) {
        var today = new Date;
        return (today.getTime() - startTime.getTime()) / (endTime.getTime() - startTime.getTime());
    }

    //bills variable
    var activeBillsResults;
    var newBillsResults;
    var billResults;
    var billCurActiveNav = "";
    var activeBillText = "";
    var newBillText = "";
    var billFavID;
    var billFavCont;
    //bills functions

    function initBillCont() {
        $http({
            method: "GET",
            url: "hw8.php?type=bill",
        }).success(function(data, status){
            activeBillsResults = data["active"];
            newBillsResults = data["new"];
            billResults = activeBillsResults;
            $scope.billResults = billResults;
            $scope.billCurPage = 1;
            $scope.billTextFilter = "";
            $scope.billTitle = "Active";
        });
        billCurActiveNav = "#active-bills";
        if(localStorage.getItem("favBillID") == null) {
            localStorage.setItem("favBillID", JSON.stringify([]));
            localStorage.setItem("favBillCont", JSON.stringify([]));
        } else {
            billFavID = JSON.parse(localStorage.getItem("favBillID"));
            billFavCont = JSON.parse(localStorage.getItem("favBillCont"));
        }
    }

    //init active bills table
    $scope.billTableOfActive = function() {
        if(billCurActiveNav != "#active-bills") {
            $scope.billResults = activeBillsResults;
            $scope.billTextFilter = activeBillText;
            $scope.billTitle = "Active"
            billResults = activeBillsResults;
            $(billCurActiveNav).removeClass("active");
            billCurActiveNav = "#active-bills";
            $(billCurActiveNav).addClass("active");
            $("#bill-search").val(activeBillText);
        }
    }

    //init new bill table
    $scope.billTableOfNew = function() {
        if(billCurActiveNav != "#new-bills") {
            $scope.billResults = newBillsResults;
            $scope.billTextFilter = newBillText;
            $scope.billTitle = "New";
            billResults = newBillsResults;
            $(billCurActiveNav).removeClass("active");
            billCurActiveNav = "#new-bills";
            $(billCurActiveNav).addClass("active");
            $("#bill-search").val(newBillText);
        }
    }

    //handle bill search text change
    $scope.billTextChange = function() {
        if(billCurActiveNav == "#active-bills") {
            activeBillText = $scope.billSearch;
            $scope.billTextFilter = activeBillText;
        } else {
            newBillText = $scope.billSearch;
            $scope.billTextFilter = newBillText;
        }
    }

    //view bill's detail button click
    $scope.viewBillDetails = function(curBill) {
        var billID = curBill["bill_id"];
        if(billFavID.indexOf(billID) == -1)
            $scope.favBillFlag = false;
        else
            $scope.favBillFlag = true;
        $scope.bill_detail = curBill;
    }

    //bill favorite button
    $scope.favBill = function(curBill) {
        var billID = curBill["bill_id"];
        var index = billFavID.indexOf(billID);
        if(index != -1) {
            billFavID.splice(index, 1);
            billFavCont.splice(index, 1);
            $scope.favBillFlag = false;
        } else {
            billFavID.push(billID);
            billFavCont.push(curBill);
            $scope.favBillFlag = true;
        }
        localStorage.setItem("favBillID", JSON.stringify(billFavID));
        localStorage.setItem("favBillCont", JSON.stringify(billFavCont));
    }

    //committees variable
    var allComResults;
    var comCurActiveNav = "";
    var comCurChamber = "";
    var comHouseText = "";
    var comSenateText = "";
    var comJointText = "";
    var comFavID;
    var comFavCont;

    //committees functions

    function initComCont() {
        $http({
            method: "GET",
            url: "hw8.php?type=com",
        }).success(function(data, status){
            allComResults = data["results"];
            $scope.comResults = allComResults;
            $scope.comCurChamber = "house";
            $scope.comCurPage = 1;
            $scope.comTitle = "House";
        });
        comCurActiveNav = "#house-com";
        if(localStorage.getItem("favComID") == null) {
            localStorage.setItem("favComID", JSON.stringify([]));
            localStorage.setItem("favComCont", JSON.stringify([]));
        } else {
            comFavID = JSON.parse(localStorage.getItem("favComID"));
            comFavCont = JSON.parse(localStorage.getItem("favComCont"));
        }
    }

    //init house committees table
    $scope.comTableOfHouse = function() {
        if(comCurActiveNav != "#house-com") {
            $scope.comCurChamber = "house";
            $scope.comTextFilter = comHouseText;
            $scope.comTitle = "House"
            $(comCurActiveNav).removeClass("active");
            comCurActiveNav = "#house-com";
            $(comCurActiveNav).addClass("active");
            $("#com-search").val(comHouseText);
        }
    }

    //init senate committees table
    $scope.comTableOfSenate = function() {
        if(comCurActiveNav != "#senate-com") {
            $scope.comCurChamber = "senate";
            $scope.comTextFilter = comSenateText;
            $scope.comTitle = "Senate";
            $(comCurActiveNav).removeClass("active");
            comCurActiveNav = "#senate-com";
            $(comCurActiveNav).addClass("active");
            $("#com-search").val(comSenateText);
        }
    }

    //init joint committees table
    $scope.comTableOfJoint = function() {
        if(comCurActiveNav != "#joint-com") {
            $scope.comCurChamber = "joint";
            $scope.comTextFilter = comJointText;
            $scope.comTitle = "Joint";
            $(comCurActiveNav).removeClass("active");
            comCurActiveNav = "#joint-com";
            $(comCurActiveNav).addClass("active");
            $("#com-search").val(comJointText);
        }
    }

    //handle change of committees search text
    $scope.comTextChange = function() {
        if(comCurActiveNav == "#house-com") {
            comHouseText = $scope.comSearch;
            $scope.comTextFilter = comHouseText;
        } else if(comCurActiveNav == "#senate-com"){
            comSenateText = $scope.comSearch;
            $scope.comTextFilter = comSenateText;
        } else {
            comJointText = $scope.comSearch;
            $scope.comTextFilter = comJointText;
        }
    }

    //favorite committee button
    $scope.favCom = function(curCom) {
        var comID = curCom["committee_id"];
        var htmlComID = "#" + comID;
        var index = comFavID.indexOf(comID);
        if(index != -1) {
            comFavID.splice(index, 1);
            comFavCont.splice(index, 1);
            $(htmlComID).removeClass("fa-star");
            $(htmlComID).addClass("fa-star-o");
        } else {
            comFavID.push(comID);
            comFavCont.push(curCom);
            $(htmlComID).removeClass("fa-star-o");
            $(htmlComID).addClass("fa-star");
        }
        localStorage.setItem("favComID", JSON.stringify(comFavID));
        localStorage.setItem("favComCont", JSON.stringify(comFavCont));
    }


    $scope.favComFlag = function(com_id) {
        var index = comFavID.indexOf(com_id);
        if(index == -1)
            return false;
        else
            return true;
    }


    var favCurActiveNav = "";

    function initFavCont() {
        $scope.curFav = "Legislators";
        favCurActiveNav = "#fav-leg";
    }

    function refeshFavCont() {
        if(favCurActiveNav == "#fav-leg") 
            $scope.favResults = legFavCont;
        else if(favCurActiveNav == "#fav-bill")
            $scope.favResults = billFavCont;
        else if(favCurActiveNav == "#fav-com")
            $scope.favResults = comFavCont;
    }

    $scope.favTableOfLeg = function() {
        if(favCurActiveNav != "#fav-leg") {
            $scope.favResults = legFavCont;
            $scope.curFav = "Legislators";
            $(favCurActiveNav).removeClass("active");
            favCurActiveNav = "#fav-leg";
            $(favCurActiveNav).addClass("active");
        }
    }
    $scope.favTableOfBill = function() {
        if(favCurActiveNav != "#fav-bill") {
            $scope.favResults = billFavCont;
            $scope.curFav = "Bills"
            $(favCurActiveNav).removeClass("active");
            favCurActiveNav = "#fav-bill";
            $(favCurActiveNav).addClass("active");
        }
        
    }
    $scope.favTableOfCom = function() {
        if(favCurActiveNav != "#fav-com") {
            $scope.favResults = comFavCont;
            $scope.curFav = "Committees"
            $(favCurActiveNav).removeClass("active");
            favCurActiveNav = "#fav-com";
            $(favCurActiveNav).addClass("active");
        }
    }
    $scope.deleteFav = function(curResult) {
        if(favCurActiveNav == "#fav-leg") {
            var legID = curResult["bioguide_id"];
            var index = legFavID.indexOf(legID);
            legFavID.splice(index, 1);
            legFavCont.splice(index, 1);
            localStorage.setItem("favLegID", JSON.stringify(legFavID));
            localStorage.setItem("favLegCont", JSON.stringify(legFavCont));
        } else if(favCurActiveNav == "#fav-bill") {
            var billID = curResult["bill_id"];
            var index = billFavID.indexOf(billID);
            billFavID.splice(index, 1);
            billFavCont.splice(index, 1);
            localStorage.setItem("favBillID", JSON.stringify(billFavID));
            localStorage.setItem("favBillCont", JSON.stringify(billFavCont));
        } else if(favCurActiveNav == "#fav-com") {
            var comID = curResult["committee_id"];
            var index = comFavID.indexOf(comID);
            comFavID.splice(index, 1);
            comFavCont.splice(index, 1);
            localStorage.setItem("favComID", JSON.stringify(comFavID));
            localStorage.setItem("favComCont", JSON.stringify(comFavCont));
        }
        refeshFavCont();
    }
    $scope.viewFavDetails = function(curResult) {
        if(favCurActiveNav == "#fav-leg") {
            $scope.viewLegDetails(curResult);
            $scope.changeToLegCont();
            $("#leg-cont").carousel("next");
        } else if(favCurActiveNav == "#fav-bill") {
            $scope.viewBillDetails(curResult);
            $scope.changeToBillsCont();
            $("#bills-cont").carousel("next");
        }
    }
});

$(document).ready(function(){
    $(".carousel").each(function() {
        $(this).carousel({
            interval: false
        });
    });
    
    //drawing of side bar
    $("#menu-btn").click(function() {
        if($("#side-nav").css("display") == "none") {
            $("#side-nav").css("display", "inline");
            $("#main-content").css("margin-left", "15%");
        } else {
            $("#side-nav").css("display", "none");
            $("#main-content").css("margin-left", "0");
        }
    });
});