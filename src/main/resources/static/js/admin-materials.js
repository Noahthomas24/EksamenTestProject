let allMaterials = [];
let editingMaterialId = null;

// -------------------------------------------------
// HENT MATERIALER
// -------------------------------------------------
async function loadMaterials() {
    const res = await fetch("/api/materials", { credentials: "include" });
    allMaterials = await res.json();
    applyFilters();
}

// -------------------------------------------------
// FILTRERING / SØGNING
// -------------------------------------------------
function applyFilters() {
    const search = document.getElementById("searchInput").value.toLowerCase();

    const filtered = allMaterials.filter(m =>
        m.name.toLowerCase().includes(search) ||
        m.unit.toLowerCase().includes(search)
    );

    renderMaterials(filtered);
}

// -------------------------------------------------
// VIS MATERIALELISTE
// -------------------------------------------------
function renderMaterials(materials) {
    const list = document.getElementById("materialList");
    list.innerHTML = "";

    if (materials.length === 0) {
        list.innerHTML = "<p>Ingen materialer fundet.</p>";
        return;
    }

    materials.forEach(m => {
        const div = document.createElement("div");
        div.className = "card";

        div.innerHTML = `
            <h3>${m.name}</h3>
            <p><strong>Pris:</strong> ${m.price} kr</p>
            <p><strong>Enhed:</strong> ${m.unit}</p>

            <div class="card-actions">
                <button onclick="openEditMaterial(${m.id})">Redigér</button>
                <button class="danger" onclick="deleteMaterial(${m.id})">Slet</button>
            </div>
        `;

        list.appendChild(div);
    });
}

// -------------------------------------------------
// OPRET NYT MATERIALE
// -------------------------------------------------
document.getElementById("newMaterialBtn").onclick = () => {
    editingMaterialId = null;

    document.getElementById("modalTitle").textContent = "Nyt materiale";

    document.getElementById("materialName").value = "";
    document.getElementById("materialPrice").value = "";
    document.getElementById("materialUnit").value = "";

    modal.classList.remove("hidden");
};

// -------------------------------------------------
// REDIGÉR MATERIALE
// -------------------------------------------------
async function openEditMaterial(id) {
    editingMaterialId = id;

    const res = await fetch(`/api/materials/${id}`, { credentials: "include" });
    const m = await res.json();

    document.getElementById("modalTitle").textContent = "Redigér materiale";

    document.getElementById("materialName").value = m.name;
    document.getElementById("materialPrice").value = m.price;
    document.getElementById("materialUnit").value = m.unit;

    modal.classList.remove("hidden");
}

// -------------------------------------------------
// GEM MATERIALE (POST ELLER PUT)
// -------------------------------------------------
document.getElementById("saveMaterialBtn").onclick = async () => {

    const materialObj = {
        name: document.getElementById("materialName").value,
        price: Number(document.getElementById("materialPrice").value),
        unit: document.getElementById("materialUnit").value
    };

    if (editingMaterialId === null) {
        // OPRET
        await fetch("/api/materials", {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(materialObj)
        });
    } else {
        // REDIGÉR
        await fetch(`/api/materials/${editingMaterialId}`, {
            method: "PUT",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(materialObj)
        });
    }

    modal.classList.add("hidden");
    loadMaterials();
};

// -------------------------------------------------
// SLET MATERIALE
// -------------------------------------------------
async function deleteMaterial(id) {
    if (!confirm("Vil du slette dette materiale?")) return;

    await fetch(`/api/materials/${id}`, {
        method: "DELETE",
        credentials: "include"
    });

    loadMaterials();
}

// -------------------------------------------------
// MODAL CLOSE
// -------------------------------------------------
const modal = document.getElementById("materialModal");
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
loadMaterials();
