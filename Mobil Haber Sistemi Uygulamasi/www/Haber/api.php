<?php
/*
    Yönetim paneli ve Mobil uygulamanın veri akışını sağlayacak web servisi.
    
*/
error_reporting(E_ALL);
ini_set('display_errors', 1);
include("database.php");
$db = new Database();
$method = $_SERVER['REQUEST_METHOD'];
$request = explode("/", substr(@$_SERVER['PATH_INFO'], 1));

if ($method == "GET") {
    if (sizeof($request) == 1) {
        switch ($request[0]) {
            case "Haber":
                if(isset($_GET["turler"]))print(json_encode($db->turHaberleriCek(explode(",",$_GET["turler"]))));
                else print(json_encode($db->haberleriCek()));
                break;
            case "Tur":
                print(json_encode($db->turleriCek()));
                break;
        }
    } elseif (sizeof($request) == 2) {
        switch ($request[0]) {
            case "Haber":
                if(isset($_GET["action"])){
                    $act=$_GET["action"];
                    if($act=="begen")$db->begen($request[1]);
                    else if($act=="begenme")$db->begenme($request[1]);
                }
                else
                    print(json_encode($db->haberCek($request[1])));
                break;
            case "Tur":
                print(json_encode($db->turCek($request[1])));
                break;
        }
    }
} else {
    $p = $_POST;

    switch ($p["method"]) {
        case "PUT":

                switch ($request[0]) {
                    case "Haber":
                        $target_dir = "uploads/";
                        $target_file = $target_dir . basename($_FILES["resim"]["name"]);
                        move_uploaded_file($_FILES["resim"]["tmp_name"], $target_file);
            
                        $haber = new Haber(
                            NULL,
                            $p["baslik"],
                            $p["icerik"],
                            $p["turid"],
                            $p["tarih"],
                            0,
                            0,
                            0,
                            $target_file
                        );
						//Haber eklendikten sonra eklenen haberin idsini alıyoruz
                        $id=$db->haberEkle($haber);
                        //push not gönder
						//$serv key, firebase sunucusunun anaktarı
						//Token ise cihazımızın
                        $serv_key="AAAAHz7JTPM:APA91bElWTd8UXB_jyb75NBlIgV7XvcEAWUjIVg-T96BrMQqILVj60chexaWxvjd-0Kb8xnPJutcV5zGPW-luxz5bF72UDYlFW8hd-MwcQ8E_qntS5FsjV7601jY49b6E9tEuoYr2tyB";
                        $token="c2vkw9T-900:APA91bHayANMyg_kUO2tP12bwTv05HTEEJaAwbTF5Xa1pyzLFYAAkM_d_uo40fnAsWlLV72V-TICyhyHp29ZwB0ZCm2fxZsFgugMz4W93y-EuB7FF7OzQE4Xhz_sjGC8zGORKqS7geeR";
                        $url = 'https://fcm.googleapis.com/fcm/send';
                        $fields['registration_tokens'] = array($token);
						
						
                        $fields["notification"]=array("title"=>"Yeni haber geldi","body"=>$p["baslik"],"click_action"=>"com.example.haberler_TARGET_NOTIFICATION");
                        $fields['to'] = $token;
                        $fields["data"]=array("id"=>$id);
                        $headers = array(
                            'Content-Type:application/json',
                                'Authorization:key='.$serv_key
                            );
                        $ch = curl_init();
                        curl_setopt($ch, CURLOPT_URL, $url);
                        curl_setopt($ch, CURLOPT_POST, true);
                        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
                        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
                        $result = curl_exec($ch);
                        curl_close($ch);
                        print($result);
                        print("<br>".$id);
						//sunucuyla haberleşme işini yapıyor
						
                        break;
                    case "Tur":
                        $db->turEkle(new Tur(NULL, $p["tur"]));
                        break;
                }
                break;
        case "POST":
            switch ($request[0]) {
                case "Haber":
                    if($_FILES["resim"]["name"]!=""){
                        $target_dir = "uploads/";
                        $target_file = $target_dir . basename($_FILES["resim"]["name"]);
                        move_uploaded_file($_FILES["resim"]["tmp_name"], $target_file);
                    }
                    else $target_dir=NULL;
                    $haber = new Haber(
                        $p["id"],
                        $p["baslik"],
                        $p["icerik"],
                        $p["turid"],
                        $p["tarih"],
                        NULL,
                        NULL,
                        NULL,
                        $target_file
                    );
                    $db->haberDuzenle($haber);
                    break;
                case "Tur":
                    $db->turDuzenle(new Tur($p["id"], $p["tur"]));
                    break;
            }
            break;
        case "DELETE":
            switch ($request[0]) {
                case "Haber":
                    $db->haberSil($request[1]);
                    break;
                case "Tur":
                    $db->turSil($request[1]);
                    break;
            }
            break;
    }
    print "Yönlendiriliyor.";
    header('Location: '."../admin.php");
}
