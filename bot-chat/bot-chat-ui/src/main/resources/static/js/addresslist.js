var laddaArray = new Array();

function addressUserClick(userId, userName, headImage) {
	console.log("点击的用户：" + userId);
	$("#receiver").val(userId);
	if (1 == userId) {
		$.ajax({
			type : "POST",
			url : "/user/depart/getUserInDept",
			data : {
				deaprtId : "5"
			},
			contentType : "application/x-www-form-urlencoded",
			dataType : "json",
			success : function(data) {
				staffdata = data;
				var template = $.templates("#staffListTemp");
				$('#div_chat_container').hide();
				$('#div_addfriend_container').show();

				template.link("#div_addfriend", data);
			},
		});
	} else {
		var content = {};
		content.fromSendTime = moment().format('x');
		content.chatAboutId = userId + "-" + getCookie("userId");
		content.userId = getCookie("userId");
		content.newOrHis = "new";
		sendMessage("GetChatRecord", content);

		$("#div_messageContent").empty();
		$("#div_message_box").show();

		$("#input_click_friend_userId").val(userId);
		$("#input_click_friend_userName").val(userName);
		$("#input_click_friend_headImage").val(headImage);
	}
}