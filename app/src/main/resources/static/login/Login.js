const form = document.getElementById('loginForm');
const alertBox = document.getElementById('formAlert');
const submitBtn = document.getElementById('submitBtn');

function showAlert(message, type = 'error') {
    alertBox.textContent = message;
    alertBox.className =
        'mb-4 rounded-lg p-3 text-sm ' +
        (type === 'success'
                ? 'bg-emerald-900/40 text-emerald-300 border border-emerald-700'
                : 'bg-red-900/40 text-red-300 border border-red-700'
        );
    alertBox.classList.remove('hidden');
}

function clearAlert() {
    alertBox.classList.add('hidden');
    alertBox.textContent = '';
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearAlert();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;


    if (!username || !password) {
        showAlert('Completá todos los campos requeridos.');
        return;
    }

    const payload = { username: username, password: password };

    submitBtn.disabled = true;
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Iniciando...';

    try {
        const res = await fetch('http://localhost:8080/user/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
        });

        const body = await res.text();

        if (!res.ok) {
            showAlert(`No se pudo iniciar sesión: ${body}`);
            return;
        }

        const id = body.trim();

        if (Number.isNaN(id)) {
            showAlert("El servidor devolvió algo inesperado: " + body);
            return;
        }

        showAlert('Inicio de sesión exitoso.', 'success');


        localStorage.setItem("userId", id);
        setTimeout(() => location.href = '/home?id=' + id, 1200);

    } catch (err) {
        showAlert('Error de red o CORS.'+ err.message);

    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = originalText;
    }
});