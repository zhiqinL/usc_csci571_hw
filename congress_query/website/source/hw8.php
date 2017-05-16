<?php
define("LEG", "http://congress.api.sunlightfoundation.com/legislators?apikey=47faa7d1f6614201b615759ab2c1aff1");
define("BILL", "http://congress.api.sunlightfoundation.com/bills?apikey=47faa7d1f6614201b615759ab2c1aff1");
define("COM", "http://congress.api.sunlightfoundation.com/committees?apikey=47faa7d1f6614201b615759ab2c1aff1");

$reqType = $_GET["type"];
switch ($reqType) {
    case "legislators":
        getLegContent();
        break;
    case "leg_detail":
        getLegDetailContent();
        break;
    case "bill":
        getBillContent();
        break;
    case "com":
        getComContent();
        break;
}

//get all legislators information
function getLegContent() {
    $leg_url = LEG . "&per_page=all";
    echo file_get_contents($leg_url);
}

//get legislator detail information
function getLegDetailContent() {
    $leg_id = $_GET["leg_id"];
    $leg_bill_url = BILL . "&per_page=5&sponsor_id=" . $leg_id;
    $leg_con_url = COM . "&per_page=5&member_ids=" . $leg_id;
    $leg_bill_json = json_decode(file_get_contents($leg_bill_url), true);
    $leg_con_json = json_decode(file_get_contents($leg_con_url), true);
    $result_json = array("bills" => $leg_bill_json["results"], "committees" => $leg_con_json["results"]);
    echo json_encode($result_json);
}

//get all bills information
function getBillContent() {
    $active_bill_url = BILL. "&per_page=50&history.active=true&order=introduced_on";
    $new_bill_url = BILL. "&per_page=50&history.active=false&order=introduced_on";
    $active_bill_json = json_decode(file_get_contents($active_bill_url), true);
    $new_bill_json = json_decode(file_get_contents($new_bill_url), true);
    $result_json = array("active" => $active_bill_json["results"], "new" => $new_bill_json["results"]);
    echo json_encode($result_json);
}

//get all committees information
function getComContent() {
    $com_url = COM . "&per_page=all";
    echo file_get_contents($com_url);
}
?>