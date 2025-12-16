let allCases = [];
let allCustomers = [];
let allMaterials = [];
let editingCaseId = null;

const modal = document.getElementById("caseModal");

// -------------------------------------------------
// HENT DATA
// -------------------------------------------------
async function loadInitialData() {
    const [casesRes, customersRes, materialsRes] = await Promise.all([
        fetch("/api/cases", { credentials: "include" }),
        fetch("/api/customers", { credentials: "include" }),
        fetch("/api/materials", { credentials: "include" })
    ]);

    allCases = await casesRes.json();
    allCustomers = await customersRes.json();
    allMaterials = await materialsRes.json();

    populateCustomerDropdown();
    applyFilters();
}

// -------------------------------------------------
// KUNDER DROPDOWN
// -------------------------------------------------
function populateCustomerDropdown() {
    const dropdown = document.getElementById("caseCustomerSelect");
    dropdown.innerHTML = "";

    allCustomers.forEach(c => {
        const opt = document.createElement("option");
        opt.value = c.id;
        opt.textContent = `${c.firstName} ${c.lastName}`;
        dropdown.appendChild(opt);
    });
}

// -------------------------------------------------
// MATERIAL DROPDOWN ROW
// -------------------------------------------------
function addMaterialRow(existingMaterial = null) {
    const row = document.createElement("div");
    row.className = "material-row";

    // Dropdown
    const materialSelect = document.createElement("select");
    materialSelect.className = "material-id";

    allMaterials.forEach(m => {
        const opt = document.createElement("option");
        opt.value = m.id;
        opt.textContent = m.name;
        materialSelect.appendChild(opt);
    });

    if (existingMaterial) {
        materialSelect.value = existingMaterial.materialId;
    }

    // Mængde input
    const amountInput = document.createElement("input");
    amountInput.type = "number";
    amountInput.placeholder = "Mængde";
    amountInput.className = "material-amount";
    amountInput.value = existingMaterial?.quantity ?? "";

    // Fjern-knap
    const removeBtn = document.createElement("button");
    removeBtn.className = "secondary";
    removeBtn.textContent = "X";
    removeBtn.onclick = () => row.remove();

    row.appendChild(materialSelect);
    row.appendChild(amountInput);
    row.appendChild(removeBtn);

    document.getElementById("materialsContainer").appendChild(row);
}

// knap: tilføj materiale
document.getElementById("addMaterialBtn").onclick = () => addMaterialRow();

// -------------------------------------------------
// FILE UPLOAD FOR CASE
// -------------------------------------------------

function triggerUpload(caseId) {
    const fileInput = document.getElementById("hiddenFileInput");

    // When the file is selected
    fileInput.onchange = async () => {
        const file = fileInput.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            const res = await fetch(`/api/cases/${caseId}/files`, {
                method: "POST",
                credentials: "include",
                body: formData
            });

            if (!res.ok) {
                alert("Kunne ikke uploade filen.");
                return;
            }

            alert("Filen blev uploadet!");
        } catch (err) {
            alert("Der skete en fejl under upload.");
            console.error(err);
        }

        fileInput.value = ""; // reset for next upload
    };

    fileInput.click(); // open picker
}


// -------------------------------------------------
// FILTRERING
// -------------------------------------------------
function applyFilters() {
    const search = document.getElementById("searchInput").value.toLowerCase();
    const status = document.getElementById("statusFilter").value;

    let filtered = allCases;

    if (search) {
        filtered = filtered.filter(c =>
            c.title.toLowerCase().includes(search) ||
            c.description.toLowerCase().includes(search)
        );
    }

    if (status !== "ALL") {
        filtered = filtered.filter(c => c.status === status);
    }

    renderCases(filtered);
}

// -------------------------------------------------
// RENDER SAGER (MED MATERIALER)
// -------------------------------------------------
function renderCases(cases) {
    const list = document.getElementById("caseList");
    list.innerHTML = "";

    cases.forEach(c => {
        let materialsHtml = "<em>Ingen materialer</em>";

        if (c.materials && c.materials.length > 0) {
            materialsHtml = "<ul class='case-materials'>";

            c.materials.forEach(cm => {
                const material = allMaterials.find(
                    m => m.id === cm.materialId
                );

                const name = material ? material.name : "Ukendt materiale";
                const unitPrice = cm.effectiveUnitPrice ?? 0;
                const total = unitPrice * cm.quantity;

                materialsHtml += `
                    <li>
                        ${name} – ${cm.quantity} stk
                        (${unitPrice} kr/stk · ${total} kr i alt)
                    </li>
                `;
            });

            materialsHtml += "</ul>";
        }

        const div = document.createElement("div");
        div.className = "card";

        div.innerHTML = `
            <h3>${c.title}</h3>
            <p>${c.description}</p>
            <p><strong>Status:</strong> ${c.status}</p>

            <div class="case-materials-wrapper">
                <strong>Materialer:</strong>
                ${materialsHtml}
            </div>

            <div class="card-actions">
                <button onclick="openEditCase(${c.id})">Redigér</button>
                <button onclick="toggleCaseStatus(${c.id}, '${c.status}')">
                    ${c.status === "OPEN" ? "Luk sag" : "Genåbn sag"}
                </button>
                <button class="danger" onclick="deleteCase(${c.id})">Slet</button>
                <!-- ⭐ NEW: Upload button -->
                <button onclick="triggerUpload(${c.id})">Upload dokument</button>
            </div>
        `;

        list.appendChild(div);
    });
}

