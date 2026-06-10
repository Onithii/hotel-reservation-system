const form = document.getElementById("reservationForm");

form.addEventListener("submit", async (e) => {

    e.preventDefault();

    const reservation = {

        customerName: document.getElementById("customerName").value,

        email: document.getElementById("email").value,

        roomNumber: parseInt(document.getElementById("roomNumber").value),

        checkInDate: document.getElementById("checkInDate").value,

        checkOutDate: document.getElementById("checkOutDate").value,

        status: document.getElementById("status").value
    };

    const response = await fetch("/reservations", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(reservation)
    });

    const data = await response.json();

    console.log(data);

    alert("Reservation Created!");
});