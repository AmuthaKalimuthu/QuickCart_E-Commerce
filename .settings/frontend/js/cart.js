// cart.js
const API_BASE = 'http://localhost:8080/api';
const cartList = document.getElementById('cart-list');
const placeOrderBtn = document.getElementById('place-order-btn');
const cartAlert = document.getElementById('cart-alert');

function getToken() {
    return localStorage.getItem('jwt');
}

async function fetchCart() {
    try {
        const res = await fetch(`${API_BASE}/cart`, {
            headers: { 'Authorization': 'Bearer ' + getToken() }
        });
        if (!res.ok) throw new Error(await res.text());
        const cart = await res.json();
        let html = '<table class="table"><tr><th>Product</th><th>Quantity</th><th>Price</th><th></th></tr>';
        cart.items.forEach(item => {
            html += `<tr>
                <td>${item.productName}</td>
                <td><input type='number' min='1' value='${item.quantity}' onchange='updateItem(${item.id}, this.value)'></td>
                <td>$${item.price}</td>
                <td><button onclick='removeItem(${item.id})'>Remove</button></td>
            </tr>`;
        });
        html += `</table><h3>Total: $${cart.total}</h3>`;
        cartList.innerHTML = html;
    } catch (err) {
        cartList.innerHTML = `<div class='alert'>${err.message}</div>`;
    }
}

window.updateItem = async function(itemId, quantity) {
    try {
        const res = await fetch(`${API_BASE}/cart/item/${itemId}?quantity=${quantity}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + getToken() }
        });
        if (!res.ok) throw new Error(await res.text());
        fetchCart();
    } catch (err) {
        cartAlert.style.display = 'block';
        cartAlert.textContent = err.message;
    }
}

window.removeItem = async function(itemId) {
    try {
        const res = await fetch(`${API_BASE}/cart/item/${itemId}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + getToken() }
        });
        if (!res.ok) throw new Error(await res.text());
        fetchCart();
    } catch (err) {
        cartAlert.style.display = 'block';
        cartAlert.textContent = err.message;
    }
}

if (placeOrderBtn) {
    placeOrderBtn.onclick = async function() {
        try {
            const res = await fetch(`${API_BASE}/orders/place`, {
                method: 'POST',
                headers: { 'Authorization': 'Bearer ' + getToken() }
            });
            if (!res.ok) throw new Error(await res.text());
            cartAlert.style.display = 'block';
            cartAlert.classList.add('success');
            cartAlert.textContent = 'Order placed successfully!';
            fetchCart();
        } catch (err) {
            cartAlert.style.display = 'block';
            cartAlert.textContent = err.message;
        }
    }
}

if (cartList) fetchCart(); 