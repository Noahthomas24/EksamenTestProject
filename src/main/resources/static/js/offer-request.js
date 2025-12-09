document.getElementById("offerForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const offer = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        phone: document.getElementById("phone").value,
        email: document.getElementById("email").value,
        description: document.getElementById("description").value,
        type: document.getElementById("type").value
    };

    // Send til backend
    const res = await fetch("/api/offer-requests", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(offer)
    });

    if (res.ok) {
        document.getElementById("offerForm").classList.add("hidden");
        document.getElementById("successMessage").classList.remove("hidden");
    } else {
        alert("Der skete en fejl. Prøv igen senere.");
    }
});
