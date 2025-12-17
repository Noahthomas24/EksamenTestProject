let allOffers = [];

// -------------------------------------------------
// HENT OFFER REQUESTS
// -------------------------------------------------
async function loadOffers() {
    const res = await fetch("/api/offer-requests", {
        credentials: "include"
    });

    if (!res.ok) {
        alert("Kunne ikke hente tilbudsforespørgsler");
        return;
    }

    allOffers = await res.json();
    applyFilters();
}

// -------------------------------------------------
// SØGNING
// -------------------------------------------------
function applyFilters() {
    const q = document
        .getElementById("searchInput")
        .value
        .toLowerCase();

    const filtered = allOffers.filter(o =>
        `${o.firstName} ${o.lastName}`.toLowerCase().includes(q) ||
        (o.email && o.email.toLowerCase().includes(q)) ||
        (o.phoneNumber && o.phoneNumber.toLowerCase().includes(q)) ||
        (o.description && o.description.toLowerCase().includes(q))
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
        list.innerHTML = "<p><em>Ingen tilbudsforespørgsler.</em></p>";
        return;
    }

    offers.forEach(o => {
        const div = document.createElement("div");
        div.className = "card";

        div.innerHTML = `
            <h3>${o.firstName} ${o.lastName}</h3>

            <p><strong>Email:</strong> ${o.email ?? "-"}</p>
            <p><strong>Telefon:</strong> ${o.phoneNumber ?? "-"}</p>
            <p><strong>Postnummer:</strong>${o.zipcode ?? "-"}</p>
            <p><strong>Type:</strong> ${prettyType(o.type)}</p>

            <p><strong>Beskrivelse:</strong></p>
            <p>${o.description}</p>

            <div class="card-actions">
                <button onclick="acceptOffer(${o.id})">
                    Acceptér → Opret sag
                </button>

                <button class="danger" onclick="denyOffer(${o.id})">
                    Afvis
                </button>
            </div>
        `;

        list.appendChild(div);
    });
}

// -------------------------------------------------
// ACCEPT → OPRET SAG
// -------------------------------------------------
async function acceptOffer(id) {
    if (!confirm("Vil du oprette en sag ud fra denne forespørgsel?")) return;

    const res = await fetch(`/api/offer-requests/${id}/accept`, {
        method: "PUT",
        credentials: "include"
    });

    if (!res.ok) {
        alert("Kunne ikke acceptere forespørgslen");
        return;
    }

    loadOffers();
}

// -------------------------------------------------
// DENY → SLET
// -------------------------------------------------
async function denyOffer(id) {
    if (!confirm("Vil du afvise og slette denne forespørgsel?")) return;

    const res = await fetch(`/api/offer-requests/${id}/deny`, {
        method: "DELETE",
        credentials: "include"
    });

    if (!res.ok) {
        alert("Kunne ikke slette forespørgslen");
        return;
    }

    loadOffers();
}

// -------------------------------------------------
// TYPE VISNING
// -------------------------------------------------
function prettyType(type) {
    switch (type) {
        case "WOODCRAFT": return "Snedker";
        case "FURNITURE": return "Møbler";
        case "SPECIAL": return "Specialopgave";
        default: return type;
    }
}

// -------------------------------------------------
// EVENTS
// -------------------------------------------------
document.getElementById("searchInput").oninput = applyFilters;

// -------------------------------------------------
// START
// -------------------------------------------------
loadOffers();
