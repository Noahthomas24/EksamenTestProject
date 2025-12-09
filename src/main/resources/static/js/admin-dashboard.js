let caseStatusChart;
let offerTypeChart;

// MAIN LOAD FUNCTION
async function loadDashboard() {
    const [custRes, caseRes, matRes, offerRes] = await Promise.all([
        fetch("/api/customers", { credentials: "include" }),
        fetch("/api/cases", { credentials: "include" }),
        fetch("/api/materials", { credentials: "include" }),
        fetch("/api/offer-requests", { credentials: "include" })
    ]);

    const customers = await custRes.json();
    const cases = await caseRes.json();
    const materials = await matRes.json();
    const offers = await offerRes.json();

    // 🟦 Statistik
    document.getElementById("customerCount").textContent = customers.length;
    document.getElementById("materialCount").textContent = materials.length;
    document.getElementById("offerCount").textContent = offers.length;

    const openCases = cases.filter(c => c.status === "OPEN").length;
    const closedCases = cases.filter(c => c.status === "CLOSED").length;

    document.getElementById("openCasesCount").textContent = openCases;
    document.getElementById("closedCasesCount").textContent = closedCases;

    // 🟩 Grafer
    renderCaseChart(openCases, closedCases);
    renderOfferTypeChart(offers);

    // 🟧 Aktivitet
    renderActivityLog(cases, offers);
}

// ------------------------------------------------------
// ⭐ GRAF: Sagsstatus
// ------------------------------------------------------
function renderCaseChart(openCount, closedCount) {

    if (caseStatusChart) caseStatusChart.destroy();

    const ctx = document.getElementById("caseStatusChart");
    caseStatusChart = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: ["Åbne", "Lukkede"],
            datasets: [{
                data: [openCount, closedCount],
                backgroundColor: ["#4CAF50", "#F44336"]
            }]
        },
        options: { responsive: true }
    });
}

// ------------------------------------------------------
// ⭐ GRAF: Fordeling af tilbudstyper
// ------------------------------------------------------
function renderOfferTypeChart(offers) {

    if (offerTypeChart) offerTypeChart.destroy();

    const typeCounts = {
        WOODCRAFT: 0,
        FURNITURE: 0,
        SPECIAL: 0
    };

    offers.forEach(o => typeCounts[o.type]++);

    const ctx = document.getElementById("offerTypeChart");

    offerTypeChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["Træarbejde", "Møbler", "Specialopgave"],
            datasets: [{
                data: [
                    typeCounts.WOODCRAFT,
                    typeCounts.FURNITURE,
                    typeCounts.SPECIAL
                ],
                backgroundColor: ["#2196F3", "#FFC107", "#9C27B0"]
            }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } }
        }
    });
}

// ------------------------------------------------------
// ⭐ Seneste aktivitet
// ------------------------------------------------------
function renderActivityLog(cases, offers) {
    const log = document.getElementById("activityLog");
    log.innerHTML = "";

    const activities = [];

    // Tilføj cases
    cases.slice(-5).forEach(c => {
        activities.push({
            text: `Ny sag: ${c.caseName} (${c.status})`,
            when: c.id
        });
    });

    // Tilføj offers
    offers.slice(-5).forEach(o => {
        activities.push({
            text: `Ny forespørgsel fra ${o.firstName} ${o.lastName}`,
            when: o.id + 10000
        });
    });

    // Sortér nyeste først
    activities.sort((a, b) => b.when - a.when);

    // Vis de 8 nyeste
    activities.slice(0, 8).forEach(a => {
        const li = document.createElement("li");
        li.textContent = a.text;
        log.appendChild(li);
    });
}

loadDashboard();
