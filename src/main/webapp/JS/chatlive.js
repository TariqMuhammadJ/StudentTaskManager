/**
 * 
 */


const params = new URLSearchParams(window.location.search);
const userId = params.get("userId");
const targetId = params.get("targetid");
const messages = document.querySelector(".messages");
const Socket = new WebSocket(`wss://basilisk-comic-wrongly.ngrok-free.app/StudyTask/chat/${userId}/${targetId}`)
const messageFrom = document.getElementById("message-form");

document.addEventListener("DOMContentLoaded", () => {
	Socket.onopen = () => {
	    console.log("WebSocket connection created" + userId);
	};


    const fetchMessages = () => {
        fetch(`/StudyTask/allChats?userId=${userId}&targetId=${targetId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch messages");
                }
                return response.json();
            })
            .then(data => {
                // If we received messages, process and display them
                if (data.length > 0) {
                    // Clear existing messages (if you want to update with only new messages)
                    data.forEach(chat => {
                        const messageDiv = document.createElement("div");
                        messageDiv.classList.add("message");

                        // Add .you or .them based on sender
                        if (chat.senderId == userId) {
                            messageDiv.classList.add("you");
                        } else {
                            messageDiv.classList.add("them");
                        }

                        // Set chat text content
                        messageDiv.textContent = chat.chatText;

                        // Add timestamp (sentAt)
                        const timestampDiv = document.createElement("div");
                        timestampDiv.classList.add("timestamp");
                        const date = new Date(chat.sentAt);
                        timestampDiv.textContent = date.toLocaleString(); // Format as you like

                        // Append timestamp below message
                        messageDiv.appendChild(timestampDiv);

                        // Append message div to container
                        messages.appendChild(messageDiv);

                    });

                    // Scroll to the bottom to show latest message
                    messages.scrollTop = messages.scrollHeight;
                }
            })
            .catch(error => {
                console.error("Error loading messages:", error);
            });
    };

    // Fetch messages immediately on page load
    fetchMessages();

    // Set an interval to fetch messages every 1 second (1000 milliseconds)
 
});



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
	messages.scrollTop = messages.scrollHeight;
	
})


Socket.onmessage = function(event){
	console.log(event.data);
	let newMessage = document.createElement("div");
	newMessage.className = "message";
	newMessage.textContent = event.data;
	messages.append(newMessage);
	messages.scrollTop = messages.scrollHeight;
}


const backButton = document.getElementById("back");

if (backButton) {
    backButton.addEventListener("click", function (e) {
        if (Socket && Socket.readyState === WebSocket.OPEN) {
            console.log("Closing WebSocket connection before navigating back...");
            Socket.close(); // Close the socket
        }
    });
}
