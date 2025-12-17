let allCustomers = [];
let editingCustomerId = null;

// -------------------------------------------------
// HENT KUNDER
// -------------------------------------------------
async function loadCustomers() {
    const res = await fetch("/api/customers", { credentials: "include" });
    allCustomers = await res.json();
    applyFilters();
}

// -------------------------------------------------
// SØGNING OG FILTRERING
// -------------------------------------------------
function applyFilters() {
    const search = document.getElementById("searchInput").value.toLowerCase();

    const filtered = allCustomers.filter(c =>
        c.firstName.toLowerCase().includes(search) ||
        c.lastName.toLowerCase().includes(search) ||
        c.email.toLowerCase().includes(search) ||
        c.phoneNumber.includes(search)
    );

    renderCustomers(filtered);
}

// -------------------------------------------------
// VIS KUNDER
// -------------------------------------------------
function renderCustomers(customers) {
    const list = document.getElementById("customerList");
    list.innerHTML = "";

    if (customers.length === 0) {
        list.innerHTML = "<p>Ingen kunder fundet.</p>";
        return;
    }

    customers.forEach(c => {
        const div = document.createElement("div");
        div.className = "card";

        div.innerHTML = `
            <h3>${c.firstName} ${c.lastName}</h3>
            <p><strong>Email:</strong> ${c.email}</p>
            <p><strong>Tlf:</strong> ${c.phoneNumber ?? "-"}</p>
            <p><strong>Adresse:</strong> ${c.address ?? "-"}</p>
            <p><strong>Postnummer:</strong> ${c.zipCode ?? "-"}</p>

            <div class="card-actions">
                <button onclick="openEditCustomer(${c.id})">Redigér</button>
                <button class="danger" onclick="deleteCustomer(${c.id})">Slet</button>
            </div>
        `;

        list.appendChild(div);
    });
}

// -------------------------------------------------
// OPRET NY KUNDE
// -------------------------------------------------
document.getElementById("newCustomerBtn").onclick = () => {
    editingCustomerId = null;

    document.getElementById("modalTitle").textContent = "Opret kunde";

    document.getElementById("firstName").value = "";
    document.getElementById("lastName").value = "";
    document.getElementById("email").value = "";
    document.getElementById("phone").value = "";
    document.getElementById("address").value = "";
    document.getElementById("zipcode").value = "";
    document.getElementById("city").value = "";

    modal.classList.remove("hidden");
};

// -------------------------------------------------
// REDIGÉR KUNDE
// -------------------------------------------------
async function openEditCustomer(id) {
    editingCustomerId = id;

    const res = await fetch(`/api/customers/${id}`, { credentials: "include" });
    const c = await res.json();

    document.getElementById("modalTitle").textContent = "Redigér kunde";

    document.getElementById("firstName").value = c.firstName;
    document.getElementById("lastName").value = c.lastName;
    document.getElementById("email").value = c.email;
    document.getElementById("phone").value = c.phoneNumber ?? "";
    document.getElementById("address").value = c.address ?? "";
    document.getElementById("zipcode").value = c.zipCode ?? "";
    document.getElementById("city").value = c.city ?? "";

    modal.classList.remove("hidden");
}

// -------------------------------------------------
// GEM KUNDE
// -------------------------------------------------
document.getElementById("saveCustomerBtn").onclick = async () => {
    const customerObj = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        phoneNumber: document.getElementById("phone").value,
        address: document.getElementById("address").value,
        zipCode: document.getElementById("zipcode").value,
        city: document.getElementById("city").value
    };

    const url = editingCustomerId === null
        ? "/api/customers"
        : `/api/customers/${editingCustomerId}`;

    const method = editingCustomerId === null ? "POST" : "PUT";

    await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(customerObj)
    });

    modal.classList.add("hidden");
    loadCustomers();
};

// -------------------------------------------------
// SLET KUNDE
// -------------------------------------------------
async function deleteCustomer(id) {
    if (!confirm("Vil du slette denne kunde?")) return;

    await fetch(`/api/customers/${id}`, {
        method: "DELETE",
        credentials: "include"
    });

    loadCustomers();
}

// -------------------------------------------------
// MODAL
// -------------------------------------------------
const modal = document.getElementById("customerModal");
document.getElementById("closeModalBtn").onclick = () => {
    modal.classList.add("hidden");
};

// -------------------------------------------------
// EVENTS
// -------------------------------------------------
document.getElementById("searchInput").oninput = applyFilters;

// -------------------------------------------------
// START
// -------------------------------------------------
loadCustomers();
