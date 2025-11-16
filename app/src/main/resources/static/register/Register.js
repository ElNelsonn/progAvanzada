

const form = document.getElementById('registerForm');
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
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (!username || !password) {
        showAlert('Completá todos los campos requeridos.');
        return;
    }

    if (password.length <= 5) {
        showAlert('La contraseña es muy corta.');
        return;
    }


    if (password !== confirmPassword) {
        showAlert('Las contraseñas no coinciden.');
        return;
    }

    const payload = { username: username, password: password };

    submitBtn.disabled = true;
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Creando...';

    try {
        const res = await fetch('http://localhost:8080/user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
        });

        const isJson = res.headers.get('content-type')?.includes('application/json');
        const data = isJson ? await res.json() : null;

        if (!res.ok) {
            const message = await res.text();

            showAlert(`No se pudo registrar: ${message}`);
            return;
        }

        showAlert('Cuenta creada con éxito.', 'success');

        setTimeout(() => location.href = '/login', 1200);

    } catch (err) {
        showAlert('Error de red o CORS.');
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = originalText;
    }
});