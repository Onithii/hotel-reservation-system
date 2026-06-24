const API_BASE_URL = 'http://localhost:8080/api';

// 1. Setup Event Listeners on load
window.addEventListener('DOMContentLoaded', () => {
    loadAvailableRooms();

    document.getElementById('refreshBtn').addEventListener('click', loadAvailableRooms);
    document.getElementById('bookingForm').addEventListener('submit', handleFormSubmit);
});

// 2. Fetch available rooms from Spring Boot API and populate the dropdown menu
function loadAvailableRooms() {
    const dropdown = document.getElementById('roomSelect');
    dropdown.innerHTML = '<option value="">Loading available inventory...</option>';

    fetch(`${API_BASE_URL}/rooms/available`)
        .then(response => {
            if (!response.ok) throw new Error("Failed to pull rooms.");
            return response.json();
        })
        .then(rooms => {
            dropdown.innerHTML = '';
            if (rooms.length === 0) {
                dropdown.innerHTML = '<option value="">❌ No available rooms left!</option>';
                return;
            }
            dropdown.innerHTML = '<option value="">-- Choose a Room --</option>';
            rooms.forEach(room => {
                const option = document.createElement('option');
                option.value = room.id;
                option.textContent = `Room ${room.roomNumber} - ${room.roomType} ($${room.pricePerNight}/night)`;
                dropdown.appendChild(option);
            });
        })
        .catch(error => {
            console.error(error);
            dropdown.innerHTML = '<option value="">⚠️ Error loading rooms. Is backend running?</option>';
        });
}

// 3. Handle Form Submission (Submit DTO JSON object to Backend)
function handleFormSubmit(e) {
    e.preventDefault();

    const receiptDiv = document.getElementById('receipt');
    receiptDiv.style.display = 'none';

    // Pack inputs into our strict DTO structural layout
    const requestDTO = {
        userId: parseInt(document.getElementById('userId').value),
        roomId: parseInt(document.getElementById('roomSelect').value),
        checkInDate: document.getElementById('checkInDate').value,
        checkOutDate: document.getElementById('checkOutDate').value
    };

    // Fire HTTP POST request
    fetch(`${API_BASE_URL}/reservations`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestDTO)
    })
    .then(async response => {
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.error || "Validation error or room occupied.");
        }
        return data;
    })
    .then(reservation => {
        // Render receipt beautifully on success screen showing automated 12% calculation
        receiptDiv.className = 'success';
        receiptDiv.style.padding = '15px';
        receiptDiv.innerHTML = `
            <strong>🎉 Booking Confirmed!</strong><br><br>
            Booking Reference ID: #${reservation.id}<br>
            Guest Name: ${reservation.customerName}<br>
            Assigned Room: Room ${reservation.roomNumber}<br>
            Total Charge (with 12% Tax): <strong>$${reservation.totalPrice.toFixed(2)}</strong><br>
            System Status: ${reservation.status}
        `;
        receiptDiv.style.display = 'block';

        // Reload available inventory choices automatically
        loadAvailableRooms();
    })
    .catch(error => {
        receiptDiv.className = 'error';
        receiptDiv.style.padding = '15px';
        receiptDiv.innerHTML = `<strong>⚠️ Booking Failed:</strong><br>${error.message}`;
        receiptDiv.style.display = 'block';
    });
}