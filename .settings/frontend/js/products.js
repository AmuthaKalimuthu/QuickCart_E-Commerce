// products.js
const API_BASE = 'http://localhost:8080/api';
const productsList = document.getElementById('products-list');

function getToken() {
    return localStorage.getItem('jwt');
}

async function fetchProducts() {
    const res = await fetch(`${API_BASE}/products`);
    const products = await res.json();
    let html = '<table class="table"><tr><th>Name</th><th>Description</th><th>Price</th><th></th></tr>';
    products.forEach(p => {
        html += `<tr>
            <td>${p.name}</td>
            <td>${p.description || ''}</td>
            <td>$${p.price}</td>
            <td>${getToken() ? `<button onclick="addToCart(${p.id})">Add to Cart</button>` : ''}</td>
        </tr>`;
    });
    html += '</table>';
    productsList.innerHTML = html;
}

window.addToCart = async function(productId) {
    const quantity = 1;
    try {
        const res = await fetch(`${API_BASE}/cart/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getToken()
            },
            body: JSON.stringify({ productId, quantity })
        });
        if (!res.ok) throw new Error(await res.text());
        alert('Added to cart!');
    } catch (err) {
        alert('Error: ' + err.message);
    }
}

if (productsList) fetchProducts(); 