/**
 * 
 */
const params = new URLSearchParams(window.location.search);
const userId = params.get("userId");
const targetId = params.get("targetid");
const messages = document.querySelector(".messages");
const Socket = new WebSocket(`ws://localhost:8080/StudyTask/chat/${userId}/${targetId}`)
const messageFrom = document.getElementById("message-form");

Socket.onopen = ()=> {
	console.log("Web socket connection created");
	
}

messageFrom.addEventListener("submit", function(event){
	event.preventDefault();
	const messageInput = messageFrom.querySelector("#message");
	const message = messageInput.value;
	Socket.send(message);
	messageInput.value = "";
	let youMessage = document.createElement("div");
	youMessage.classList.add("message", "you");
	youMessage.textContent = message;
	messages.append(youMessage);
	
})


Socket.onmessage = function(event){
	console.log(event.data);
	let newMessage = document.createElement("div");
	newMessage.className = "message";
	newMessage.textContent = event.data;
	messages.append(newMessage);
}