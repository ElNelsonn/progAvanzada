//-------------------------------------------
// Obtener userId desde URL o localStorage
//-------------------------------------------

const params = new URLSearchParams(window.location.search);
let userId = Number(params.get("id"));

if (!userId) {
    userId = Number(localStorage.getItem("userId"));
}

if (!userId) {
    window.location.href = "/login";
} else {
    localStorage.setItem("userId", userId);
}

//-------------------------------------------
// Fetch APIs
//-------------------------------------------

async function fetchVideos() {
    const res = await fetch("http://localhost:8080/video/all");

    if (!res.ok) {
        throw new Error("Error obteniendo videos: " + await res.text());
    }

    return await res.json();
}

async function fetchUser(id) {
    const res = await fetch(`http://localhost:8080/user/me/${id}`);

    if (!res.ok) {
        throw new Error("Error obteniendo usuario: " + await res.text());
    }

    return await res.json();
}

//-------------------------------------------
// Variables globales
//-------------------------------------------

let currentUser = null;
let allVideos = [];

//-------------------------------------------
// Inicialización
//-------------------------------------------

async function init() {
    try {
        currentUser = await fetchUser(userId);
        allVideos = await fetchVideos();

        console.log("Usuario:", currentUser);
        console.log("Videos:", allVideos);

        renderVideos(allVideos);

    } catch (err) {
        console.error(err);
        alert("Error cargando datos iniciales.");
    }
}

//-------------------------------------------
// Render de videos
//-------------------------------------------

function getYoutubeId(url) {
    const regExp = /(?:youtu\.be\/|youtube\.com\/(?:watch\?v=|embed\/|shorts\/))([^?&]+)/;
    const match = url.match(regExp);
    return match ? match[1] : null;
}

function renderVideos(videos) {
    const container = document.getElementById("videoList");
    container.innerHTML = "";

    videos.forEach(video => {

        const youtubeId = getYoutubeId(video.url);
        const thumbnail = youtubeId
            ? `https://img.youtube.com/vi/${youtubeId}/hqdefault.jpg`
            : null;

        const isLiked = currentUser.likedVideoIds.includes(video.id);

        const item = document.createElement("div");
        item.className =
            "flex items-center bg-neutral-800 rounded-lg p-4 hover:bg-neutral-700 transition cursor-pointer";

        item.innerHTML = `
            <div class="w-16 h-16 bg-neutral-700 rounded mr-4 overflow-hidden">
                ${
            thumbnail
                ? `<img src="${thumbnail}" class="w-full h-full object-cover">`
                : ""
        }
            </div>

            <div class="flex-1">
                <p class="font-semibold">${video.title}</p>
                <p class="text-neutral-400 text-sm">${video.description ?? ""}</p>
            </div>

            <button class="likeBtn ml-4 px-3 py-1 rounded text-sm transition ${
            isLiked
                ? "bg-red-600 hover:bg-red-500"
                : "bg-neutral-700 hover:bg-neutral-600"
        }">
                ❤️ ${video.likes}
            </button>
        `;


        item.addEventListener("click", () => {
            openVideoModal(video);
        });


        const likeBtn = item.querySelector(".likeBtn");

        likeBtn.addEventListener("click", async (e) => {
            e.stopPropagation(); // no abrir modal

            try {
                if (isLiked) {

                    await unlikeVideo(userId, video.id);

                    video.likes--;
                    currentUser.likedVideoIds =
                        currentUser.likedVideoIds.filter(vId => vId !== video.id);

                } else {

                    await likeVideo(userId, video.id);

                    video.likes++;
                    currentUser.likedVideoIds.push(video.id);
                }

                // Actualizar UI
                renderVideos(videos);

            } catch (error) {
                console.error(error);
                alert("Error al cambiar el like.");
            }
        });

        container.appendChild(item);
    });
}


//-------------------------------------------
// Función para recargar videos desde el backend
//-------------------------------------------

async function loadVideos() {
    try {
        allVideos = await fetchVideos();
        renderVideos(allVideos);
    } catch (err) {
        console.error(err);
        alert("Error recargando videos.");
    }
}

//-------------------------------------------
// Logout
//-------------------------------------------

document.getElementById("logoutBtn").addEventListener("click", () => {
    localStorage.removeItem("userId");
    window.location.href = "/login";
});

//-------------------------------------------
// Modal: agregar video
//-------------------------------------------

const modal = document.getElementById("addVideoModal");
const addVideoBtn = document.getElementById("addVideoBtn");
const cancelBtn = document.getElementById("cancelVideoBtn");
const saveBtn = document.getElementById("saveVideoBtn");

addVideoBtn.onclick = () => {
    modal.classList.remove("hidden");
};

cancelBtn.onclick = () => {
    modal.classList.add("hidden");
};

saveBtn.onclick = async () => {
    const title = document.getElementById("videoTitle").value.trim();
    const url = document.getElementById("videoURL").value.trim();
    const description = document.getElementById("videoDescription").value.trim();

    if (!title || !url) {
        alert("El título y la URL son obligatorios.");
        return;
    }

    const videoData = { title, url, description };

    try {
        const response = await fetch("http://localhost:8080/video", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(videoData)
        });

        if (response.ok) {
            alert("Video agregado correctamente.");
            modal.classList.add("hidden");

            // limpiar campos
            document.getElementById("videoTitle").value = "";
            document.getElementById("videoURL").value = "";
            document.getElementById("videoDescription").value = "";

            // refrescar lista
            await loadVideos();

        } else {
            alert("Error al agregar el video.");
        }

    } catch (error) {
        console.error("Error:", error);
        alert("No se pudo conectar con el servidor.");
    }
};
//-------------------------------------------
// MODAL DE VIDEO PLAYER
//-------------------------------------------

const videoPlayerModal = document.getElementById("videoPlayerModal");
const closePlayerBtn = document.getElementById("closePlayerBtn");
const playerFrame = document.getElementById("playerFrame");
const playerTitle = document.getElementById("playerTitle");
const playerDescription = document.getElementById("playerDescription");


function openVideoModal(video) {
    const youtubeId = getYoutubeId(video.url);

    playerFrame.src = youtubeId
        ? `https://www.youtube.com/embed/${youtubeId}`
        : "";

    playerTitle.textContent = video.title;
    playerDescription.textContent = video.description || "";

    videoPlayerModal.classList.remove("hidden");
}


function closeVideoModal() {
    playerFrame.src = "";
    videoPlayerModal.classList.add("hidden");
}

closePlayerBtn.onclick = closeVideoModal;

// cerrar al hacer click fuera del modal
videoPlayerModal.addEventListener("click", (e) => {
    if (e.target === videoPlayerModal) {
        closeVideoModal();
    }
});


//-------------------------------------------
// Ejecutar init al cargar la página
//-------------------------------------------

window.onload = init;

async function likeVideo(userId, videoId) {
    const res = await fetch(`http://localhost:8080/video/like/${userId}/${videoId}`, {
        method: "POST"
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }
}

async function unlikeVideo(userId, videoId) {
    const res = await fetch(`http://localhost:8080/video/unlike/${userId}/${videoId}`, {
        method: "POST"
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }
}
