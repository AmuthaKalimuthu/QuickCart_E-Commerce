// orders.js
const API_BASE = 'http://localhost:8080/api';
const ordersList = document.getElementById('orders-list');

function getToken() {
    return localStorage.getItem('jwt');
}

async function fetchOrders() {
    try {
        const res = await fetch(`${API_BASE}/orders/history`, {
            headers: { 'Authorization': 'Bearer ' + getToken() }
        });
        if (!res.ok) throw new Error(await res.text());
        const orders = await res.json();
        let html = '';
        if (orders.length === 0) {
            html = '<p>No orders found.</p>';
        } else {
            orders.forEach(order => {
                html += `<div class='order'><h3>Order #${order.id} - ${new Date(order.createdAt).toLocaleString()}</h3>`;
                html += '<table class="table"><tr><th>Product</th><th>Quantity</th><th>Price</th></tr>';
                order.items.forEach(item => {
                    html += `<tr><td>${item.productName}</td><td>${item.quantity}</td><td>$${item.price}</td></tr>`;
                });
                html += `</table><strong>Total: $${order.total}</strong></div><hr/>`;
            });
        }
        ordersList.innerHTML = html;
    } catch (err) {
        ordersList.innerHTML = `<div class='alert'>${err.message}</div>`;
    }
}

if (ordersList) fetchOrders(); 