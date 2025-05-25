document.addEventListener("DOMContentLoaded", () => {
	document.querySelectorAll(".chat-icon").forEach((chatLink) =>{
		chatLink.addEventListener("click", function(e){
			e.preventDefault();
			window.location.href = chatLink.getAttribute("href");
		})
	})
})



