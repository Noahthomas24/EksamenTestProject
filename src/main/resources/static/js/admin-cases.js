let allCases = [];
let allCustomers = [];
let allMaterials = [];
let editingCaseId = null;

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

    if (search.length > 0) {
        filtered = filtered.filter(c =>
            c.caseName.toLowerCase().includes(search) ||
            c.description.toLowerCase().includes(search)
        );
    }

    if (status !== "ALL") {
        filtered = filtered.filter(c => c.status === status);
    }

    renderCases(filtered);
}

// -------------------------------------------------
// RENDER
// -------------------------------------------------
function renderCases(cases) {
    const list = document.getElementById("caseList");
    list.innerHTML = "";

    cases.forEach(s => {
        const div = document.createElement("div");
        div.className = "card";

        div.innerHTML = `
            <h3>${s.caseName}</h3>
            <p>${s.description}</p>
            <p><strong>Kunde:</strong> ${s.customer?.firstName || ""} ${s.customer?.lastName || ""}</p>
            <p><strong>Status:</strong> ${s.status}</p>

            <div class="card-actions">
                <button onclick="openEditCase(${s.id})">Redigér</button>
                <button onclick="toggleCaseStatus(${s.id}, '${s.status}')">
                    ${s.status === "OPEN" ? "Luk sag" : "Genåbn sag"}
                </button>
                <button class="danger" onclick="deleteCase(${s.id})">Slet</button>
                <!-- ⭐ NEW: Upload button -->
                <button onclick="triggerUpload(${s.id})">Upload dokument</button>
            </div>
        `;

        list.appendChild(div);
    });
}

// -------------------------------------------------
// ÅBEN/LUK SAG
// -------------------------------------------------
async function toggleCaseStatus(id, currentStatus) {
    const newStatus = currentStatus === "OPEN" ? "CLOSED" : "OPEN";

    await fetch(`/api/cases/${id}/status`, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status: newStatus })
    });

    loadInitialData();
}

// -------------------------------------------------
// SLET SAG
// -------------------------------------------------
async function deleteCase(id) {
    if (!confirm("Vil du slette sagen?")) return;

    await fetch(`/api/cases/${id}`, {
        method: "DELETE",
        credentials: "include"
    });

    loadInitialData();
}

// -------------------------------------------------
// OPRET / REDIGER SAG
// -------------------------------------------------
async function openEditCase(id) {
    editingCaseId = id;

    const res = await fetch(`/api/cases/${id}`, { credentials: "include" });
    const s = await res.json();

    document.getElementById("modalTitle").textContent = "Redigér sag";

    document.getElementById("caseTitle").value = s.caseName;
    document.getElementById("caseDescription").value = s.description;
    document.getElementById("caseCustomerSelect").value = s.customerId;

    const matContainer = document.getElementById("materialsContainer");
    matContainer.innerHTML = "";

    s.materials.forEach(m => addMaterialRow({
        materialId: m.materialId,
        quantity: m.amount
    }));

    modal.classList.remove("hidden");
}

// -------------------------------------------------
// OPRET SAG
// -------------------------------------------------
document.getElementById("newCaseBtn").onclick = () => {
    editingCaseId = null;

    document.getElementById("modalTitle").textContent = "Opret sag";
    document.getElementById("caseTitle").value = "";
    document.getElementById("caseDescription").value = "";
    document.getElementById("caseCustomerSelect").value = allCustomers[0]?.id || "";

    document.getElementById("materialsContainer").innerHTML = "";

    modal.classList.remove("hidden");
};

// -------------------------------------------------
// GEM SAG (PUT eller POST)
// -------------------------------------------------
document.getElementById("saveCaseBtn").onclick = async () => {
    const materials = [];

    document.querySelectorAll(".material-row").forEach(row => {
        materials.push({
            materialId: Number(row.querySelector(".material-id").value),
            quantity: Number(row.querySelector(".material-amount").value)
        });
    });

    const body = {
        caseName: document.getElementById("caseTitle").value,
        description: document.getElementById("caseDescription").value,
        customerId: Number(document.getElementById("caseCustomerSelect").value),
        type: "WOODCRAFT",
        materials: materials
    };

    if (editingCaseId === null) {
        await fetch("/api/cases", {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
    } else {
        await fetch(`/api/cases/${editingCaseId}`, {
            method: "PUT",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
    }

    modal.classList.add("hidden");
    loadInitialData();
};

// -------------------------------------------------
// MODAL CLOSE
// -------------------------------------------------
document.getElementById("closeModalBtn").onclick = () => {
    modal.classList.add("hidden");
};

// -------------------------------------------------
// EVENTS
// -------------------------------------------------
document.getElementById("searchInput").oninput = applyFilters;
document.getElementById("statusFilter").onchange = applyFilters;

// -------------------------------------------------
// START
// -------------------------------------------------
loadInitialData();
