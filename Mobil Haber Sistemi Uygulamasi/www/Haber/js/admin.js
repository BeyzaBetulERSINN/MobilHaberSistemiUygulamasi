var hostname="http://localhost/Haber";
$(document).ready(()=>{

    haberListele();
    turListele();

});

async function haberCek(id){
    var haber= await $.ajax(hostname+"/api.php/Haber/"+id,
    {
        method:"GET"
    });
    return await haber;
}
function haberListele(){
    $.ajax(hostname+"/api.php/Haber",
    {
        method:"GET"
    }).done(data=>{
        data=JSON.parse(data)
        console.log(data);
        
        var innerHtml="<table>";
        innerHtml+=`<tr>
                    <th>ID</th>
                    <th>Başlık</th>
                    <th>İçerik</th>
                    <th>TürID</th>
                    <th>Tarih</th>
                    <th>Resim</th>
                    <th>+</th>
                    <th>-</th>
                    <th>Görüntülenme</th>
                    <th>Düzenle</th>
                    <th>Sil</th>
                    </tr>`;
        data.forEach(e => {
            
            innerHtml+=`<tr>
                        <td>${e.id}</td>
                        <td>${e.baslik}</td>
                        <td>${e.icerik}</td>
                        <td>${e.turid}</td>
                        <td>${e.tarih}</td>
                        <td>${e.resim}</td>
                        <td>${e.begenme}</td>
                        <td>${e.begenmeme}</td>
                        <td>${e.goruntulenme}</td>
                        <td><button onclick="haberDuzenle(${e.id})">Düzenle</button></td>
                        <td><button onclick="haberSil(${e.id})">Sil</button></td>
                        </tr>`;
        });
        innerHtml+="</table>";  
        $(".haberdiv").html(innerHtml);
    });
}
function haberEkle(){
    closePopup();
    $(".haberDuzenle").show();
    $(".haberDuzenle>#divTitle").html("Haber Düzenle");
    $("input[name='method']").val("PUT");
}
async function haberDuzenle(id){
    closePopup();
    $(".haberDuzenle").show();
    var haber=JSON.parse(await haberCek(id));
    $(".haberDuzenle>#divTitle").html("Haber Düzenle");
    $("input[name='method']").val("POST");
    $("input[name='id']").val(id);

    $("input[name='baslik']").val(haber.baslik);
    $("textarea[name='icerik']").html(haber.icerik);
    $("input[name='turid']").val(haber.turid);
    $("input[name='tarih']").val(haber.tarih);

}
function haberSil(id){
    var onay=confirm("Silmek istediğinize emin misiniz?");

    if(onay){
        $.post(hostname+"/api.php/Haber/"+id,{method:"DELETE"}).then((d)=>{
            haberListele();
        });
    }

}


async function turCek(id){
    var haber= await $.ajax(hostname+"/api.php/Tur/"+id,
    {
        method:"GET"
    });
    return await haber;
}
function turListele(){
    var innerHtml="<table>";
    innerHtml+=`<tr>
                <th>ID</th>
                <th>Tür</th>
                <th>Düzenle</th>
                <th>Sil</th>
                </tr>`;


    $.ajax(hostname+"/api.php/Tur",
    {
        method:"GET"
    }).done(data=>{
        data=JSON.parse(data)
        console.log(data);
        data.forEach(e => {
            $("select[name='turid']").append(`<option value=${e.id}>${e.tur}</option>`);
            innerHtml+=`<tr>
                        <td>${e.id}</td>
                        <td>${e.tur}</td>
                        <td><button onclick="turDuzenle(${e.id})">Düzenle</button></td>
                        <td><button onclick="turSil(${e.id})">Sil</button></td>
                        </tr>`;

        });
        innerHtml+="</table>";  
        $(".kategoriDiv").html(innerHtml);

    });

}
function turEkle(){
    closePopup();
    $(".turDuzenle").show();
    $(".turDuzenle>#divTitle").html("Tür Ekle");
    $("input[name='method']").val("PUT");
}
async function turDuzenle(id){
    closePopup();
    $(".turDuzenle").show();
    var tur=await turCek(id);
    var tur=JSON.parse(tur);
    $(".turDuzenle>#divTitle").html("Tür Düzenle");
    $("input[name='method']").val("POST");
    $("input[name='id']").val(id);
    $("input[name='tur']").val(tur.tur);
}
function turSil(id){
    var onay=confirm("Silmek istediğinize emin misiniz? Bir türü silmek bazı haberlerin yok olmasına neden olabilir.");

    if(onay){
        $.post(hostname+"/api.php/Tur/"+id,{method:"DELETE"}).then((d)=>{
            turListele();
        });
    }
}

function closePopup(){
    $(".haberDuzenle").hide();
    $(".turDuzenle").hide();
}
