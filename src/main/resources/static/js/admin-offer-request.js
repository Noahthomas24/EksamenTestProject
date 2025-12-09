let allOffers = [];
let editingOfferId = null;

// -------------------------------------------------
// HENT ALLE OFFER REQUESTS
// -------------------------------------------------
async function loadOffers() {
    const res = await fetch("/api/offer-requests", { credentials: "include" });
    allOffers = await res.json();
    applyFilters();
}

// -------------------------------------------------
// SØGNING
// -------------------------------------------------
function applyFilters() {
    const q = document.getElementById("searchInput").value.toLowerCase();

    const filtered = allOffers.filter(o =>
        o.firstName.toLowerCase().includes(q) ||
        o.lastName.toLowerCase().includes(q) ||
        o.description.toLowerCase().includes(q)
    );

    renderOffers(filtered);
}

// -------------------------------------------------
// RENDER LISTE
// -------------------------------------------------
function renderOffers(offers) {
    const list = document.getElementById("offerList");
    list.innerHTML = "";

    if (offers.length === 0) {
        list.innerHTML = "<p>Ingen forespørgsler fundet.</p>";
        return;
    }

    offers.forEach(o => {
        const div = document.createElement("div");
        div.className = "card";

        div.innerHTML = `
            <h3>${o.firstName} ${o.lastName}</h3>
            <p><strong>Email:</strong> ${o.email}</p>
            <p><strong>Telefon:</strong> ${o.phone}</p>
            <p><strong>Beskrivelse:</strong> ${o.description}</p>
            <p><strong>Type:</strong> ${o.type}</p>

            <div class="card-actions">
                <button onclick="openEditOffer(${o.id})">Redigér</button>
                <button class="danger" onclick="deleteOffer(${o.id})">Slet</button>
            </div>
        `;

        list.appendChild(div);
    });
}

// -------------------------------------------------
// ÅBN MODAL TIL REDIGERING
// -------------------------------------------------
async function openEditOffer(id) {
    editingOfferId = id;

    const res = await fetch(`/api/offer-requests/${id}`, { credentials: "include" });
    const o = await res.json();

    document.getElementById("modalTitle").textContent = "Redigér forespørgsel";

    document.getElementById("firstName").value = o.firstName;
    document.getElementById("lastName").value = o.lastName;
    document.getElementById("phone").value = o.phone;
    document.getElementById("email").value = o.email;
    document.getElementById("description").value = o.description;
    document.getElementById("type").value = o.type;

    modal.classList.remove("hidden");
}

// -------------------------------------------------
// GEM ÆNDRINGER
// -------------------------------------------------
document.getElementById("saveOfferBtn").onclick = async () => {
    const updated = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        phone: document.getElementById("phone").value,
        email: document.getElementById("email").value,
        description: document.getElementById("description").value,
        type: document.getElementById("type").value
    };

    await fetch(`/api/offer-requests/${editingOfferId}`, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updated)
    });

    modal.classList.add("hidden");
    loadOffers();
};

// -------------------------------------------------
// SLET REQUEST
// -------------------------------------------------
async function deleteOffer(id) {
    if (!confirm("Vil du slette denne forespørgsel?")) return;

    await fetch(`/api/offer-requests/${id}`, {
        method: "DELETE",
        credentials: "include"
    });

    loadOffers();
}

// -------------------------------------------------
// MODAL CLOSE
// -------------------------------------------------
const modal = document.getElementById("offerModal");
document.getElementById("closeModalBtn").onclick = () => modal.classList.add("hidden");

// -------------------------------------------------
// EVENTS
// -------------------------------------------------
document.getElementById("searchInput").oninput = applyFilters;

// -------------------------------------------------
// START
// -------------------------------------------------
loadOffers();
