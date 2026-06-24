const API_BASE_URL = 'http://localhost:8080/api';

window.addEventListener('DOMContentLoaded', () => {
    loadAllReservations();
    document.getElementById('loadReservationsBtn').addEventListener('click', loadAllReservations);
});

// 1. Fetch all reservations (Read operation)
function loadAllReservations() {
    const tableBody = document.getElementById('reservationsBody');
    tableBody.innerHTML = '<tr><td colspan="8" style="text-align: center;">Loading management rows...</td></tr>';

    fetch(`${API_BASE_URL}/reservations`)
        .then(response => response.json())
        .then(data => {
            tableBody.innerHTML = '';
            if (data.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="8" style="text-align: center;">No reservations found in the system.</td></tr>';
                return;
            }

            data.forEach(res => {
                const tr = document.createElement('tr');

                // Set up visual badges for the status values
                let badgeClass = 'status-confirmed';
                if(res.status === 'CHECKED_OUT') badgeClass = 'status-checkedout';
                if(res.status === 'CANCELLED') badgeClass = 'status-cancelled';

                // Determine which buttons to show based on state
                let actionButtons = '';
                if (res.status === 'CONFIRMED') {
                    actionButtons = `
                        <button class="btn-action btn-checkout" onclick="updateBookingStatus(${res.id}, 'CHECKED_OUT')">Check Out</button>
                        <button class="btn-action btn-cancel" onclick="updateBookingStatus(${res.id}, 'CANCELLED')">Cancel</button>
                    `;
                }
                // Admin can drop record permanently anytime
                actionButtons += `<button class="btn-action btn-delete" onclick="deleteBooking(${res.id})">Delete</button>`;

                tr.innerHTML = `
                    <td><strong>#${res.id}</strong></td>
                    <td>${res.customerName}</td>
                    <td>Room ${res.roomNumber}</td>
                    <td>${res.checkInDate}</td>
                    <td>${res.checkOutDate}</td>
                    <td>$${res.totalPrice.toFixed(2)}</td>
                    <td><span class="status-badge ${badgeClass}">${res.status}</span></td>
                    <td>${actionButtons}</td>
                `;
                tableBody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error(error);
            tableBody.innerHTML = '<tr><td colspan="8" style="text-align: center; color: red;">Error fetching database. Is backend running?</td></tr>';
        });
}

// 2. Handle Status Corrections (Update operation mapping to updateStatus)
function updateBookingStatus(id, newStatus) {
    if(!confirm(`Are you sure you want to change reservation #${id} to ${newStatus}?`)) return;

    fetch(`${API_BASE_URL}/reservations/${id}/status?newStatus=${newStatus}`, {
        method: 'PUT'
    })
    .then(response => {
        if (!response.ok) throw new Error("Could not modify reservation state.");
        alert(`Reservation status updated to ${newStatus}!`);
        loadAllReservations(); // Reload UI data map
    })
    .catch(error => alert(error.message));
}

// 3. Handle Administrative Removals (Delete operation mapping to deleteReservation)
function deleteBooking(id) {
    if(!confirm(`⚠️ Warning! This will permanently wipe reservation #${id} from MySQL database tables. Proceed?`)) return;

    fetch(`${API_BASE_URL}/reservations/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) throw new Error("Could not execute administrative drop execution.");
        alert("Record completely removed from database storage.");
        loadAllReservations();
    })
    .catch(error => alert(error.message));
}