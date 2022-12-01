function kiemTraTen()
{
    var reg = /^[A-Z]{1}[a-zA-Z ]*$/;
    var ten = document.getElementById("txtName").value;
    if(reg.test(ten))
    {
        document.getElementById("errTen").innerHTML="";
        return true;
    }
    {
        document.getElementById("errTen").innerHTML="Yêu cầu nhập chữ cái đầu in hoa";
        return false;
    }
}


function kiemTraSDT()
{
    var reg = /(^(09|03|07|08|05)+([0-9]{8})$)/;
    var SDT = document.getElementById("txtSDT").value;
    if(reg.test(SDT))
    {
        document.getElementById("errSDT").innerHTML="";
        return true;
    }
    {
        document.getElementById("errSDT").innerHTML="Yêu cầu nhập số có 10 chữ số với 2 số đầu là (03, 05, 07, 08, 09)";
        return false;
    }
}

function kiemTraDC()
{
    var reg = /^[a-zA-Z0-9]+[a-zA-Z0-9]*$/;
    var DC = document.getElementById("txt").value;
    if(reg.test(DC))
    {
        document.getElementById("errDC").innerHTML="";
        return true;
    }
    {
        document.getElementById("errDC").innerHTML="";
        return false;
    }
}
function kiemTraEmail()
{
    var reg = /^[a-zA-Z]+[a-zA-Z0-9]*\@gmail.com$/;
    var txtemail = document.getElementById("txtemail").value;
    if(reg.test(txtemail))
    {
        document.getElementById("errEmail").innerHTML="";
        return true;
    }
    {
        document.getElementById("errEmail").innerHTML="Nhập định dạng abc123@gmail.com";
        return false;
    }
}
function kiemTraemail() {
    var reg = /^[a-zA-Z]+[a-zA-Z0-9]*\@gmail.com$/;
    var txtemail = document.getElementById("txtEmail").value;
    if (reg.test(txtemail)) {
        document.getElementById("erremail").innerHTML = "";
        return true;
    }
    {
        document.getElementById("erremail").innerHTML = "Nhập định dạng abc123@gmail.com";
        return false;
    }
}
function ktratrung() {
    var mkhau1 = document.getElementById("mk1").value;
    var mkhau2 = document.getElementById("mk2").value;
    if (mkhau1 == mkhau2) {
        document.getElementById("er4").innerHTML = "";
        return true;
    }
    else {
        document.getElementById("er4").innerHTML = "khong trung";
        return false;
    }
}
function ktratrung() {
    var mkhau1 = document.getElementById("mk1").value;
    var mkhau2 = document.getElementById("mk2").value;
    if (mkhau1 == mkhau2) {
        document.getElementById("er4").innerHTML = "";
        return true;
    }
    else {
        document.getElementById("er4").innerHTML = "khong trung";
        return false;
    }
}
function ktramk1() {
    var reg1 = /^[A-Za-z0-9!@#$%^&*()]{6,}$/;
    var mkhau1 = document.getElementById("mk1").value;
    if (reg1.test(mkhau1)) {
        document.getElementById("er41").innerHTML = "";
        return true;
    }
    else {
        document.getElementById("er41").innerHTML = "Tối thiểu 6 kí tự";
        return false;
    }
}


function ktrns() {
    var n = document.getElementById("ns").value;
    n = new Date(n);
    nht = new Date();
    var t = n.getFullYear() < nht.getFullYear();
    if (t) {
        document.getElementById("errns").innerHTML = "";
        return true;
    }
    else {
        document.getElementById("errns").innerHTML = "Năm sinh phải nhỏ hơn năm hiện tại";
        return false;
    }
}
function dangky() {
    if (kiemTraTen() && kiemTraSDT() && kiemTraDC() && kiemTraEmail() && ktratrung() && ktramk1() && ktrns() )
    {
        alert("Đăng ký thành công!");
    }
    else
    {
        alert("Đăng ký không thành công! Vui lòng xem lại thông tin cần nhập!");
    }
}

function kiemTraTenTT() {
    var reg = /^[A-Z]{1}[a-zA-Z ]*$/;
    var ten = document.getElementById("txtNameTT").value;
    if (reg.test(ten)) {
        document.getElementById("errTenTT").innerHTML = "";
        return true;
    }
    {
        document.getElementById("errTenTT").innerHTML = "Yêu cầu nhập chữ cái đầu in hoa";
        return false;
    }
}
function kiemTraSDTTT() {
    var reg = /((09|03|07|08|05)+([0-9]{8})\b)/;
    var SDT = document.getElementById("txtSDTTT").value;
    if (reg.test(SDT)) {
        document.getElementById("errSDTTT").innerHTML = "";
        return true;
    }
    {
        document.getElementById("errSDTTT").innerHTML = "Yêu cầu nhập số có 10 chữ số với 2 số đầu là (03, 05, 07, 08, 09)";
        return false;
    }
}
function dathang() {
    if (kiemTraTenTT() && kiemTraSDTTT()) {
        alert("Đặt hàng thành công!")
    }
    else {
        alert("Đặt hàng không thành công! Vui lòng xem lại thông tin cần nhập!")
    }

}
