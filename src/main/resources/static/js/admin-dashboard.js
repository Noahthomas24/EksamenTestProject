let caseStatusChart;
let materialPerCaseChart;

document.addEventListener("DOMContentLoaded", () => {
    loadDashboard();
});

// ------------------------------------------------------
// LOADING
// ------------------------------------------------------
function showLoading() {
    document.getElementById("loadingOverlay").classList.remove("hidden");
}

function hideLoading() {
    document.getElementById("loadingOverlay").classList.add("hidden");
}

// ------------------------------------------------------
// MAIN LOAD FUNCTION
// ------------------------------------------------------
async function loadDashboard() {
    showLoading();

    try {
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
        renderMaterialPerCaseChart(cases);


    } catch (err) {
        console.error("Dashboard fejl:", err);
        alert("Kunne ikke indlæse dashboard-data");
    } finally {
        hideLoading();
    }
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
        options: {
            responsive: true,
            plugins: {
                legend: { position: "bottom" }
            }
        }
    });
}

function renderMaterialPerCaseChart(cases) {
    if (materialPerCaseChart) materialPerCaseChart.destroy();

    const labels = [];
    const data = [];

    cases.forEach(c => {
        let total = 0;

        c.materials?.forEach(cm => {
            total += (cm.quantity || 0) * (cm.unitPrice || 0);
        });

        labels.push(c.title);
        data.push(total);
    });

    const ctx = document.getElementById("materialPerCaseChart");

    materialPerCaseChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels,
            datasets: [{
                label: "Samlet materialepris (kr.)",
                data,
                backgroundColor: "#8BC34A"
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: ctx => ctx.raw + " kr."
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: v => v + " kr."
                    }
                }
            }
        }
    });
}



// ------------------------------------------------------
// AUTH
// ------------------------------------------------------
document.getElementById("logoutBtn").onclick = async () => {
    try {
        await fetch("/auth/logout", {
            method: "POST",
            credentials: "include"
        });
    } catch (e) {
        console.error("Logout failed", e);
    }

    window.location.href = "/index.html";
};
