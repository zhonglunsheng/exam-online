function login(){
	if(form1.username.value==''){
		alert('用户名不能为空！');
		return false;
	}
	if(form1.password.value==''){
		alert('密码不能为空！');
		return false;
	}
	var email = $("#email").val();
	var password = $("#password").val();
	var role=$('input:radio[name="role"]:checked').val();

	$.post("/system/login",
		{
			email: email,
			password: password,
			role: role
		},
		function(data,status){
			if(data.code == 0){
				window.location.href = "/user/main";
			}else{
				layer.msg('用户名或密码错误');
			}
		});
}
window.onload = function(){
	var i3 = document.getElementsByClassName('input_3');
	for(var i=0;i<i3.length;i++){
		i3[i].onmouseover = function(){
			this.style.backgroundColor = "#23271F";
			this.style.color = "#fff";
		}
		i3[i].onmouseout = function(){
			this.style.backgroundColor = "#fff";
			this.style.color = "#23271F";
		}
	}
	var pass = document.getElementById("password");
	pass.onfocus = function(){
		pass.type = "password";	
	}
}