// -------------------------------------------------
// MATERIALER – RÆKKE MED PRIS
// -------------------------------------------------
function addMaterialRow(existingMaterial = null) {
    const row = document.createElement("div");
    row.className = "material-row";

    const materialSelect = document.createElement("select");
    materialSelect.className = "material-id";

    allMaterials.forEach(m => {
        const opt = document.createElement("option");
        opt.value = m.id;
        opt.textContent = m.name;
        materialSelect.appendChild(opt);
    });

    const amountInput = document.createElement("input");
    amountInput.type = "number";
    amountInput.placeholder = "Mængde";
    amountInput.className = "material-amount";

    const priceInfo = document.createElement("div");
    priceInfo.className = "material-price";
    priceInfo.textContent = "Pris: –";

    const removeBtn = document.createElement("button");
    removeBtn.textContent = "X";
    removeBtn.className = "secondary";
    removeBtn.onclick = () => row.remove();

    function updatePrice() {
        const material = allMaterials.find(
            m => m.id === Number(materialSelect.value)
        );
        const qty = Number(amountInput.value || 0);

        if (!material || material.pricePerUnit == null || qty <= 0) {
            priceInfo.textContent = "Pris: –";
            return;
        }

        const total = material.pricePerUnit * qty;
        priceInfo.textContent =
            `Pris: ${material.pricePerUnit} kr • I alt: ${total} kr`;
    }

    materialSelect.onchange = updatePrice;
    amountInput.oninput = updatePrice;

    if (existingMaterial) {
        materialSelect.value = existingMaterial.materialId;
        amountInput.value = existingMaterial.quantity;
        updatePrice();
    }

    row.append(
        materialSelect,
        amountInput,
        priceInfo,
        removeBtn
    );

    document.getElementById("materialsContainer").appendChild(row);
}

// -------------------------------------------------
// OPRET SAG
// -------------------------------------------------
document.getElementById("newCaseBtn").onclick = () => {
    editingCaseId = null;

    document.getElementById("modalTitle").textContent = "Opret sag";
    document.getElementById("caseTitle").value = "";
    document.getElementById("caseDescription").value = "";
    document.getElementById("materialsContainer").innerHTML = "";

    modal.classList.remove("hidden");
};

// -------------------------------------------------
// REDIGÉR SAG
// -------------------------------------------------
async function openEditCase(id) {
    editingCaseId = id;

    const res = await fetch(`/api/cases/${id}`, { credentials: "include" });
    const c = await res.json();

    document.getElementById("modalTitle").textContent = "Redigér sag";
    document.getElementById("caseTitle").value = c.title;
    document.getElementById("caseDescription").value = c.description;
    document.getElementById("caseCustomerSelect").value = c.customerId;

    const container = document.getElementById("materialsContainer");
    container.innerHTML = "";

    c.materials.forEach(m => addMaterialRow(m));

    modal.classList.remove("hidden");
}

// -------------------------------------------------
// GEM SAG (type = "Snedker")
// -------------------------------------------------
document.getElementById("saveCaseBtn").onclick = async () => {
    const materials = [];

    document.querySelectorAll(".material-row").forEach(row => {
        const materialId = Number(row.querySelector(".material-id").value);
        const qtyValue = row.querySelector(".material-amount").value;
        const quantity = Number(qtyValue);

        if (!materialId || !qtyValue || quantity <= 0) return;

        materials.push({
            materialId,
            quantity,
            description: "",
            unitPrice: 0
        });
    });

    const body = {
        title: document.getElementById("caseTitle").value.trim(),
        description: document.getElementById("caseDescription").value.trim(),
        customerId: Number(document.getElementById("caseCustomerSelect").value),
        type: "Snedker",
        materials
    };

    const url = editingCaseId === null
        ? "/api/cases"
        : `/api/cases/${editingCaseId}`;

    const method = editingCaseId === null ? "POST" : "PUT";

    const res = await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    if (!res.ok) {
        const err = await res.text();
        console.error("SAVE CASE ERROR:", err);
        alert("Kunne ikke gemme sag – tjek felterne");
        return;
    }

    modal.classList.add("hidden");
    loadInitialData();
};

// -------------------------------------------------
// STATUS / SLET
// -------------------------------------------------
async function toggleCaseStatus(id, status) {

    const currentStatus = status === "CLOSED" ? "CLOSED" : "OPEN";
    const newStatus = currentStatus === "OPEN" ? "CLOSED" : "OPEN";

    await fetch(`/api/cases/${id}/status?status=${newStatus}`, {
        method: "PUT",
        credentials: "include"
    });

    loadInitialData();
}

// -------------------------------------------------
// EVENTS
// -------------------------------------------------
document.getElementById("addMaterialBtn").onclick = () => addMaterialRow();
document.getElementById("closeModalBtn").onclick = () => modal.classList.add("hidden");
document.getElementById("searchInput").oninput = applyFilters;
document.getElementById("statusFilter").onchange = applyFilters;

// -------------------------------------------------
// START
// -------------------------------------------------
loadInitialData();
