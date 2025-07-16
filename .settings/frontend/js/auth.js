// auth.js
const API_BASE = 'http://localhost:8080/api';

// Helper: Save token
function saveToken(token) {
    localStorage.setItem('jwt', token);
}

// Helper: Get token
function getToken() {
    return localStorage.getItem('jwt');
}

// Login
const loginForm = document.getElementById('login-form');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        try {
            const res = await fetch(`${API_BASE}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            if (!res.ok) throw new Error(await res.text());
            const data = await res.json();
            saveToken(data.token);
            window.location.href = 'products.html';
        } catch (err) {
            document.getElementById('login-alert').style.display = 'block';
            document.getElementById('login-alert').textContent = err.message;
        }
    });
}

// Register
const registerForm = document.getElementById('register-form');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const role = document.getElementById('role').value;
        try {
            const res = await fetch(`${API_BASE}/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password, role })
            });
            if (!res.ok) throw new Error(await res.text());
            document.getElementById('register-alert').style.display = 'block';
            document.getElementById('register-alert').classList.add('success');
            document.getElementById('register-alert').textContent = 'Registration successful! Please login.';
            setTimeout(() => window.location.href = 'login.html', 1500);
        } catch (err) {
            document.getElementById('register-alert').style.display = 'block';
            document.getElementById('register-alert').textContent = err.message;
        }
    });
} 