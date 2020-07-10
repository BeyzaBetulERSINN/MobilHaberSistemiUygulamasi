<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    class Haber{
        public $id,$baslik,$icerik,$turid,$tarih,$begenme,$begenmeme,$goruntulenme,$resim;
        function __construct($id,$baslik,$icerik,$turid,$tarih,$begenme,$begenmeme,$goruntulenme,$resim){
            $this->id=$id;
            $this->baslik=$baslik;
            $this->icerik=$icerik;
            $this->turid=$turid;
            $this->tarih=$tarih;
            $this->begenme=$begenme;
            $this->begenmeme=$begenmeme;
            $this->goruntulenme=$goruntulenme;
            $this->resim=$resim;
        }
        function json(){
            return json_encode(get_object_vars($this));
        }
        function array(){
            return get_object_vars($this);
        }

    }

    class Tur{
        public $id,$tur;
        function __construct($id,$tur){
            $this->id=$id;
            $this->tur=$tur;
        }
        function json(){
            return json_encode(get_object_vars($this));
        }
        function array(){
            return get_object_vars($this);
        }
    }


    class Database{
        private $host="mysql:host=localhost;dbname=haberodev";
        private $username="root";
        private $password="";
        private $dbname="haberodev";
        
        /*function __construct($host,$username,$password,$dbname){
            $this->host=$host;
            $this->username=$username;
            $this->password=$password;
            $this->dbname=$dbname;
        } */

        function connect(){
            try{
                $db=new PDO($this->host,$this->username,$this->password);
                $db->exec("SET NAMES 'utf8'; SET CHARSET 'utf8'");
                $db->setAttribute( PDO::ATTR_PERSISTENT, TRUE );
                $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );      
                return $db;
            }
            catch(PDOException $e){
                print $e->getMessage();
            }
        }

        function haberleriCek(){
            //Tüm haberler çekilip dizi halinde geri döndürülecek.
            $db=$this->connect();
            $query=$db->query("SELECT * FROM haberler");
            $retArr=[];
            foreach($query as $row)
                array_push($retArr,new Haber(
                    $row["id"],
                    $row["baslik"],
                    $row["icerik"],
                    $row["turid"],
                    $row["tarih"],
                    $row["begenme"],
                    $row["begenmeme"],
                    $row["goruntulenme"],
                    $row["resim"]
                ));
            return $retArr;
        }
        function haberCek($id){
            $db=$this->connect();
            $query=$db->query("SELECT * FROM haberler WHERE id=$id");
            $haber=new Haber(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
            $query->setFetchMode(PDO::FETCH_INTO,$haber);
            $query->fetch();
            $db->exec("UPDATE haberler SET goruntulenme=goruntulenme+1 WHERE id=$id");
            return $haber;
        }
        function turHaberleriCek($kategoriDizisi){
            //Verilen kategori idlerine ait haberler çekilip döndürülecek. 
            $db=$this->connect();
            $whereCond="turid=".$kategoriDizisi[0];
            for($i=1;$i<sizeof($kategoriDizisi);$i++)
                $whereCond.=" OR turid=".$kategoriDizisi[$i];

            $query=$db->query("SELECT * FROM haberler WHERE $whereCond");
            $retArr=[];
            foreach($query as $row)
                array_push($retArr,new Haber(
                    $row["id"],
                    $row["baslik"],
                    $row["icerik"],
                    $row["turid"],
                    $row["tarih"],
                    $row["begenme"],
                    $row["begenmeme"],
                    $row["goruntulenme"],
                    $row["resim"]
                ));
            return $retArr;
        }


        function begen($haberId){
            //Verilen haberin begeni sayısı bir arttırılır.
            $db=$this->connect();
            $query=$db->query("UPDATE haberler SET begenme=1, begenmeme=0 WHERE id=$haberId");
        }

        function begenme($haberId){
            //Verilen haberin begeni sayısı bir azaltılır.
            $db=$this->connect();
            $query=$db->query("UPDATE haberler SET begenmeme=1, begenme=0 WHERE id=$haberId");
        }


        function haberEkle($haber){
            try{
            $db=$this->connect();
            $queryStr="INSERT INTO haberler SET 
            baslik=:baslik,            
            icerik = :icerik,
            turid=:turid,
            tarih=:tarih,
            begenme=:begenme,
            begenmeme=:begenmeme,
            goruntulenme=:goruntulenme,
            resim=:resim";
            $arr=$haber->array();
            array_shift($arr);
            $query=$db->prepare($queryStr);
            $query->execute($arr);
            }catch(PDOException $e){
                print $e->getMessage();
            }
            return $db->lastInsertId();
        }

        function haberDuzenle($haber){
            $data=array(
                "id"=>$haber->id,
                "baslik"=>$haber->baslik,
                "icerik"=>$haber->icerik,
                "turid"=>$haber->turid,
                "tarih"=>$haber->tarih
            );
            $db=$this->connect();
            $query="UPDATE haberler SET 
            baslik=:baslik,            
            icerik = :icerik,
            turid=:turid,
            tarih=:tarih";
            if($haber->resim!=NULL){
                $query=$query.",resim=:resim";
                $data["resim"]=$haber->resim;
            }
            $query=$query." WHERE id=:id";
            $query=$db->prepare($query);
            $query->execute($data);
        }

        function haberSil($haberId){
            $db=$this->connect();
            $query="DELETE FROM haberler WHERE id=$haberId";
            $db->exec($query);
        }
        function turleriCek(){
            $db=$this->connect();
            $query=$db->query("SELECT * FROM turler");
            $retArr=[];
            foreach($query as $row)
                array_push($retArr,new Tur($row["id"],$row["tur"]));
            return $retArr;
        }
        function turCek($id){
            $db=$this->connect();
            $query=$db->query("SELECT * FROM turler WHERE id=$id");
            $tur=new Tur(NULL,NULL);
            $query->setFetchMode(PDO::FETCH_INTO,$tur);
            $query->fetch();
            return $tur;
        }
        function turEkle($tur){
            $db=$this->connect();
            $queryStr="INSERT INTO turler SET tur=:tur";
            $arr=$tur->array();
            array_shift($arr);
            $query=$db->prepare($queryStr);
            $query->execute($arr);
        }
        function turDuzenle($tur){
            $db=$this->connect();
            $queryStr="UPDATE turler SET tur=:tur WHERE id=:id";
            $arr=$tur->array();
            $query=$db->prepare($queryStr);
            $query->execute($arr);
        }
        function turSil($id){
            $db=$this->connect();
            $query="DELETE from turler WHERE id=$id";
            $db->exec($query);
        }

        function toString(){
            return $this->host."-".$this->username."-".$this->password."-".$this->dbname;
        }
    }
?>