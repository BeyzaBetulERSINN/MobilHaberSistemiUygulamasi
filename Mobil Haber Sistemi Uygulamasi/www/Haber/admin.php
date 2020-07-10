<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Haber Uygulaması Yönetim Paneli</title>
    <link rel="stylesheet" href="css/admin.css">
    <script src="js/jquery.js"></script>
    <script src="js/admin.js"></script>
</head>

<body>
    <center>
        <div class="container">
            <div class="kategoriDiv"></div>
            <button onclick="turEkle()" style="width:200px;"> Kategori Ekle</button>
            
            <div class="haberdiv"></div>
            <button onclick="haberEkle()" style="width:500px;"> Haber Ekle</button>
            
            <div class="turDuzenle mForm">
                <div style="float:left;margin-right:10px; color:red;" onclick="closePopup()" class="close">X</div>
                <div id="divTitle">Ekle</div>
                <form id="haberForm" action="api.php/Tur" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="method" value="PUT"></input>
                    <input type="hidden" name="id" value=""></input>
                     <label for="tur">Tür:</label><input type="text" name="tur"></input>
                    <input type="submit" value="Gönder">
                </form>
            </div>
            
            <div class="haberDuzenle mForm">
                <div style="float:left;margin-right:10px; color:red;" onclick="closePopup()" class="close">X</div>
                <div id="divTitle">Ekle</div>
                <form id="haberForm" action="api.php/Haber" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="method" value="PUT"></input>
                    <input type="hidden" name="id" value=""></input>

                    <label for="baslik">Başlık:</label><input type="text" name="baslik"></input>
                    <label for="icerik">İçerik:</label><textarea name="icerik"></textarea>
                    <label for="turid">Tür:</label><select type="text" name="turid">

                    </select>
                    <label for="tarih">Tarih:</label><input type="date" name="tarih"></input>
                    <label for="resim">Resim:</label><input type="file" name="resim"></input>
                    <input type="submit" value="Gönder">
                </form>
            </div>
        </div>
    </center>
</body>

</html>